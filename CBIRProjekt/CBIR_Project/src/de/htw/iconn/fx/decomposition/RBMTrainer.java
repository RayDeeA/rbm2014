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
    
    // GET HIDDEN
    
	public double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data) {
		return getHiddenAllRBMs(benchmarkController, data, false, false);
	}
	
	public double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeVisible){
		return getHiddenAllRBMs(benchmarkController, data, binarizeVisible, true);
	}
    
    private double[][] getHiddenAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeHidden, boolean useBinarize) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] visibleData = data;
        
        for(RBMSettingsController rbmSettingsController : rbmSettingsList) {
        	double[][] hiddenData;
        	if(useBinarize) {
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
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}

		double[][] hiddenData2Dimensions = getHiddenSingleRBM(controller, data2Dimensions, binarizeHidden);

		double hiddenData[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			hiddenData[i] = hiddenData2Dimensions[0][i];
		}

		return hiddenData;
	}
	
    // GET VISIBLE
	
	public double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data) {
		return getVisibleAllRBMs(benchmarkController, data, false, false);
	}
	
	public double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeVisible){
		return getVisibleAllRBMs(benchmarkController, data, binarizeVisible, true);
	}
    
    private double[][] getVisibleAllRBMs(BenchmarkController benchmarkController, double[][] data, boolean binarizeVisible, boolean useBinarize) {
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkController.getModel().getRbmSettingsList();

        double[][] hiddenData = data;
        ListIterator<RBMSettingsController> rbmSettingsListIterator = rbmSettingsList.listIterator(rbmSettingsList.size());
        
        while(rbmSettingsListIterator.hasPrevious()) {
        	RBMSettingsController rbmSettingsController = rbmSettingsListIterator.previous();
        	double[][] visibleData;
        	if(useBinarize) {
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
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}

		double[][] visibleData2Dimensions = getVisibleSingleRBM(controller, data2Dimensions, binarizeVisible);

		double visibleData[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			visibleData[i] = visibleData2Dimensions[0][i];
		}

		return visibleData;
    }
    
    // DAYDREAM
    
    public double[] daydreamAllRBMs(BenchmarkController controller, double[] data, boolean binarizeHidden, boolean binarizeVisible) {
		double[][] data2Dimensions = new double[1][data.length];
		for (int i = 0; i < data.length; i++) {
			data2Dimensions[0][i] = data[i];
		}
		
		double[][] hiddenData2Dimensions = this.getHiddenAllRBMs(controller, data2Dimensions, binarizeHidden);
		double[][] visibleData2Dimensions = this.getVisibleAllRBMs(controller, hiddenData2Dimensions, binarizeHidden);
		
		double visibleData[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			visibleData[i] = visibleData2Dimensions[0][i];
		}
    	
    	return visibleData;
    }
    
    public double[] daydreamSingleRBM(RBMSettingsController controller, double[] data, boolean binarizeHidden, boolean binarizeVisible) {
    	
    	double[] hiddenData = this.getHiddenSingleRBM(controller, data, binarizeHidden);
    	double[] visibleData = this.getVisibleSingleRBM(controller, hiddenData, binarizeVisible);
    	
    	return visibleData;
    }

}
 