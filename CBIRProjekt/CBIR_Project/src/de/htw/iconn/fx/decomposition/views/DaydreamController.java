/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.views;

import de.htw.cbir.ARBMFeature;
import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.IFXController;
import de.htw.iconn.fx.decomposition.RBMSettingsController;
import de.htw.iconn.rbm.RBMStack;

import java.net.URL;
import java.util.LinkedList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.glass.events.WindowEvent;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class DaydreamController extends AController implements EventHandler {

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
    
	public void setRbmStack(RBMStack rbmStack) {
		this.model.setRBMStack(rbmStack);
	}
    
    @FXML
    private void btn_loadImageAction(ActionEvent event) {
        Image image = this.model.loadImage();
        if(!image.isError()) {
            this.imgv_Input.setImage(image);
        } else {
            System.out.println("error");
        }
    	
    	this.btn_daydream.setDisable(false);
    	this.btn_stopDaydream.setDisable(true);
    }
    
    @FXML
    private void btn_generateImageAction(ActionEvent event) {
    	this.imgv_Input.setImage(this.model.generateImage());
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
            	imgv_Result.setImage(model.daydream((int)imgv_Result.getFitWidth(), (int)imgv_Result.getFitHeight()));
            }
        }, delay, period);
    	this.btn_daydream.setDisable(true);
    	this.btn_stopDaydream.setDisable(false);
	}
    
    @FXML
	private void btn_stopDaydreamAction(ActionEvent event) {
    	stopDreaming();
	}
    
    @FXML
    private void btn_hiddenStatesAction(ActionEvent event) {
    	this.model.setUseHiddenStates(this.btn_hiddenStates.isSelected());
    }
    
    @FXML
    private void btn_visibleStatesAction(ActionEvent event) {
    	this.model.setUseVisibleStates(this.btn_visibleStates.isSelected());
    }
    
    public void stopDreaming() {
    	if(timer != null) {
	    	timer.cancel();
	    	timer.purge();
	    	this.btn_daydream.setDisable(true);
	    	this.btn_stopDaydream.setDisable(true);
    	}
    }

	@Override
	public Node getView() {
		return this.view;
	}

	@Override
	public void handle(Event arg0) {
		stopDreaming();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}