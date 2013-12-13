/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.rbm.ARBMFeature;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class WeightsVisualizationController extends AController {

    @FXML
    private AnchorPane view;
    
    @FXML
    private ImageView imgView;

    
    WeightsVisualizationModel model;
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new WeightsVisualizationModel();   
    }
    
    public void setDimensions(int x, int y){     
        this.model.setDCT(x, y);   
    }
    public void setDisplayDimensions(){
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

    public WeightsVisualizationModel getModel() {
        return this.model;
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
