package restaurant;

import agent.Agent;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.gui.RestaurantGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.

	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
public List <mycustomer>customers
= new ArrayList<mycustomer>();
private HostAgent host;
private CookAgent cook;
enum CustomerState{
	waiting,seated,asked, readytoorder, askedtoorder,ordered, 
	waitingfororder, waitingforfood, eating, leaving;
}
class mycustomer {
	CustomerAgent c;
	int table_num;
	boolean at_table;
	
	String choice;
	CustomerState state;
	
	mycustomer(CustomerAgent cust, int tablenum){
	c=cust;
	table_num=tablenum;
	state= CustomerState.waiting;
	
	}
}
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore waitForOrder =new Semaphore(0,true);
	public boolean w_at_table;

	public WaiterGui waiterGui = null;

	public WaiterAgent(String name) {
		super();

		this.name = name;
		
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return customers;
	}
	
	public void setHost(HostAgent host) {
		this.host = host;
	}
	public void setCook(CookAgent cook) {
		this.cook = cook;
	}
	// Messages
 
	public void msgSeatCustomer(CustomerAgent c, int table_num){
		Do("Recieved msg seat cust");
		mycustomer cust = new mycustomer (c, table_num);
		cust.state= CustomerState.waiting;
		customers.add(cust);
	stateChanged();
		
	}
		public void msgReadyToOrder(CustomerAgent c){
			Do("Recieved msg ready to order");
			
			mycustomer mc=customers.get(0);
			
			for (int i = 0; i< customers.size();i++){
				
				if (customers.get(i).c == c){
					customers.get(i).state=CustomerState.askedtoorder;
					stateChanged();
				}
				else
					mc= null;
			}
			
				
		
		
	}
		
	public void msgHereIsMyChoice(CustomerAgent c, String choice){
		//findthingforme();
		Do("Recieved msg here is mychoice");
		
		for (int i = 0; i< customers.size();i++){
			
			if (customers.get(i).c == c){
				customers.get(i).state=CustomerState.ordered;
				customers.get(i).choice=choice;
					stateChanged();
				}
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//waitForOrder.release();
		//customers.remove(customer);
		//waiterGui.DoLeaveCustomer();
		//w_at_table=false;
		
	}
	}
	public void msgFoodReady(int table){
		Do("Recieved msg from cook that food is ready");
		waiterGui.DoBringToTable(table); 
		for (int i = 0; i< customers.size();i++){
			if(customers.get(i).table_num==table){
				customers.get(i).state=CustomerState.waitingforfood;
				stateChanged();
			}
		}
		
	}


	public void msgAtTable() {//from animation
	print("at table");

		atTable.release();// = true;
		w_at_table=true;
		
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
	
		for (mycustomer c : customers) {
	
				if (c.state == CustomerState.waiting) {
					SeatCustomer(c, c.table_num);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
				if (c.state == CustomerState.askedtoorder) {
					if(w_at_table==true){
						Do("waiter at table and customer is ready to order");
					TakeOrder(c);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
					}
					else
					{
						waiterGui.DoBringToTable( c.table_num);
						Do("walking");
					}
				}
				if (c.state == CustomerState.ordered) {
					Do("customer has ordered "+c.choice);
					waiterGui.DoLeaveCustomer();
					CallCook(c,c.table_num,c.choice);
					return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				if (c.state == CustomerState.waitingforfood) {
					if(w_at_table==true){
						Do("customer recieved order");
						waiterGui.DoLeaveCustomer();
						c.c.msgFoodIsHere();
						c.state = CustomerState.eating;
							customers.remove(c);
							return true;
				//return true to the abstract agent to reinvoke the scheduler.
					}
					else
					{
						waiterGui.DoBringToTable( c.table_num);
						Do("walking");
					}
				}

					
					else
						Do(" ");
			}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void SeatCustomer(mycustomer customer, int table) {
		
		customer.c.msgSitAtTable(this, table);
		customer.state=CustomerState.seated;
		DoSeatCustomer(customer.c, table);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//customers.remove(customer);
		waiterGui.DoLeaveCustomer();
		w_at_table=false;
		
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		//int tnum=tables.size();
		int []tablelist = new int[3];
		tablelist[0]=200;
		tablelist[1]=300;
		tablelist[2]=400;
		//int tablenum= table.tableNumber;
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(table); 

	}

public void TakeOrder (mycustomer c){
	Do("take order");
	c.c.msgWhatsYourOrder();
	c.state=CustomerState.asked;
}
public void CallCook(mycustomer c, int tnum, String choice){
	
	cook.msgCookOrder(this, tnum, choice);
	c.state=CustomerState.waitingfororder;
}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}


/*
		public String toString() {
			return "table " + tableNumber;
		}*/
	}





























