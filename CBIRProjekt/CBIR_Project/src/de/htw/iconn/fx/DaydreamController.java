/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.htw.cbir.RBMWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class DaydreamController implements Initializable {

    @FXML
    private Button btn_generateImage;
    @FXML
    private Button btn_daydream;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_Input;

	
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
    
    public void setRBMWrapper(RBMWrapper rbmWrapper) {
    	this.model.setRbmWrapper(rbmWrapper);
    }
    
    @FXML
    private void btn_generateImageAction(ActionEvent event) {
    	this.imgv_Input.setImage(this.model.generateImage());
    	this.btn_daydream.setDisable(false);
    }

    @FXML
	private void btn_daydreamAction(ActionEvent event) {
		class Daydream extends TimerTask {
			public void run() {
				if (currentDream == maxDreams) {
					timer.cancel();
					timer.purge();
					return;
				}

				imgv_Result.setImage(model.daydream());
				currentDream++;
			}
		}

		// And From your main() method or any other method
		timer = new Timer();
		timer.schedule(new Daydream(), 0, 1000);
	}


    
}
