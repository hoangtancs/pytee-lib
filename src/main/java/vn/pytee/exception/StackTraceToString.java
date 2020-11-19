package vn.pytee.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceToString {
	private String getStackTraceAsStr(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String sStackTrace = sw.toString();

		return sStackTrace;
	}

	public static void main(String[] args) {
		StackTraceToString app = new StackTraceToString();
		try {
			int div = 1 / 0;
			System.out.println("INFO: div = " + div);
		} catch (Exception ex) {
			System.out.println("ERROR: " + app.getStackTraceAsStr(ex));
		}
	}
}