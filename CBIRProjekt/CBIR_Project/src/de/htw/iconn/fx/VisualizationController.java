/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import de.htw.cbir.ARBMFeature;
import java.awt.image.BufferedImage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class VisualizationController implements Initializable, IFXController {

    @FXML
    private AnchorPane view;
    
    @FXML
    private ImageView imgView;

    
    VisualizationModel model;
    private Timer timer;
       
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
    public void setDisplayDimensions(){
        
        //(int)imgView.getFitWidth()
       this.model.setDisplayDimensions(view.getWidth(), view.getHeight());
    }
    
    public void setRBMFeature(ARBMFeature rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
    }
    public void setWeights(double[][] w) {
    	this.model.setWeights(w);
        update();
    }
    
   public void update(){
       imgView.setImage(this.model.generateImage());
    }
    
	@Override
	public Node getView() {
		return this.view;
	}
    
}
