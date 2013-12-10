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
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
    @FXML
    private ComboBox<?> cmb_rbmImplementation;
    @FXML
    private ComboBox<?> cmb_rbmFeature;
    @FXML
    private ComboBox<?> cmb_logisticFunction;
    @FXML
    private TextField txt_inputSize;
    @FXML
    private TextField txt_outputSize;

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
