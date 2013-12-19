/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.ErrorViewController;
import de.htw.iconn.fx.WeightsVisualizationController;
import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;

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
 * @author moritz
 */
public class RBMSettingsVisualizationsController extends AController {

    private RBMSettingsVisualizationsModel model;
    private ErrorViewController errorViewController;
    private ImageViewer featuresView;
    private WeightsVisualizationController weightsViewController;
    @FXML
    private TextField txt_weightsInterval;
    @FXML
    private TextField txt_errorInterval;
    @FXML
    private TextField txt_featuresInterval;

    @FXML
    private AnchorPane view;
    @FXML
    private CheckBox cbx_showWeights;
    @FXML
    private CheckBox cbx_showErrorGraph;
    
    private RBMSettingsVisualizationsModel model;
    private ErrorViewController errorViewController;
    private WeightsVisualizationController weightsViewController;
    @FXML
    private TextField txt_weightsInterval;
    @FXML
    private TextField txt_errorInterval;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ErrorViewController errorViewController = null;
        try {
            errorViewController = (ErrorViewController) loadController("../views/ErrorView.fxml");
        } catch (IOException ex) {
            Logger.getLogger(RBMSettingsVisualizationsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.model = new RBMSettingsVisualizationsModel(this, errorViewController);
        this.update();
    }    

    @FXML
    private void cbx_showWeightsAction(ActionEvent event) {
        this.model.setShowWeights(cbx_showWeights.isSelected()); 
        
    }
    
    @FXML
    private void cbx_showErrorGraphAction(ActionEvent event) {
        this.model.setShowErrorGraph(cbx_showErrorGraph.isSelected());
        if(cbx_showErrorGraph.isSelected()) {
            this.model.getErrorViewController().show();
        }
        else {
            this.model.getErrorViewController().hide();
        }
    }

    @FXML
    private void cbx_showFeaturesAction(ActionEvent event) {
        this.model.setShowFeatures(cbx_showFeatures.isSelected());

        if (this.model.isShowFeatures()) {
            initFeaturesView();

        } else {
            if (this.featuresView != null) {
                this.featuresView.close();
            }
        }
        this.update();
    }

    @Override
    public Node getView() {
        return view;
    }
    
    public RBMSettingsVisualizationsModel getModel(){
        return this.model;
       
    }

    @Override
    public void update() {
        this.cbx_showErrorGraph.setSelected(this.model.isShowErrorGraph());
        this.cbx_showWeights.setSelected(this.model.isShowWeights());
        this.txt_weightsInterval.setText(new Integer(this.model.getWeightsInterval()).toString());
        this.txt_errorInterval.setText(new Integer(this.model.getErrorInterval()).toString());
    }

    public void update(RBMInfoPackage pack) {
    
    	if(this.cbx_showErrorGraph.isSelected()){
            this.errorViewController.update(pack.getError());
        }
    	
    }

    @FXML
    private void txt_weightsIntervalKey(KeyEvent event) {
        try{
            this.model.setWeightsInterval(Integer.parseInt(this.txt_weightsInterval.getText()));
        }catch(NumberFormatException e){
            
        }
    }

    @FXML
    private void txt_errorIntervalKey(KeyEvent event) {
        try {
            this.model.setErrorInterval(Integer.parseInt(this.txt_errorInterval.getText()));
        } catch (NumberFormatException e) {

        }
    }

    @FXML
    private void txt_errorIntervalKey(KeyEvent event) {
        try{
            this.model.setErrorInterval(Integer.parseInt(this.txt_errorInterval.getText()));
        }catch(NumberFormatException e){
            
        }
    }


    private void initFeaturesView() {
        this.featuresView = new ImageViewer(new ImageManager());
        this.featuresView.show();
    }

}
