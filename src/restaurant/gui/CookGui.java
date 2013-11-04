package restaurant.gui;


import restaurant.CookAgent;

import java.awt.*;

public class CookGui implements Gui {

    private CookAgent agent = null;

    private int xPos = 370, yPos = 300;//default waiter position
    private int xDestination = 370, yDestination = 300;//default start position
  


    public CookGui(CookAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    	
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == 350) & (yDestination == 290)) {
           Cook();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void Prep(){

    	
        xDestination = 350;
        yDestination = 290;
    			
    }

    public void Cook() {
        xDestination = 360;
        yDestination = 320;
    }
    
    public void Plate() {
        xDestination = 350;
        yDestination = 300;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
