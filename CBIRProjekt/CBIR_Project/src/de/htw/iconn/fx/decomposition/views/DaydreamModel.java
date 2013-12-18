package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.BenchmarkController;
import de.htw.iconn.fx.decomposition.RBMTrainer;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.ImageScaler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DaydreamModel {

    private final DaydreamController controller;

    Random random = new Random();
	
    BufferedImage calcImage;
    BufferedImage visImage;

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

    public Image daydream(int visWidth, int visHeight) {
    	RBMTrainer trainer = new  RBMTrainer();
    	
    	int width = this.benchmarkController.getModel().getImageEdgeSize();
    	int height = this.benchmarkController.getModel().getImageEdgeSize();
    	
    	double[] data = new double[this.calcImage.getWidth() * this.calcImage.getHeight()];
    	int[] calcImagePixels = this.calcImage.getRGB(0, 0, width, height, null, 0, width);
    	for(int i = 0; i < data.length; i++) {
    		data[i] = (calcImagePixels[i] & 0xff) / (double)255.0f;
    	}
    	
        double[] visibleDataForCalc = trainer.daydreamAllRBMs(this.benchmarkController, data, this.useHiddenStates, this.useVisibleStates);
        double[] visibleDataForVis = trainer.daydreamAllRBMs(this.benchmarkController, data, false, false);
        
        int[] visImagePixels = new int[width*height];
    	for(int i = 0; i < calcImagePixels.length; i++) {
    		int calcValue = (int) Math.round(visibleDataForCalc[i] * 255);
    		int visValue = (int) Math.round(visibleDataForVis[i] * 255);
    		calcImagePixels[i] = (0xFF << 24) | (calcValue << 16) | (calcValue << 8) | calcValue;
    		visImagePixels[i] = (0xFF << 24) | (visValue << 16) | (visValue << 8) | visValue;
    	}
    	
        this.calcImage.setRGB(0, 0, width, height, calcImagePixels, 0, width);
        this.visImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.visImage.setRGB(0, 0, width, height, visImagePixels, 0, width);
        
		ImageScaler imageScaler = new ImageScaler();

        WritableImage image = new WritableImage(visWidth, visHeight);
        SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(this.visImage, visWidth, visHeight), image);

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
