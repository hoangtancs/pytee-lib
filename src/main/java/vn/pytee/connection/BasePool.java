package vn.pytee.connection;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BasePool<T> {
	protected BlockingQueue<T> pool;
	private ScheduledExecutorService executorService;

	protected int initialPoolSize;
	protected int maxPoolSize;
	protected long validationInterval;
	protected int currentPoolSize;

	public BasePool(final int initialPoolSize, final int maxPoolSize, final long validationInterval) {
		this.initialPoolSize = initialPoolSize;
		this.maxPoolSize = maxPoolSize;
		this.validationInterval = validationInterval;
	}

	public T borrowObject() {
		if (pool == null) {
			return null;
		}

		if (pool.peek() == null && currentPoolSize < maxPoolSize) {
			growPoolUp();
		}

		T obj = null;
		try {
			do {
				obj = pool.take();

				if (isAvailable(obj))
					break;
				currentPoolSize--;
				growPoolUp();
				obj = null;
			} while (obj == null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public void returnObject(T object) {
		if (pool == null) {
			return;
		}
		if (object == null) {
			return;
		}

		this.pool.offer(object);
	}

	public void shutdown() {
		if (executorService != null) {
			executorService.shutdown();
		}
		pool.clear();
	}

	public Iterator<T> iterator() {
		return pool.iterator();
	}

	protected abstract void growPoolUp();
	protected abstract void destroy(T obj);
	protected abstract boolean isAvailable(T obj);

	public void startPool() {
		initialize(initialPoolSize);

		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				int size = pool.size();
				if (size < initialPoolSize) {
					int sizeToBeAdded = initialPoolSize - size;
					for (int i = 0; i < sizeToBeAdded; i++) {
						growPoolUp();
					}
				} else if (size > maxPoolSize) {
					int sizeToBeRemoved = size - maxPoolSize;
					for (int i = 0; i < sizeToBeRemoved; i++) {
						currentPoolSize--;
						pool.poll();
					}
				}
			}
		}, validationInterval, validationInterval, TimeUnit.SECONDS);

	}

	private void initialize(final int minIdle) {
		pool = new LinkedBlockingQueue<T>();
		currentPoolSize = 0;
		for (int i = 0; i < minIdle; i++) {
			growPoolUp();
		}
	}

	public int getCurrentPoolSize() {
		return pool.size();
	}
}