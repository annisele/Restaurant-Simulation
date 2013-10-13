package restaurant;

import agent.Agent;
import restaurant.MarketAgent;
import restaurant.CookAgent.OrderState;
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
public class CashierAgent extends Agent {
	
	
	private WaiterAgent waiter;
	int revenue;
	public List<order> cashiers_list
	= new ArrayList<order>();
	public List<order> bad_orders
	= new ArrayList<order>();
	public Map<String,Double> Menu= new HashMap<String, Double>();
	public class order{
		int cost;
		String choice;
		CustomerAgent cust;
		double payment,debt;
		OrderState state;
		
		order(CustomerAgent c, String item ){
		cust=c;
		choice=item;
		debt=0;
		state=OrderState.nothing;
		}
	}
	
	public enum OrderState
	{ nothing, adding, waiting, paid,indebt };
	public int num=0;
	Timer timer = new Timer();
	
	public void setWaiter(WaiterAgent waitr) {
		this.waiter = waitr;
	}
	// Messages
	
	public CashierAgent(){
		super();
		revenue=0;
		Menu.put("chicken",10.99);	
		Menu.put("skeak",15.99);
		Menu.put("salad",5.99);
		Menu.put("pizza",8.99);
		
	}

	
	public void msgCustomerOrder(CustomerAgent c, String ch) {
		Do("Recieved customer order");
		order o= new order(c,ch);
		o.state= OrderState.adding;
		cashiers_list.add(o);
		stateChanged();
	}
	public double msgGetCheck(CustomerAgent c){
		Do("Giving check to waiter");
		for(order current: cashiers_list){
			if(current.cust==c){
				return current.payment;
			}
	}
		return 0;
	}
	public void msgHereIsMoney(CustomerAgent c, Double m){
		Do("Recieved money from cust");
		CheckPayment(c,m);
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
		
		for (order o : cashiers_list) {
			if (o.state == OrderState.adding) {
				Do("adding");
				CreateCheck(o);
				return true;
			}
			if (o.state == OrderState.paid) {
				Do("Customer has paid in full");
				cashiers_list.remove(o);
				return true;
			}
			
			if (o.state == OrderState.indebt) {
				KeepTrack(o);
				
				return true;
			}
		}
		
		
		return true;
	}

	// Actions
	
	
	private void CreateCheck(order o){
		Do("creating check "+o.choice+" menu  "+Menu.entrySet());
		for(order current: cashiers_list){
			if(current==o){
	current.payment=Menu.get(o.choice);
	Do("customer"+o.cust+ "needs to pay: "+current.payment);
	current.state= OrderState.waiting;
	stateChanged();
			}
		}
		
	}
	private void CheckPayment(CustomerAgent c,double m){
		Do("checking payment");
		for(order current: cashiers_list){
			if(current.cust==c){
				if(current.payment==m){
					current.state= OrderState.paid;
					revenue+=m;
					stateChanged();
				}
				else{
					Do("wrong amount");
				current.state= OrderState.indebt;
				revenue+=m;
				current.debt=current.payment-m;
				stateChanged();
				}
			}
		}
	}
	private void KeepTrack(order o){
		Do("Customer "+ o.cust+ "is in debt");
		bad_orders.add(o);
	}
	
	
	

	// The animation DoXYZ() routines
	

	//utilities





}
































	
	




















