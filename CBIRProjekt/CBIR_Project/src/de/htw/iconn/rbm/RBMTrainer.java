/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.rbm;

import java.util.LinkedList;
import java.util.ListIterator;

import de.htw.iconn.enhancement.RBMEnhancer;
import de.htw.iconn.enhancement.TrainingVisualizer;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.persistence.XMLEndTrainingLogger;
import de.htw.iconn.persistence.XMLTrainingLogger;
import de.htw.iconn.logistic.ILogistic;
import de.htw.iconn.settings.RBMSettingsLearningRateController;
import de.htw.iconn.settings.RBMSettingsLearningRateModel;
import de.htw.iconn.settings.RBMSettingsLoggerController;
import de.htw.iconn.settings.RBMSettingsLoggerModel;
import de.htw.iconn.settings.RBMSettingsMainController;
import de.htw.iconn.settings.RBMSettingsMainModel;
import de.htw.iconn.settings.RBMSettingsStoppingConditionController;
import de.htw.iconn.settings.RBMSettingsStoppingConditionModel;
import de.htw.iconn.settings.RBMSettingsVisualizationsController;
import de.htw.iconn.settings.RBMSettingsVisualizationsModel;
import de.htw.iconn.settings.RBMSettingsWeightsController;
import de.htw.iconn.settings.RBMSettingsWeightsModel;
import de.htw.iconn.main.BenchmarkController;
import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsModel;
import de.htw.iconn.views.ErrorViewModel;

/**
 *
 * @author radek, christoph
 */
public class RBMTrainer {
	
    // TRAINING

    public void trainAllRBMs(BenchmarkController benchmarkController) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();
        RBMSettingsController lastController = null;

