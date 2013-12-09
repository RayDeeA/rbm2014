/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
        IRBM rbm = new RBMJBlas(15 , 4, 0.1, new DefaultLogisticMatrixFunction());
        this.model = new RBMSettingsModel(rbm);
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
