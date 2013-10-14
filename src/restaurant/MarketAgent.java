package restaurant;

import agent.Agent;
import restaurant.CookAgent.OrderState;
import restaurant.CookAgent.order;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MarketAgent extends Agent {
	
	String name;
	private CookAgent cook;
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
	public Deque <String> items= new LinkedList<String>();

	
	
	// Messages
	
	public MarketAgent(String store_name){
		super();
		store_name=name;
		
	}
	public String getname(){
		return name;
	}
	public void msgLowOnItem(CookAgent c, String item){
		Do("Recieved msg that "+item+" is low");
		setCook(c);
		if(checkorder(item)==true){
			items.addFirst(item);
			status= OrderStatus.ordering;
			stateChanged();	
		}
		else
			noInventory(item);
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
			status= OrderStatus.finished;
			timer.schedule(new TimerTask(){
				
				Object cookie = 1;
				
				public void run() {
					Restock();
					Do("restocked");
					
					Do(""+status);
					
					
				}
			}, 1000);
			return true;
			}
		else
		return false;
		
	}

	// Actions
	private boolean checkorder(String c){
		if (c.equals( "chicken")) {
			if (v.chicken!=0){
			v.chicken-=10;
			return true;
			}
			else
			return false;
		}
			
		if (c.equals("steak")){
			if (v.steak!=0){
				v.steak-=10;
			return true;
		}
		else
			return false;
		}
			
		if (c.equals( "salad")){
			if (v.salad!=0){
			v.salad-=10;
			return true;
		}
		else
			return false;
		}
		if (c.equals( "pizza")){
			if (v.pizza!=0){
			v.pizza-=10;
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
		String item= items.getFirst();
		Do("RESTOCKED: "+item);
		cook.msgHereAreItems(item);
		stateChanged();
	}
	private void setCook(CookAgent cook2) {
		this.cook=cook2;
		
	}
	
}
	// The animation DoXYZ() routines
	


	
	




















