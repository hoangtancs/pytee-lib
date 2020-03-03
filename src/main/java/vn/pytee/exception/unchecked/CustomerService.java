package vn.pytee.exception.unchecked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerService {
	public void analyze(List<String> data) {
		if (data.size() > 50) {
			// runtime exception
			throw new ListTooLargeException("List can't exceed 50 items!");
		}
	}

	public static void main(String[] args) {
		CustomerService obj = new CustomerService();

		// create 100 size
		List<String> data = new ArrayList<>(Collections.nCopies(100, "mkyong"));

		obj.analyze(data);
	}
}