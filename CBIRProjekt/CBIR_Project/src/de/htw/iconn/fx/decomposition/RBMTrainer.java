/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import java.util.LinkedList;
import java.util.ListIterator;

import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.TrainingVisualizer;
import de.htw.iconn.fx.decomposition.enhancement.XMLEndTrainingLogger;
import de.htw.iconn.fx.decomposition.enhancement.XMLTrainingLogger;
import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import de.htw.iconn.fx.decomposition.rbm.IRBM;
import de.htw.iconn.fx.decomposition.rbm.RBMJBlas;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsLearningRateController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsLearningRateModel;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsLoggerController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsLoggerModel;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsMainController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsMainModel;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsStoppingConditionController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsStoppingConditionModel;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsVisualizationsController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsVisualizationsModel;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsWeightsController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsWeightsModel;
import de.htw.iconn.fx.decomposition.tools.ImageViewer;
import de.htw.iconn.fx.decomposition.views.ErrorViewModel;

/**
 *
 * @author christoph
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

                double[][] data = lastRBM.getHidden(lastModel.getData(), lastWeightsModel.isBinarizeHidden());

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
        double learningRate = learningRateModel.getConstantLearningRate();
        int seed = weightsModel.getSeed();
        boolean useSeed = weightsModel.isUseSeed();
        double[][] weights = weightsModel.getWeights();

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
    
	public double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data) {
		return getHiddenAllRBMs(benchmarkController, data, false, false);
	}
	
	public double[] getHiddenAllRBMs1D(BenchmarkController benchmarkController, double[] data, boolean binarizeHidden){
		double[][] data2Dimensions = vectorToMatrix(data);
		
		double[][] hiddenData2Dimensions = getHiddenAllRBMs(benchmarkController, data2Dimensions, binarizeHidden, true);
		
		double hiddenData[] = matrixToVector(hiddenData2Dimensions);

		return hiddenData;
	}
	
	public double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeHidden){
		return getHiddenAllRBMs(benchmarkController, data, binarizeHidden, true);
	}
    
    private double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeHidden, boolean useBinarizeOptionGiven) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] visibleData = data;
        
        for(RBMSettingsController rbmSettingsController : rbmSettingsList) {
        	double[][] hiddenData;
        	if(useBinarizeOptionGiven) {
        		hiddenData = this.getHiddenSingleRBM(rbmSettingsController, visibleData, binarizeHidden);
        	} else {
        		hiddenData = this.getVisibleSingleRBM(rbmSettingsController, visibleData);
        	}
        	visibleData = hiddenData;
        }
        
        double[][] hiddenDataFinal = visibleData;
        return hiddenDataFinal;
    }
    
    public double[][] getHiddenSingleRBM(RBMSettingsController controller, double[][] data) {
    	boolean binarizeHidden = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeHidden();
    	return this.getHiddenSingleRBM(controller, data, binarizeHidden);
    }
    
    public double[][] getHiddenSingleRBM(RBMSettingsController controller, double[][] data, boolean binarizeHidden) {
    	double[][] hiddenData = null;
    	
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
    			+ "parameter list (double[][] data)"
    		);
    	}
    	
    	return hiddenData;
    }
    
	public double[] getHiddenSingleRBM(RBMSettingsController controller, double[] data, boolean binarizeHidden) {
		double[][] data2Dimensions = vectorToMatrix(data);

		double[][] hiddenData2Dimensions = getHiddenSingleRBM(controller, data2Dimensions, binarizeHidden);

		double hiddenData[] = matrixToVector(hiddenData2Dimensions);

		return hiddenData;
	}
	
    // GET VISIBLE
	
	public double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data) {
		return getVisibleAllRBMs(benchmarkController, data, false, false);
	}
	
	public double[] getVisibleAllRBMs1D(BenchmarkController benchmarkController, double[] data, boolean binarizeVisible){
		double[][] data2Dimensions = vectorToMatrix(data);
		// TODO
		double[][] visibleData2Dimensions = getVisibleAllRBMs(benchmarkController, data2Dimensions, binarizeVisible, true);
		
		double visibleData[] = matrixToVector(visibleData2Dimensions);

		return visibleData;
	}
	
	public double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeVisible){
		return getVisibleAllRBMs(benchmarkController, data, binarizeVisible, true);
	}
    
    private double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeVisible, boolean useBinarizeOptionGiven) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] hiddenData = data;
        ListIterator<RBMSettingsController> rbmSettingsListIterator = rbmSettingsList.listIterator(rbmSettingsList.size());
        
        while(rbmSettingsListIterator.hasPrevious()) {
        	RBMSettingsController rbmSettingsController = rbmSettingsListIterator.previous();
        	double[][] visibleData;
        	if(useBinarizeOptionGiven) {
        		visibleData = this.getVisibleSingleRBM(rbmSettingsController, hiddenData, binarizeVisible);
        	} else {
        		visibleData = this.getVisibleSingleRBM(rbmSettingsController, hiddenData);
        	}
        	
        	hiddenData = visibleData;
        }
        
        double[][] visibleDataFinal = hiddenData;
        return visibleDataFinal;
    }

    
    public double[][] getVisibleSingleRBM(RBMSettingsController controller, double[][] data, boolean binarizeVisible) {
    	IRBM rbm = this.createRBMForTemporaryUse(controller);
    	return rbm.getVisible(data, binarizeVisible);
    }
    
    public double[][] getVisibleSingleRBM(RBMSettingsController controller, double[][] data) {
    	boolean binarizeVisible = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeVisible();
    	return this.getVisibleSingleRBM(controller, data, binarizeVisible);
    }
    
    public double[] getVisibleSingleRBM(RBMSettingsController controller, double[] data) {
    	boolean binarizeVisible = controller.getModel().getController(RBMSettingsWeightsController.class).getModel().isBinarizeVisible();
    	return this.getVisibleSingleRBM(controller, data, binarizeVisible);
    }
    
    public double[] getVisibleSingleRBM(RBMSettingsController controller, double[] data, boolean binarizeVisible) {
		double[][] data2Dimensions = vectorToMatrix(data);

		double[][] visibleData2Dimensions = getVisibleSingleRBM(controller, data2Dimensions, binarizeVisible);

		double visibleData[] = matrixToVector(visibleData2Dimensions);

		return visibleData;
    }
    
    // DAYDREAM
    
    public double[] daydreamAllRBMs(BenchmarkController controller, double[] data, boolean binarizeHidden, boolean binarizeVisible) {
		double[][] data2Dimensions = vectorToMatrix(data);
		
		double[][] hiddenData2Dimensions = this.getHiddenAllRBMs(controller, data2Dimensions, binarizeHidden);
		double[][] visibleData2Dimensions = this.getVisibleAllRBMs(controller, hiddenData2Dimensions, binarizeVisible);
		
		double visibleData[] = matrixToVector(visibleData2Dimensions);
    	
    	return visibleData;
    }
    
    public double[] daydreamSingleRBM(RBMSettingsController controller, double[] data, boolean binarizeHidden, boolean binarizeVisible) {
    	
    	double[] hiddenData = this.getHiddenSingleRBM(controller, data, binarizeHidden);
    	double[] visibleData = this.getVisibleSingleRBM(controller, hiddenData, binarizeVisible);
    	
    	return visibleData;
    }
    
    private double[] matrixToVector(double[][] matrix) {
		double vector[] = new double[matrix[0].length];
		for (int i = 0; i < matrix[0].length; i++) {
			vector[i] = matrix[0][i];
		}
		return vector;
    }
    
    private double[][] vectorToMatrix(double[] vector) {
		double[][] matrix = new double[1][vector.length];
		for (int i = 0; i < vector.length; i++) {
			matrix[0][i] = vector[i];
		}
		return matrix;
    }

}
 
