/**
 * This class paints the terrain
 */
import javax.swing.*;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;

public class FlowPanel extends JPanel {
	Terrain land;
   
      /**
    * The constructor takes terrain as parameter
    * @param terrain passed to constructor
    */
	FlowPanel(Terrain terrain) {
		land=terrain;
	}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}
      // draw the waterImage over the landscape
      if (land.getWaterImage() != null){
         g.drawImage(land.getWaterImage(), 0, 0, null);
      }
      repaint();
	}
}