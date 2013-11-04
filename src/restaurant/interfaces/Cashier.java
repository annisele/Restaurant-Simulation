package restaurant.interfaces;
import restaurant.MarketAgent;
import restaurant.test.mock.*;


public interface Cashier {
	
	void msgHereIsMoney(Customer customer, Double check);
	

	public static EventLog log = new EventLog();

	void addMarket(MarketAgent m);

	void msgHereIsBill(Market m, Double p);


	void msgCustomerOrder(Waiter w, Customer c, int t, String ch);


	//double msgGetCheck(Customer c);
	
	
}