/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.views;

import de.htw.iconn.main.AController;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;

import org.jblas.util.Random;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author turbodiesel
 */
public class ErrorViewController extends AController implements EventHandler {

    @FXML
    private LineChart<Number, Number> chart_line;
    @FXML
    private final NumberAxis xaxis = new NumberAxis();
    @FXML
    private final NumberAxis yaxis = new NumberAxis();
    @FXML
    private AnchorPane view;
    @FXML
    private LineChart.Series<Number, Number> series;

    private ErrorViewModel model;

    private final Stage errorViewStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Parent root = (Parent) this.getView();
        Scene scene = new Scene(root, 600, 400);
        errorViewStage.setScene(scene);
        errorViewStage.setOnCloseRequest(this);

        chart_line.setId("Error Chart");
        chart_line.setTitle("Error Chart");
        xaxis.setAutoRanging(true);
        yaxis.setAutoRanging(true);
        xaxis.setForceZeroInRange(false);

        series = new XYChart.Series<Number, Number>();
        series.setName("error value");

        chart_line.getData().add(series);

        model = new ErrorViewModel(this);
        this.update();
    }

    @Override
    public Node getView() {
        return view;
    }

    public void setDimensions(int inputSize, int outputSize) {
        // TODO Auto-generated method stub

    }

    public void setDisplayDimensions() {
        // TODO Auto-generated method stub

    }

    private void addDummyDataToErrorList() {
        Random r = new Random();
        for (int i = 0; i < 13; i++) {
            this.getModel().getErrors().add(r.nextDouble() * 123);
        }
    }

    private void buildGraph() {

        final LinkedList<Double> errors = this.getModel().getErrors();
        final LinkedList<Integer> epochs = this.getModel().getEpochs();

        if (errors.size() != 0) {
            
          chart_line.getData().get(0).getData().add(new XYChart.Data<Number, Number>(
                    epochs.getLast(), 
                    errors.getLast()));
        }
    }

    @Override
    public void update() {
        buildGraph();
    }

    /**
     * @return the model
     */
    public ErrorViewModel getModel() {
        return model;
    }

    public void hide() {
        errorViewStage.hide();
    }

    public void show() {
        errorViewStage.show();
    }

    @Override
    public void handle(Event t) {
    }

}
