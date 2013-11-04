package restaurant.interfaces;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.MarketAgent;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.MarketAgent.OrderStatus;
//import restaurant.MarketAgent.item;

public interface Market {
	
	
	public abstract void setCashier(CashierAgent cash2);
	
	public abstract void msgLowOnItem(CookAgent c, String food, int order_num);
	public abstract void hack_steak();
}