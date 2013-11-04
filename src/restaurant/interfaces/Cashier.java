package restaurant.interfaces;
import restaurant.MarketAgent;
import restaurant.test.mock.*;


public interface Cashier {
	
	void msgHereIsMoney(Customer customer, Double check);
	

	public static EventLog log = new EventLog();

	void addMarket(Market m);

	void msgHereIsBill(Market m, Double p);


	void msgCustomerOrder(Waiter w, Customer c, int t, String ch);

	void modBalance(double i);


	//double msgGetCheck(Customer c);
	
	
}