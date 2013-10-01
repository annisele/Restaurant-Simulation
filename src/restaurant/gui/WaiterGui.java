package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int tablenum;
    
    public static final int xTable = 100;
    public static final int yTable = 150;
    //public List<int>[] a= new ArrayList<int>[5]();
    int[] tablelist = new int[3];
   
	

    public WaiterGui(WaiterAgent agent,RestaurantGui gui) {
        this.agent = agent;
        tablelist[0]=100;
    	tablelist[1]=200;
    	tablelist[2]=300;
    }

    public void updatePosition() {
    	int tempnum = tablenum*100 ;
    	
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == tempnum + 20) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
        else
        	agent.w_at_table=false;
        	
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable( int b){

    	//agent.print("waiter animation is coming to table!");
    	tablenum= b;
    	if (b==1){
        xDestination = tablelist[0] + 20;
        yDestination = yTable - 20;
    	}
    	if (b==2){
            xDestination = tablelist[1] + 20;
            yDestination = yTable - 20;
        	}
    	if (b==3){
            xDestination = tablelist[2] + 20;
            yDestination = yTable - 20;
        	}
    	
    		
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}