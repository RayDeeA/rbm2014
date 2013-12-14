/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;



/**
 * FXML Controller class
 *
 * @author christoph
 */
public class ControlCenterController extends AController  {
    @FXML
    private MenuItem mnu_newRbm;
    @FXML
    private AnchorPane view;
    @FXML
    private VBox vbox;
        
    private BenchmarkController benchmarkController;
    @FXML
    private MenuItem mnu_saveConfiguration;
    @FXML
    private MenuItem mnu_loadConfiguration;
    
    private ConfigurationSaver configurationSaver;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.configurationSaver = new ConfigurationSaver();
        
        try {
            benchmarkController =  (BenchmarkController)loadController("Benchmark.fxml");
            
            vbox.getChildren().add(benchmarkController.getView());
            
        } catch (IOException ex) {
            Logger.getLogger(ControlCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void mnu_newRbmAction(ActionEvent event) {
        try {
            
            RBMSettingsController controller = (RBMSettingsController)loadController("RBMSettings.fxml"); 
            benchmarkController.getModel().add(controller);
            vbox.getChildren().add(controller.getView());
        } catch (IOException ex) {
            System.out.println("Could not load controller");
            Logger.getLogger(ControlCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @FXML
    private void mnu_saveConfigurationAction(ActionEvent event) {
        try {
            configurationSaver.saveConfigurationToFile(this.benchmarkController.getModel());
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            System.err.println("ERROR: could not save configuration to file");
            Logger.getLogger(ControlCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnu_loadConfigurationAction(ActionEvent event) {
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }
}
