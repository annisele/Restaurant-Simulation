package restaurant;

import agent.Agent;
import restaurant.MarketAgent;
import restaurant.CookAgent.OrderState;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.HostGui;
import restaurant.interfaces.*;

import restaurant.interfaces.Cashier;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends Agent implements Cashier {
	
	
	private WaiterAgent waiter;
	private CustomerAgent cust;
	int revenue;
	public List<order> cashiers_list
	= new ArrayList<order>();
	public List<order> bad_orders
	= new ArrayList<order>();
	private Map<String,Double> Menu= new HashMap<String, Double>();
	public class order{
		int cost;
		String choice;
		Customer cust;
		double payment,debt;
		OrderState state;
		
		order(Customer c, String item ){
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
	public void setCustomer(CustomerAgent c) {
		this.cust = c;
	}
	// Messages
	
	public CashierAgent(){
		super();
		revenue=0;
		Menu.put("chicken",10.99);	
		Menu.put("steak",15.99);
		Menu.put("salad",5.99);
		Menu.put("pizza",8.99);
		
	}

	@Override
	public void msgCustomerOrder(Customer c, String ch) {
		Do("Recieved "+c.getCustomerName()+"'s "+ch+" order");
		order o= new order(c,ch);
		o.state= OrderState.adding;
		Do("order: "+o.choice+" cust: "+o.cust);
		cashiers_list.add(o);
		stateChanged();
	}
	@Override
	public double msgGetCheck(Customer c){
		Do("Giving check to waiter");
		for(order current: cashiers_list){
			if(current.cust==c){
				return current.payment;
			}
	}
		return 0;
	}
	public void msgHereIsBill(Double p){
		Do("Recieve bill from market");
		if(revenue>=p){
		revenue-=p;
		Do("Market has been paid in full.");
		}
		else{
			revenue=0;
			double m_debt= p-revenue;
			Do("Casier owes $"+m_debt);
		}
		
	}
	@Override
	public void msgHereIsMoney(Customer c, Double m){
		Do("Recieved money from cust");
		CheckPayment(c,m);
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 * @return 
	 */
	public boolean pickAndExecuteAnAction() {
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
		//cust.msgTEST();
	o.payment=Menu.get(o.choice);
	Do("creating $"+o.payment+" check for "+o.cust);
	o.state= OrderState.waiting;
	stateChanged();
			}
		
		
	
	private void CheckPayment(Customer c,double m){
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
































	
	




















