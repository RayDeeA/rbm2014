/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.settings;

import de.htw.iconn.enhancement.RBMEnhancer;
import de.htw.iconn.views.BarthelVisualizationController;
import de.htw.iconn.views.ErrorViewController;
import de.htw.iconn.views.FeatureViewer;
import de.htw.iconn.main.AController;
import de.htw.iconn.views.WeightsVisualizationController;
import de.htw.iconn.views.imageviewer.ImageViewerController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
    @FXML
    private TextField txt_weightsInterval;
    @FXML
    private TextField txt_errorInterval;
    @FXML
    private TextField txt_featuresInterval;
    @FXML
    private TextField txt_BarthelInterval;

    @FXML
    private AnchorPane view;
    @FXML
    private CheckBox cbx_showWeights;
    @FXML
    private CheckBox cbx_showErrorGraph;
    @FXML
    private CheckBox cbx_showFeatures;
    @FXML
    private CheckBox cbx_showBarthel;
    
    @FXML
    private Label lbl_weightsInterval;
    @FXML
    private Label lbl_errorInterval;
    @FXML
    private Label lbl_FeaturesInterval;
    @FXML
    private Label lbl_BarthelInterval;


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
        WeightsVisualizationController weightsVisualizationController = null;
        try {
            weightsVisualizationController = (WeightsVisualizationController) 
                    loadController("../views/WeightsVisualization.fxml");
        } catch (IOException ex) {
            Logger.getLogger(RBMSettingsVisualizationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageViewerController imageViewController = null;
        try {
        	imageViewController = (ImageViewerController) loadController("../views/imageviewer/ImageViewer.fxml");
        } catch (IOException ex) {
            Logger.getLogger(RBMSettingsVisualizationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        BarthelVisualizationController barthelVisualizationController = null;
        try {
        	barthelVisualizationController = (BarthelVisualizationController) loadController("../views/BarthelVisualizationView.fxml");
        } catch (IOException ex) {
            Logger.getLogger(BarthelVisualizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbl_errorInterval.setText("x " + RBMEnhancer.BASE_INTERVAL);
        lbl_weightsInterval.setText("x " + RBMEnhancer.BASE_INTERVAL);
        lbl_FeaturesInterval.setText("x " + RBMEnhancer.BASE_INTERVAL);
        this.model = new RBMSettingsVisualizationsModel(this, errorViewController, weightsVisualizationController, imageViewController, barthelVisualizationController);
        
        this.update();
    }
    

    @FXML
    private void cbx_showWeightsAction(ActionEvent event) {
        this.model.setShowWeights(cbx_showWeights.isSelected());
        final WeightsVisualizationController weightsVisualizationController = 
                this.model.getWeightsVisualizationController();
        if(cbx_showWeights.isSelected()) {
            weightsVisualizationController.show();
        } else {
            
            weightsVisualizationController.hide();
        }
        
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
        if(cbx_showFeatures.isSelected()) {
            this.model.getImageViewController().show();
        }
        else {
            this.model.getImageViewController().close();
        }
    }
    
    @FXML
    private void cbx_showBarthelAction(ActionEvent event) {
        this.model.setShowBarthel(cbx_showBarthel.isSelected());
        if(cbx_showBarthel.isSelected()) {
            this.model.getBarthelVisualizationController().show();
        }
        else {
        	this.model.getBarthelVisualizationController().hide();
        }
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
        this.cbx_showFeatures.setSelected(this.model.isShowFeatures());
        this.txt_weightsInterval.setText(new Integer(this.model.getWeightsInterval()).toString());
        this.txt_errorInterval.setText(new Integer(this.model.getErrorInterval()).toString());
        this.txt_featuresInterval.setText(new Integer(this.model.getFeaturesInterval()).toString());
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
    private void txt_featuresIntervalKey(KeyEvent event) {
        try {
            this.model.setFeaturesInterval(Integer.parseInt(this.txt_featuresInterval.getText()));
        } catch (NumberFormatException e) {

        }
    }
    
    @FXML
    private void txt_BarthelIntervalKey(KeyEvent event) {
        try {
            this.model.setBarthelInterval(Integer.parseInt(this.txt_BarthelInterval.getText()));
        } catch (NumberFormatException e) {

        }
    }

}
