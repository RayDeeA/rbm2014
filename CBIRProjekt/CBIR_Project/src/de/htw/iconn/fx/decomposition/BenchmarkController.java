/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class BenchmarkController extends AController {
    @FXML
    private Label txt_imageSetSelected;
    
    @FXML
    private AnchorPane view;
    
    private BenchmarkModel model; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new BenchmarkModel();
    }    

    @FXML
    private void btn_loadImageSetAction(ActionEvent event) {
    }

    @FXML
    private void cbx_imageViewerAction(ActionEvent event) {
    }

    @FXML
    private void btn_testFeaturesAction(ActionEvent event) {
    }

    @FXML
    private void cmb_mAPTestsAction(ActionEvent event) {
    }

    @FXML
    private void btn_startmAPTestAction(ActionEvent event) {
    }

    @FXML
    private void btn_daydreamAction(ActionEvent event) {
    }

    @Override
    public Node getView() {
        return view;
    }

    /**
     * @return the model
     */
    public BenchmarkModel getModel() {
        return model;
    }


    
}
