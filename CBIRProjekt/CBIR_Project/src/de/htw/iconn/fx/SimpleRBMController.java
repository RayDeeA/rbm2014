package de.htw.iconn.fx;

import de.htw.cbir.ImageManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class SimpleRBMController implements Initializable, IFXController {

    @FXML
    private Insets x1;
    @FXML
    private Insets x2;
    @FXML
    private ToggleGroup stoppingConditionRadios;
    @FXML
    private Button btn_loadImageSet;
    @FXML
    private CheckBox cbx_imageViewer;
    @FXML
    private Button btn_startTraining;
    @FXML
    private Button btn_startEvolution;
    @FXML
    private CheckBox cbx_logger;
    @FXML
    private CheckBox cbx_visualization;
    @FXML
    private TextField txt_updateFrequency;
    @FXML
    private Button btn_runVisible;
    @FXML
    private Button btn_runHidden;
    @FXML
    private Button btn_daydream;
    @FXML
    private Button btn_saveRbmFile;
    @FXML
    private ComboBox<?> cmb_mapTests;
    @FXML
    private Button btn_startTest;
    @FXML
    private Label lbl_imageSet;
    @FXML
    private ComboBox<?> cmb_rbmImplementation;
    @FXML
    private ComboBox<?> cmb_rbmFeature;
    @FXML
    private ComboBox<?> cmb_logisticFunction;
    @FXML
    private TextField txt_inputSize;
    @FXML
    private TextField txt_outputSize;
    @FXML
    private RadioButton rdi_epochs;
    @FXML
    private TextField txt_epochs;
    @FXML
    private RadioButton rdi_error;
    @FXML
    private TextField txt_error;
    @FXML
    private TextField txt_learningRate;
    @FXML
    private CheckBox cbx_momentum;
    @FXML
    private TextField txt_seed;
    @FXML
    private CheckBox cbx_binarizeProbabilities;
    @FXML
    private CheckBox cbx_seed;
    @FXML
    private CheckBox cbx_bias;
    @FXML
    private CheckBox cbx_randomOrder;

    @FXML
    private AnchorPane view;

    private ImageViewer imageViewer;
    private ChartViewerController chartViewerController;
    private SimpleRBMModel model;

    private Stage imageViewerStage;
    private Stage chartViewerStage;
    @FXML
    private CheckBox cbx_map;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new SimpleRBMModel();
        initCmb();
        updateView();
        loadImageSet("CBIR_Project/images/WebImages_71x6/");
    }

    private void initCmbImageManager() {
        List<String> mapTest;
        if (this.model.getImageManager() != null) {
            mapTest = new LinkedList<>(this.model.getImageManager().getGroupNames());
        } else {
            mapTest = new LinkedList<>();
        }
        ObservableList mapTestObs = FXCollections.observableList(mapTest);
        this.cmb_mapTests.setItems(mapTestObs);
    }

    private void initCmb() {

        initCmbImageManager();

        List<String> rbmImplementation = new LinkedList<>(Arrays.asList(this.model.getRbmImplementations()));
        ObservableList rbmImplementationObs = FXCollections.observableList(rbmImplementation);
        this.cmb_rbmImplementation.setItems(rbmImplementationObs);

        List<String> rbmFeature = new LinkedList<>(Arrays.asList(this.model.getRbmFeatures()));
        ObservableList rbmFeatureObs = FXCollections.observableList(rbmFeature);
        this.cmb_rbmFeature.setItems(rbmFeatureObs);

        List<String> logisticFunction = new LinkedList<>(Arrays.asList(this.model.getLogisticFunctions()));
        ObservableList logisticFunctionObs = FXCollections.observableList(logisticFunction);
        this.cmb_logisticFunction.setItems(logisticFunctionObs);
    }

    private void updateView() {
        if (this.model.getImageManager() == null) {
            lbl_imageSet.setText("no image set selected");
        } else {
            lbl_imageSet.setText(this.model.getImageManager().getImageSetName());
        }
        this.cbx_imageViewer.setSelected(this.model.isShowImageViewer());
        this.cbx_randomOrder.setSelected(this.model.isUseRandomOrder());
        this.cbx_logger.setSelected(this.model.isUseLogger());
        this.cbx_visualization.setSelected(this.model.isShowVisualization());
        this.txt_updateFrequency.setText(new Integer(this.model.getUpdateFrequency()).toString());
        this.txt_inputSize.setText(new Integer(this.model.getInputSize()).toString());
        this.txt_outputSize.setText(new Integer(this.model.getOutputSize()).toString());
        this.rdi_epochs.setSelected(this.model.getStoppingCondition() == 0);
        this.rdi_error.setSelected(this.model.getStoppingCondition() == 1);
        this.txt_epochs.setText(new Integer(this.model.getEpochs()).toString());
        this.txt_error.setText(new Double(this.model.getError()).toString());
        this.txt_learningRate.setText(new Double(this.model.getLearningRate()).toString());
        this.cbx_momentum.setSelected(this.model.isUseMomentum());
        this.cbx_seed.setSelected(this.model.isUseSeed());
        this.txt_seed.setText(new Integer(this.model.getSeed()).toString());
        this.cbx_bias.setSelected(this.model.isUseBias());
        this.cbx_binarizeProbabilities.setSelected((this.model.isBinarizeProbabilities()));

        this.btn_startTraining.setDisable(this.model.getImageManager() == null);
        this.btn_startEvolution.setDisable(this.model.getImageManager() == null);
        this.btn_runVisible.setDisable(!this.model.isRbmTrained());
        this.btn_runHidden.setDisable(!this.model.isRbmTrained());
        this.btn_daydream.setDisable(!this.model.isRbmTrained());
        this.btn_saveRbmFile.setDisable(!this.model.isRbmTrained());
        this.btn_startTest.setDisable(!this.model.isRbmTrained());
        this.cmb_mapTests.setDisable(!this.model.isRbmTrained());

        this.txt_epochs.setDisable(this.model.getStoppingCondition() != 0);
        this.txt_error.setDisable(this.model.getStoppingCondition() != 1);
        this.txt_seed.setDisable(!this.model.isUseSeed());
    }

    @FXML
    private void btn_loadImageSetAction(ActionEvent event) {
        loadImageSet(null);
    }
    
    @FXML
    private void loadImageSet(String path) {
        File file;
        if(path == null){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("CBIR_Project/images"));
            Stage fileChooserStage = new Stage();
            file = directoryChooser.showDialog(fileChooserStage);
        }else{
            file = new File(path);
        }
        if (file != null) {
            this.model.setImageManager(new ImageManager(file));
            if (cbx_imageViewer.isSelected()) {
                initializeImageView();
            }
        }
        this.updateView();
    }

    private Object loadController(String url) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        loader.load();
        return loader.getController();
    }

    private void initializeImageView() {
        try {

            this.imageViewer = new ImageViewer();
            this.imageViewer.draw(this.model.getImageManager().getImages());


        } catch (IOException ex) {
            Logger.getLogger(SimpleRBMController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initializeChartView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ChartViewer.fxml"));

            Scene scene = new Scene(root, 600, 400);
            this.chartViewerStage = new Stage();
            this.chartViewerStage.setTitle("Map Viewer");
            this.chartViewerStage.setScene(scene);

            this.chartViewerController = (ChartViewerController) loadController("ChartViewer.fxml");

           // this.chartViewerController.draw(lineChart);

            this.chartViewerStage.show();

        } catch (IOException ex) {
            Logger.getLogger(SimpleRBMController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void cbx_imageViewerAction(ActionEvent event) {
        if (cbx_imageViewer.isSelected() && this.model.getImageManager() != null) {
            initializeImageView();
        } else {
            if (this.imageViewerStage != null) {
                this.imageViewerStage.close();
            }
        }
    }

    @FXML
    private void btn_startTrainingAction(ActionEvent event) {
        this.model.trainRBM();
    }

    @FXML
    private void btn_startEvolutionAction(ActionEvent event) {
    }

    @FXML
    private void cbx_loggerAction(ActionEvent event) {
    }

    @FXML
    private void cbx_visualizationAction(ActionEvent event) {
    }

    @FXML
    private void txt_updateFrequencyAction(ActionEvent event) {
    }

    @FXML
    private void btn_runVisibleAction(ActionEvent event) {
    }

    @FXML
    private void btn_runHiddenAction(ActionEvent event) {
    }

    @FXML
    private void btn_daydreamAction(ActionEvent event) {
    }

    @FXML
    private void btn_saveRbmFileAction(ActionEvent event) {
    }

    @FXML
    private void cmb_mapTestsAction(ActionEvent event) {
    }

    @FXML
    private void btn_startTestAction(ActionEvent event) {
    }

    @FXML
    private void cmb_rbmImplementationAction(ActionEvent event) {
    }

    @FXML
    private void cmb_rbmFeatureAction(ActionEvent event) {
    }

    @FXML
    private void cmb_logisticFunctionAction(ActionEvent event) {
    }

    @FXML
    private void txt_inputSizeAction(ActionEvent event) {
    }

    @FXML
    private void txt_outputSizeAction(ActionEvent event) {
    }

    @FXML
    private void rdi_epochsAction(ActionEvent event) {
    }

    @FXML
    private void txt_epochsAction(ActionEvent event) {
    }

    @FXML
    private void rdi_errorAction(ActionEvent event) {
    }

    @FXML
    private void txt_errorAction(ActionEvent event) {
    }

    @FXML
    private void txt_learningRateAction(ActionEvent event) {
    }

    @FXML
    private void cbx_momentumAction(ActionEvent event) {
    }

    @FXML
    private void cbx_seedAction(ActionEvent event) {
    }

    @FXML
    private void txt_seedAction(ActionEvent event) {
    }

    @FXML
    private void cbx_binarizeProbabilitiesAction(ActionEvent event) {
    }

    @FXML
    private void cbx_biasAction(ActionEvent event) {
    }

    @FXML
    private void cbx_randomOrderAction(ActionEvent event) {
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @FXML
    private void cbx_mapAction(ActionEvent event) {
        if (cbx_map.isSelected() && this.model.getImageManager() != null) {
            initializeChartView();
        } else {
            if (this.chartViewerStage != null) {
                this.chartViewerStage.close();
            }
        }
    }

}
