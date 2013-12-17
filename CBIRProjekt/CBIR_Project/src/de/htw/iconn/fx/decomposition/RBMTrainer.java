/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import java.util.LinkedList;
import java.util.ListIterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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

/**
 *
 * @author christoph
 */
public class RBMTrainer {

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

        if (visualizationsModel.isShowWeights()) {
            rbmEnhancer.addEnhancement(new TrainingVisualizer(weightsInterval, visualizationsModel.getWeightVisualizationController()));
        }

        if (visualizationsModel.isShowErrorGraph()) {
            rbmEnhancer.addEnhancement(new TrainingVisualizer(errorInterval, visualizationsModel.getErrorViewController()));
        }

        rbmEnhancer.train(model.getData(), stoppingConditionModel.getEpochs(), weightsModel.isBinarizeHidden(), weightsModel.isBinarizeVisible());

        weightsModel.setWeights(rbmEnhancer.getWeights());
        
        System.out.println("Training finished");
    }
    
    
    public double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean isBinarizeHidden) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] visibleData = data;
        
        for(RBMSettingsController rbmSettingsController : rbmSettingsList) {
        	double[][] hiddenData = this.getHiddenSingleRBM(rbmSettingsController, visibleData, isBinarizeHidden);
        	visibleData = hiddenData;
        }
        
        double[][] hiddenDataFinal = visibleData;
        return hiddenDataFinal;
    }
    
    public double[][] getHiddenSingleRBM(RBMSettingsController controller, double[][] data, boolean isBinarizeHidden) {
    	double[][] hiddenData = null;
    	
    	IRBM rbm = this.createRBMForTemporaryUse(controller);
    	
    	if(data != null) {
    		hiddenData = rbm.getHidden(data, isBinarizeHidden);
    	} else if(controller.getModel().getData() != null) {
    		data = controller.getModel().getData();
    		hiddenData = rbm.getHidden(data, isBinarizeHidden);
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
    
	public double[] getHiddenSingleRBM(RBMSettingsController controller, double[] data, boolean isBinarizeHidden) {
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}

		double[][] hiddenData2Dimensions = getHiddenSingleRBM(controller, data2Dimensions, isBinarizeHidden);

		double hiddenData[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			hiddenData[i] = hiddenData2Dimensions[0][i];
		}

		return hiddenData;
	}
    
    public double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean isBinarizeVisible) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] hiddenData = data;
        ListIterator<RBMSettingsController> rbmSettingsListIterator = rbmSettingsList.listIterator(rbmSettingsList.size());
        
        while(rbmSettingsListIterator.hasPrevious()) {
        	RBMSettingsController rbmSettingsController = rbmSettingsListIterator.previous();
        	double[][] visibleData = this.getVisibleSingleRBM(rbmSettingsController, hiddenData, isBinarizeVisible);
        	hiddenData = visibleData;
        }
        
        double[][] visibleDataFinal = hiddenData;
        return visibleDataFinal;
    }

    
    public double[][] getVisibleSingleRBM(RBMSettingsController controller, double[][] data, boolean isBinarizeVisible) {
    	IRBM rbm = this.createRBMForTemporaryUse(controller);
    	return rbm.getVisible(data, isBinarizeVisible);
    }
    
    public double[] getVisibleSingleRBM(RBMSettingsController controller, double[] data, boolean isBinarizeVisible) {
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}

		double[][] visibleData2Dimensions = getVisibleSingleRBM(controller, data2Dimensions, isBinarizeVisible);

		double visibleData[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			visibleData[i] = visibleData2Dimensions[0][i];
		}

		return visibleData;
    }
    
    public double[] daydreamAllRBMs(BenchmarkController controller, double[] data, boolean isBinarizeHidden, boolean isBinarizeVisible) {
    	// TODO
    	
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}
		
		
    	
    	throw new NotImplementedException();
    }
    
    public double[] daydreamSingleRBM(RBMSettingsController controller, double[] data, boolean isBinarizeHidden, boolean isBinarizeVisible) {
    	
    	double[] hiddenData = this.getHiddenSingleRBM(controller, data, isBinarizeHidden);
    	double[] visibleData = this.getVisibleSingleRBM(controller, hiddenData, isBinarizeVisible);
    	
    	return visibleData;
    }

}
 