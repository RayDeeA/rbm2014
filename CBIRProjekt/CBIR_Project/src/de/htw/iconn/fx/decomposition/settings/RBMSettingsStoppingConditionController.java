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
    private RadioButton rdi_epochs;
    @FXML
    private ToggleGroup grp_stoppingCondition;
    @FXML
    private RadioButton rdi_error;
    @FXML
    private TextField txt_epochs;
    @FXML
    private TextField txt_error;
    @FXML
    private AnchorPane view;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void rdi_epochsAction(ActionEvent event) {
    }

    @FXML
    private void rdi_errorAction(ActionEvent event) {
    }

    @FXML
    private void txt_epochsKeyTyped(KeyEvent event) {
    }

    @FXML
    private void txt_errorKeyTyped(KeyEvent event) {
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
}
