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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
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
    private Button btn_stopDaydream;
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
    	this.btn_generateImage.setDisable(true);
    	this.btn_daydream.setDisable(false);
    	this.btn_stopDaydream.setDisable(true);
    }

    @FXML
	private void btn_daydreamAction(ActionEvent event) {
    	
    	int delay = 0; // delay for 3 sec. 
        int period = 50; // repeat every 5 sec. 
        Timer timer = new Timer();
        this.timer = timer;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
            	System.out.println("Dream");
            	imgv_Result.setImage(model.daydream());
            }
        }, delay, period);
    	this.btn_generateImage.setDisable(true);
    	this.btn_daydream.setDisable(true);
    	this.btn_stopDaydream.setDisable(false);
	}
    
    @FXML
	private void btn_stopDaydreamAction(ActionEvent event) {
    	timer.cancel();
    	timer.purge();
    	this.btn_generateImage.setDisable(false);
    	this.btn_daydream.setDisable(true);
    	this.btn_stopDaydream.setDisable(true);
	}
    
    @FXML
    private void btn_hiddenStatesAction(ActionEvent event) {
    	this.model.setUseHiddenStates(this.btn_hiddenStates.isSelected());
    }
    
    @FXML
    private void btn_visibleStatesAction(ActionEvent event) {
    	this.model.setUseVisibleStates(this.btn_visibleStates.isSelected());
    }

	@Override
	public Node getView() {
		return this.view;
	}
    
}
