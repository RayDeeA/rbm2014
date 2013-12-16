/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import java.util.LinkedList;

import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.TrainingVisualizer;
import de.htw.iconn.fx.decomposition.enhancement.XMLEndTrainingLogger;
import de.htw.iconn.fx.decomposition.enhancement.XMLTrainingLogger;
import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import de.htw.iconn.fx.decomposition.rbm.ARBMAdapter;
import de.htw.iconn.fx.decomposition.rbm.IRBM;
import de.htw.iconn.fx.decomposition.rbm.RBMAdapterGeneral;
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

                ARBMAdapter lastRbmAdapter = createRBMForTemporaryUse(lastController);
                RBMSettingsModel lastModel = lastController.getModel();
                RBMSettingsWeightsModel lastWeightsModel = lastModel.getController(RBMSettingsWeightsController.class).getModel();

                double[][] data = lastRbmAdapter.getHidden(lastModel.getData(), lastWeightsModel.isBinarizeHidden());

                c.getModel().setData(data);
            }
            this.trainSingleRBM(c);
            lastController = c;
        }
        System.out.println("Training for all RBMs finished");
    }

    public ARBMAdapter createRBMForTemporaryUse(RBMSettingsController controller) {
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

        return new RBMAdapterGeneral(new RBMJBlas(inputSize, outputSize, learningRate, logisticFunction, useSeed, seed, weights));
    }

    public void trainSingleRBM(RBMSettingsController controller) {
        System.out.println("Training started...");
        RBMSettingsModel model = controller.getModel();
        RBMSettingsWeightsModel weightsModel = model.getController(RBMSettingsWeightsController.class).getModel();
        RBMSettingsLoggerModel loggerModel = model.getController(RBMSettingsLoggerController.class).getModel();
        RBMSettingsStoppingConditionModel stoppingConditionModel = model.getController(RBMSettingsStoppingConditionController.class).getModel();
        RBMSettingsVisualizationsModel visualizationsModel = model.getController(RBMSettingsVisualizationsController.class).getModel();
        ARBMAdapter rbm = this.createRBMForTemporaryUse(controller);

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

}
