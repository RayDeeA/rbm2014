package de.htw.iconn.views;

import java.awt.image.BufferedImage;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import de.htw.iconn.enhancement.IVisualizeObserver;
import de.htw.iconn.enhancement.RBMInfoPackage;
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
		
		int inputSize = ((RBMSettingsMainController)(rbmSettingsControllers.get(0).getModel().getController(RBMSettingsMainController.class))).getModel().getInputSize();
		int outputSize = ((RBMSettingsMainController)(rbmSettingsControllers.get(rbmSettingsControllers.size() - 1).getModel().getController(RBMSettingsMainController.class))).getModel().getOutputSize();
		
		RBMTrainer rbmTrainer = new RBMTrainer();
		
		Pic[] pics = new Pic[outputSize];

		for(int i = 0; i < outputSize; i++) {
			float[] hiddenData = new float[outputSize];  
			hiddenData[i] = 1.0f;
			
			float[] visibleData = rbmTrainer.getVisibleAllRBMs1D(benchmarkController.getModel(), hiddenData, false);

			int imageWidth = (int) Math.sqrt(visibleData.length), imageHeight = (int) Math.sqrt(visibleData.length);

			WritableImage image = new WritableImage(imageWidth, imageHeight);
			PixelWriter writer = image.getPixelWriter();

			for (int y = 0; y < imageHeight; y++) {
				for (int x = 0; x < imageWidth; x++) {
					int value = (int) Math.max(Math.min(255, (visibleData[y * imageWidth + x] * 255)), 0);
					Color color = Color.rgb(value, value, value);
					writer.setColor(x, y, color);
				}
			}
			
			BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
			
			Pic pic = new Pic();
			pic.setDisplayImage(bi);
			pic.setOrigWidth(bi.getWidth());
			pic.setOrigHeight(bi.getHeight());
			pic.setRank(i);
			pics[i] = pic;
		}
		
		super.setImages(pics);
	}
	
}
