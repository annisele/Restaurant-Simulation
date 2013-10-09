package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent.AgentState;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	//Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Sarah");
	private CookAgent cook = new CookAgent();
	private HostGui hostGui = new HostGui(host);

	private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
	private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customer");
	private ListPanel waiterPanel = new ListPanel(this, "Waiter");

	private JPanel group = new JPanel();
	private String type;

	private RestaurantGui gui; //reference to main gui

	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;

		host.setGui(hostGui);


		gui.animationPanel.addGui(hostGui);

		host.startThread();
		cook.startThread();
		/*this.addPerson("Waiter", "waiter 1", false);
        this.addPerson("Waiter", "waiter 2", false);
        this.addPerson("Waiter", "waiter 3", false);*/
		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(2, 1, 10, 10));

		group.add(customerPanel);
		group.add(waiterPanel);
		initRestLabel();
		add(restLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(new JLabel("               "), BorderLayout.EAST);
		restLabel.add(new JLabel("               "), BorderLayout.WEST);
	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 * 
	 */

	public void showInfo(String type, String name) {

		if (type.equals("Customer")) {
			System.out.println("CUST "+type+" "+name);
			for (int i = 0; i < customers.size(); i++) {
				if (customers.get(i).getName() == name){
					System.out.println("CUST processed: "+ name);
					gui.updateInfoPanel(customers.get(i));
				}
			}
		}
		if (type.equals("Waiter")) {
			System.out.println("WAIT");
			for (int i = 0; i < waiters.size(); i++) {
				//WaiterAgent temp = waiters.get(i);
				if (waiters.get(i).getName().equals(name)){
					System.out.println("WAIT processed");
					gui.updateInfoPanel(waiters.get(i));
				}
			}

		}
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	public void addPerson(String type, String name, boolean hungry) {

		if (type.equals("Customer")) {
			System.out.println("add person- cust");
			CustomerAgent c = new CustomerAgent(name);	
			CustomerGui g = new CustomerGui(c, gui);
			//WaiterAgent w = new WaiterAgent(name);	

			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setGui(g);
			customers.add(c);
			if (hungry==true){
				c.getGui().setHungry();
			}
			c.startThread();
		}

		if (type.equals("Waiter")) {
			System.out.println("add person- waiter");
			WaiterAgent w = new WaiterAgent(name);	
			WaiterGui g = new WaiterGui(w, gui);

			gui.animationPanel.addGui(g);// dw
			w.setHost(host);
			w.setCook(cook);
			w.setGui(g);

			waiters.add(w);
			host.addWaiter(w);

			w.startThread();
		}
	}

	//use same function for cook and waiter
}
