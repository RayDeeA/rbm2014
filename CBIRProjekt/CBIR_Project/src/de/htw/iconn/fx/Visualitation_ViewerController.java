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
public class Visualitation_ViewerController implements Initializable, IFXController {

    @FXML
    private Button btn_generateImage;
    @FXML
    private AnchorPane view;
	
	VisualizationModel model;
        
	Timer timer;
	int visCounter = 60;
	int current = 0;
	
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new VisualizationModel();
    }
    
    public void setDimensions(int x, int y){
    
        
    }
    
    public void setRBMFeature(ARBMFeature rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
    }
    
	@Override
	public Node getView() {
		return this.view;
	}
    
}
