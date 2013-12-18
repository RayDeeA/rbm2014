/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.ImageViewer;
import de.htw.iconn.fx.decomposition.views.ErrorViewController;
import de.htw.iconn.fx.decomposition.views.WeightsVisualizationController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * FXML Controller class
 *
 * @author moritz
 */
public class RBMSettingsVisualizationsController extends AController {

	private RBMSettingsVisualizationsModel model;
	private ErrorViewController errorViewController;
	private ImageViewer featuresView;
	private WeightsVisualizationController weightsViewController;
	@FXML
	private TextField txt_weightsInterval;
	@FXML
	private TextField txt_errorInterval;
	@FXML
	private TextField txt_featuresInterval;
	private Stage errorStage;

    @FXML
    private AnchorPane view;
    @FXML
    private CheckBox cbx_showWeights;
    @FXML
    private CheckBox cbx_showErrorGraph;
    @FXML
    private CheckBox cbx_showFeatures;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsVisualizationsModel(this);
        this.update();
    }

    @FXML
    private void cbx_showWeightsAction(ActionEvent event) {
        this.model.setShowWeights(cbx_showWeights.isSelected());

    }

    @FXML
    private void cbx_showErrorGraphAction(ActionEvent event) {
        this.model.setShowErrorGraph(cbx_showErrorGraph.isSelected());

        if (this.model.isShowErrorGraph()) {
            initErrorView();

            this.errorViewController.setDisplayDimensions();
//			this.updateError();

		} else {
			if (this.errorStage != null) {
				this.errorStage.close();
			}
		}
		this.update();
	}
	
	@FXML
	private void cbx_showFeaturesAction(ActionEvent event) {
		this.model.setShowFeatures(cbx_showFeatures.isSelected());
		
		if (this.model.isShowFeatures()) {
			initFeaturesView();

		} else {
			if (this.featuresView != null) {
				this.featuresView.close();
			}
		}
		this.update();
    }

    @Override
    public Node getView() {
        return view;
    }

    public RBMSettingsVisualizationsModel getModel() {
        return this.model;

    }

    @Override
    public void update() {
        this.cbx_showErrorGraph.setSelected(this.model.isShowErrorGraph());
        this.cbx_showWeights.setSelected(this.model.isShowWeights());
        this.txt_weightsInterval.setText(new Integer(this.model.getWeightsInterval()).toString());
        this.txt_errorInterval.setText(new Integer(this.model.getErrorInterval()).toString());
        this.txt_featuresInterval.setText(new Integer(this.model.getFeaturesInterval()).toString());
    }

    @FXML
    private void txt_weightsIntervalKey(KeyEvent event) {
        try {
            this.model.setWeightsInterval(Integer.parseInt(this.txt_weightsInterval.getText()));
        } catch (NumberFormatException e) {

        }
    }


	@FXML
	private void txt_errorIntervalKey(KeyEvent event) {
		try{
			this.model.setErrorInterval(Integer.parseInt(this.txt_errorInterval.getText()));
		}catch(NumberFormatException e){
			
		}
	}

    @FXML
    private void txt_featuresIntervalKey(KeyEvent event) {
        try {
            this.model.setFeaturesInterval(Integer.parseInt(this.txt_featuresInterval.getText()));
        } catch (NumberFormatException e) {

        }
    }

    private void initErrorView() {
        try {
            this.errorViewController = (ErrorViewController) loadController("../views/ErrorView.fxml");
            Parent root = (Parent) this.errorViewController.getView();
            Scene scene = new Scene(root, 600, 400);

			this.errorStage = new Stage();
			this.errorStage.setTitle("Error Viewer");
			this.errorStage.setScene(scene);
			this.errorStage.setY(42.0);;
			this.errorStage.setX(42.0);
			this.errorStage.setWidth(600.0);
			this.errorStage.show();
			this.model.setShowErrorGraph(true);
//			this.update();
		} catch (IOException ex) {
			Logger.getLogger(RBMSettingsVisualizationsController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void initFeaturesView() {
    	    this.featuresView = new ImageViewer(new ImageManager());
    	    this.featuresView.show();
	}

}
