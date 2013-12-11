/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

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
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.enhancements.RBMEnhancer;
import de.htw.iconn.rbm.functions.ILogistic;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Moritz
 */
public class RBMSettingsModel {
    

    private final AController[] controllers;
    private final TreeItem[] items;
    
    private double[][] data;

    private IRBM rbm;

    public RBMSettingsModel(IRBM rbm, TreeItem[] items, AController[] controllers) {
        this.rbm = rbm;
        this.controllers = controllers;
        this.items = items;
    }
    
    
    public void setRBM(IRBM rbm) {
        this.rbm = rbm;
    }
    
    public IRBM getRBM() {
        return rbm;
    }
    
    public TreeItem[] getTreeItems() {
        return this.items;
    }
    
    public AController[] getControllers() {
        return this.controllers;
    }
    
    public <T extends AController> T getController(Class<T> type) {
        for (AController aController : controllers) {
            if(aController.getClass().equals(type)) {
                return type.cast(aController);
            }
        }
        return null;
    } 
    
    public void trainRBM() {
       RBMSettingsMainModel mainModel = this.getController(RBMSettingsMainController.class).getModel();
       RBMSettingsWeightsModel weightsModel =  this.getController(RBMSettingsWeightsController.class).getModel();
       RBMSettingsLearningRateModel learningRateModel = this.getController(RBMSettingsLearningRateController.class).getModel();
       RBMSettingsLoggerModel loggerModel =  this.getController(RBMSettingsLoggerController.class).getModel();
       RBMSettingsStoppingConditionModel stoppingConditionModel = this.getController(RBMSettingsStoppingConditionController.class).getModel();
       RBMSettingsVisualizationsModel visualizationsModel = this.getController(RBMSettingsVisualizationsController.class).getModel();
       
       int inputSize = mainModel.getInputSize();
       int outputSize = mainModel.getOutputSize();
       ILogistic logisticFunction = mainModel.getSelectedLogisticFunctionImplementation();
       
       double learningRate = learningRateModel.getConstantLearningRate();
       
       if(weightsModel.isInitializedWeights()) {
           
           if(loggerModel.isContinuousLoggerOn() || 
              loggerModel.isFinalLoggerOn() || 
              visualizationsModel.isShowErrorGraph() ||
               visualizationsModel.isShowWeights()) {
               this.rbm = new RBMEnhancer(new RBMJBlas(inputSize, outputSize, learningRate, logisticFunction), null);
           }
       }
       this.rbm.train(data, stoppingConditionModel.getEpochs(), true, true);
    }

    /**
     * @return the data
     */
    public double[][] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(double[][] data) {
        this.data = data;
    }
    
    
    
}
