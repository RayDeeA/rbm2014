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
    private TextField txt_continuousInterval;
    
    private RBMSettingsLoggerModel model; 

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsLoggerModel(this);
        this.update();
    }    

    @FXML
    private void cbx_continuousLoggerAction(ActionEvent event) {
        this.model.setContinuousLoggerOn(cbx_continuousLogger.isSelected());
    }

    @FXML
    private void cbx_finalLoggerAction(ActionEvent event) {
        this.model.setFinalLoggerOn(cbx_finalLogger.isSelected());
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
    public RBMSettingsLoggerModel getModel() {
        return model;
    }

    @FXML
    private void txt_continuousIntervalKey(KeyEvent event) {
        try {
            this.model.setContinuousInterval(Integer.parseInt(this.txt_continuousInterval.getText()));
        } catch(NumberFormatException e) {
            
        }
    }

    @Override
    public void update() {
        this.cbx_continuousLogger.setSelected(this.model.isContinuousLoggerOn());
        this.cbx_finalLogger.setSelected(this.model.isFinalLoggerOn());
        this.txt_continuousInterval.setText(new Integer(this.model.getContinuousInterval()).toString());
    }
    
}
