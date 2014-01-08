/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.settings;

import de.htw.iconn.persistence.Conserve;
import de.htw.iconn.views.ErrorViewController;

/**
 *
 * @author christoph
 */
public class RBMSettingsVisualizationsModel {

    private final RBMSettingsVisualizationsController controller;
    private final ErrorViewController errorViewController;
    
    @Conserve
    private boolean showWeights = false;
    @Conserve
    private boolean showErrorGraph = false;
    @Conserve
    private int weightsInterval = 1000;
    @Conserve
    private int errorInterval = 1000;
    @Conserve
    private int featuresInterval = 1000;

    RBMSettingsVisualizationsModel(RBMSettingsVisualizationsController controller, 
            ErrorViewController errorViewController) {

        this.errorViewController = errorViewController;
        this.controller = controller;    
    }

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

    public ErrorViewController getErrorViewController() {
        return this.errorViewController;
    }

}
