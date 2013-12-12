/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
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

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsVisualizationsModel(this);
    }    

    @FXML
    private void cbx_showWeightsAction(ActionEvent event) {
        this.model.setShowWeights(cbx_showWeights.isSelected()); 
        
    }
    
    @FXML
    private void cbx_showErrorGraphAction(ActionEvent event) {
        this.model.setShowErrorGraph(cbx_showErrorGraph.isSelected());
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

}
