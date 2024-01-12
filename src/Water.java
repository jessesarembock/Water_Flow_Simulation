/**
 * This class acts as an object, which holds an array of water heights
 */
public class Water {

   private static float [][] water;
   private static final float WATER_PLACEMENT_VALUE = 0.03f;
   private static final float WATER_RUNOFF_VALUE = 0.01f;
   
      /**
    * The constructor takes x and y dimensions as parameters and returns a 2-dimensional array of those dimensions
    * @param dimx passed to constructor
    * @param dimy passed to constructor
    */
   public Water(int dimx, int dimy) {
      water = new float[dimx][dimy];
   }
   /**
    *
    * @return water[x][y]
    * @param x passed to the method
    * @param y passed to the method
    */
   public static float getWater(int x, int y) {
      return water[x][y];
   }
   /**
    *
    * @param x passed to the method
    * @param y passed to the method
    */
   public static void placeWater(int x, int y) {
      water[x][y] = water[x][y] + WATER_PLACEMENT_VALUE;
   }  
   /**
    *
    * @param x passed to the method
    * @param y passed to the method
    */
   public static synchronized void addWater(int x, int y) {
      water[x][y] = water[x][y] + WATER_RUNOFF_VALUE;
   }
   /**
    *
    * @param x passed to the method
    * @param y passed to the method
    */
   public static synchronized void subtractWater(int x, int y) {
      water[x][y] = water[x][y] - WATER_RUNOFF_VALUE;
   }
   /**
    *
    * @param x passed to the method
    * @param y passed to the method
    */
   public static synchronized void clearWater(int x, int y) {
      water[x][y] = 0;
   }  
}