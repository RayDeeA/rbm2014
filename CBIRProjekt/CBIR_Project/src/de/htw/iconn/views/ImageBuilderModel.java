package de.htw.iconn.views;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.image.ImageScaler;
import de.htw.iconn.image.Pic;
import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsMainController;

public class ImageBuilderModel {

    private final ImageBuilderController controller;
	private BenchmarkModel benchmarkModel;
	
    private boolean useHiddenStates;
	private boolean useVisibleStates;
    
	public ImageBuilderModel(ImageBuilderController controller) {
        this.controller = controller;
	}
    
	public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
		this.benchmarkModel = benchmarkModel;
	}
	
    public void setUseHiddenStates(boolean useHiddenStates) {
        this.useHiddenStates = useHiddenStates;
    }

    public void setUseVisibleStates(boolean useVisibleStates) {
        this.useVisibleStates = useVisibleStates;
    }

	public List<Image> getHiddenImages(int visWidth, int visHeight) {
		List<RBMSettingsController> rbmSettingsControllers = benchmarkModel.getRbmSettingsList();
		
		int outputSize = ((RBMSettingsMainController)(rbmSettingsControllers.get(rbmSettingsControllers.size() - 1).getModel().getController(RBMSettingsMainController.class))).getModel().getOutputSize();
		
		RBMTrainer rbmTrainer = new RBMTrainer();
		
		List<Image> hiddenimages = new ArrayList<Image>();

		for(int i = 0; i < outputSize; i++) {
			float[] hiddenData = new float[outputSize];  
			hiddenData[i] = 1.0f;
			
			float[] visibleData = rbmTrainer.getVisibleAllRBMs1D(benchmarkModel, hiddenData, false);

			BufferedImage image = DataConverter.pixelIntensityDataToImage(visibleData, 0);
			
			ImageScaler imageScaler = new ImageScaler();

	        Image hiddenImage = SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(image, visWidth, visHeight), null);
	        hiddenimages.add(hiddenImage);
		}
		
		return hiddenimages;
	}

	public Image getVisibleImage(float[] hiddenData, int visWidth, int visHeight) {
		RBMTrainer trainer = new RBMTrainer();
		float[] visibleData = trainer.getVisibleAllRBMs1D(this.benchmarkModel, hiddenData, false);
		
		BufferedImage image = DataConverter.pixelIntensityDataToImage(visibleData, 0.0f);
		
		ImageScaler imageScaler = new ImageScaler();

        WritableImage visibleImage = SwingFXUtils.toFXImage(imageScaler.getScaledImageNeirestNeighbour(image, visWidth, visHeight), null);
    	
    	return visibleImage;
	}

}
