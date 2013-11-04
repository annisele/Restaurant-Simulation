package restaurant.interfaces;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.test.mock.EventLog;

public interface Waiter {
	
	
	public static EventLog log = new EventLog();
	public abstract void setHost(HostAgent host);
	public abstract void setCook(CookAgent cook);
	public abstract void setCashier(CashierAgent cashier);
	public abstract void wantsaBreak();
	public abstract void msgCanBreak(boolean bool);
	public abstract void msgSeatCustomer(CustomerAgent c, int table_num);
	
		public abstract void msgReadyToOrder(CustomerAgent c);
			
				
		
	public abstract void msgHereIsMyChoice(CustomerAgent c, String choice);
		
	
	
	public abstract void msgnofood(int table);
	public abstract void msgFoodReady(int table);
	public abstract void msgReadyToPay(CustomerAgent c);
	public abstract void msgHereIsCheck(int table,double p);
	public abstract void msgCustLeave(CustomerAgent c);
	public abstract void msgAtTable();
	public abstract void msgAtLobby();
	public abstract void msgAtKitchen();


	//public abstract void msgHereIsCheck(int c, double d);
	



}