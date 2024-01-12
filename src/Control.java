/**
 * This class controls the running of the threads over the permuted lists
 */
import java.util.ArrayList;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Control implements Runnable {

   ArrayList<Integer> permute;
   static volatile boolean flag;
   static AtomicInteger counter = new AtomicInteger();
   static int count;
   static JLabel label = new JLabel();
   
   public Control(ArrayList<Integer> permute) {
      this.permute = permute;
   }
   
   public void runWaterFlow() {
      Terrain.waterFlow(permute);
   }
   
   public void run() {
      while (flag == true) {
         runWaterFlow();
         count++;
         if (count%4 == 0) {
            counter.getAndIncrement();
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  label.setText(String.valueOf(counter));
               }
            });
         }
      }
   }
}
   
      
   