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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class RBMSettingsLearningRateController extends AController{
    @FXML
    private TextField txt_learningRate;
    @FXML
    private AnchorPane view;
    
    private RBMSettingsLearningRateModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsLearningRateModel(this);
        this.txt_learningRate.setText("" + this.model.getConstantLearningRate());
    }    

    private void txt_learningRateKeyTyped(KeyEvent event) {
        try {
            this.model.setConstantLearningRate(Double.parseDouble(txt_learningRate.getText()));
        } catch(NumberFormatException e) {

        }       
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
    public RBMSettingsLearningRateModel getModel() {
        return model;
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
