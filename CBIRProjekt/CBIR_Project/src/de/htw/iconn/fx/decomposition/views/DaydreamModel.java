package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.BenchmarkController;
import de.htw.iconn.fx.decomposition.RBMTrainer;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.ImageScaler;
import de.htw.iconn.fx.decomposition.tools.Pic;

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

public class DaydreamModel {

    private final DaydreamController controller;

    Random random = new Random();
	
    BufferedImage calcImage;
    BufferedImage daydreamImage;

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
            ImageManager imageManager = new ImageManager();
            this.calcImage = imageManager.loadImage(file).getDisplayImage();

            ImageScaler imageScaler = new ImageScaler();
            
            WritableImage image = new WritableImage(visWidth, visHeight);
            SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.calcImage, visWidth, visHeight), image);

            return image;
        } else {
            return null;
        }
    }

    public Image generateImage(int visWidth, int visHeight) {
        int width = 28;
        int height = 28;
        
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

    public Image daydream(int visWidth, int visHeight) {
    	RBMTrainer trainer = new  RBMTrainer();
    	
    	double[] data = new double[this.calcImage.getWidth() * this.calcImage.getHeight()];
    	int[] calcImagePixels = this.calcImage.getRGB(0, 0, 28, 28, null, 0, 28);
    	for(int i = 0; i < data.length; i++) {
    		data[i] = (calcImagePixels[i] & 0xff) / (double)255.0f;
    	}
    	
        double[] visibleData = trainer.daydreamAllRBMs(this.benchmarkController, data, this.useHiddenStates, this.useVisibleStates);
        
    	for(int i = 0; i < calcImagePixels.length; i++) {
    		int value = (int) Math.round(visibleData[i] * 255);
    		calcImagePixels[i] = (0xFF << 24) | (value << 16) | (value << 8) | value;
    	}
    	
        this.calcImage.setRGB(0, 0, 28, 28, calcImagePixels, 0, 28);
        
		ImageScaler imageScaler = new ImageScaler();

        WritableImage image = new WritableImage(visWidth, visHeight);
        SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.calcImage, visWidth, visHeight), image);

        return image;
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
