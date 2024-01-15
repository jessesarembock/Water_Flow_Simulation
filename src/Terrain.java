/**
 * This class acts as the Model part of the design
 */
import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Terrain {

	static float [][] height; // regular grid of height values
   static float [][] waterSurface; //regular grid of water surface heights
   static int dimx, dimy; // data dimensions
	BufferedImage img; // greyscale image for displaying the terrain top-down
   static BufferedImage waterImg; // image for displaying the water flow
   static Color transparent = new Color(0.0f, 0.0f, 1.0f, 0.0f);
   static Color blue = new Color(0.0f, 0.0f, 1.0f, 1.0f);
   static Water water;

	static ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)
   static ArrayList<Integer> subPermute1;
   static ArrayList<Integer> subPermute2;
   static ArrayList<Integer> subPermute3;
   static ArrayList<Integer> subPermute4;
	
	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)
	static int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)
	static int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	public BufferedImage getImage() {
		  return img;
	}
   
   // get waterImage
   public BufferedImage getWaterImage() {
      return waterImg;
   }
	
	// convert linear position into 2D location in grid
	static void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y	
	}
	
	// convert height values to greyscale colour and populate an image
	void deriveImage()
	{
		img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;
		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = height[x][y];
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (height[x][y] - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
				 img.setRGB(x, y, col.getRGB());
			}
	}
   
   // populate waterImage
   void deriveWaterImage()
   {
      waterImg = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
   }
	
	// generate a permuted list of linear index positions to allow a random
	// traversal over the terrain
	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}
   // split permuted list into 4
   void splitPermute() {
      
      subPermute1 = new ArrayList<Integer>();
      subPermute2 = new ArrayList<Integer>();
      subPermute3 = new ArrayList<Integer>();
      subPermute4 = new ArrayList<Integer>();
      
      for (int idx = 0; idx < dim()/4; idx++) {
         subPermute1.add(permute.get(idx));
      }
      for (int idx = dim()/4; idx < dim()/2; idx++) {
         subPermute2.add(permute.get(idx));
      }
      for (int idx = dim()/2; idx < dim()*3/4; idx++) {
         subPermute3.add(permute.get(idx));
      }
      for (int idx = dim()*3/4; idx < dim(); idx++) {
         subPermute4.add(permute.get(idx));
      }
   }
   
	// find permuted 2D location from a linear index in the
	// range [0, dimx*dimy)
	static synchronized void getPermute(int i, int [] loc) {
		locate(permute.get(i), loc);
	}
   // method to add water to lowest neighbour and subtract water from each gridpoint that transfers water
   static void waterFlow(ArrayList<Integer> permute) {
      
      for(int y = 0; y < dimy; y++){
	      for(int x = 0; x < dimx; x++) {
            synchronized(water) {waterSurface[x][y] = height[x][y] + Water.getWater(x, y);}
         }
      }
         
      int[] cords = new int[2];
         
      for (Integer i : permute) {
         getPermute(i, cords);
         if (cords[0] == 0 || cords [0] == dimx-1 || cords[1] == 0 || cords[1] == dimy-1) {
            //clear water at boundary
            Water.clearWater(cords[0], cords[1]);
            waterImg.setRGB(cords[0], cords[1], transparent.getRGB());
         }
         else if (Water.getWater(cords[0], cords[1]) > 0) {
            int[] lowestNeighbor = findLowestNeighbor(cords[0], cords[1], waterSurface);
            if (lowestNeighbor[0] != cords[0] || lowestNeighbor[1] != cords[1]) {
               Water.addWater(lowestNeighbor[0], lowestNeighbor[1]);
               waterImg.setRGB(lowestNeighbor[0], lowestNeighbor[1], blue.getRGB());
               Water.subtractWater(cords[0], cords[1]);
            }
         }
         if (Water.getWater(cords[0], cords[1]) == 0) {
            waterImg.setRGB(cords[0], cords[1], transparent.getRGB());
         }
      }
   }
   
   private static int[] findLowestNeighbor(int x, int y, float[][] waterSurface) {
      float minLevel = waterSurface[x][y];
      int[] lowest = new int[] {x, y};

      for (int i = -1; i <= 1; i++) {
         for (int j = -1; j <= 1; j++) {
            int newX = x + i;
            int newY = y + j;
            // Check boundaries and ignore the center cell
            if (newX >= 0 && newX < waterSurface.length && newY >= 0 && newY < waterSurface[0].length && !(i == 0 && j == 0)) {
               if (waterSurface[newX][newY] < minLevel) {
                  minLevel = waterSurface[newX][newY];
                  lowest[0] = newX;
                  lowest[1] = newY;
               }
            }
         }
      }
      return lowest;
   }
	
	// read in terrain from file
	void readData(String fileName){ 
      
      //initialize Scanner variable
      Scanner sc = null;
      
      //initialize String variable for data values to be assigned to before conversion to float
      String token = null;

		try{ 
			sc = new Scanner(new File(fileName));

			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			
			// populate height grid
			height = new float[dimx][dimy];

			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++) {
               token = sc.next();
               height[x][y] = Float.parseFloat(token);
            }
			}
         
         water = new Water(dimx, dimy);
         
         waterSurface = new float[dimx][dimy];
         
			// create randomly permuted list of indices for traversal 
			genPermute();
         
         //split permuted list into 4 sub-lists
         splitPermute();
			
			// generate greyscale heightfield image
			deriveImage();
         
         // generate waterImage
         deriveWaterImage();
         
		   }
		   catch (IOException e){ 
			   System.out.println("Unable to open input file "+fileName);
			   e.printStackTrace();
            System.exit(1);
		   }
		   catch (java.util.InputMismatchException e){ 
			   System.out.println("Malformed input file "+fileName);
			   e.printStackTrace();
            System.exit(1);
		   }
         catch (NumberFormatException e) {
            System.out.println("Invalid float format: " + token);
            e.printStackTrace();
            System.exit(1);
         }
         finally {
            if (sc != null) {
               sc.close();
            }
         }
	   }
   }