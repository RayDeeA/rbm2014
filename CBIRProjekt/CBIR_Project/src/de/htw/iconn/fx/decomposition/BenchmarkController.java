/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.cbir.ImageManager;
import de.htw.iconn.fx.SimpleRBMController;
import de.htw.iconn.fx.decomposition.views.DaydreamController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class BenchmarkController extends AController {
    
    @FXML
    private AnchorPane view;
    
    private BenchmarkModel model; 
    @FXML
    private CheckBox cbx_imageViewer;
    @FXML
    private ComboBox<?> cmb_mAPTests;
    @FXML
    private Label lbl_imageSetSelected;
    @FXML
    private DaydreamController daydreamController;
    
    private Stage daydreamStage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new BenchmarkModel(this);
        loadImageSet(new File("CBIR_Project/images/Test_10x5/"));
    }    

    @FXML
    private void btn_loadImageSetAction(ActionEvent event) {
        loadImageSet(null);
    }

    private void loadImageSet(File file) {
        if (file == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("CBIR_Project/images"));
            Stage fileChooserStage = new Stage();
            file = directoryChooser.showDialog(fileChooserStage);
        }
        if (file != null) {
            this.model.setImageManager(new ImageManager(file));

            if (this.model.isShowImageViewer()) {
                initializeImageView();
            }

            this.initCmbImageManager();
            this.updateView();
        }
    }
    
    private void initializeImageView() {

        this.model.createImageViewer();
        this.model.getImageViewer().show();
    }
    
    @FXML
    private void cbx_imageViewerAction(ActionEvent event) {
        if(this.model.getImageViewer() == null)
            
        this.model.createImageViewer();
        this.model.getImageViewer().show();
    }

    @FXML
    private void btn_testFeaturesAction(ActionEvent event) {
    }

    @FXML
    private void cmb_mAPTestsAction(ActionEvent event) {
    }

    @FXML
    private void btn_startmAPTestAction(ActionEvent event) {
    }

    @FXML
    private void btn_openDaydreamAction(ActionEvent event) {
		try {
			this.daydreamController = (DaydreamController) new DaydreamController().loadController("views/DaydreamView.fxml");
			Parent root = (Parent) this.daydreamController.getView();

			Scene scene = new Scene(root, 600, 400);
			this.daydreamStage = new Stage();
			this.daydreamStage.setTitle("Daydream");
			this.daydreamStage.setScene(scene);
			this.daydreamController.setRbmStack(this.model.getRbmStack());

			//Event<WindowEvent> event = new Event<>(arg0);
			daydreamStage.setOnCloseRequest(daydreamController);
			
			// this.prChartViewerController.show(lineChart);
			this.daydreamStage.show();

		} catch (IOException ex) {
			Logger.getLogger(SimpleRBMController.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    
    @FXML
    private void btn_openRunHiddenAction(ActionEvent event) {
    	
    }

    @Override
    public Node getView() {
        return view;
    }

    /**
     * @return the model
     */
    public BenchmarkModel getModel() {
        return model;
    }
    
    private void initCmbImageManager() {
        List<String> mapTest;
        if (this.model.getImageManager() != null) {
            mapTest = new LinkedList<>(this.model.getImageManager().getGroupNames());
            mapTest.add(0, "All");
        } else {
            mapTest = new LinkedList<>();
        }
        ObservableList mapTestObs = FXCollections.observableList(mapTest);
        this.cmb_mAPTests.setItems(mapTestObs);
        this.cmb_mAPTests.getSelectionModel().selectFirst();
    }

    private void updateView() {
        if (this.model.getImageManager() == null) {
            lbl_imageSetSelected.setText("no image set selected");
        } else {
            lbl_imageSetSelected.setText(this.model.getImageManager().getImageSetName());
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }


    
}
