/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.ResourceBundle;

import de.htw.iconn.fx.decomposition.rbm.ARBMAdapter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class RunHiddenController extends AController implements EventHandler {
    @FXML
    private Button btn_loadNewTestImage;
    @FXML
    private Button btn_runHidden;
    @FXML
    private ToggleButton btn_hiddenStates;
    @FXML
    private ToggleButton btn_visibleStates;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_Input;
    @FXML
    private AnchorPane view;
    
    private RunHiddenModel model;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RunHiddenModel(this);
        this.update();
    }
    
    public void setRBMFeature(ARBMAdapter rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
    }

    @FXML
    private void btn_loadNewTestImageAction(ActionEvent event) {
        Image image = this.model.openFile4();
        if(!image.isError()) {
            this.imgv_Input.setImage(image);
            btn_runHidden.setDisable(false);
        } else {
            System.out.println("error");
        }
    }
    
    @FXML
    private void btn_hiddenStatesAction(ActionEvent event) {
    	this.model.setUseHiddenStates(this.btn_hiddenStates.isSelected());
    }
    
    @FXML
    private void btn_visibleStatesAction(ActionEvent event) {
    	this.model.setUseVisibleStates(this.btn_visibleStates.isSelected());
    }

    @FXML
    private void btn_runHiddenAction(ActionEvent event) {
        this.imgv_Result.setImage(this.model.runHidden());
    }

    @Override
    public Node getView() {
            return this.view;
    }

    @Override
    public void handle(Event arg0) {
            // TODO Auto-generated method stub

    }
    
    @Override
    public void update(){
        
    }
}
