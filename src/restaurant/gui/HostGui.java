package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int tablenum;
    
    public static final int xTable = 200;
    public static final int yTable = 250;

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    	int tempnum = tablenum*100 +100;
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
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int b, int[]a){

    	
    	tablenum= b;
    	if (b==1){
        xDestination = a[0] + 20;
        yDestination = yTable - 20;
    	}
    	if (b==2){
            xDestination = a[1] + 20;
            yDestination = yTable - 20;
        	}
    	if (b==3){
            xDestination = a[2] + 20;
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
