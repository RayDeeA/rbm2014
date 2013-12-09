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

public class DaydreamModel {
	
	Random random = new Random();
	
    ARBMFeature rbmFeature;
    Pic pic;
    Image daydreamImage;

    private boolean useHiddenStates;
	private boolean useVisibleStates;
    
	public DaydreamModel() {
    	this.useHiddenStates = false;
    	this.useVisibleStates = false;
	}
	
    public void setRbmFeature(ARBMFeature rbmFeature) {
        this.rbmFeature = rbmFeature;
    }
    
    public Image generateImage() {
        int width = 28;
        int height = 28;

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
            	int value = random.nextInt(256);
            	Color color = Color.rgb(value, value, value);
            	
                writer.setColor(x, y, color);
            }
        }
        
         BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        
        this.pic = new Pic();
        this.pic.setDisplayImage(bufferedImage);
        this.pic.setName("Daydream Image");
        this.pic.setOrigWidth(width);
        this.pic.setOrigHeight(height);

        return image;
    }
    
    public Image daydream() {
    	double[] hiddenData = rbmFeature.getHidden(this.pic, this.useHiddenStates);
    	double[] visibleData = rbmFeature.getVisible(hiddenData, this.useVisibleStates);
    	
    	int width = this.pic.getDisplayImage().getWidth();
    	int height = this.pic.getDisplayImage().getHeight();
    	
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
                int value = (int) (Math.max(Math.min(visibleData[pos] * 255, 255), 0));
                writer.setColor(x, y, Color.rgb(value, value, value));
            }
        }
        
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        
        this.pic.setDisplayImage(bufferedImage);
        
        return image; 
    	
    }
    
    public void setUseHiddenStates(boolean useHiddenStates) {
		this.useHiddenStates = useHiddenStates;
	}

	public void setUseVisibleStates(boolean useVisibleStates) {
		this.useVisibleStates = useVisibleStates;
	}
}
