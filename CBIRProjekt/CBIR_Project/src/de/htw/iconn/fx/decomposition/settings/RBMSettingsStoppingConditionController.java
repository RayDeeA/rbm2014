/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class RBMSettingsStoppingConditionController extends AController{
    @FXML
    private TextField txt_epochs;
    @FXML
    private TextField txt_error;
    @FXML
    private AnchorPane view;   
    @FXML
    private CheckBox cbx_epochs;
    @FXML
    private CheckBox cbx_error;
    
    private RBMSettingsStoppingConditionModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsStoppingConditionModel();
    }    

    @FXML
    private void txt_epochsKeyTyped(KeyEvent event) {
        try{
            this.model.setEpochs(Integer.parseInt(this.txt_epochs.getText()));
        }catch(NumberFormatException e){
            
        }
    }

    @FXML
    private void txt_errorKeyTyped(KeyEvent event) {
        try{
            this.model.setError(Double.parseDouble(this.txt_error.getText()));
        }catch(NumberFormatException e){
            
        }
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
    public RBMSettingsStoppingConditionModel getModel(){
        return this.model;
    }

    @FXML
    private void cbx_epochsAction(ActionEvent event) {
        this.model.setEpochsOn(cbx_epochs.isSelected());
    }

    @FXML
    private void cbx_errorAction(ActionEvent event) {
        this.model.setErrorOn(cbx_error.isSelected());
    }
}
