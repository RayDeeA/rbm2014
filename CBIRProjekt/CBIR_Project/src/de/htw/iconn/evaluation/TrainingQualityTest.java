package de.htw.iconn.evaluation;

import java.io.File;

import de.htw.iconn.image.ImageManager;
import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.tools.Chooser;

public class TrainingQualityTest {
	
	public static float getMSE(BenchmarkModel benchmarkModel) {
		File folder = Chooser.openDirectoryChooser("CBIR_Project/images");
		ImageManager imageManager = new ImageManager(folder, benchmarkModel);
		
		float[][] originalData = imageManager.getImageData();
		float[][] synthesizedData;
		
		RBMTrainer trainer = new RBMTrainer();
		
        float[][] hiddenDataForVis = trainer.getHiddenAllRBMs(benchmarkModel, originalData, false);
        synthesizedData = trainer.getVisibleAllRBMs(benchmarkModel, hiddenDataForVis, false);
        
        float error = 0.0f;
        for(int i = 0; i < originalData.length; i++) {
        	error += calcMSE(originalData[i], synthesizedData[i]);
        }
        
        float norm = benchmarkModel.getImageEdgeSize() * benchmarkModel.getImageEdgeSize() * originalData.length;
        error = (float) (255.0f * Math.sqrt( (1.0f / norm)  * error));
        
        return error;
	}
	
    private static float calcMSE(float[] data1, float[] data2) {
    	float mse = 0;
    	int n = data1.length;
    	for(int i = 0; i < n; i++) {
    		float error = data1[i] - data2[i];
    		mse += error * error;
    	}

    	return mse;
    }
	
}
