package restaurant.test;
import java.text.ParseException;

import junit.framework.TestCase;
import restaurant.CashierAgent;
import restaurant.CashierAgent.OrderState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import static org.junit.Assert.*;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockCustomer customer;
	MockCustomer customer2;
	MockWaiter waiter;
	MockWaiter waiter2;
	MockMarket market;
	MockMarket market2;
	MockMarket market3;

	public void setUp() throws Exception{
		super.setUp();                
		cashier = new CashierAgent();                
		customer = new MockCustomer("mockcustomer"); 
		customer2 = new MockCustomer("mockcustomer2"); 
		waiter =new MockWaiter("mockwaiter");
		waiter2 =new MockWaiter("mockwaiter2");
		market =new MockMarket("mockmarket");
		market2 =new MockMarket("mockmarket2");
		market3 =new MockMarket("mockmarket3");
	}   

	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.                        
		Cashier.log.clear();
		MockWaiter.log.clear();
		MockCustomer.log.clear();
		MockMarket.log.clear();
		//check preconditions
		System.out.println("FIRST TEST NORM ONE MARKET ");
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.cashiers_list.size(), 0);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ Cashier.log.toString(), 0, Cashier.log.size());
		
		assertEquals(
                "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                + waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
                "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                + customer.log.toString(), 0, customer.log.size());
		assertEquals(
                "MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                + market.log.toString(), 0, market.log.size());

		//step 1 of the test
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

		cashier.msgCustomerOrder(waiter, customer,1, "chicken");
		//check postconditions for step 1 and preconditions for step 2


		assertTrue("Cashier should have logged \"Received order\" but didn't. His log reads instead: " 
				+ Cashier.log.getLastLoggedEvent().toString(), Cashier.log.containsString("Recieved order"));

		
		assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
				 
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.cashiers_list.get(0).state == OrderState.adding);
		assertTrue("Cashier's scheduler should have returned true (needs to react to order's adding), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		 
		assertTrue("MockWaiter should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("waiter has recieved check"));
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.cashiers_list.get(0).state == OrderState.waiting);
		
		//step 2 of the test
		cashier.addMarket(market);
		
		cashier.msgHereIsBill(market, 5.99);
		
		//check postconditions for step 2 / preconditions for step 3
		  assertTrue("Cashier should have logged \"Received bill\" but didn't. His log reads instead: " 
                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved bill"));
		 
		  //the balance is greater than the bill so market should be paid
		/*try{
			Thread.sleep(500);
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		  System.out.println("hersadds "+cashier.markets.size());
			
		 //assertTrue("CashierBill should contain a bill of price = $5.99. It contains something else instead: $" 
	       //          + cashier.markets.get(0).bill, cashier.markets.get(0).bill == 5.99);
		
		 // assertTrue("Cashier should have logged \"Market has been paid in full\" but didn't. His log reads instead: " 
           //       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Market has been paid in full"));
		  assertTrue("Cashier's scheduler should have returned false (order status has not changed), but didn't.", 
					cashier.pickAndExecuteAnAction());
		  
		
			
		  System.out.println("her "+ market.log.size());
			 
			assertTrue("Market should have logged \"Received money\" but didn't. His log reads instead: " 
		               + market.log.getLastLoggedEvent().toString(), market.log.containsString("market has recieved money"));
	
			
		  
		  //step 3

		  cashier.msgHereIsMoney(customer, 10.99);
		  assertTrue("Cashier should have logged \"Received money\" but didn't. His log reads instead: " 
                  + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved money"));
		  	
		//NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
		//
		assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
		
		//System.out.println("");
		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
                 + cashier.cashiers_list.get(0).payment, cashier.cashiers_list.get(0).payment == 10.99);
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.cashiers_list.get(0).state == OrderState.paid);
		assertTrue("Cashier's scheduler should have returned true , but didn't.", 
				cashier.pickAndExecuteAnAction());
		/* assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
                    
                       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved customer order"));
		 */
		assertEquals("Cashier should have 0 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 0);
		
		//check postconditions for step 3 / preconditions for step 4



	}//end one normal customer scenario
	public void testTwoNormalCustomerScenario()
	{
		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.                        
		Cashier.log.clear();
		MockWaiter.log.clear();
		MockCustomer.log.clear();
		MockMarket.log.clear();
		//check preconditions
		//setUp() runs first before this test!

				//check preconditions
		System.out.println("SECOND TEST NORM TWO MARKET ");
				assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.cashiers_list.size(), 0);                
				assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ Cashier.log.toString(), 0, Cashier.log.size());
				
				assertEquals(
		                "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
		                                + waiter.log.toString(), 0, waiter.log.size());

				assertEquals(
		                "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
		                                + customer.log.toString(), 0, customer.log.size());
				assertEquals(
		                "MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
		                                + market.log.toString(), 0, market.log.size());

				//step 1 of the test
				assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

				cashier.msgCustomerOrder(waiter, customer,1, "chicken");
				
				
				//check postconditions for step 1 and preconditions for step 2


				assertTrue("Cashier should have logged \"Received order\" but didn't. His log reads instead: " 
						+ Cashier.log.getLastLoggedEvent().toString(), Cashier.log.containsString("Recieved order"));

				
				assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
						 
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.adding);
				assertTrue("Cashier's scheduler should have returned true (needs to react to order's adding), but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				 
				assertTrue("MockWaiter should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
		                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("waiter has recieved check"));
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.waiting);
				
				//step 2 of the test
				cashier.addMarket(market);
				cashier.addMarket(market2);
			
				cashier.msgHereIsBill(market, 5.99);
				cashier.msgHereIsBill(market2, 10.99);
				//check postconditions for step 2 / preconditions for step 3
				  assertTrue("Cashier should have logged \"Received bill\" but didn't. His log reads instead: " 
		                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved bill"));
			
				  	
				 //assertTrue("CashierBill should contain a bill of price = $5.99. It contains something else instead: $" 
			       //          + cashier.markets.get(0).bill, cashier.markets.get(0).bill == 5.99);
				
				 // assertTrue("Cashier should have logged \"Market has been paid in full\" but didn't. His log reads instead: " 
		           //       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Market has been paid in full"));
				  assertTrue("Cashier's scheduler should have returned false (order status has not changed), but didn't.", 
							cashier.pickAndExecuteAnAction());
				  
				
					
				  System.out.println("her "+ market.log.size());
					 
					assertTrue("Market should have logged \"Received money\" but didn't. His log reads instead: " 
				               + market.log.getLastLoggedEvent().toString(), market.log.containsString("market has recieved money"));
					assertTrue("Market should have logged \"Received money\" but didn't. His log reads instead: " 
				               + market2.log.getLastLoggedEvent().toString(), market.log.containsString("market has recieved money"));
						  
				  //step 3

				  cashier.msgHereIsMoney(customer, 10.99);
				  assertTrue("Cashier should have logged \"Received money\" but didn't. His log reads instead: " 
		                  + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved money"));
				  	
				//NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
				//
				assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
				
				//System.out.println("");
				assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
		                 + cashier.cashiers_list.get(0).payment, cashier.cashiers_list.get(0).payment == 10.99);
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.paid);
				assertTrue("Cashier's scheduler should have returned true , but didn't.", 
						cashier.pickAndExecuteAnAction());
				/* assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
		                    
		                       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved customer order"));
				 */
				assertEquals("Cashier should have 0 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 0);
				
				//check postconditions for step 3 / preconditions for step 4


				/*    assertTrue("MockWaiter should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
		                        + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("test"));
				 */
				/*       assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
		                                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("test"));


		                assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
		                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
				 */



				/*      
		                //step 4
		                assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		                                        cashier.pickAndExecuteAnAction());

		                //check postconditions for step 4
		                assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
		                                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));


		                assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
		                                cashier.bills.get(0).state == cashierBillState.done);

		                assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
		                                cashier.pickAndExecuteAnAction());
				 */
	}//end two normal customer scenario
	public void testThreeNormalCustomerScenario()
	{
		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.                        
		Cashier.log.clear();
		MockWaiter.log.clear();
		MockCustomer.log.clear();
		MockMarket.log.clear();
		//check preconditions
		//setUp() runs first before this test!

				//check preconditions
		System.out.println("THIRD TEST NORM TWO CUSTOMERS ");
				assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.cashiers_list.size(), 0);                
				assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ Cashier.log.toString(), 0, Cashier.log.size());
				
				assertEquals(
		                "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
		                                + waiter.log.toString(), 0, waiter.log.size());

				assertEquals(
		                "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
		                                + customer.log.toString(), 0, customer.log.size());
				assertEquals(
		                "MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
		                                + market.log.toString(), 0, market.log.size());

				//step 1 of the test
				assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

				cashier.msgCustomerOrder(waiter, customer,1, "chicken");
				cashier.msgCustomerOrder(waiter, customer2,2, "pizza");
				//check postconditions for step 1 and preconditions for step 2


				assertTrue("Cashier should have logged \"Received order\" but didn't. His log reads instead: " 
						+ Cashier.log.getLastLoggedEvent().toString(), Cashier.log.containsString("Recieved order"));

				
				assertEquals("Cashier should have 2 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 2);
						 
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.adding);
				assertTrue("Cashier's scheduler should have returned true (needs to react to order's adding), but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				 
				assertTrue("MockWaiter should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
		                + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("waiter has recieved check"));
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.waiting);
				
				//step 2 of the test
				cashier.addMarket(market);
			
			
				cashier.msgHereIsBill(market, 5.99);
				//check postconditions for step 2 / preconditions for step 3
				  assertTrue("Cashier should have logged \"Received bill\" but didn't. His log reads instead: " 
		                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved bill"));
			
				  	
				 //assertTrue("CashierBill should contain a bill of price = $5.99. It contains something else instead: $" 
			       //          + cashier.markets.get(0).bill, cashier.markets.get(0).bill == 5.99);
				
				 // assertTrue("Cashier should have logged \"Market has been paid in full\" but didn't. His log reads instead: " 
		           //       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Market has been paid in full"));
				  assertTrue("Cashier's scheduler should have returned false (order status has not changed), but didn't.", 
							cashier.pickAndExecuteAnAction());
				  
				
					
				 	 
					assertTrue("Market should have logged \"Received money\" but didn't. His log reads instead: " 
				               + market.log.getLastLoggedEvent().toString(), market.log.containsString("market has recieved money"));
						  
				  //step 3

				  cashier.msgHereIsMoney(customer, 10.99);
				  assertTrue("Cashier should have logged \"Received money\" but didn't. His log reads instead: " 
		                  + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved money"));
				  	
				//NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
				//
				assertEquals("Cashier should have 2 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 2);
				
				//System.out.println("");
				assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
		                 + cashier.cashiers_list.get(0).payment, cashier.cashiers_list.get(0).payment == 10.99);
				assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
						cashier.cashiers_list.get(0).state == OrderState.paid);
				assertTrue("Cashier's scheduler should have returned true , but didn't.", 
						cashier.pickAndExecuteAnAction());
				/* assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
		                    
		                       + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved customer order"));
				 */
				System.out.println("HERE "+cashier.cashiers_list.size());
				assertEquals("Cashier should have 0 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
				
				//check postconditions for step 3 / preconditions for step 4


				/*    assertTrue("MockWaiter should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
		                        + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("test"));
				 */
				/*       assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
		                                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("test"));


		                assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
		                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
				 */



				/*      
		                //step 4
		                assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		                                        cashier.pickAndExecuteAnAction());

		                //check postconditions for step 4
		                assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
		                                + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));


		                assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
		                                cashier.bills.get(0).state == cashierBillState.done);

		                assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
		                                cashier.pickAndExecuteAnAction());
				 */
	}//end two normal customer scenari
	

}