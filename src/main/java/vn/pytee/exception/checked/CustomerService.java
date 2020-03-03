package vn.pytee.exception.checked;

import vn.pytee.exception.Customer;

public class CustomerService {
	public Customer findByName(String name) throws NameNotFoundException {
		if ("".equals(name))
			throw new NameNotFoundException("Name is empty!");

		return new Customer(name);
	}

	public static void main(String[] args) {
		CustomerService obj = new CustomerService();

		try {
			Customer cus = obj.findByName("");
			System.out.println(cus.getName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}
}