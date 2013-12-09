/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class RBMSettingsMainController extends AController {
    @FXML
    private AnchorPane view;
    
    private RBMSettingsMainModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cmb_rbmImplementationAction(ActionEvent event) {
    }

    @FXML
    private void cmb_rbmFeatureAction(ActionEvent event) {
    }

    @FXML
    private void cmb_logisticFunctionAction(ActionEvent event) {
    }

    @FXML
    private void txt_inputSizeAction(ActionEvent event) {
    }

    @FXML
    private void txt_inputSizeKeyTyped(KeyEvent event) {
    }

    @FXML
    private void txt_outputSizeAction(ActionEvent event) {
    }

    @FXML
    private void txt_outputSizeKeyTyped(KeyEvent event) {
    }

    @Override
    public Node getView() {
        return view;
    }
    
    public RBMSettingsMainModel getModel() {
        return model;
    }
    
}
