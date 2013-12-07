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
public class VisualizationController implements Initializable, IFXController {

    @FXML
    private AnchorPane view;
    
    @FXML
    private ImageView img;
    
    VisualizationModel model;
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new VisualizationModel();
    }
    
    public void setDimensions(int x, int y){     
        this.model.setDCT(x, y);   
    }
    public void setDisplayDimensions(int w, int h){
       this.model.setDisplayDimensions(w, h);
    }
    
    public void setRBMFeature(ARBMFeature rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
    }
    
	@Override
	public Node getView() {
		return this.view;
	}
    
}
