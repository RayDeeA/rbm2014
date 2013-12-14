/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.rbm.IRBM;
import de.htw.iconn.fx.decomposition.rbm.RBMJBlas;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //dummy rbm only temporary for testing
        
        
        TreeItem<String> settingsRBM = new TreeItem<>("RBM");
        settingsRBM.setExpanded(true);
        
        TreeItem<String> settingsMain = new TreeItem<>("Main");
        TreeItem<String> settingsWeights = new TreeItem<>("Weights");
        TreeItem<String> settingsStoppingCondition = new TreeItem<>("Stopping Conditions");
        TreeItem<String> settingsLearningRate = new TreeItem<>("Learning Rate");
        TreeItem<String> settingsVisualizations = new TreeItem<>("Training Visualizations");
        TreeItem<String> settingsLogger = new TreeItem<>("Logger");
       
        TreeItem[] items = new TreeItem[]{
            settingsMain,
            settingsWeights,
            settingsStoppingCondition,
            settingsLearningRate,
            settingsVisualizations,
            settingsLogger
        }; 
        
        AController[] controllers = new AController[]{
            addSettings(settingsRBM, settingsMain, "settings/RBMSettingsMain.fxml"),
            addSettings(settingsRBM, settingsWeights, "settings/RBMSettingsWeights.fxml"),
            addSettings(settingsRBM, settingsStoppingCondition, "settings/RBMSettingsStoppingCondition.fxml"),
            addSettings(settingsRBM, settingsLearningRate, "settings/RBMSettingsLearningRate.fxml"),
            addSettings(settingsRBM, settingsVisualizations, "settings/RBMSettingsVisualizations.fxml"),
            addSettings(settingsRBM, settingsLogger, "settings/RBMSettingsLogger.fxml")
        };
        
        
        IRBM rbm = new RBMJBlas(15 , 4, 0.1, new DefaultLogisticMatrixFunction());
        this.model = new RBMSettingsModel(items, controllers, this);
        
        trv_rbmSettingsMenue.setRoot(settingsRBM);

        trv_rbmSettingsMenue.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);   
        
        trv_rbmSettingsMenue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>(){

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> ov, TreeItem<String> oldItem, TreeItem<String> newItem) { 
                TreeItem[] items = model.getTreeItems();
                
                int idx = 0;
                for (int i = 0; i < items.length; i++) {
                    if(items[i].equals(newItem)) {
                        idx = i;
                        break;
                    }
                }        
                vbox_rbmSettingsTemplatePane.getChildren().clear();
                vbox_rbmSettingsTemplatePane.getChildren().add(model.getControllers()[idx].getView());
            }
            
        });
        this.update(this.model, null);
    }    

    private AController addSettings(TreeItem<String> root, TreeItem<String> child, String controllerURL) {
            
        AController controller = null;
        try {
            controller = (AController)loadController(controllerURL);
           
        } catch (IOException ex) {
            Logger.getLogger(RBMSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }

               
        root.getChildren().add(child);
        return (AController) controller;
    }
    @FXML
    private void btn_intiializeRBMAction(ActionEvent event) {
    }

    @FXML
    private void btn_srartRBMTrainingAction(ActionEvent event) {
        this.model.trainRBM();
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

    @Override
    public void update(Observable o, Object arg) {
    
    }
}
