package restaurant;

import agent.Agent;
import restaurant.CookAgent.OrderState;
import restaurant.CookAgent.order;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Market;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MarketAgent extends Agent implements Market{
	
	String name;
	private CookAgent cook;
	private CashierAgent cashier;
	public class inventory{
		int steak;
		int chicken;
		int salad;
		int pizza;
		
		inventory() {
		steak=50;
		chicken=50;
		salad=50;
		pizza=50;
		
		}
	}
	public inventory v= new inventory();
	public enum OrderStatus
	{ none, ordering, loading, finished };
	OrderStatus status=OrderStatus.none;
	Timer timer = new Timer();
	private class item{
	String item;
	int unit;
	double payment;
	
	item(String n, int u){
		item=n;
		unit= u;
	}
	}
	public Deque <item> items= new LinkedList<item>();

	
	
	// Messages
	
	public MarketAgent(String store_name){
		super();
		name=store_name;
		
	}
	public String getName(){
		return name;
	}
	public void msgLowOnItem(CookAgent c, String food, int order_num){
		Do("Recieved msg that cook needs "+food);
		setCook(c);
		item temp= new item(food,order_num);
		if(checkorder(temp)==true){
			items.addFirst(temp);
			Do("here: "+ items.getFirst().item);
			status= OrderStatus.ordering;
			stateChanged();	
			temp=null;
		}
		else
			noInventory(food);
			temp=null;
	}
	public void msgHereIsMoney(double b) {
		Do("Recieved money from cashier!");
		status=OrderStatus.finished;
		stateChanged();
		
	}
	public void hack_steak(){
		v.steak=0;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 * @return 
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(status==OrderStatus.ordering){
			status=OrderStatus.finished;
			timer.schedule(new TimerTask(){
				
				Object cookie = 1;
				
				public void run() {
					Restock();
					
				}
			}, 1000);
			
			return true;
			}
	/*	if(status==OrderStatus.finished){
			items.removeFirst();
			return true;
		}*/
		
		return false;
	}

	// Actions
	private boolean checkorder(item t){
		if (t.item.equals( "chicken")) {
			if (v.chicken!=0){
			v.chicken-=t.unit;
			t.payment=10.99*t.unit;
			return true;
			}
			else
			return false;
		}
			
		if (t.item.equals("steak")){
			if (v.steak!=0){
				v.steak-=t.unit;
				t.payment=15.99*t.unit;
			return true;
		}
		else
			return false;
		}
			
		if (t.item.equals( "salad")){
			if (v.salad!=0){
			v.salad-=t.unit;
			t.payment=5.99*t.unit;
			return true;
		}
		else
			return false;
		}
		if (t.item.equals( "pizza")){
			if (v.pizza!=0){
			v.pizza-=t.unit;
			t.payment=8.99*t.unit;
			return true;
		}
		else
			return false;
		}
		return false;
	}
	
	private void noInventory(String i) {
		cook.msgNoInventory(i);
		
	}
	private void Restock(){
		item ordered_item= items.getLast();
		Do("Delivering: "+ordered_item.item);
		cook.msgHereAreItems(ordered_item.item);
		cashier.msgHereIsBill(this,ordered_item.payment);
		items.removeLast();
		
	}
	private void setCook(CookAgent cook2) {
		this.cook=cook2;
		
	}
	public void setCashier(CashierAgent cash2) {
		this.cashier=cash2;
		
	}
	
	
}
	// The animation DoXYZ() routines
	


	
	




















