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
    private WeightsVisualizationController weightsVisualizationController;
    private ErrorViewController errorViewController;
    
    public RBMSettingsVisualizationsModel() {
    	this.weightsVisualizationController = new WeightsVisualizationController();
    	this.errorViewController = new ErrorViewController();
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

	public void updateError() {
		// TODO Auto-generated method stub
		// probably the place to call the update of the controller
		
	}

	public void updateWeights() {
		// TODO Auto-generated method stub
		this.errorViewController.update(4.2);
	}
    
    
}
