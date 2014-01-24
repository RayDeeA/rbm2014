/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.settings;

import de.htw.iconn.persistence.Conserve;
import de.htw.iconn.views.BarthelVisualizationController;
import de.htw.iconn.views.ErrorViewController;
import de.htw.iconn.views.WeightsVisualizationController;
import de.htw.iconn.views.imageviewer.ImageViewerController;

/**
 *
 * @author christoph
 */
public class RBMSettingsVisualizationsModel {

    private final RBMSettingsVisualizationsController controller;
    private final ErrorViewController errorViewController;
    private final WeightsVisualizationController weightsVisualizationController;
	private final ImageViewerController imageViewController;
	private final BarthelVisualizationController barthelVisualizationController;
    
    @Conserve
    private boolean showWeights = false;
    @Conserve
    private boolean showErrorGraph = false;
    @Conserve
    private boolean showFeatures = false;
    @Conserve
    private boolean showBarthel = false;
    @Conserve
    private int weightsInterval = 1;
    @Conserve
    private int errorInterval = 1;
    @Conserve
    private int featuresInterval = 1;
    @Conserve
    private int barthelInterval = 1;


    RBMSettingsVisualizationsModel(RBMSettingsVisualizationsController controller, ErrorViewController errorViewController, WeightsVisualizationController weightsVisualizationController, ImageViewerController imageViewController, BarthelVisualizationController barthelVisualizationController) {
        this.weightsVisualizationController = weightsVisualizationController;
        this.errorViewController = errorViewController;
        this.imageViewController = imageViewController;
        this.barthelVisualizationController = barthelVisualizationController;
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
	
	public int getBarthelInterval() {
		return barthelInterval;
	}

	public void setBarthelInterval(int barthelInterval) {
		this.barthelInterval = barthelInterval;
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
    
    public boolean isShowFeatures() {
		return showFeatures;
	}

	public void setShowFeatures(boolean showFeatures) {
		this.showFeatures = showFeatures;
	}
	
    public boolean isShowBarthel() {
		return showBarthel;
	}

	public void setShowBarthel(boolean showBarthel) {
		this.showBarthel = showBarthel;
	}

    public ErrorViewController getErrorViewController() {
        return this.errorViewController;
    }

	/**
     * @return the weightsVisualizationController
     */
    public WeightsVisualizationController getWeightsVisualizationController() {
        return weightsVisualizationController;
    }
    
    public ImageViewerController getImageViewController() {
        return imageViewController;
    }
    
    public BarthelVisualizationController getBarthelVisualizationController() {
        return barthelVisualizationController;
    }

}
