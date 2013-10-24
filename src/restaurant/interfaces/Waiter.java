package restaurant.interfaces;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CustomerAgent.AgentEvent;

public interface Waiter {
	
	
	public abstract void setWaiter(WaiterAgent waitr);
	
	
	public abstract void setHost(HostAgent host);
	public abstract void setCook(CookAgent cook);
	public abstract void setCashier(CashierAgent cashier);


	public abstract double msgGetCheck(Customer c);
	



}