package de.htw.iconn.fx;

import de.htw.cbir.ARBMFeature;
import java.awt.image.BufferedImage;
import java.util.Random;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import de.htw.cbir.model.Pic;

public class VisualizationModel {
	
    Random random = new Random();
	
    ARBMFeature rbmFeature;
    Pic pic;
    Image vizImage;
    
    int input;
    int output;
    int height;
    int width;
    
    double[][] weights;

	public VisualizationModel() {
		
	}
      
    public void setRbmFeature(ARBMFeature rbmFeature) {
        this.rbmFeature = rbmFeature;
    } 
    
    public void setDCT(int i, int o) {
        this.input = i;
        this.output = o;
    }
    
    public void setDisplayDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }
    
     public void setWeights(double[][] w) {
    	this.weights = w;
    }
    
    public Image generateImage() {

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < output; y++) {
            for (int x = 0; x < input; x++) {
            	
                
                //int value = weights[y][x] ;
            	//Color color = Color.rgb(value, value, value);
            	
               // writer.setColor(x, y, color);
            }
        }

        return image;
    }
    
    public Image visualImage(double[][] visWeights) {
    	
       weights = rbmFeature.getWeights();

       int width = this.pic.getDisplayImage().getWidth();
       int height = this.pic.getDisplayImage().getHeight();
    	
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
                int value = (int) (Math.max(Math.min(weights[0][pos] * 255, 255), 0));
                writer.setColor(x, y, Color.rgb(value, value, value));
            }
        }
        
        BufferedImage bufferedImage = null;
        SwingFXUtils.fromFXImage(image, bufferedImage);
        
        this.pic.setDisplayImage(bufferedImage);
        
        return image; 
    	
    }
}
