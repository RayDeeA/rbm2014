package de.htw.iconn.views;

import de.htw.iconn.main.BenchmarkController;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.image.ImageHelper;
import de.htw.iconn.image.ImageManager;
import de.htw.iconn.image.ImageScaler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DaydreamModel {

    private final DaydreamController controller;

    Random random = new Random();
	
    BufferedImage calcImage;
    BufferedImage visibleImage;
    BufferedImage hiddenImage;

    private boolean useHiddenStates;
	private boolean useVisibleStates;

	private BenchmarkController benchmarkController;
    
	public DaydreamModel(DaydreamController controller) {
        this.useHiddenStates = false;
        this.useVisibleStates = false;
        this.controller = controller;
	}
    
    public Image loadImage(int visWidth, int visHeight) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("CBIR_Project/images"));
        Stage fileChooserStage = new Stage();

        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (file != null) {
            this.calcImage = ImageHelper.loadImage(file);

            ImageScaler imageScaler = new ImageScaler();
            
            WritableImage image = new WritableImage(visWidth, visHeight);
            SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.calcImage, visWidth, visHeight), image);

            return image;
        } else {
            return null;
        }
    }

    public Image generateImage(int visWidth, int visHeight) {
        int width = this.benchmarkController.getModel().getImageEdgeSize();
        int height = this.benchmarkController.getModel().getImageEdgeSize();
        
        int[] imagePixels = new int[width * height]; 
        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
            	int value = random.nextInt(256);
            	imagePixels[pos] = (0xFF << 24) | (value << 16) | (value << 8) | value;
            }
        }
        
        this.calcImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.calcImage.setRGB(0, 0, width, height, imagePixels, 0, width);
		
		ImageScaler imageScaler = new ImageScaler();

        WritableImage image = new WritableImage(visWidth, visHeight);
        SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.calcImage, visWidth, visHeight), image);

        return image;
    }

    public void daydream(int maxHiddenImageWidth) {
    	RBMTrainer trainer = new  RBMTrainer();
    	
    	int width = this.benchmarkController.getModel().getImageEdgeSize();
    	int height = this.benchmarkController.getModel().getImageEdgeSize();
    	
    	// Get pixels from calculation image and convert it to normalized data  
    	float[] data = new float[this.calcImage.getWidth() * this.calcImage.getHeight()];
    	int[] calcImagePixels = this.calcImage.getRGB(0, 0, width, height, null, 0, width);
    	for(int i = 0; i < data.length; i++) {
    		data[i] = (calcImagePixels[i] & 0xff) / 255.0f;
    	}
    	
    	// Create visible daydream data, which is used for the next calculation step 
        float[] visibleDataForCalc = trainer.daydreamAllRBMs(this.benchmarkController.getModel(), data, this.useHiddenStates, this.useVisibleStates);
        
        // Create hidden and visible daydream data, which is used for visualization
        float[] hiddenDataForVis = trainer.getHiddenAllRBMs1D(this.benchmarkController.getModel(), data, false);
        float[] visibleDataForVis = trainer.getVisibleAllRBMs1D(this.benchmarkController.getModel(), hiddenDataForVis, false);
        
        // Convert hiddenData to pixels
        int[] hiddenImagePixels = new int[hiddenDataForVis.length];
        int hiddenImageWidth = maxHiddenImageWidth;
        int hiddenImageHeight = (int) Math.ceil(hiddenImagePixels.length / maxHiddenImageWidth);
        
        int counter = 0;
        for(int y = 0; y < hiddenImageHeight; y++) {
        	for(int x = 0; x < hiddenImageWidth; x++) {
        		if(counter <= hiddenImagePixels.length) {
        			int pos = y*hiddenImageWidth+x;
            		int hiddenValue = (int) Math.round(hiddenDataForVis[pos] * 255);
            		hiddenImagePixels[pos] = (0xFF << 24) | (hiddenValue << 16) | (hiddenValue << 8) | hiddenValue;
        		}
        		counter++;
        	}
        }
        
        // Convert visibleData to pixels
        int[] visImagePixels = new int[width*height];
    	for(int i = 0; i < calcImagePixels.length; i++) {
    		int calcValue = (int) Math.round(visibleDataForCalc[i] * 255);
    		int visibleValue = (int) Math.round(visibleDataForVis[i] * 255);
    		calcImagePixels[i] = (0xFF << 24) | (calcValue << 16) | (calcValue << 8) | calcValue;
    		visImagePixels[i] = (0xFF << 24) | (visibleValue << 16) | (visibleValue << 8) | visibleValue;
    	}
    	
        this.calcImage.setRGB(0, 0, width, height, calcImagePixels, 0, width);
        this.visibleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.visibleImage.setRGB(0, 0, width, height, visImagePixels, 0, width);
        this.hiddenImage = new BufferedImage(hiddenImageWidth, hiddenImageHeight, BufferedImage.TYPE_INT_RGB);
        this.hiddenImage.setRGB(0, 0, hiddenImageWidth, hiddenImageHeight, hiddenImagePixels, 0, hiddenImageWidth);
    }
    
    public Image getVisibleImage(int visWidth, int visHeight) {
		ImageScaler imageScaler = new ImageScaler();

        WritableImage visibleImage = new WritableImage(visWidth, visHeight);
        SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.visibleImage, visWidth, visHeight), visibleImage);
    	
    	return visibleImage;
    }
    
    public Image getHiddenImage(int scalingFactor) {
		ImageScaler imageScaler = new ImageScaler();
		
		int width = this.hiddenImage.getWidth();
		int height = this.hiddenImage.getHeight();
		
		int visWidth = this.hiddenImage.getWidth() * scalingFactor;
		int visHeight = this.hiddenImage.getHeight() * scalingFactor;

        WritableImage hiddenImage = new WritableImage(visWidth, visHeight);
        SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.hiddenImage, visWidth, visHeight), hiddenImage);
    	
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(hiddenImage, null);
        		
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(1);
        g2d.setStroke(bs);
        
        for(int y = 0; y < height; y++) {
        	if(y != 0) {
        		g2d.drawLine(0, (y) * scalingFactor, visWidth, (y) * scalingFactor);
        	}
        }
        
    	for(int x = 0; x < width; x++) {
    		if(x != 0) {
    			g2d.drawLine((x) * scalingFactor, 0, (x) * scalingFactor, visHeight);
    		}
    	}
        
    	return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public void setUseHiddenStates(boolean useHiddenStates) {
        this.useHiddenStates = useHiddenStates;
    }

    public void setUseVisibleStates(boolean useVisibleStates) {
        this.useVisibleStates = useVisibleStates;
    }

	public void setBenchmarkController(BenchmarkController benchmarkController) {
		this.benchmarkController = benchmarkController;
	}

}
