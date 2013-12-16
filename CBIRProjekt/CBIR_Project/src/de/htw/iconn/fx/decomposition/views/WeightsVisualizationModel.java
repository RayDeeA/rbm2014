package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.rbm.ARBMAdapter;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.awt.image.BufferedImage;
import java.util.Random;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class WeightsVisualizationModel {
    private final WeightsVisualizationController controller;
	
    Random random = new Random();
	
    ARBMAdapter rbmFeature;
    Pic pic;
    Image vizImage;
    Random rand = new Random();
    Random r1 = new Random();
    Random r2 = new Random();
    
    int input;
    int output;
    double height;
    double width;
    
    double[][] weights;

    public WeightsVisualizationModel(WeightsVisualizationController controller) {
        this.controller = controller;
    }
      
    public void setRbmFeature(ARBMAdapter rbmFeature) {
        this.rbmFeature = rbmFeature;
    } 
    
    public void setDCT(int i, int o) {
        this.input = i;
        this.output = o;
    }
    
    public void setDisplayDimensions(double w, double h) {
        this.width = w;
        this.height = h;
    }
    
     public void setWeights(double[][] w) {
    	this.weights = w;
    }
     
    public int[] resizePixels(int[] pixels,int w1,int h1,int w2,int h2) {
    
    int[] temp = new int[w2*h2] ;
    double x_ratio = w1/(double)w2 ;
    double y_ratio = h1/(double)h2 ;
    double px, py ; 
    
        for (int i=0;i<h2;i++) {
            for (int j=0;j<w2;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                temp[(i*w2)+j] = pixels[(int)((py*w1)+px)] ;
            }
        }
        
    return temp ;
}
    
    public int getIntFromColor(int Red, int Green, int Blue){
        
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    
     public Image generateRandomImage() {
        
        int[] pixelsImage = new int[input * output];
        
        WritableImage image = new WritableImage(input, output);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < output; y++) {
            for (int x = 0; x < input; x++) {
                
                int pos  = y * input + x;
            	
                int r = rand.nextInt(255) ;
                int g = rand.nextInt(255) ;
                int b = rand.nextInt(0) ;
                
                pixelsImage[pos] = getIntFromColor(r, g, b);
                
            }
        }
        //INPUT  = X 
        //OUTPUT = Y
        int[] resizedPic = resizePixels(pixelsImage, input, output, (int)width, (int)height);
        Image tmpImage   =  getImageFromArray(resizedPic, (int) width, (int)height);
      
        return tmpImage;
     }
        
    public Image generateImage() {
        
        int[] pixelsImage = new int[input * output];
        
        double start = -10;
        double end = 10;
        
        weights = new double[input][output];
        
        //DUMMY Data
        for (int y = 0; y < output; y++) {
            for (int x = 0; x < input; x++) {
                double randomValue = start + (end - start) * r1.nextDouble();
                weights[x][y] = randomValue;
            }
        }
        
        if(weights != null) {
			double[][] weights = relativateWeights(this.weights);
			
			float rc, gc, bc;

			for (int y = 0; y < output; y++) {
				for (int x = 0; x < input; x++) {
                                    
                                    int pos  = y * input + x;
            	
                                    float  current = (float)weights[x][y];
					if(current > 0) {
						rc = 1.0f - current;
						gc = 1.0f;
						bc = 1.0f - current;		
					}
					else {
						current = 1 + current;
						rc = 1.0f;
						gc = current;
						bc = current;
					}
					rc*= 255;
                                        gc*= 255;
                                        bc*= 255;
                                        
                                        pixelsImage[pos] = getIntFromColor((int) rc, (int) gc,(int) bc);
                                       	
				}
			} 
		} 
        //INPUT  = X 
        //OUTPUT = Y
        int[] resizedPic =  resizePixels(pixelsImage, input, output, (int)width, (int)height);
        Image tmpImage   =  getImageFromArray(resizedPic, (int) width, (int)height);
      
        return tmpImage;
    }
    
    private double[][] relativateWeights(double[][] weights) {
		final double[][] result = new double[weights.length][weights[0].length];
		double max = 0;
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				final double currentAbs = Math.abs(weights[i][j]);
				if(currentAbs > max) {
					max = currentAbs;
				}
			}
		}
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = Math.max(-1, Math.min(1, (weights[i][j] / 5.0f)));
			}
		}
		return result;
	}
    
    public static Image getImageFromArray(int[] pixels, int width, int height) {
         
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }

}
