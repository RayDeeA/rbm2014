/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private TextField txt_outputSize;
    @FXML
    private Label lbl_inputSize;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsMainModel(this);
        initCmb();
        this.update(this.model, null);
    }
    
    private void initCmb() {
        List<String> rbmImplementation = new LinkedList<>(Arrays.asList(this.model.getRbmImplementations()));
        ObservableList rbmImplementationObs = FXCollections.observableList(rbmImplementation);
        this.cmb_rbmImplementation.setItems(rbmImplementationObs);
        this.cmb_rbmImplementation.getSelectionModel().selectFirst();

        List<String> rbmFeature = new LinkedList<>(Arrays.asList(this.model.getRbmFeatures()));
        ObservableList rbmFeatureObs = FXCollections.observableList(rbmFeature);
        this.cmb_rbmFeature.setItems(rbmFeatureObs);
        this.cmb_rbmFeature.getSelectionModel().select(1);

        List<String> logisticFunction = new LinkedList<>(Arrays.asList(this.model.getLogisticFunctions()));
        ObservableList logisticFunctionObs = FXCollections.observableList(logisticFunction);
        this.cmb_logisticFunction.setItems(logisticFunctionObs);
        this.cmb_logisticFunction.getSelectionModel().selectFirst();
    }

    @FXML
    private void cmb_rbmImplementationAction(ActionEvent event) {
        this.model.setSelectedRbmImplementation(cmb_rbmImplementation.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void cmb_rbmFeatureAction(ActionEvent event) {
        this.model.setSelectedRbmFeature(cmb_rbmFeature.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void cmb_logisticFunctionAction(ActionEvent event) {
        this.model.setSelectedLogisticFunction(cmb_logisticFunction.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void txt_outputSizeKey(KeyEvent event) {
        try{
            this.model.setOutputSize(Integer.parseInt(this.txt_outputSize.getText()));
        }catch(NumberFormatException e){
            
        }
    }

    @Override
    public Node getView() {
        return view;
    }
    
    public RBMSettingsMainModel getModel() {
        return model;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.cmb_logisticFunction.getSelectionModel().select(this.model.getSelectedLogisticFunction());
        this.cmb_rbmFeature.getSelectionModel().select(this.model.getSelectedRbmFeature());
        this.cmb_rbmImplementation.getSelectionModel().select(this.model.getSelectedRbmImplementation());
        this.txt_outputSize.setText(new Integer(this.model.getOutputSize()).toString());
        this.lbl_inputSize.setText(new Integer(this.model.getInputSize()).toString());
    }
    
}
