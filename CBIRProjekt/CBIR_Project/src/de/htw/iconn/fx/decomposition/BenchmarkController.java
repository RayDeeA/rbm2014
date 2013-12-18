/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.views.DaydreamController;
import de.htw.iconn.fx.decomposition.views.PRTMAPController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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

    private DaydreamController daydreamController;
    
    private RBMTrainer rbmTrainer;
    
    private Stage daydreamStage;
    @FXML
    private Button btn_openDaydream;
    @FXML
    private Button btn_openRunHidden;
    @FXML
    private Button btn_trainAll;
    @FXML
    private TextField txt_imageEdgeSize;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PRTMAPController tmpController = null;
        try {
            tmpController = (PRTMAPController) loadController("views/PRTMAP.fxml");
        } catch (IOException ex) {
            Logger.getLogger(BenchmarkController.class.getName()).log(Level.SEVERE, null, ex);
        }

        model = new BenchmarkModel(this, tmpController);
        loadImageSet(new File("CBIR_Project/images/Test_10x5/"));
        this.rbmTrainer = new RBMTrainer();
        this.update();
    }

    @FXML
    private void btn_loadImageSetAction(ActionEvent event) {
        loadImageSet(Chooser.openDirectoryChooser("CBIR_Project/images"));
    }

    private void loadImageSet(File file) {
        if (file != null) {
            this.model.setImageManager(new ImageManager(file));

            this.initCmbImageManager();
            if (this.model.isShowImageViewer()) {
                this.model.getImageViewer().show();
            }
            this.updateView();
        }
    }

    @FXML
    private void cbx_imageViewerAction(ActionEvent event) {
        this.model.setShowImageViewer(this.cbx_imageViewer.isSelected());
        if (this.model.getImageViewer() != null) {
            if (this.model.isShowImageViewer()) {
                this.model.getImageViewer().show();
            } else {
                this.model.getImageViewer().close();
            }
        }
    }

    @FXML
    private void btn_testFeaturesAction(ActionEvent event) {
    }

    @FXML
    private void cmb_mAPTestsAction(ActionEvent event) {
        this.model.setSelectedMAPTest(this.cmb_mAPTests.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void btn_startmAPTestAction(ActionEvent event) {
        this.model.getPRTMAPController().show();
        // make test        
    }

    @FXML
    private void btn_openDaydreamAction(ActionEvent event) {
        try {
            this.daydreamController = (DaydreamController) new DaydreamController().loadController("DaydreamView.fxml");
            Parent root = (Parent) this.daydreamController.getView();
            
            this.daydreamController.setBenchmarkController(this);

            Scene scene = new Scene(root, 600, 400);
            this.daydreamStage = new Stage();
            this.daydreamStage.setTitle("Daydream");
            this.daydreamStage.setScene(scene);
            // this.daydreamController.setRbmStack(this.model.getRbmStack());

            daydreamStage.setOnCloseRequest(daydreamController);

            this.daydreamStage.show();

        } catch (IOException ex) {

        }
    }

    @FXML
    private void btn_openRunHiddenAction(ActionEvent event) {
    	//TODO
    	throw new UnsupportedOperationException();
    }
    
    @FXML
    private void btn_trainAllAction(ActionEvent event) {
    	this.rbmTrainer.trainAllRBMs(this);
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
        this.cmb_mAPTests.getSelectionModel().select(this.model.getSelectedMAPTest());
    }

    private void updateView() {
        if (this.model.getImageManager() == null) {
            lbl_imageSetSelected.setText("no image set selected");
        } else {
            lbl_imageSetSelected.setText(this.model.getImageManager().getImageSetName());
        }
    }

    public void update() {
        this.cbx_imageViewer.setSelected(this.model.isShowImageViewer());
        this.cmb_mAPTests.getSelectionModel().select(this.model.getSelectedMAPTest());
        this.txt_imageEdgeSize.setText(new Integer(this.model.getImageEdgeSize()).toString());
    }

    @FXML
    private void txt_imageEdgeSizeKey(KeyEvent event) {
        try{
            this.model.setImageEdgeSize(Integer.parseInt(this.txt_imageEdgeSize.getText()));
        } catch(NumberFormatException e){
            
        }
    }

}
