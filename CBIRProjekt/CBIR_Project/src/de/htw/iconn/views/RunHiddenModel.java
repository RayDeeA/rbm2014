/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.views;

import de.htw.iconn.rbm.ARBMAdapter;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.image.ImageHelper;
import de.htw.iconn.image.ImageManager;
import de.htw.iconn.image.ImageScaler;
import de.htw.iconn.image.Pic;
import de.htw.iconn.main.BenchmarkModel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

/**
 *
 * @author Radek
 */
public class RunHiddenModel {
    
    private final RunHiddenController controller;
    
    float[] calcImageData;
    BufferedImage visibleImage;
    BufferedImage hiddenImage;

    private boolean useHiddenStates;
	private boolean useVisibleStates;

	private BenchmarkModel benchmarkModel;
    
    public RunHiddenModel(RunHiddenController controller) {
    	useHiddenStates = false;
    	useVisibleStates = false;
        this.controller = controller;
    }
    
    public Image loadImage(int visWidth, int visHeight) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("CBIR_Project/images"));
        Stage fileChooserStage = new Stage();

        File file = fileChooser.showOpenDialog(fileChooserStage);
        if (file != null) {
            this.calcImageData = DataConverter.processPixelIntensityData(ImageHelper.loadImage(file), this.benchmarkModel.getImageEdgeSize(), this.benchmarkModel.isBinarizeImages(), this.benchmarkModel.isInvertImages(), this.benchmarkModel.getMinData(), this.benchmarkModel.getMaxData());

            ImageScaler imageScaler = new ImageScaler();
            WritableImage image = SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(DataConverter.pixelIntensityDataToImage(this.calcImageData, this.benchmarkModel.getMinData()), visWidth, visHeight), null);

            return image;
        } else {
            return null;
        }
    }
    
    public void runHidden(int maxHiddenImageWidth) {
    	RBMTrainer trainer = new  RBMTrainer();
    	
    	int width = this.benchmarkModel.getImageEdgeSize();
    	int height = this.benchmarkModel.getImageEdgeSize();
        
        // Create hidden and visible daydream data, which is used for visualization
        float[] hiddenDataForVis = trainer.getHiddenAllRBMs1D(this.benchmarkModel, this.calcImageData, false);
        float[] visibleDataForVis = trainer.getVisibleAllRBMs1D(this.benchmarkModel, hiddenDataForVis, false);
        
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
    	
        this.visibleImage = DataConverter.pixelIntensityDataToImage(visibleDataForVis, this.benchmarkModel.getMinData());
        this.hiddenImage = DataConverter.pixelIntensityDataToImage(hiddenDataForVis, 0.0f);
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

	public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
		this.benchmarkModel = benchmarkModel;
	}
}
