package de.htw.iconn.fx.decomposition.views;

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
	
	RBMStack rbmStack;
    Pic pic;
    Image daydreamImage;

    private boolean useHiddenStates;
	private boolean useVisibleStates;
    
	public DaydreamModel() {
    	this.useHiddenStates = false;
    	this.useVisibleStates = false;
	}
	
	public void setRBMStack(RBMStack rbmStack) {
		this.rbmStack = rbmStack;
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
    
    public Image daydream(int visWidth, int visHeight) {
    	double[] hiddenData = null;//rbmStack.getHidden(this.pic, this.useHiddenStates);
    	double[] visibleDataForCalculation =  null;//rbmStack.rbmFeature.getVisible(hiddenData, this.useVisibleStates);
    	double[] visibleDataForVisualization =  null; //rbmStack.rbmFeature.getVisible(hiddenData, false);
    	
    	int width = this.pic.getDisplayImage().getWidth();
    	int height = this.pic.getDisplayImage().getHeight();
    	
        WritableImage imageVis = new WritableImage(width, height);
        PixelWriter writerVis = imageVis.getPixelWriter();
        
        WritableImage imageCalc = new WritableImage(width, height);
        PixelWriter writerCalc = imageCalc.getPixelWriter();
        
        // Set values for calculation image as they are
        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
                int valueCalc = (int) visibleDataForCalculation[pos] * 255;
                writerCalc.setColor(x, y, Color.rgb(valueCalc, valueCalc, valueCalc));
            }
        }
        
        // Set values for bigger visualization image using neirest neighbour
        double x_ratio = width/(double)visWidth;
        double y_ratio = height/(double)visHeight;
        for (int y = 0; y < visHeight; y++) {
        	double py = Math.floor(y*y_ratio) ;
            for (int x = 0; x < visWidth; x++) {
                double px = Math.floor(x*x_ratio) ;
                int valueVis = (int) visibleDataForVisualization[(int)(py * width + px)] * 255;
                writerVis.setColor(x, y, Color.rgb(valueVis, valueVis, valueVis));
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
