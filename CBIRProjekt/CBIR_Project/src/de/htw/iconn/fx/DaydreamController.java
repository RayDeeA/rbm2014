/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import de.htw.cbir.ARBMFeature;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class DaydreamController implements Initializable, IFXController {

    @FXML
    private Button btn_generateImage;
    @FXML
    private Button btn_daydream;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_Input;
    @FXML
    private AnchorPane view;
	
	DaydreamModel model;
	
	Timer timer;
	int maxDreams = 60;
	int currentDream = 0;
	
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new DaydreamModel();
    }
    
    public void setRBMFeature(ARBMFeature rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
    }
    
    @FXML
    private void btn_generateImageAction(ActionEvent event) {
    	this.imgv_Input.setImage(this.model.generateImage());
    	this.btn_daydream.setDisable(false);
    }

    @FXML
	private void btn_daydreamAction(ActionEvent event) {
		this.imgv_Result.setImage(model.daydream());
	}

	@Override
	public Node getView() {
		return this.view;
	}
    
}
