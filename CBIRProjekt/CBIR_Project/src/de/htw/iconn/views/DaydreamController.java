
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.views;

import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkModel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class DaydreamController extends AController {
	
    @FXML
    private Button btn_generateImage;
    @FXML
    private Button btn_daydream;
    @FXML
    private Button btn_stopDaydream;
    @FXML
    private Button btn_Next;
    @FXML
    private ToggleButton btn_hiddenStates;
    @FXML
    private ToggleButton btn_visibleStates;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_ResultHidden;
    @FXML
    private ImageView imgv_Input;
    @FXML
    private AnchorPane view;

    DaydreamModel model;

    Timer timer;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new DaydreamModel(this);
        this.update();
    }
    
    @FXML
    private void btn_loadImageAction(ActionEvent event) {
        Image image = this.model.loadImage((int) imgv_Result.getFitWidth(), (int) imgv_Result.getFitHeight());
        if (!image.isError()) {
            this.imgv_Input.setImage(image);
        } else {
            System.out.println("error");
        }
        this.btn_daydream.setDisable(false);
        this.btn_Next.setDisable(false);
    }

    @FXML
    private void btn_generateImageAction(ActionEvent event) {
        this.imgv_Input.setImage(this.model.generateImage((int) imgv_Result.getFitWidth(), (int) imgv_Result.getFitHeight()));
        this.btn_daydream.setDisable(false);
        this.btn_Next.setDisable(false);
    }

    @FXML
    private void btn_daydreamAction(ActionEvent event) {
        int delay = 0; // delay for 3 sec. 
        int period = 50;
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Dream");
                model.daydream();
                imgv_Result.setImage(model.getVisibleImage((int)imgv_Result.getFitWidth(), (int)imgv_Result.getFitHeight()));
                Image hiddenImage = model.getHiddenImage(10);
                imgv_ResultHidden.setFitWidth(hiddenImage.getWidth());
                imgv_ResultHidden.setFitHeight(hiddenImage.getHeight());
                imgv_ResultHidden.setImage(hiddenImage);
            }
        }, delay, period);
        this.btn_daydream.setDisable(true);
        this.btn_stopDaydream.setDisable(false);
    }
    
    @FXML
    private void btn_NextAction(ActionEvent event) {
    	model.daydream();
    	imgv_Result.setImage(model.getVisibleImage((int)imgv_Result.getFitWidth(), (int)imgv_Result.getFitHeight()));
        Image hiddenImage = model.getHiddenImage(10);
        imgv_ResultHidden.setFitWidth(hiddenImage.getWidth());
        imgv_ResultHidden.setFitHeight(hiddenImage.getHeight());
        imgv_ResultHidden.setImage(hiddenImage);
    }

    @FXML
    private void btn_stopDaydreamAction(ActionEvent event) {
        stopDreaming();
    }

    @FXML
    private void btn_hiddenStatesAction(ActionEvent event) {
        this.model.setUseHiddenStates(this.btn_hiddenStates.isSelected());
    }

    @FXML
    private void btn_visibleStatesAction(ActionEvent event) {
        this.model.setUseVisibleStates(this.btn_visibleStates.isSelected());
    }
    
    public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
    	this.model.setBenchmarkModel(benchmarkModel);
    }

    public void stopDreaming() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            this.btn_daydream.setDisable(false);
            this.btn_stopDaydream.setDisable(true);
        }
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @Override
    public void update() {

    }

}
