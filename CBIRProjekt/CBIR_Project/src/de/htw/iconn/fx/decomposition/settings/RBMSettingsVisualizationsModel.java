/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;
import  de.htw.iconn.fx.*;

/**
 *
 * @author christoph
 */

public class RBMSettingsVisualizationsModel {
    
    private boolean showWeights = false;
    private boolean showErrorGraph = false;
    
    private WeightsVisualizationModel model;
    private WeightVisualizationController wvC;
    private TrainingViewController tvC;
    
    public boolean isShowWeights() {
        return showWeights;
    }

    public void setShowWeights(boolean showWeights) {
        this.showWeights = showWeights;
    }

    public boolean isShowErrorGraph() {
        return showErrorGraph;
    }

    public void setShowErrorGraph(boolean showErrorGraph) {
        this.showErrorGraph = showErrorGraph;
    }
    
    public WeightVisualizationController getWeightVisualizationController(){
        return this.wvC;  
    }
    
    public TrainingViewController getTrainingViewController(){
        return this.tvC;     
    }
    
    
}
