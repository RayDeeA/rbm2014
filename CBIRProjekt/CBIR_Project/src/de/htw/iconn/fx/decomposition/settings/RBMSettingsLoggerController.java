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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class RBMSettingsLoggerController extends AController{
    @FXML
    private AnchorPane view;
    @FXML
    private CheckBox cbx_continuousLogger;
    @FXML
    private CheckBox cbx_finalLogger;
    @FXML
    private TextField txt_frequency;
    
    private RBMSettingsLoggerModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsLoggerModel();
        cbx_continuousLogger.setSelected(this.model.isContinuousLoggerOn());
        cbx_finalLogger.setSelected(this.model.isFinalLoggerOn());
        
    }    

    @FXML
    private void cbx_continuousLoggerAction(ActionEvent event) {
        this.model.setContinuousLoggerOn(cbx_continuousLogger.isSelected());
    }

    @FXML
    private void cbx_finalLoggerAction(ActionEvent event) {
        this.model.setFinalLoggerOn(cbx_finalLogger.isSelected());
    }

    @FXML
    private void txt_frequencyKeyTyped(KeyEvent event) {

        try {
            this.model.setContinuousIntervall(Integer.parseInt(txt_frequency.getText()));
        } catch(NumberFormatException e) {

        }
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
    public RBMSettingsLoggerModel getModel() {
        return model;
    }
    
}
