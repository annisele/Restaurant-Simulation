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
public Map<String,Double> Menu= new HashMap<String, Double>();
private HostAgent host;
private CookAgent cook;
private CashierAgent cashier;
enum CustomerState{
	waiting,seated,asked, readytoorder, askedtoorder,ordered, 
	waitingfororder, mustreorder, pend, waitingforfood, eating, waitingtopay,leaving;
}
class mycustomer {
	CustomerAgent c;
	int table_num;
	boolean at_table;
	double check;
	String choice;
	CustomerState state;
	
	mycustomer(CustomerAgent cust, int tablenum){
	c=cust;
	table_num=tablenum;
	state= CustomerState.waiting;
	check=0;
	
	}
}
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atLobby =new Semaphore(1,true);
	public boolean w_at_table;
	public boolean w_at_lobby;
	private boolean WantToGoOnBreak=false;

	public WaiterGui waiterGui = null;

	public WaiterAgent(String name) {
		super();
		Menu.put("chicken",10.99);	
		Menu.put("skeak",15.99);
		Menu.put("salad",5.99);
		Menu.put("pizza",8.99);
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
	public void setCashier(CashierAgent c) {
		this.cashier = c;
	}
	// Messages
	public void wantsaBreak(){
		Do("waiter wants break");
		WantToGoOnBreak=true;
		waiterGui.DoLeaveCustomer();
	}
 
	public void msgSeatCustomer(CustomerAgent c, int table_num){
		Do("Recieved msg seat "+c);
		//GetsMenu();
		mycustomer cust = new mycustomer (c, table_num);
		cust.state= CustomerState.waiting;
		customers.add(cust);
	stateChanged();
		
	}
		public void msgReadyToOrder(CustomerAgent c){
			Do("Recieved msg "+c+" is ready to order");
				
			for (int i = 0; i< customers.size();i++){
				
				if (customers.get(i).c == c){
					customers.get(i).state=CustomerState.askedtoorder;
					stateChanged();
				}
			}
			try {
				atLobby.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
	public void msgHereIsMyChoice(CustomerAgent c, String choice){
		//findthingforme();
		Do("Recieved msg "+c+" has made choice");
		
		for (int i = 0; i< customers.size();i++){
			
			if (customers.get(i).c == c){
				customers.get(i).state=CustomerState.ordered;
				customers.get(i).choice=choice;
				stateChanged();
				}
		
		//waitForOrder.release();
		//customers.remove(customer);
		//waiterGui.DoLeaveCustomer();
		//w_at_table=false;
		
	}
	}
	
	public void msgnofood(int table){
		Do("recieved msg that food is unavailable");
		waiterGui.DoBringToTable(table); 
		for (int i = 0; i< customers.size();i++){
			if(customers.get(i).table_num==table){
				customers.get(i).state=CustomerState.mustreorder;
				stateChanged();
			}
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
	public void msgReadyToPay(CustomerAgent c){
		Do("Recieved msg cust is ready to pay");
		
		for (int i = 0; i< customers.size();i++){
			if(customers.get(i).c==c){
				customers.get(i).check=cashier.msgGetCheck(c);
				waiterGui.DoBringToTable(customers.get(i).table_num); 
				customers.get(i).state=CustomerState.waitingtopay;
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
	public void msgAtLobby() {//from animation
		

			atLobby.release();// = true;
			w_at_lobby=true;
			
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
					if(w_at_lobby==true){
					SeatCustomer(c, c.table_num);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
				}
				if (c.state == CustomerState.askedtoorder) {
					if(w_at_table==true||w_at_lobby==false){
						Do("waiter at table and customer is ready to order");
					TakeOrder(c);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
					}
					else
					{
						waiterGui.DoBringToTable( c.table_num);
						Do("walking");
						w_at_lobby=false;
					}
				}
				if (c.state == CustomerState.ordered) {
					Do("customer has ordered "+c.choice);
					if(w_at_lobby==true){
					CallCook(c,c.table_num,c.choice);
					return true;//return true to the abstract agent to reinvoke the scheduler.
					}
					else
						waiterGui.DoLeaveCustomer();
				}
				if (c.state == CustomerState.mustreorder) {
					if(w_at_table==true){
					Do("customer must reorder");
					//waiterGui.DoBringToTable( c.table_num);
					c.c.msgReorder(c.table_num,c.choice);
					c.state=CustomerState.pend;
					return true;//return true to the abstract agent to reinvoke the scheduler.
					}
					else
					{
						waiterGui.DoBringToTable( c.table_num);
						Do("walking");
					}
				}
				if (c.state == CustomerState.waitingforfood) {
					if(w_at_table==true){
						Do("customer recieved order");
						BroughtFood(c);
						c.state = CustomerState.eating;
							
							return true;
				//return true to the abstract agent to reinvoke the scheduler.
					}
					else
					{
						waiterGui.DoBringToTable( c.table_num);
						Do("walking");
					}
				}
				if(c.state == CustomerState.waitingtopay){
					if(w_at_table==true){
						c.c.msgHereIsCheck(c.check);
						Do("customer recieved check");
						waiterGui.DoLeaveCustomer();
						customers.remove(c);
						return true;
					}
				}
					
					
			}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
/*	private void GetsMenu (){
		if(chicken_low==false){
		Menu.put("chicken",10.99);
		}
		if(steak_low==false){
		Menu.put("skeak",15.99);
		}
		if(salad_low==false){
		Menu.put("salad",5.99);
		}
		if(pizza_low==false){
		Menu.put("pizza",8.99);
		}
		
		Do("MENU: "+ Menu.entrySet());
	}*/
	private void SeatCustomer(mycustomer customer, int table) {
		try {
			atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w_at_lobby=false;
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
	//go to table
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	c.c.msgWhatsYourOrder();
	c.state=CustomerState.asked;
	//leave customer
	
}
//reorder function
public void BroughtFood(mycustomer c){
	waiterGui.DoLeaveCustomer();
	c.c.msgFoodIsHere();
}
public void CallCook(mycustomer c, int tnum, String choice){
	cashier.msgCustomerOrder(c.c,choice);
	cook.msgCookOrder(this, tnum, choice);
	Do("gives cashier order");
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





























