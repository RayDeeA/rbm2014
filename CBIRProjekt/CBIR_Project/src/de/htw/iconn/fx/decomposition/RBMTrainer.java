/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.TrainingVisualizer;
import de.htw.iconn.fx.decomposition.enhancement.XMLEndTrainingLogger;
import de.htw.iconn.fx.decomposition.enhancement.XMLTrainingLogger;
import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import de.htw.iconn.fx.decomposition.rbm.ARBMFeature;
import de.htw.iconn.fx.decomposition.rbm.RBMFeatureDCT;
import de.htw.iconn.fx.decomposition.rbm.RBMFeaturePixel;
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
     public void train(RBMSettingsController controller){
               
            RBMSettingsMainModel mainModel = controller.getModel().getController(RBMSettingsMainController.class).getModel();
            RBMSettingsWeightsModel weightsModel = controller.getModel().getController(RBMSettingsWeightsController.class).getModel();
            RBMSettingsLearningRateModel learningRateModel = controller.getModel().getController(RBMSettingsLearningRateController.class).getModel();
            RBMSettingsLoggerModel loggerModel = controller.getModel().getController(RBMSettingsLoggerController.class).getModel();
            RBMSettingsStoppingConditionModel stoppingConditionModel = controller.getModel().getController(RBMSettingsStoppingConditionController.class).getModel();
            RBMSettingsVisualizationsModel visualizationsModel = controller.getModel().getController(RBMSettingsVisualizationsController.class).getModel();
            
            int inputSize = mainModel.getInputSize();
            int outputSize = mainModel.getOutputSize();
            ILogistic logisticFunction = mainModel.getSelectedLogisticFunctionImplementation();
            double learningRate = learningRateModel.getConstantLearningRate();
            
            RBMEnhancer rbm = new RBMEnhancer(new RBMJBlas(inputSize, outputSize, learningRate, logisticFunction));

            if(loggerModel.isFinalLoggerOn()) {
                rbm.addEnhancement(new XMLEndTrainingLogger());
            }

            if(loggerModel.isContinuousLoggerOn()) {
                rbm.addEnhancement(new XMLTrainingLogger());
            }
            
            int weightsInterval = visualizationsModel.getWeightsInterval();
            int errorInterval = visualizationsModel.getErrorInterval();

            if(visualizationsModel.isShowWeights()) {
                rbm.addEnhancement(new TrainingVisualizer(weightsInterval, visualizationsModel.getWeightVisualizationController()));
            }
            
            if(visualizationsModel.isShowErrorGraph()) {
                    rbm.addEnhancement(new TrainingVisualizer(errorInterval, visualizationsModel.getErrorViewController()));
            }
            
            if (!weightsModel.isInitializedWeights()) {
                System.out.println(weightsModel.getWeights()[0][0]);
                rbm.setWeights(weightsModel.getWeights());
            }
            
            ARBMFeature feature;

            if(mainModel.getSelectedRbmFeature() == 0)
                feature = new RBMFeaturePixel(inputSize, outputSize, rbm);
            else            
                feature = new RBMFeatureDCT(inputSize, outputSize, rbm);
     }
}
