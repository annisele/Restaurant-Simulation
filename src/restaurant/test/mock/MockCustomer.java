
package restaurant.test.mock;

import restaurant.interfaces.Customer;
import restaurant.interfaces.Cashier;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;

public class MockCustomer extends Mock implements Customer{
	
	public Cashier cashier; 
	//public EventLog log;
	public MockCustomer(String name){
		super(name);
	}


	@Override
public void setWaiter(WaiterAgent waitr){
	
	}
	
	@Override
	public void setHost(HostAgent host){
		
	}
	@Override
	public void setCook(CookAgent cook) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setCashier(CashierAgent cashier) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hack_chicken() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hack_salad() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hack_steak() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgSitAtTable(WaiterAgent w, int a) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgWhatsYourOrder() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgReorder(int table, String c) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgFoodIsHere() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public  void msgHereIsCheck(double c) {
		  log.add(new LoggedEvent("Received check from cashier"));
/*
	        if(this.name.toLowerCase().contains("thief")){
	                //test the non-normative scenario where the customer has no money if their name contains the string "theif"
	                cashier.IAmShort(this, 0);

	        }else if (this.name.toLowerCase().contains("rich")){
	                //test the non-normative scenario where the customer overpays if their name contains the string "rich"
	                cashier.HereIsMyPayment(this, Math.ceil(total));

	        }else{
	                //test the normative scenario
	                cashier.HereIsMyPayment(this, total);
	        }
		*/
	}
	
	@Override
	public void msgPaying() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	
}