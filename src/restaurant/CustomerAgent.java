package restaurant;

import restaurant.WaiterAgent.mycustomer;
import restaurant.gui.CustomerGui;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent {
	
	
	private int tnum;
	private String name, choice;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	

	// agent correspondents
	private HostAgent host;
	private WaiterAgent waiter;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, WaitingForWaiter, WaitingForFood, Eating, DoneEating, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, readytoOrder, ordered,gotFood, doneEating, doneLeaving};
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
	public void msgFoodIsHere(){
		event = AgentEvent.gotFood;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
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
			choice=g_choice();
			Order(choice);
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.gotFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			leaveTable();
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
		Random g = new Random();
		int i = g.nextInt(4);
		if (i==0)
			choice="steak";
		if (i==1)
			choice="chicken";
		if (i==2)
			choice="salad";
		if (i==3)
			choice="pizza";
		return choice;
		
	}
	private void Order (String ch){
		Do("Order "+ch);
		waiter.msgHereIsMyChoice(this, ch);
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
	
	private void leaveTable() {
		Do("Leaving.");
		host.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
		event = AgentEvent.doneLeaving;
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










































