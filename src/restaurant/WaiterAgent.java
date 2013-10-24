package restaurant;
import restaurant.interfaces.*;
import restaurant.interfaces.Cashier;
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
private Cashier cashier;
enum CustomerState{
	waiting,seated,asked, readytoorder, askedtoorder,ordered, 
	waitingfororder, mustreorder, pend, waitingforfood, eating, 
	waitingtopay,finished,leaving;
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
	Timer breaktimer = new Timer();
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atLobby =new Semaphore(1,true);
	public boolean w_at_table;
	public boolean w_at_lobby;
	private boolean WantToGoOnBreak=false;
	private boolean WaitingToBreak=false;

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
	}
	public void msgCanBreak(boolean bool){
		if(bool==true){
			Do("host allow break");
			WaitingToBreak=true;
			WantToGoOnBreak=false;
		}
		if(bool==false){
			Do("host refuse break");
			WantToGoOnBreak=false;
			waiterGui.reset();
					
		}
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
		
		for (int i = 0; i< customers.size();i++){
			if(customers.get(i).table_num==table){
				customers.get(i).state=CustomerState.mustreorder;
				stateChanged();
			}
		}
	}
	public void msgFoodReady(int table){
		Do("Recieved msg from cook that food is ready");
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
				customers.get(i).state=CustomerState.waitingtopay;
				stateChanged();
			}
		}
	}

	public void msgCustLeave(CustomerAgent c){
		for (int i = 0; i< customers.size();i++){
			if (customers.get(i).c == c){
				customers.remove(customers.get(i));
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
	//if want to break
		//ask host
		if(WantToGoOnBreak==true){
			host.msgAskToBreak(this);
			return true;
		}
		if(WantToGoOnBreak==false&&WaitingToBreak==true){
			if(customers.size()==0){
				waiterGui.DoLeaveCustomer();
				waiterGui.set();
				Do("On break");
				breaktimer.schedule(new TimerTask(){
					Object cookie = 1;
					public void run() {
						EndBreak();
						WaitingToBreak=false;
					}
				}, 8000);
			}
		}
		for (mycustomer c : customers) {
	
				if (c.state == CustomerState.waiting) {
					if(w_at_lobby==true){
					SeatCustomer(c, c.table_num);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
				}
				
				if (c.state == CustomerState.askedtoorder) {
					
						Do("waiter at table and customer is ready to order");
					TakeOrder(c);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
					
				}
	
				if (c.state == CustomerState.ordered) {
					Do("customer has ordered "+c.choice);
					
					if(w_at_lobby==true){
						c.state=CustomerState.waitingfororder;
						DeliverOrder(c,c.table_num,c.choice);
						return true;
					}
					
						
					
				}
				if (c.state == CustomerState.mustreorder) {
					
					Do("customer must reorder");
					
					ReOrder(c);
					//waiterGui.DoBringToTable( c.table_num);
				
					c.state=CustomerState.pend;
					return true;//return true to the abstract agent to reinvoke the scheduler.
					
				}
				if (c.state == CustomerState.waitingforfood) {
					
						Do("customer recieved order");
						GettingFood(c);
						ServeFood(c);
						c.state = CustomerState.eating;
							
							return true;
				//return true to the abstract agent to reinvoke the scheduler.
					
				}
				if(c.state == CustomerState.waitingtopay){
				
						DeliverCheck(c);
						c.state=CustomerState.finished;
						return true;
					
				}
					
					
			}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	//goone
	private void SeatCustomer(mycustomer customer, int table) {
		waiterGui.DoLeaveCustomer();
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
	
	waiterGui.DoBringToTable( c.table_num);
	w_at_lobby=false;
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Do("take order");
	c.c.msgWhatsYourOrder();
	waiterGui.DoLeaveCustomer();
	w_at_table=false;
	
	
	
}
//reorder function
public void DeliverOrder (mycustomer c, int tnum, String choice){
 
 try {
		atLobby.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
 cook.msgCookOrder(this, tnum, choice);
}
public void GettingFood(mycustomer c){
	Do("waiter is picking up order");
	//getfood, go to lobby try at lobby
	//go to table
	waiterGui.DoLeaveCustomer();
	try {
		atLobby.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	 Do("gives cashier "+c.c+" order");
	 cashier.msgCustomerOrder(c.c,c.choice);
	
}
public void ServeFood(mycustomer c){
	Do("waiter is delivering order");
	waiterGui.DoBringToTable(c.table_num); 
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	c.c.msgFoodIsHere();
	waiterGui.DoLeaveCustomer();
}
/*servefood
 * at tabe aquire
 * msg customer here is food
 * leavecustomer
 */

public void ReOrder(mycustomer c){
	waiterGui.DoBringToTable(c.table_num); 
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	waiterGui.DoLeaveCustomer();
	w_at_table=false;
	c.c.msgReorder(c.table_num,c.choice);
	
}
public void DeliverCheck(mycustomer c){
Do("delivering check");
	c.check=cashier.msgGetCheck(c.c);
	waiterGui.DoBringToTable(c.table_num); 
	try {
		atTable.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	c.c.msgHereIsCheck(c.check);
	Do("customer recieved check");
	waiterGui.DoLeaveCustomer();
	customers.remove(c);
}

public void EndBreak(){
	Do("End break");
	host.msgRestate(this);
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





























