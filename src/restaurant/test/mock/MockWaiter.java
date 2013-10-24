
package restaurant.test.mock;

import restaurant.CashierAgent.order;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.interfaces.Cashier;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;

public class MockWaiter extends Mock implements Waiter{
	
	public Cashier cashier; 
	public EventLog log;
	public MockWaiter(String name){
		super(name);
	}

	@Override
	public double msgGetCheck(Customer c){
		  log.add(new LoggedEvent("test"));
	return 0;
	}
	
	@Override
public void setWaiter(WaiterAgent waitr){
	
	}
	
	@Override
	public void setHost(HostAgent host){
		
	}
	@Override
	public void setCook(CookAgent cook) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setCashier(CashierAgent cashier) {
		// TODO Auto-generated method stub
		
	}






	
}