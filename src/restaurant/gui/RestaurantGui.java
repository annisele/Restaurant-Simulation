package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel glabel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 350;

                //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
       // animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
    	
    	JFrame frame = new JFrame ("whatever");
    	JPanel panel = new JPanel ();
    	setBounds(50, 50, WINDOWX, WINDOWY);
setLayout(new BorderLayout());
        //setLayout(new BoxLayout((Container) getContentPane(), 
        	//	BoxLayout.Y_AXIS));
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        frame.add(restPanel, BorderLayout.WEST);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
       frame.add(infoPanel,BorderLayout.SOUTH);
       Dimension aniDim = new Dimension(WINDOWX, WINDOWY);
       animationPanel.setPreferredSize(aniDim);
       frame.add(animationPanel,BorderLayout.EAST);
       
       
     //  ImageIcon image =  new ImageIcon ("C:/Users/Lenovo/Desktop/game/medusa.png");
       
       //glabel = new JLabel();
      // glabel.setIcon(image);
     //glabel.setText("okokok");
    //  frame.add(glabel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;
       
        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) person;
            stateCB.setText("Go on Break????");
            //Should checkmark be there?
            stateCB.setEnabled(waiter.getGui().onBreak());
             stateCB.setSelected(!waiter.getGui().onBreak());
            //Is customer hungry? Hack. Should ask customerGui
          stateCB.validate();
            // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Waiter: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB && currentPerson instanceof CustomerAgent) {
        		System.out.println("create cust");
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
           
        }
        if (e.getSource() == stateCB && currentPerson instanceof WaiterAgent) {
            
        		System.out.println("create waitr");
                WaiterAgent w = (WaiterAgent) currentPerson;
                w.getGui().setBreak();
   
                stateCB.setEnabled(false);
  
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
        public void setWaiterEnabled(WaiterAgent w) {
        if (currentPerson instanceof WaiterAgent) {
            WaiterAgent wait = (WaiterAgent) currentPerson;
            if (w.equals(wait)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
       // gui.setTitle("csci201 Restaurant");
       // gui.setVisible(true);
        //gui.setResizable(false);
        //gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
