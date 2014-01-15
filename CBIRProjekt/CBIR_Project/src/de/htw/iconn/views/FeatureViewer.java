package de.htw.iconn.views;

import java.awt.image.BufferedImage;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import de.htw.iconn.enhancement.IVisualizeObserver;
import de.htw.iconn.enhancement.RBMInfoPackage;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.image.ImageViewer;
import de.htw.iconn.image.Pic;
import de.htw.iconn.main.BenchmarkController;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsMainController;

public class FeatureViewer extends ImageViewer implements IVisualizeObserver {
	
	private BenchmarkController benchmarkController;

	public FeatureViewer(BenchmarkController benchmarkController) {
		super();
		this.benchmarkController = benchmarkController;
	}
	
	public void update(RBMInfoPackage info) {
		// TODO
	}

	public void update() {
		List<RBMSettingsController> rbmSettingsControllers = this.benchmarkController.getModel().getRbmSettingsList();
		
		int outputSize = ((RBMSettingsMainController)(rbmSettingsControllers.get(rbmSettingsControllers.size() - 1).getModel().getController(RBMSettingsMainController.class))).getModel().getOutputSize();
		
		RBMTrainer rbmTrainer = new RBMTrainer();
		
		Pic[] pics = new Pic[outputSize];

		for(int i = 0; i < outputSize; i++) {
			float[] hiddenData = new float[outputSize];  
			hiddenData[i] = 1.0f;
			
			float[] visibleData = rbmTrainer.getVisibleAllRBMs1D(benchmarkController.getModel(), hiddenData, false);

			BufferedImage image = DataConverter.pixelIntensityDataToImage(visibleData, 0);
			
			Pic pic = new Pic();
			pic.setDisplayImage(image);
			pic.setOrigWidth(image.getWidth());
			pic.setOrigHeight(image.getHeight());
			pic.setRank(i);
			pics[i] = pic;
		}
		
		super.setImages(pics);
	}
	
}
