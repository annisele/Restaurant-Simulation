package restaurant.interfaces;
import restaurant.test.mock.*;


public interface Cashier {
	
	void msgHereIsMoney(Customer customer, Double check);

	void msgCustomerOrder(Customer c, String choice);

	double msgGetCheck(Customer c);

	
	
	
}