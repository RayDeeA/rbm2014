/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jblas.util.Random;

import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * @author turbodiesel
 */
public class TrainingViewController implements Initializable, IFXController {

	private ArrayList<Double> errors = new ArrayList<Double>();

	@FXML
	private ScatterChart <Number, Number> scatter_chart;
	@FXML
	private NumberAxis xaxis;
	@FXML
	private NumberAxis yaxis;
    private TrainingViewController model;      
    @FXML
    private AnchorPane view;
    @FXML
    private ScatterChart<Number, Number> chart_scatter; 
    @FXML
    private CategoryAxis chartaxis_epochs;
    @FXML
    private NumberAxis chartaxis_error;
    
    private SequentialTransition animation;
    
    final int width = 500, height = 400, margin_top = 60, margin_right = 300, margin_bottom = 20, margin_left = 20;

	private ScatterChart.Series<Number,Number> series;


	public void addErrorToGraph(Double error) {
		errors.add(error);
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

	public void update(Double input) {
		errors.add(input);
		buildGraph();

	}

	private void addDummyData() {
		Random r = new Random();
		for (int i = 0; i < 73; i++) {
			errors.add(r.nextDouble() * 123);
		}
	}

	public void buildGraph() {
		addDummyData();
		xaxis = new NumberAxis();
		xaxis.setForceZeroInRange(false);
		yaxis = new NumberAxis(0, 200, 13);
		scatter_chart = new ScatterChart<Number, Number>(xaxis, yaxis);
		scatter_chart.setId("Error Chart");
		scatter_chart.setTitle("Error Chart");
		xaxis.setAutoRanging(true);
		yaxis.setAutoRanging(true);

		//		series = new ScatterChart.Series<Number,Number>();     
		series = new XYChart.Series<Number,Number>();       

		for (int i = 0; i < errors.size(); i++) {
//			series.getData().add(new ScatterChart.Data<Number, Number>(i, errors.get(i)));
			series.getData().add(new XYChart.Data<Number, Number>(errors.get(i), i));


		}

		scatter_chart.getData().add(series);
//		System.out.println("build graph - size of list" + errors.get(13));

		//		return scatter_chart;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		buildGraph();
	}
        public TrainingViewController getTrainingViewController(){
        return this.model;
        } 
        
}

