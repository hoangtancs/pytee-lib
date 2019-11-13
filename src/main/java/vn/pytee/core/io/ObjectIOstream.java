package vn.pytee.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectIOstream {
	public Object input(String filePath) {
		Object data = null;

		File file = new File(filePath);
		FileInputStream fileInputStream = null;
		ObjectInputStream ois = null;

		try {
			fileInputStream = new FileInputStream(file);
			ois = new ObjectInputStream(fileInputStream);

			data = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != ois)
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return data;
	}

	public void output(String filePath, Object data) {
		File file = new File(filePath);
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream oos = null;

		try {
			fileOutputStream = new FileOutputStream(file);
			oos = new ObjectOutputStream(fileOutputStream);

			oos.writeObject(data);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != oos)
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}