package vn.pytee.connection;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class Pool extends BasePool<Connection> {
	public final static int MAX_TIME_CHECK = 3;
	private DataSource dataSource;

	public Pool(DataSource dataSource, int initPoolSize, int maxPoolSize, long validationInterval) {
		super(initPoolSize, maxPoolSize, validationInterval);
		this.dataSource = dataSource;
		super.startPool();
	}

	@Override
	protected synchronized void growPoolUp() {
		if (currentPoolSize >= maxPoolSize) {
			return;
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Add connection to pool
		if (conn != null) {
			pool.offer(conn);
			currentPoolSize++;
		}
	}

	@Override
	protected void destroy(Connection obj) {
		try {
			obj.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isAvailable(Connection conn) {
		try {
			if (conn.isClosed())
				return false;

			if (!conn.isValid(MAX_TIME_CHECK))
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}