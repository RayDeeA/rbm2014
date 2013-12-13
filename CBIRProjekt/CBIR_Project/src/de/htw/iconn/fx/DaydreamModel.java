package de.htw.iconn.fx;

import de.htw.cbir.ARBMFeature;
import de.htw.cbir.ImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.RBMStack;

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
    
    public Image loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("CBIR_Project/images"));
        Stage fileChooserStage = new Stage();
        
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if(file != null) {
            ImageManager imageManager = new ImageManager();
            this.pic = imageManager.loadImage(file);
            
            BufferedImage bufferedImage = this.pic.getDisplayImage();
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            
            this.pic = new Pic();
            this.pic.setDisplayImage(bufferedImage);
            this.pic.setName("Daydream Image");
            this.pic.setOrigWidth(width);
            this.pic.setOrigHeight(height);
            
            WritableImage image = new WritableImage(width, height);
            SwingFXUtils.toFXImage(bufferedImage, image);
            
            return image;
        } else return null;
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
    	double[] visibleDataForCalculation = rbmFeature.getVisible(hiddenData, this.useVisibleStates);
    	double[] visibleDataForVisualization = rbmFeature.getVisible(hiddenData, false);
    	
    	int width = this.pic.getDisplayImage().getWidth();
    	int height = this.pic.getDisplayImage().getHeight();
    	
        WritableImage imageVis = new WritableImage(width, height);
        WritableImage imageCalc = new WritableImage(width, height);
        PixelWriter writerVis = imageVis.getPixelWriter();
        PixelWriter writerCalc = imageCalc.getPixelWriter();

        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
                int valueVis = (int) (Math.max(Math.min(visibleDataForVisualization[pos] * 255, 255), 0));
                writerVis.setColor(x, y, Color.rgb(valueVis, valueVis, valueVis));
                int valueCalc = (int) (Math.max(Math.min(visibleDataForCalculation[pos] * 255, 255), 0));
                writerCalc.setColor(x, y, Color.rgb(valueCalc, valueCalc, valueCalc));
            }
        }
        
        BufferedImage bufferedImageCalc = SwingFXUtils.fromFXImage(imageCalc, null);
        
        this.pic.setDisplayImage(bufferedImageCalc);
        
        return imageVis; 
    	
    }
    
    public void setUseHiddenStates(boolean useHiddenStates) {
		this.useHiddenStates = useHiddenStates;
	}

	public void setUseVisibleStates(boolean useVisibleStates) {
		this.useVisibleStates = useVisibleStates;
	}
}
