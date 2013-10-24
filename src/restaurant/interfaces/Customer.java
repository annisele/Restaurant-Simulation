package restaurant.interfaces;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CustomerAgent.AgentEvent;

public interface Customer {
	
	
	public abstract void setWaiter(WaiterAgent waitr);
	
	
	public abstract void setHost(HostAgent host);
	public abstract void setCook(CookAgent cook);
	public abstract void setCashier(CashierAgent cashier);
	
public abstract void hack_chicken();
	

public abstract void hack_salad();

public abstract void hack_steak();

public abstract String getCustomerName() ;

// Messages

public abstract void gotHungry() ;//from animation


public abstract void msgSitAtTable(WaiterAgent w, int a) ;


public abstract void msgAnimationFinishedGoToSeat() ;

public abstract void msgWhatsYourOrder();

public abstract void msgReorder(int table, String c);

public abstract void msgFoodIsHere();

public abstract void msgHereIsCheck(double c);

public abstract void msgPaying() ;
	//from animation

public abstract void msgAnimationFinishedLeaveRestaurant() ;
	//from animation




}