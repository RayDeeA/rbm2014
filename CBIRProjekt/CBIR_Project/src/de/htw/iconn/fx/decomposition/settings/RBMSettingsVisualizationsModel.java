/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AModel;
import de.htw.iconn.fx.decomposition.views.ErrorViewController;
import de.htw.iconn.fx.decomposition.views.WeightsVisualizationController;
import de.htw.iconn.fx.decomposition.views.WeightsVisualizationModel;

/**
 *
 * @author christoph
 */

public class RBMSettingsVisualizationsModel extends AModel{
    
    private boolean showWeights = false;
    private boolean showErrorGraph = false;
    
    private WeightsVisualizationModel model;
    private final WeightsVisualizationController weightsVisualizationController;
    private final ErrorViewController errorViewController;
    
    public RBMSettingsVisualizationsModel(RBMSettingsVisualizationsController controller) {
    	this.weightsVisualizationController = new WeightsVisualizationController();
    	this.errorViewController = new ErrorViewController();
        this.addObserver(controller);
    }
    
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
    
    public WeightsVisualizationController getWeightVisualizationController(){
        return this.weightsVisualizationController;  
    }
    
    public ErrorViewController getErrorViewController(){
        return this.errorViewController;     
    }
}
