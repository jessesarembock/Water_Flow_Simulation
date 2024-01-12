/**
 * This class handles the clicking of the mouse on the terrain
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class WaterClickListener extends MouseAdapter {
   
   private static final int RADIUS = 3;
   Color blue = new Color(0.0f, 0.0f, 1.0f, 1.0f);
   
   //adjust water levels around the point that was clicked and make those pixels blue
   public void mouseClicked(MouseEvent e) {
      for (int x = -RADIUS; x <= RADIUS; x++) {
         for (int y = -RADIUS; y <= RADIUS; y++) {
            Water.placeWater(e.getX()+x, e.getY()+y);
            Terrain.waterImg.setRGB(e.getX()+x, e.getY()+y, blue.getRGB());
         }
      }
   }
}