/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class RBMSettingsController extends AController {
    @FXML
    private AnchorPane view;
    @FXML
    private TreeView<String> trv_rbmSettingsMenue;
    @FXML
    private VBox vbox_rbmSettingsTemplatePane;
    @FXML
    private Button btn_intiializeRBM;
    @FXML
    private Button btn_srartRBMTraining;
    @FXML
    private Button btn_cancelRBMTraining;
    @FXML
    private Insets x1;
    
    private RBMSettingsModel model;
    
    private AController[] controllers;
    private TreeItem[] items;

    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //dummy rbm only temporary for testing
        IRBM rbm = new RBMJBlas(15 , 4, 0.1, new DefaultLogisticMatrixFunction());
        this.model = new RBMSettingsModel(rbm);
        
        TreeItem settingsRBM = new TreeItem<String>("RBM");
        settingsRBM.setExpanded(true);
        TreeItem settingsMain = new TreeItem<String>("Main");
        RBMSettingsMainController controller = null;
        try {
            controller = (RBMSettingsMainController) loadController("RBMSettingsMain.fxml");
           
        } catch (IOException ex) {
            Logger.getLogger(RBMSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(controller != null)
        controllers = new AController[]{controller};
        items = new TreeItem[]{settingsMain};
        
        trv_rbmSettingsMenue.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        trv_rbmSettingsMenue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>(){

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> ov, TreeItem<String> oldItem, TreeItem<String> newItem) { 
                int idx = 0;
                for (int i = 0; i < items.length; i++) {
                    if(items[i] == newItem) {
                        idx = i;
                        break;
                    }
                }        
                vbox_rbmSettingsTemplatePane.getChildren().clear();
                vbox_rbmSettingsTemplatePane.getChildren().add(controllers[idx].getView());
            }
            
        });
        
        settingsRBM.getChildren().add(settingsMain);
        trv_rbmSettingsMenue.setRoot(settingsRBM);
        
        //trv_rbmSettingsMenue.setRoot(new TreeItem<String>(RBM));
    }    

    @FXML
    private void btn_intiializeRBMAction(ActionEvent event) {
    }

    @FXML
    private void btn_srartRBMTrainingAction(ActionEvent event) {
    }

    @FXML
    private void btn_cancelRBMTrainingAction(ActionEvent event) {
    }

    @Override
    public Node getView() {
        return view;
    }

    public RBMSettingsModel getModel() {
       return this.model;
    }
    
}
