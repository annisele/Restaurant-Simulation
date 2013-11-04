
package restaurant.test.mock;

import restaurant.interfaces.Customer;
import restaurant.interfaces.Cashier;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;

public class MockMarket extends Mock implements Market{
	
	public Cashier cashier; 
	public EventLog log;
	public MockMarket(String name){
		super(name);
	}

	@Override
	public void msgLowOnItem(CookAgent c, String food, int order_num){
		
	}
	
	@Override
	public void setCashier(CashierAgent cashier) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void hack_steak() {
		// TODO Auto-generated method stub
		
	}


	
}