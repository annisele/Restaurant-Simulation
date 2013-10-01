package restaurant;

import agent.Agent;
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
public class CookAgent extends Agent {
	
	
	private WaiterAgent waiter;
	public class order{
		WaiterAgent w;
		String choice;
		int table;
		OrderState state;
		
		order(WaiterAgent w_, String c_, int t_){
			w=w_;
			choice=c_;
			table=t_;
			state=OrderState.prep;
		}
	}
	public List <order> orders= new ArrayList<order>();
	public enum OrderState
	{ prep, cooking, done };
	public int num=0;
	Timer timer = new Timer();
	private boolean cooking=false;
	
	
	public void setWaiter(WaiterAgent waitr) {
		this.waiter = waitr;
	}
	// Messages
	
	public CookAgent(){
		super();
		cooking=false;
	}

	public void msgCookOrder(WaiterAgent w, int tnum, String choice) {
		Do("Recieve msg to cook order");
		//waiter=w;
		order o = new order (w,choice, tnum);
		//o.state= OrderState.prep;
		orders.add(o);
		stateChanged();
	}
	public void msgFoodDone(order oo) {
		oo.state= OrderState.done;
		stateChanged();
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
		if(cooking==false){
		for (order o : orders) {
			if (o.state == OrderState.prep) {
				cooking= true;
				CookIt(o);
				return true;
			}
			
			if (o.state == OrderState.done) {
				PlateIt(o);
				
				return true;
			}
		}
		
		}
		return false;
	}

	// Actions

	private void CookIt(order o){
		find(o);
		timer.schedule(new TimerTask(){
			Object cookie = 1;
			
			public void run() {
				orders.get(num).state= OrderState.done;
				Do("finished cooking");
				cooking=false;
				stateChanged();
			}
		}, 3000);
		
	}
	private void find(order o){
		
		for(int i=0; i<orders.size();i++){
			if( orders.get(i)==o){
			num=i;
			}
		}
	}
	
	private void PlateIt(order o){
		setWaiter(o.w);
		Do("done plating");
		waiter.msgFoodReady(o.table);
		orders.remove(o);
		
	}
}
	// The animation DoXYZ() routines
	

	//utilities

	
	




















