package restaurant;

import agent.Agent;
import restaurant.MarketAgent;
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
	public List<MarketAgent> markets
	= new ArrayList<MarketAgent>();
	public Map<String,Double> Menu= new HashMap<String, Double>();

	public class inventory{
		int steak;
		boolean steak_low=false;
		boolean steak_gone=false;
		int chicken;
		boolean chicken_low=false;
		boolean chicken_gone=false;
		int salad;
		boolean salad_low=false;
		boolean salad_gone=false;
		int pizza;
		boolean pizza_low=false;
		boolean pizza_gone=false;
		
		inventory( int st, int ch, int sa, int pi ){
		steak=st;
		chicken=ch;
		salad=sa;
		pizza=pi;
		
		}
	}
	int x;
	public inventory v;
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
	String name;
	private boolean cooking=false;
	private int c_market=0;
	public void setWaiter(WaiterAgent waitr) {
		this.waiter = waitr;
	}
	// Messages
	
	public CookAgent(int st,int ch,int sa,int pi){
		super();
		v= new inventory(st,ch,sa,pi);
		this.name="Cook";
		cooking=false;
	Menu.put("chicken",10.99);	
		Menu.put("skeak",15.99);
		Menu.put("salad",5.99);
		Menu.put("pizza",8.99);
		
	}
	public String getName() {
		return name;
	}
	public void hack_chicken(){
		v.chicken=0;
	}
	public void hack_salad(){
		v.salad=0;
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
	
	public void msgHereAreItems(String item){
		Do("Recieved msg items are restocked");
				v.steak=+10;
				if(item.equals("steak")){
			v.steak_low=false;
			if(v.steak_gone==true){
				addToMenu("steak");
			}
		}
		if(item.equals("chicken")){
			v.chicken=+10;
			v.chicken_low=false;
			if(v.chicken_gone==true){
				addToMenu("chicken");
			}
		}
		if(item.equals("salad")){
			v.salad=+10;
			v.salad_low=false;
			if(v.salad_gone==true){
				addToMenu("salad");
			}
		}
		if(item.equals("pizza")){
			v.pizza=+10;
			v.pizza_low=false;
			if(v.pizza_gone==true){
				addToMenu("pizza");
			}
		}
			
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
		if (c.equals("chicken")) {
			if (v.chicken!=0){
			v.chicken--;
				if(v.chicken<=5){
					v.chicken_low=true;
					callMarket(c);
					Do("order chicken !!!!");
				}
			return true;
			}
			else{
				callMarket(c);
			Do("order chicken !!!!");
			return false;
			}
		}
			
		if (c.equals("steak")){
			if (v.steak!=0){
			v.steak--;
			if(v.steak<=5){
				v.steak_low=true;
				callMarket(c);
			}
			return true;
		}
		else{
			callMarket(c);
			return false;
		}
		}
			
		if (c.equals( "salad")){
			if (v.salad!=0){
			v.salad--;
			if(v.salad<=5){
				v.salad_low=true;
				callMarket(c);
			}
			return true;
		}
		else{
			callMarket(c);
			return false;
		}
		}
		if (c.equals( "pizza")){
			if (v.pizza!=0){
			v.pizza--;
			if(v.pizza<=5){
				v.pizza_low=true;
				callMarket(c);
			}
			return true;
		}
		else
		{
			callMarket(c);
			return false;
		}
		}
		return false;
	}
	private void notcool(order o,int t){
		Do("not cool-"+o.choice+ " is out");
		setWaiter(o.w);
		removeFromMenu(o.choice);
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
		return 0;
	}
	
	private void PlateIt(order o){
		setWaiter(o.w);
		Do("done plating");
		waiter.msgFoodReady(o.table);
		orders.remove(o);
		
	}
	private void removeFromMenu(String choice){
		Menu.remove(choice);
		if(choice.equals("steak")){
			v.steak_gone=true;
		}
		if(choice.equals("chicken")){
			v.chicken_gone=true;
		}
		if(choice.equals("salad")){
			v.salad_gone=true;
		}
		if(choice.equals("pizza")){
			v.pizza_gone=true;
		}
	}
	private void addToMenu(String choice){
		if(choice.equals("steak")){
			Menu.put("steak",15.99);
			v.steak_gone=false;
		}
		if(choice.equals("chicken")){
			Menu.put("chicken",11.99);
			v.chicken_gone=false;
		}
		if(choice.equals("salad")){
			Menu.put("salad",5.99);
			v.salad_gone=false;
		}
		if(choice.equals("pizza")){
			Menu.put("pizza",8.99);
			v.pizza_gone=false;
		}
	}
	private void callMarket(String item){
		markets.get(c_market).msgLowOnItem(this, item);
		c_market++;
	}
	

	// The animation DoXYZ() routines
	

	//utilities

public void addMarket(MarketAgent m){
	markets.add(m);
}




}
































	
	




