        int counter = 1;
        for (RBMSettingsController c : rbmSettingsList) {
            System.out.println("RBM " + counter++);
            if (lastController != null) {

                IRBM lastRBM = createRBMForTemporaryUse(lastController);
                RBMSettingsModel lastModel = lastController.getModel();
                RBMSettingsWeightsModel lastWeightsModel = lastModel.getController(RBMSettingsWeightsController.class).getModel();

                float[][] data = lastRBM.getHidden(lastModel.getData(), lastWeightsModel.isBinarizeHidden());

                c.getModel().setData(data);
            }
            this.trainSingleRBM(c);
            lastController = c;
        }
        System.out.println("Training for all RBMs finished");
    }

    public IRBM createRBMForTemporaryUse(RBMSettingsController controller) {
        RBMSettingsModel model = controller.getModel();
        RBMSettingsMainModel mainModel = model.getController(RBMSettingsMainController.class).getModel();
        RBMSettingsWeightsModel weightsModel = model.getController(RBMSettingsWeightsController.class).getModel();
        RBMSettingsLearningRateModel learningRateModel = model.getController(RBMSettingsLearningRateController.class).getModel();

        int inputSize = mainModel.getInputSize();
        int outputSize = mainModel.getOutputSize();
        ILogistic logisticFunction = mainModel.getSelectedLogisticFunctionImplementation();
        float learningRate = learningRateModel.getConstantLearningRate();
        int seed = weightsModel.getSeed();
        boolean useSeed = weightsModel.isUseSeed();
        float[][] weights = weightsModel.getWeights();

        return new RBMJBlas(inputSize, outputSize, learningRate, logisticFunction, useSeed, seed, weights);
    }

    public void trainSingleRBM(RBMSettingsController controller) {
        System.out.println("Training started...");
        RBMSettingsModel model = controller.getModel();
        RBMSettingsWeightsModel weightsModel = model.getController(RBMSettingsWeightsController.class).getModel();
        RBMSettingsLoggerModel loggerModel = model.getController(RBMSettingsLoggerController.class).getModel();
        RBMSettingsStoppingConditionModel stoppingConditionModel = model.getController(RBMSettingsStoppingConditionController.class).getModel();
        RBMSettingsVisualizationsModel visualizationsModel = model.getController(RBMSettingsVisualizationsController.class).getModel();
        IRBM rbm = this.createRBMForTemporaryUse(controller);

        RBMEnhancer rbmEnhancer = new RBMEnhancer(rbm);

        if (loggerModel.isFinalLoggerOn()) {
            rbmEnhancer.addEnhancement(new XMLEndTrainingLogger());
        }

        if (loggerModel.isContinuousLoggerOn()) {
            rbmEnhancer.addEnhancement(new XMLTrainingLogger());
        }

        int weightsInterval = visualizationsModel.getWeightsInterval();
        int errorInterval = visualizationsModel.getErrorInterval();
        //int featuresInterval = visualizationsModel.getFeaturesInterval();

//        if (visualizationsModel.isShowWeights()) {
//            rbmEnhancer.addEnhancement(new TrainingVisualizer(weightsInterval, visualizationsModel.getWeightVisualizationController()));
//        }

        if (visualizationsModel.isShowErrorGraph()) {
            System.out.println("add visu");
            ErrorViewModel errorViewModel = visualizationsModel.getErrorViewController().getModel();
            errorViewModel.clear();
            rbmEnhancer.addEnhancement(new TrainingVisualizer(errorInterval, errorViewModel));
        }
        
        // TODO
        /*
        if (visualizationsModel.isShowFeatures()) {
        	ImageViewer featuresViewer = visualizationsModel.getFeatureViewer();
        	rbmEnhancer.addEnhancement(new TrainingVisualizer(errorInterval, featuresViewer));
        }
        */

        rbmEnhancer.train(model.getData(), stoppingConditionModel.getEpochs(), weightsModel.isBinarizeHidden(), weightsModel.isBinarizeVisible());

        weightsModel.setWeights(rbmEnhancer.getWeights());
        
        System.out.println("Training finished");
    }
    
    // GET HIDDEN
    
	public float[][] getHiddenAllRBMs(BenchmarkController benchmarkController, float[][] data) {
		return getHiddenAllRBMs(benchmarkController, data, false, false);
	}
	
	public float[] getHiddenAllRBMs1D(BenchmarkController benchmarkController, float[] data, boolean binarizeHidden){
		float[][] data2Dimensions = vectorToMatrix(data);
		
		float[][] hiddenData2Dimensions = getHiddenAllRBMs(benchmarkController, data2Dimensions, binarizeHidden, true);
		
		float hiddenData[] = matrixToVector(hiddenData2Dimensions);

		return hiddenData;
	}
	
	public float[][] getHiddenAllRBMs(BenchmarkController benchmarkController, float[][] data, boolean binarizeHidden){
		return getHiddenAllRBMs(benchmarkController, data, binarizeHidden, true);
	}
    
    private float[][] getHiddenAllRBMs(BenchmarkController benchmarkController, float[][] data, boolean binarizeHidden, boolean useBinarizeOptionGiven) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        float[][] visibleData = data;
        
        for(RBMSettingsController rbmSettingsController : rbmSettingsList) {
        	float[][] hiddenData;
        	if(useBinarizeOptionGiven) {
        		hiddenData = this.getHiddenSingleRBM(rbmSettingsController, visibleData, binarizeHidden);
        	} else {
        		hiddenData = this.getVisibleSingleRBM(rbmSettingsController, visibleData);
        	}
        	visibleData = hiddenData;
        }
        
        float[][] hiddenDataFinal = visibleData;
        return hiddenDataFinal;
    }
    
    public float[][] getHiddenSingleRBM(RBMSettingsController controller, float[][] data) {
    	boolean binarizeHidden = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeHidden();
    	return this.getHiddenSingleRBM(controller, data, binarizeHidden);
    }
    
    public float[][] getHiddenSingleRBM(RBMSettingsController controller, float[][] data, boolean binarizeHidden) {
    	float[][] hiddenData = null;
    	
    	IRBM rbm = this.createRBMForTemporaryUse(controller);
    	
    	if(data != null) {
    		hiddenData = rbm.getHidden(data, binarizeHidden);
    	} else if(controller.getModel().getData() != null) {
    		data = controller.getModel().getData();
    		hiddenData = rbm.getHidden(data, binarizeHidden);
    	} else {
    		throw new IllegalArgumentException(
    			"The data in the model was never set. "
    			+ "Not inside of the first RBMSettingsController "
    			+ "and is also not given inside of the methods "
    			+ "parameter list (float[][] data)"
    		);
    	}
    	
    	return hiddenData;
    }
    
	public float[] getHiddenSingleRBM(RBMSettingsController controller, float[] data, boolean binarizeHidden) {
		float[][] data2Dimensions = vectorToMatrix(data);

		float[][] hiddenData2Dimensions = getHiddenSingleRBM(controller, data2Dimensions, binarizeHidden);

		float hiddenData[] = matrixToVector(hiddenData2Dimensions);

		return hiddenData;
	}
	
    // GET VISIBLE
	
	public float[][] getVisibleAllRBMs(BenchmarkController benchmarkController, float[][] data) {
		return getVisibleAllRBMs(benchmarkController, data, false, false);
	}
	
	public float[] getVisibleAllRBMs1D(BenchmarkController benchmarkController, float[] data, boolean binarizeVisible){
		float[][] data2Dimensions = vectorToMatrix(data);
		// TODO
		float[][] visibleData2Dimensions = getVisibleAllRBMs(benchmarkController, data2Dimensions, binarizeVisible, true);
		
		float visibleData[] = matrixToVector(visibleData2Dimensions);

		return visibleData;
	}
	
	public float[][] getVisibleAllRBMs(BenchmarkController benchmarkController, float[][] data, boolean binarizeVisible){
		return getVisibleAllRBMs(benchmarkController, data, binarizeVisible, true);
	}
    
    private float[][] getVisibleAllRBMs(BenchmarkController benchmarkController, float[][] data, boolean binarizeVisible, boolean useBinarizeOptionGiven) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        float[][] hiddenData = data;
        ListIterator<RBMSettingsController> rbmSettingsListIterator = rbmSettingsList.listIterator(rbmSettingsList.size());
        
        while(rbmSettingsListIterator.hasPrevious()) {
        	RBMSettingsController rbmSettingsController = rbmSettingsListIterator.previous();
        	float[][] visibleData;
        	if(useBinarizeOptionGiven) {
        		visibleData = this.getVisibleSingleRBM(rbmSettingsController, hiddenData, binarizeVisible);
        	} else {
        		visibleData = this.getVisibleSingleRBM(rbmSettingsController, hiddenData);
        	}
        	
        	hiddenData = visibleData;
        }
        
        float[][] visibleDataFinal = hiddenData;
        return visibleDataFinal;
    }

    
    public float[][] getVisibleSingleRBM(RBMSettingsController controller, float[][] data, boolean binarizeVisible) {
    	IRBM rbm = this.createRBMForTemporaryUse(controller);
    	return rbm.getVisible(data, binarizeVisible);
    }
    
    public float[][] getVisibleSingleRBM(RBMSettingsController controller, float[][] data) {
    	boolean binarizeVisible = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeVisible();
    	return this.getVisibleSingleRBM(controller, data, binarizeVisible);
    }
    
    public float[] getVisibleSingleRBM(RBMSettingsController controller, float[] data) {
    	boolean binarizeVisible = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeVisible();
    	return this.getVisibleSingleRBM(controller, data, binarizeVisible);
    }
    
    public float[] getVisibleSingleRBM(RBMSettingsController controller, float[] data, boolean binarizeVisible) {
		float[][] data2Dimensions = vectorToMatrix(data);

		float[][] visibleData2Dimensions = getVisibleSingleRBM(controller, data2Dimensions, binarizeVisible);

		float visibleData[] = matrixToVector(visibleData2Dimensions);

		return visibleData;
    }
    
    // DAYDREAM
    
    public float[] daydreamAllRBMs(BenchmarkController controller, float[] data, boolean binarizeHidden, boolean binarizeVisible) {
		float[][] data2Dimensions = vectorToMatrix(data);
		
		float[][] hiddenData2Dimensions = this.getHiddenAllRBMs(controller, data2Dimensions, binarizeHidden);
		float[][] visibleData2Dimensions = this.getVisibleAllRBMs(controller, hiddenData2Dimensions, binarizeVisible);
		
		float visibleData[] = matrixToVector(visibleData2Dimensions);
    	
    	return visibleData;
    }
    
    public float[] daydreamSingleRBM(RBMSettingsController controller, float[] data, boolean binarizeHidden, boolean binarizeVisible) {
    	
    	float[] hiddenData = this.getHiddenSingleRBM(controller, data, binarizeHidden);
    	float[] visibleData = this.getVisibleSingleRBM(controller, hiddenData, binarizeVisible);
    	
    	return visibleData;
    }
    
    private float[] matrixToVector(float[][] matrix) {
		float vector[] = new float[matrix[0].length];
		for (int i = 0; i < matrix[0].length; i++) {
			vector[i] = matrix[0][i];
		}
		return vector;
    }
    
    private float[][] vectorToMatrix(float[] vector) {
		float[][] matrix = new float[1][vector.length];
		for (int i = 0; i < vector.length; i++) {
			matrix[0][i] = vector[i];
		}
		return matrix;
    }
    
    public void updateRBMs(BenchmarkController benchmarkController){
        BenchmarkModel benchmarkModel = benchmarkController.getModel();
        int inputSize = benchmarkModel.getInputSize();
        float[][] data = benchmarkModel.getInputData();
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkModel.getRbmSettingsList();
        for(RBMSettingsController settingsController : rbmSettingsList){
            RBMSettingsModel settingsModel = settingsController.getModel();
            RBMSettingsMainModel mainModel = settingsModel.getController(RBMSettingsMainController.class).getModel();
            settingsModel.setData(data);
            mainModel.setInputSize(inputSize);
            data = getHiddenSingleRBM(settingsController, data);
            inputSize = mainModel.getOutputSize();
        }
    }

}
 
