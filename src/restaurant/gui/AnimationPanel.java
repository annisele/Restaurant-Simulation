package restaurant.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import restaurant.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	static final int xloc =100;
	static final int yloc =150;
	static final int rectsize =50;
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;
	//private BufferedImage image;
    private List<Gui> guis = new ArrayList<Gui>();
  
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(8, this );
    	timer.start();
    	 try {                
             bufferImage = ImageIO.read(new File("C:/Users/Lenovo/Pictures/kitchen.png"));
          } catch (IOException ex) {
               // handle exception...
          }
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}


  

  

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        //Graphics2D w_area =(Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.WHITE);
        g2.fillRect(1, 1, 100, 50);
       // g2.setColor(Color.BLACK);
        //g2.fillRect(370, 275,100,75);
        g2.setColor(Color.BLACK);
        g2.fillRect(330, 290, 20,60);
        //Here is the table
        
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(xloc, yloc, rectsize, rectsize);//200 and 250 need to be table params
        g2.setColor(Color.ORANGE);
        g2.fillRect(xloc+100, yloc, rectsize, rectsize);
        g2.setColor(Color.ORANGE);
        g2.fillRect(xloc+200, yloc, rectsize, rectsize);
        g.drawImage(bufferImage, 350, 250, null);
        
        

        for(Gui gui : guis) {
            if (gui.isPresent()) {
            	 gui.draw(g2);
               
            }
        }
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    public void addGui(CookGui gui) {
        guis.add(gui);
    }

}
