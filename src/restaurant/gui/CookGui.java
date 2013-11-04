package restaurant.gui;


import restaurant.CookAgent;

import java.awt.*;

public class CookGui implements Gui {

    private CookAgent agent = null;

    private int xPos = 370, yPos = 300;//default waiter position
    private int xDestination = 370, yDestination = 300;//default start position
   private String text=""; 


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
 public void setText(String t){
	 text=t;
 }
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
        g.setFont(new Font("Serif", Font.BOLD,12));
        g.setColor(Color.RED);
        g.drawString(text, xPos-20, yPos+10);
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
