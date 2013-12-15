/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.ErrorViewController;
import de.htw.iconn.fx.WeightsVisualizationController;
import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author moritz
 */
public class RBMSettingsVisualizationsController extends AController {
    @FXML
    private AnchorPane view;
    @FXML
    private CheckBox cbx_showWeights;
    @FXML
    private CheckBox cbx_showErrorGraph;
    
    private RBMSettingsVisualizationsModel model;
    private ErrorViewController errorViewController;
    private WeightsVisualizationController weightsViewController;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsVisualizationsModel(this);
        this.update(this.model, null);
    }    

    @FXML
    private void cbx_showWeightsAction(ActionEvent event) {
        this.model.setShowWeights(cbx_showWeights.isSelected()); 
        
    }
    
    @FXML
    private void cbx_showErrorGraphAction(ActionEvent event) {
        this.model.setShowErrorGraph(cbx_showErrorGraph.isSelected());
        
		if (this.cbx_showErrorGraph.isSelected()) {
			generateErrorView();
			//Set DCT
			
//			this.errorViewController.setDimensions(this.model.getInputSize(), this.model.getOutputSize());
			this.errorViewController.setDisplayDimensions();
		}
//			this.updateError();
//		} else {
//			if (this.errorStage != null) {
//				this.errorStage.close();
//			}
//		}
//		this.updateView();
    }

    @Override
    public Node getView() {
        return view;
    }
    
    public RBMSettingsVisualizationsModel getModel(){
        return this.model;
       
    }

    @Override
    public void update(Observable o, Object arg) {
        this.cbx_showErrorGraph.setSelected(this.model.isShowErrorGraph());
        this.cbx_showWeights.setSelected(this.model.isShowWeights());
    }
    
    private void generateErrorView() {
    	
    }
    
    private void generateWeightsView() {
    	
    }

    public void update(RBMInfoPackage pack) {
    
    	if(this.cbx_showErrorGraph.isSelected()); 
    	
    	this.errorViewController.update(pack.getError());
    	
    }
    
    
}
