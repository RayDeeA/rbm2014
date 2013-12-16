package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gregor Altastädt
 */
public class PRTMAPController extends AController implements EventHandler {

    @FXML
    private AnchorPane view;

    @FXML

    private PRTMAPModel model;
    private final Stage prtmapStage = new Stage();

    @FXML
    private Button btn_clear;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new PRTMAPModel(this);
        
        Parent root = (Parent) this.getView();
        Scene scene = new Scene(root, 600, 400);
        prtmapStage.setTitle("Map Viewer");
        prtmapStage.setScene(scene);
        prtmapStage.setOnCloseRequest(this);

        this.update();
    }

    @Override
    public void update() {
    }

    @Override
    public Node getView() {
        return this.view;
    }

    public PRTMAPModel getModel() {
        return this.model;
    }

    @Override
    public void handle(Event t) {
    }

    public void hide() {
        prtmapStage.hide();
    }

    public void show() {
        prtmapStage.show();
    }

    @FXML
    private void btn_clearAction(ActionEvent event) {
        this.model.clearTable();
    }
}
