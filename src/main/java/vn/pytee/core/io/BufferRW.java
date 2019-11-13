package vn.pytee.core.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferRW {
	public String read(String filePath) {
		File file = new File(filePath);
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);

			String line = null;
			while (null != (line = bufferedReader.readLine())) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return null;
	}

	public String write(String filePath, String content) {
		File file = new File(filePath);
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(content);
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedWriter)
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return null;
	}
}