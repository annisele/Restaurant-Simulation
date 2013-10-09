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
	public class inventory{
		int steak;
		int chicken;
		int salad;
		int pizza;
		
		inventory( int st, int ch, int sa, int pi ){
		steak=st;
		chicken=ch;
		salad=sa;
		pizza=pi;
		
		}
	}
	public inventory v= new inventory(5,0,5,5);
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
	{ prep, notready, done };
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
		if(checkorder(choice)==true){
		//o.state= OrderState.prep;
		orders.add(o);
		stateChanged();
		}
		else
			o.state=OrderState.notready;
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
			if (o.state == OrderState.notready) {
				cooking= false;
				Do("why");
				notcool(o, o.table);
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
	private boolean checkorder(String c){
		if (c.equals( "chicken")) {
			if (v.chicken!=0){
			v.chicken--;
			return true;
			}
			else
				Do("HEYY");
			return false;
		}
			
		if (c== "steak"){
			if (v.steak!=0){
			v.steak--;
			return true;
		}
		else
			return false;
		}
			
		if (c== "salad"){
			if (v.salad!=0){
			v.salad--;
			return true;
		}
		else
			return false;
		}
		if (c== "pizza"){
			if (v.pizza!=0){
			v.pizza--;
			return true;
		}
		else
			return false;
		}
		return false;
	}
	private void notcool(order o,int t){
		Do("not cool");
		setWaiter(o.w);
		waiter.msgnofood(t);
		orders.remove(o);
	}
	private void CookIt(order o){
		Do(""+o.choice);
		num=find(o);
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
	private int find(order o){
		
		for(int i=0; i<orders.size();i++){
			if( orders.get(i)==o){
			return i;
			}
		}
		Do("WRONG");
		return 0;
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

	
	




















