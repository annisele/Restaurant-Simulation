package restaurant.test;
import junit.framework.TestCase;
import restaurant.CashierAgent;
import restaurant.CashierAgent.OrderState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
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
	MockWaiter waiter;
	MockMarket market;

	public void setUp() throws Exception{
		super.setUp();                
		cashier = new CashierAgent();                
		customer = new MockCustomer("mockcustomer"); 
		waiter =new MockWaiter("mockwaiter");
		market =new MockMarket("mockmarket");
	}   

	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.                        

		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.cashiers_list.size(), 0);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ Cashier.log.toString(), 0, Cashier.log.size());
		
		assertEquals(
                "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                + waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
                "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                + waiter.log.toString(), 0, waiter.log.size());

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
		cashier.msgHereIsBill(market, 5.99);

		//check postconditions for step 2 / preconditions for step 3
		  assertTrue("Cashier should have logged \"Received bill\" but didn't. His log reads instead: " 
                                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved bill"));
		 //the balance is greater than the bill so market should be paid
		  assertTrue("Cashier should have logged \"Market has been paid in full\" but didn't. His log reads instead: " 
                  + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Market has been paid in full"));
		  assertFalse("Cashier's scheduler should have returned false (order status has not changed), but didn't.", 
					cashier.pickAndExecuteAnAction());

		//step 3

		  cashier.msgHereIsMoney(customer, 10.99);
		  assertTrue("Cashier should have logged \"Received money\" but didn't. His log reads instead: " 
                  + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved money"));

		//NOTE: I called the scheduler in the assertTrue statement below (to succinctly check the return value at the same time)
		//
		assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 1);
		
		//System.out.println("OUT OF STOCK SENARIO "+cashier.cashiers_list.get(0).payment+" "+cashier.cashiers_list.size());
		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
                 + cashier.cashiers_list.get(0).payment, cashier.cashiers_list.get(0).payment == 10.99);
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.cashiers_list.get(0).state == OrderState.paid);
		assertTrue("Cashier's scheduler should have returned true , but didn't.", 
				cashier.pickAndExecuteAnAction());
		/* assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
                        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved customer order"));
		 */
		assertEquals("Cashier should have 1 order in cashier's list. It doesn't.", cashier.cashiers_list.size(), 0);
		
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

	}//end one normal customer scenario

}