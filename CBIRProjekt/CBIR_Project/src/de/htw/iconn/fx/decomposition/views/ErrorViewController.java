/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.jblas.util.Random;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author turbodiesel
 */
public class ErrorViewController extends AController {

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


        
        final ArrayList<Double> errors = this.getModel().getErrors();
        
        series.getData().clear();
        for (int i = 0; i < errors.size(); i++) {
            series.getData().add(new XYChart.Data<Number, Number>(i, errors.get(i)));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        chart_line.setId("Error Chart");
        chart_line.setTitle("Error Chart");
        xaxis.setAutoRanging(true);
        yaxis.setAutoRanging(true);
        xaxis.setForceZeroInRange(false);

        series = new XYChart.Series<Number, Number>();
        series.setName("dummy series");
        
        chart_line.getData().add(series);
        
        model = new ErrorViewModel(this);
        this.update();
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

}
