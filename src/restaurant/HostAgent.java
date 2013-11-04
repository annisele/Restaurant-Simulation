package restaurant;

import agent.Agent;
import restaurant.WaiterAgent.mycustomer;
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
public class HostAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public int waiternum;
	public Map<Integer,Boolean> waitingSpots= new HashMap<Integer, Boolean>();
	public Map<Integer,Boolean> waiterSpots= new HashMap<Integer, Boolean>();
	
	public List<CustomerAgent> waitingCustomers
	= new ArrayList<CustomerAgent>();
	public List<WaiterAgent> waiters
	= new ArrayList<WaiterAgent>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
    public int currentwaiter=0;
	private String name;

	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();
		waitingSpots.put(0,false);
		waitingSpots.put(1,false);
		waitingSpots.put(2,false);
		waitingSpots.put(3,false);
		waiterSpots.put(0,false);
		waiterSpots.put(1,false);
		waiterSpots.put(2,false);
		waiterSpots.put(3,false);
		waiterSpots.put(4,false);
		waiterSpots.put(5,false);
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
		
	}
	// Messages
	public void msgAskToBreak(WaiterAgent w){
		if(waiters.size()>1){
			w.msgCanBreak(true);
			waiters.remove(w);
		}
		else
			w.msgCanBreak(false);
	}
	public void msgRestate(WaiterAgent w){
		waiters.add(w);
	}
	public void msgIWantFood(CustomerAgent cust) {
		waitingCustomers.add(cust);
		stateChanged();
		Do(cust.getName());
	
	}

	public void msgLeavingTable(CustomerAgent cust) {
		for(Table t:tables){
			if(t.getOccupant()==cust){
				print(cust + " leaving " + t);
				t.setUnoccupied();
				stateChanged();
			}
		}
	
	}
//msg break
	//if only waiter null
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					if(!waiters.isEmpty()){			
					assignWaiter(waitingCustomers.get(0),  table);//the action
					
					
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
					else
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

	private void assignWaiter(CustomerAgent customer,  Table t) {
		//msgsitattable to waiter include tablenum 
		Do("assign "+currentwaiter);
		
	
		waiters.get(currentwaiter).msgSeatCustomer(customer, t.tableNumber);
		currentwaiter++;
		Do("assign "+currentwaiter+" "+waiters.size());
		if(currentwaiter>=waiters.size()){
			currentwaiter=0;
		}
		Do("assign hey "+currentwaiter);
		t.setOccupant(customer);
		waitingCustomers.remove(customer);
	}

	
//wanttobreak
	//mesg
	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	public void addWaiter(WaiterAgent we){
		
		waiters.add(we);
		Do("added "+waiters.size());
	}
	private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

}
































