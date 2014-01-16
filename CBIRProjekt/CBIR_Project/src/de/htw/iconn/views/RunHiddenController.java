/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.views;

import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkModel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class RunHiddenController extends AController {
    @FXML
    private Button btn_runHidden;
    @FXML
    private ToggleButton btn_hiddenStates;
    @FXML
    private ToggleButton btn_visibleStates;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_ResultHidden;
    @FXML
    private ImageView imgv_Input;
    @FXML
    private Label lbl_MSE;
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

    @FXML
    private void btn_loadImageAction(ActionEvent event) {
        Image image = this.model.loadImage((int) imgv_Result.getFitWidth(), (int) imgv_Result.getFitHeight());
        if (!image.isError()) {
            this.imgv_Input.setImage(image);
        } else {
            System.out.println("error");
        }
        this.btn_runHidden.setDisable(false);
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
        System.out.println("Dream");
        model.runHidden();
        imgv_Result.setImage(model.getVisibleImage((int)imgv_Result.getFitWidth(), (int)imgv_Result.getFitHeight()));
        Image hiddenImage = model.getHiddenImage(10);
        imgv_ResultHidden.setFitWidth(hiddenImage.getWidth());
        imgv_ResultHidden.setFitHeight(hiddenImage.getHeight());
        imgv_ResultHidden.setImage(hiddenImage);
        lbl_MSE.setText("MSE: " + model.getMSE());
    }

    @Override
    public Node getView() {
        return this.view;
    }
    
    @Override
    public void update(){
        
    }

	public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
		this.model.setBenchmarkModel(benchmarkModel);
	}

}
