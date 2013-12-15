package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.TrainingVisualizer;
import de.htw.iconn.fx.decomposition.enhancement.XMLEndTrainingLogger;
import de.htw.iconn.fx.decomposition.enhancement.XMLTrainingLogger;
import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import de.htw.iconn.fx.decomposition.rbm.ARBMFeature;
import de.htw.iconn.fx.decomposition.rbm.IRBM;
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
import de.htw.iconn.fx.decomposition.tools.Pic;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Moritz
 */
public class RBMSettingsModel {
    
    private final RBMSettingsController controller;

    private final AController[] controllers;
    private final TreeItem[] items;

    private Pic[] data;

    private ARBMFeature rbmFeature;

    public RBMSettingsModel(TreeItem[] items, AController[] controllers, RBMSettingsController controller){
        this.controllers = controllers;
        this.items = items;    
        this.initialize();
        this.controller = controller;
    }

    public TreeItem[] getTreeItems() {
        return this.items;
    }

    public AController[] getControllers() {
        return this.controllers;
    }

    public <T extends AController> T getController(Class<T> type) {
        for (AController aController : controllers) {
            if (aController.getClass().equals(type)) {
                return type.cast(aController);
            }
        }
        return null;
    }

    private void initialize() {
    
        RBMEnhancer rbm;
        RBMSettingsMainModel mainModel = this.getController(RBMSettingsMainController.class).getModel();
        RBMSettingsWeightsModel weightsModel = this.getController(RBMSettingsWeightsController.class).getModel();
        RBMSettingsLearningRateModel learningRateModel = this.getController(RBMSettingsLearningRateController.class).getModel();
        RBMSettingsLoggerModel loggerModel = this.getController(RBMSettingsLoggerController.class).getModel();
        RBMSettingsStoppingConditionModel stoppingConditionModel = this.getController(RBMSettingsStoppingConditionController.class).getModel();
        RBMSettingsVisualizationsModel visualizationsModel = this.getController(RBMSettingsVisualizationsController.class).getModel();

        int inputSize = mainModel.getInputSize();
        int outputSize = mainModel.getOutputSize();
        ILogistic logisticFunction = mainModel.getSelectedLogisticFunctionImplementation();

        double learningRate = learningRateModel.getConstantLearningRate();
        rbm = new RBMEnhancer(new RBMJBlas(inputSize, outputSize, learningRate, logisticFunction));
        
        if(loggerModel.isFinalLoggerOn()) {
            rbm.addEnhancement(new XMLEndTrainingLogger());
        }
        
        if(loggerModel.isContinuousLoggerOn()) {
            rbm.addEnhancement(new XMLTrainingLogger());
        }
        
        if(visualizationsModel.isShowWeights()) {
            // TODO: RBMSettingsVisualizations need to have an update intervall
            rbm.addEnhancement(new TrainingVisualizer(1000, visualizationsModel.getWeightVisualizationController()));
            
           
        }
        
        if(visualizationsModel.isShowErrorGraph()) {
        	rbm.addEnhancement(new TrainingVisualizer(1000, visualizationsModel.getErrorViewController()));
        }
        
        
        if (!weightsModel.isInitializedWeights()) {
            System.out.println(weightsModel.getWeights()[0][0]);
            rbm.setWeights(weightsModel.getWeights());
        }
        
        if(mainModel.getSelectedRbmFeature() == 0)
            rbmFeature = new RBMFeaturePixel(inputSize, outputSize, rbm);
        else            
            rbmFeature = new RBMFeatureDCT(inputSize, outputSize, rbm);
    }
    
    public void trainRBM() {
        initialize();
        RBMSettingsStoppingConditionModel stoppingConditionModel = this.getController(RBMSettingsStoppingConditionController.class).getModel();
        RBMSettingsWeightsModel weightsModel = this.getController(RBMSettingsWeightsController.class).getModel();

        rbmFeature.train(data, stoppingConditionModel.getEpochs(), weightsModel.isBinarizeHidden(), weightsModel.isBinarizeVisible());
        
        weightsModel.setWeights(rbmFeature.getWeights());
        weightsModel.setInitializedWeights(false);
    }

    /**
     * @return the data
     */
    public Pic[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Pic[] data) {
        this.data = data;
    }

    /**
     * @return the rbmFeature
     */
    public IRBM getRBM() {
        return rbmFeature.getRBM();
    }

}
