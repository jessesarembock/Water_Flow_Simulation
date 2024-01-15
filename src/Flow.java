/**
 * This class handles the GUI and starts and joins the threads
 */
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
   static Thread control1, control2, control3, control4;
   
	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
      
	public static void setupGUI(int frameX,int frameY,Terrain landdata) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
      	
         JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);
      
      JButton resetB = new JButton("Reset");
      resetB.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //change water height to 0 over all gridpoints and make all pixels in terrain transparent
            for (int x = 0; x < Terrain.getDimX(); x++) {
               for (int y = 0; y < Terrain.getDimY(); y++) {
                  Water.clearWater(x, y);
                  Terrain.waterImg.setRGB(x, y, Terrain.transparent.getRGB());
               }
            }
            Control.counter.set(0);
         }
      });
      
      JButton pauseB = new JButton("Pause");
      pauseB.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //pause sim
            Control.flag = false;
         }
      });
      
      JButton playB = new JButton("Play");
      playB.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //play sim 
            Control.flag = true;
                              
            control1 = new Thread(new Control(Terrain.subPermute1));
            control1.start();
            
            control2 = new Thread(new Control(Terrain.subPermute2));
            control2.start();
            
            control3 = new Thread(new Control(Terrain.subPermute3));
            control3.start();
            
            control4 = new Thread(new Control(Terrain.subPermute4));
            control4.start();
         }
      });
         
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton endB = new JButton("End");
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
            Control.flag = false;
				frame.dispose();
			}
		});
      
      Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
      Control.label.setBorder(border);
		
      b.add(resetB);
      b.add(pauseB);
      b.add(playB);
		b.add(endB);
      b.add(Control.label);
		g.add(b);
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.getContentPane().addMouseListener(new WaterClickListener());
        frame.setVisible(true);
	}
	
		
	public static void main(String[] args) {
		
      Terrain landdata = new Terrain();
		String filename = "Data/medsample_in.txt";
      
      if (args.length == 1) {
         filename = args[0];
      } 
      else if (args.length > 1) {
         System.out.println("Incorrect number of command line arguments. Either enter 0 command line arguments to use default file or enter 1 command line argument to specify file name.");
         System.exit(1);
      }
				
		// landscape information from file supplied as argument
		landdata.readData(filename);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
      
      control1 = new Thread(new Control(Terrain.subPermute1));
      control1.start();
            
      control2 = new Thread(new Control(Terrain.subPermute2));
      control2.start();
            
      control3 = new Thread(new Control(Terrain.subPermute3));
      control3.start();
            
      control4 = new Thread(new Control(Terrain.subPermute4));
      control4.start();
      
      try {
      control1.join();
      control2.join();
      control3.join();
      control4.join();
      }
      catch(InterruptedException e) {
         Thread.currentThread().interrupt();
      }
	}
}