/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.tools.ImageViewer;
import de.htw.iconn.fx.decomposition.views.ErrorViewController;
import de.htw.iconn.fx.decomposition.views.WeightsVisualizationController;

/**
 *
 * @author christoph
 */

public class RBMSettingsVisualizationsModel{
    private final RBMSettingsVisualizationsController controller;
    
    private boolean showWeights = false;
    private boolean showErrorGraph = false;
    
    private final WeightsVisualizationController weightsVisualizationController;
    private final ErrorViewController errorViewController;
    private final ImageViewer featureViewer;
    
    private int weightsInterval = 1000;
    private int errorInterval = 1000;
	private int featuresInterval = 1000;

	private boolean showFeatures;

    public int getWeightsInterval() {
        return weightsInterval;
    }

    public void setWeightsInterval(int weightsInterval) {
        this.weightsInterval = weightsInterval;
    }

    public int getErrorInterval() {
        return errorInterval;
    }

    public void setErrorInterval(int errorInterval) {
        this.errorInterval = errorInterval;
    }

    public int getFeaturesInterval() {
        return featuresInterval;
    }
    
	public void setFeaturesInterval(int featuresInterval) {
		this.featuresInterval = featuresInterval;
	}
    
    public RBMSettingsVisualizationsModel(RBMSettingsVisualizationsController controller) {
    	this.weightsVisualizationController = new WeightsVisualizationController();
    	this.errorViewController = new ErrorViewController();
    	this.featureViewer = new ImageViewer();
        this.controller = controller;
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
    
	public void setShowFeatures(boolean showFeatures) {
		this.showFeatures = showFeatures;
	}
	
    public boolean isShowFeatures() {
        return showFeatures;
    }
    
    public WeightsVisualizationController getWeightVisualizationController(){
        return this.weightsVisualizationController;  
    }
    
    public ErrorViewController getErrorViewController(){
        return this.errorViewController;     
    }

    public ImageViewer getFeatureViewer(){
        return this.featureViewer;     
    }



}
