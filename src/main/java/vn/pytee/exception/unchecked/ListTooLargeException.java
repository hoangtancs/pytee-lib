package vn.pytee.exception.unchecked;

public class ListTooLargeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ListTooLargeException(String message) {
		super(message);
	}
}