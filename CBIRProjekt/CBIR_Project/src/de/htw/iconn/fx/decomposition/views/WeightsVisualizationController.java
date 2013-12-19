/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.IVisualizeObserver;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;
import de.htw.iconn.fx.decomposition.rbm.ARBMAdapter;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class WeightsVisualizationController extends AController implements IVisualizeObserver {

    @FXML
    private AnchorPane view;

    @FXML
    private ImageView imgView;

    WeightsVisualizationModel model;
    
    private final Stage weightViewStage = new Stage();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new WeightsVisualizationModel(this);
        
        Parent root = (Parent) this.getView();
        Scene scene = new Scene(root, 600, 400);
        weightViewStage.setScene(scene);
        // weightViewStage.setOnCloseRequest(this);
        
    }

    public void setDimensions(int x, int y) {
        this.model.setDCT(x, y);
    }

    public void setDisplayDimensions() {
        this.model.setDisplayDimensions(view.getWidth(), view.getHeight());
    }

    public void setRBMFeature(ARBMAdapter rbmFeature) {
        this.model.setRbmFeature(rbmFeature);
    }

    public void setWeights(double[][] w) {
        this.model.setWeights(w);
        update();
    }

    @Override
    public void update() {
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
    public void update(RBMInfoPackage pack) {
		// TODO Auto-generated method stub

    }

}
