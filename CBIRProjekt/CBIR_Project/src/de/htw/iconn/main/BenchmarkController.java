/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.main;

import de.htw.iconn.tools.Chooser;
import de.htw.iconn.imageviewer.ImageViewerController;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.views.DaydreamController;
import de.htw.iconn.views.PRTMAPController;
import de.htw.iconn.views.RunHiddenController;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class BenchmarkController extends AController {

	private BenchmarkModel model;
	
    @FXML
    private AnchorPane view;
	
	// Loading
    @FXML
    private ToggleButton btn_OpenShowImages; 
    @FXML
    private Label lbl_imageSetSelected;
	
	// Preprocessing
    @FXML  
    private CheckBox cbx_Binarize;
    @FXML 
    private CheckBox cbx_Invert;
    @FXML
    private CheckBox cbx_Shuffle;
    @FXML
    private TextField txt_imageEdgeSize;
    @FXML
    private TextField txt_MinData;
    @FXML
    private TextField txt_MaxData;
	
	// Functions
    @FXML
    private ToggleButton btn_OpenDaydream;
    private DaydreamController daydreamController;
    private Stage daydreamStage;

    @FXML
    private ToggleButton btn_OpenRunHidden;
    private RunHiddenController runHiddenController;
    private Stage runHiddenStage;
    
    @FXML
    private ToggleButton btn_OpenTestFeatures;
    private RunHiddenController testFeaturesController;
    private Stage testFeaturesStage;
    
    @FXML
    private ToggleButton btn_OpenShowFeatures; 
    private RunHiddenController showFeaturesController;
    private Stage showFeaturesStage;
	
	// Evaluation
    @FXML
    private ComboBox<?> cmb_mAPTests;
	
	// Training

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PRTMAPController prtmapController = null;
        try {
            prtmapController = (PRTMAPController) loadController("../views/PRTMAP.fxml");
        } catch (IOException ex) {
            Logger.getLogger(BenchmarkController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ImageViewerController imageViewerController = null;
        try {
          imageViewerController = (ImageViewerController) loadController("../views/ImageViewer.fxml");
        } catch (IOException ex) {
          Logger.getLogger(BenchmarkController.class.getName()).log(Level.SEVERE, null, ex);
        }
        model = new BenchmarkModel(this, prtmapController, imageViewerController);
        
        loadImageSet(new File("CBIR_Project/images/Test_10x5/"));
        this.update();
    }
    
    
	// Loading

    @FXML
    private void btn_loadImageSetAction(ActionEvent event) {
        loadImageSet(Chooser.openDirectoryChooser("CBIR_Project/images"));
    }

    private void loadImageSet(File file) {
        if (file != null) {
            this.model.setImageManager(file);
            this.lbl_imageSetSelected.setText(this.model.getImageManager().getImageSetName());

            this.initCmbImageManager();
            
            if (this.model.isShowImageViewer()) {
                this.model.getImageViewer().show();
            }
            this.model.globalUpdate();        
        }
    }
    
    @FXML
    private void btn_OpenShowImagesAction(ActionEvent event) {
        this.model.setShowImageViewer(this.btn_OpenShowImages.isSelected());
        if (this.model.getImageViewer() != null) {
            if (this.model.isShowImageViewer()) {
                this.model.getImageViewer().show();
            } else {
                this.model.getImageViewer().close();
            }
        }
    }
    
    
    // Preprocessing
    
    @FXML
    private void cbx_BinarizeAction(ActionEvent event) {
        this.model.setBinarizeImages(this.cbx_Binarize.isSelected());
    }
    
    @FXML
    private void cbx_InvertAction(ActionEvent event) {
      this.model.setInvertImages(this.cbx_Invert.isSelected());
    }
    
    @FXML
    private void cbx_ShuffleAction(ActionEvent event) {
      this.model.setShuffleImages(this.cbx_Shuffle.isSelected());
    }
    
    @FXML
    private void txt_imageEdgeSizeKey(KeyEvent event) {
        try {
            this.model.setImageEdgeSize(Integer.parseInt(this.txt_imageEdgeSize.getText()));
        } catch (NumberFormatException ex) {
        	ex.printStackTrace();
        }
    }
    
    @FXML
    private void txt_MinDataKey(KeyEvent event) {
        try {
            this.model.setMinData(Float.parseFloat(this.txt_MinData.getText()));
        } catch (NumberFormatException ex) {
        	ex.printStackTrace();
        	this.model.setMinData(0.0f);
        }
    }
    
    @FXML
    private void txt_MaxDataKey(KeyEvent event) {
        try {
            this.model.setMaxData(Float.parseFloat(this.txt_MaxData.getText()));
        } catch (NumberFormatException ex) {
        	ex.printStackTrace();
        	this.model.setMaxData(1.0f);
        }
    }

    // Functions
    
    @FXML
    private void btn_OpenDaydreamAction(ActionEvent event) {
        try {
        	if(!btn_OpenDaydream.isSelected()) {
        		 this.daydreamStage.close();
        		 return;
        	}
        	
            this.daydreamController = (DaydreamController) new DaydreamController().loadController("../views/DaydreamView.fxml");
            Parent root = (Parent) this.daydreamController.getView();

            this.daydreamController.setBenchmarkModel(this.getModel());

            Scene scene = new Scene(root, 600, 400);
            this.daydreamStage = new Stage();
            this.daydreamStage.setTitle("Daydream");
            this.daydreamStage.setScene(scene);
            
            if(btn_OpenDaydream.isSelected()) this.daydreamStage.show();
            
            daydreamStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    btn_OpenDaydream.setSelected(false);
                    daydreamController.stopDreaming();
                    daydreamStage.close();
                }
            });
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }

    @FXML
    private void btn_OpenRunHiddenAction(ActionEvent event) {
        try {
           if(!btn_OpenRunHidden.isSelected()) {
              this.runHiddenStage.close();
              return;
           }
           
             this.runHiddenController = (RunHiddenController) new DaydreamController().loadController("../views/RunHiddenView.fxml");
             Parent root = (Parent) this.runHiddenController.getView();
 
             this.runHiddenController.setBenchmarkModel(this.getModel());
 
             Scene scene = new Scene(root, 600, 400);
             this.runHiddenStage = new Stage();
             this.runHiddenStage.setTitle("Run Hidden");
             this.runHiddenStage.setScene(scene);
             
             if(btn_OpenRunHidden.isSelected()) this.runHiddenStage.show();
             
             runHiddenStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                 public void handle(WindowEvent we) {
                     btn_OpenRunHidden.setSelected(false);
                     runHiddenStage.close();
                 }
             });
         } catch (IOException ex) {
           ex.printStackTrace();
         }
    }
    
    @FXML
    private void btn_OpenTestFeaturesAction(ActionEvent event) {
    	// TODO
    	throw new UnsupportedOperationException();
    }
    
    @FXML
    private void btn_OpenShowFeaturesAction(ActionEvent event) {
    	
    	if(!btn_OpenShowFeatures.isSelected()) {
            this.showFeaturesStage.close();
    		return;
        }
    	
        this.model.setShowFeatureViewer(this.btn_OpenShowFeatures.isSelected());
        
        if(this.model.getFeatureViewer() == null) {
        this.model.initFeatureViewer(this);
        }
        
        if (this.model.isShowFeatureViewer()) {
          this.model.getFeatureViewer().update();
            this.model.getFeatureViewer().show();
        } else {
            this.model.getFeatureViewer().close();
        }
    }

    
    // Evaluation
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
        this.model.setSelectedMAPTest(0);
        this.cmb_mAPTests.getSelectionModel().select(this.model.getSelectedMAPTest());

    }

    @FXML
    private void cmb_mAPTestsAction(ActionEvent event) {
        this.model.setSelectedMAPTest(this.cmb_mAPTests.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void btn_startmAPTestAction(ActionEvent event) {

        // who can do it better?:
        int index = this.cmb_mAPTests.getSelectionModel().getSelectedIndex();
        String name = this.model.getImageManager().getNameFromIndex(index);
        this.model.startMAPTest(name);
        this.model.getPRTMAPController().show();
    }

    // Training
    
    @FXML
    private void btn_trainAllAction(ActionEvent event) {
        this.model.trainRBMs();
    }
    
    @FXML
    private void btn_TrainDeepAction(ActionEvent event) {
        this.model.trainDeep();
    }
    
    @FXML
    private void btn_UpdateAction(ActionEvent event) {
        System.out.println("update action");
        this.model.globalUpdate();
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

    public void update() {
    	this.cbx_Binarize.setSelected(this.model.isBinarizeImages());
    	this.cbx_Invert.setSelected(this.model.isInvertImages());
    	this.cbx_Shuffle.setSelected(!this.model.isSorted());
        this.btn_OpenShowImages.setSelected(this.model.isShowImageViewer());
        this.btn_OpenShowFeatures.setSelected(this.model.isShowFeatureViewer());
        this.cmb_mAPTests.getSelectionModel().select(this.model.getSelectedMAPTest());
        this.txt_imageEdgeSize.setText(new Integer(this.model.getImageEdgeSize()).toString());
        this.txt_MinData.setText(new Float(this.model.getMinData()).toString());
        this.txt_MaxData.setText(new Float(this.model.getMaxData()).toString());
        if (this.model.getImageManager() == null) {
            lbl_imageSetSelected.setText("no image set selected");
        } else {
            lbl_imageSetSelected.setText(this.model.getImageManager().getImageSetName());
        }
    }
}
