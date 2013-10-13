package restaurant;

import restaurant.WaiterAgent.mycustomer;
import restaurant.gui.CustomerGui;
import agent.Agent;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent {
	
	
	private int tnum;
	private double cashmoney, customer_check;
	
	private String name, choice;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	

	// agent correspondents
	private HostAgent host;
	private WaiterAgent waiter;
	private CashierAgent cashier;
	private CookAgent cook;
	private boolean good_call;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, WaitingForWaiter,  WaitingForFood, readytopay, Eating, DoneEating, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, readytoOrder,x, ordered,gotFood, doneEating, paying, Leaving,doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		double temp= 5+(double)(Math.random()*(15));
		DecimalFormat f =new DecimalFormat("##.00");
		String formate=f.format(temp);
		try {
			cashmoney=(Double)f.parse(formate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("$$$= "+cashmoney);
		customer_check=0;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setWaiter(WaiterAgent waitr) {
		this.waiter = waitr;
	}
	public void setHost(HostAgent host) {
		this.host = host;
	}
	public void setCook(CookAgent cook) {
		this.cook = cook;
	}
	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgSitAtTable(WaiterAgent w, int a) {
		print("Received msgSitAtTable");
		waiter=w;
		event = AgentEvent.followWaiter;
		stateChanged();
		tnum=a;
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgWhatsYourOrder(){
		event = AgentEvent.ordered;
		
		stateChanged();
	}
	public void msgReorder(int table, String c){
		Do("recieved msg to reorder "+this.state);
		event = AgentEvent.x;
		Do("recieved msg to reorder "+this.event);
		
		String c_;
		do {c_= g_choice();
		}while(c_==c);
		this.choice=c_;
		stateChanged();
	}
	public void msgFoodIsHere(){
		event = AgentEvent.gotFood;
		stateChanged();
	}
	public void msgHereIsCheck(double c){
		Do("Recieved Check");
		if(cashmoney>c){
		customer_check=c;
		}
		else
			customer_check=cashmoney;
		event = AgentEvent.paying;
		stateChanged();
	}
	public void msgPaying() {
		//from animation
		cashmoney-=waiter.Menu.get(this.choice);
		event = AgentEvent.Leaving;
		
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
		Do("Has left restuarant");
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			LookingAtMenu();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.readytoOrder){
			state = AgentState.WaitingForWaiter;
			CallWaiter();
			return true;
		}
		if (state ==  AgentState.WaitingForWaiter && event == AgentEvent.ordered){
			state = AgentState.WaitingForFood;
			if (cashmoney>=6){
			choice=g_choice();
			Order(choice);
			return true;
			}
		}

		if (state ==  AgentState.WaitingForFood && event == AgentEvent.x){
			//state= AgentState.BeingSeated;
			Do("back to order");
			Reorder();
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.gotFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.readytopay;
			getCheck();
			return true;
		}
		if (state == AgentState.readytopay && event == AgentEvent.paying){
			state = AgentState.Leaving;
			LeaveToPay();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.Leaving){
			state = AgentState.DoingNothing;
			AtCashiers();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tnum);//hack; only one table
	}
	private void LookingAtMenu(){
		Do("Looking at Menu");
		Do("MENU: "+ cook.Menu.entrySet());
		timer.schedule(new TimerTask(){
			Object cookie = 1;
			public void run() {
				event = AgentEvent.readytoOrder;
				stateChanged();
			}
		}, 5000);
	}
	private void CallWaiter(){
		Do("Call Waiter");
		waiter.msgReadyToOrder(this);
	}
	private String g_choice(){


		while(true){
		
			Random g = new Random();
			Object[] values =cook.Menu.values().toArray();
			Double r_val = (Double) values[g.nextInt(values.length)];
			Do("making choice: "+r_val);
			Do("cash: "+cashmoney);
			if (r_val==11.99){
				if(cashmoney>=12){
					choice="chicken";
					break;
				}
			}
			if (r_val==15.99){
				if(cashmoney>=16){
					choice="steak";
					break;
				}
			}
			if (r_val==5.99){
				if(cashmoney>=6){
					choice="salad";
					break;
				}
			}
			if (r_val==8.99){
				if(cashmoney>=9){
					choice="pizza";
					break;
				}
			}
			}
		return choice;
	}
	private void Order (String ch){
		Do("Order "+ch);
		waiter.msgHereIsMyChoice(this, ch);
	}
	private void Reorder(){
		Do("reOrder ");
		waiter.msgHereIsMyChoice(this, this.choice);
	}
	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	private void getCheck(){
		waiter.msgReadyToPay(this);
	}
	private void LeaveToPay() {
		Do("Leaving to pay.");
		customerGui.DoExitRestaurant();
		
	}
	private void AtCashiers(){
		Do("At cashiers");
		cashier.msgHereIsMoney(this, customer_check);
		cashmoney-=customer_check;
		host.msgLeavingTable(this);
		
	}
	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public static mycustomer find(CustomerAgent c) {
		return null;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	
}










































