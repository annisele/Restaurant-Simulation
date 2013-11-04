
package restaurant.test.mock;

import restaurant.CashierAgent.order;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.interfaces.Cashier;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.*;

public class MockWaiter extends Mock implements Waiter{
	
	public Cashier cashier; 
	public MockWaiter(String name){
		super(name);
	}

	
	/*@Override
	public void msgHereIsCheck(Customer c, double d){
		  log.add(new LoggedEvent("test"));
	
	}*/
	
	
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
	public void wantsaBreak(){
		
	}@Override
	public void msgCanBreak(boolean bool){
		
	}@Override
	public void msgSeatCustomer(CustomerAgent c, int table_num){
		
	}@Override
	
		public void msgReadyToOrder(CustomerAgent c){
		
	}@Override
	public void msgHereIsMyChoice(CustomerAgent c, String choice){
		
	}@Override
	public void msgnofood(int table){
		
	}@Override
	public void msgFoodReady(int table){
		
	}@Override
	public void msgReadyToPay(CustomerAgent c){
		
	}@Override
	public void msgHereIsCheck(int table,double p){
		log.add(new LoggedEvent("waiter has recieved check"));
		 
	}@Override
	public void msgCustLeave(CustomerAgent c){
		
	}@Override
	public void msgAtTable(){
		
	}@Override
	public void msgAtLobby(){
		
	}@Override
	public void msgAtKitchen(){
		
	}





	
}