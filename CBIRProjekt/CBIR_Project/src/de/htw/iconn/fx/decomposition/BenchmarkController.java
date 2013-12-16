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
import javafx.scene.control.ToggleButton;
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

    private PRTMAPController prtmapController;

    private Stage daydreamStage;
    private Stage prtmapStage;
    @FXML
    private Button btn_openDaydream;
    @FXML
    private Button btn_openRunHidden;
    @FXML
    private ToggleButton toggleBtn_PRTMAP;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new BenchmarkModel(this);
        loadImageSet(new File("CBIR_Project/images/Test_10x5/"));
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
    }

    @FXML
    private void btn_openDaydreamAction(ActionEvent event) {
        try {
            this.daydreamController = (DaydreamController) new DaydreamController().loadController("DaydreamView.fxml");
            Parent root = (Parent) this.daydreamController.getView();

            Scene scene = new Scene(root, 600, 400);
            this.daydreamStage = new Stage();
            this.daydreamStage.setTitle("Daydream");
            this.daydreamStage.setScene(scene);
            this.daydreamController.setRbmStack(this.model.getRbmStack());

            daydreamStage.setOnCloseRequest(daydreamController);

            this.daydreamStage.show();

        } catch (IOException ex) {

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
    }

    @FXML
    private void toggleBtn_PRTMAPAction(ActionEvent event) {

        this.model.setVisibilityPRTMAPViewer(this.toggleBtn_PRTMAP.isSelected());
        if (this.model.getPRTMAPController() != null) {
            this.model.getPRTMAPController().toggleVisibility();
        } else {
            try {
                this.model.setPRTMAPController();
                this.prtmapController = (PRTMAPController) this.model.getPRTMAPController().loadController("PRTMAP.fxml");

                Parent root = (Parent) this.prtmapController.getView();
                Scene scene = new Scene(root, 600, 400);
                this.prtmapStage = new Stage();
                this.prtmapStage.setTitle("Map Viewer");
                this.prtmapStage.setScene(scene);
                prtmapStage.setOnCloseRequest(prtmapController);
                this.prtmapStage.show();
            } catch (IOException ex) {
            }
        }
    }
}
