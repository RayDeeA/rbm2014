/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.jblas.util.Random;

import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;


/**
 * FXML Controller class
 * @author turbodiesel
 */
public class TrainingViewController implements Initializable, IFXController {

	private ArrayList<Double> errors = new ArrayList<Double>();

	@FXML
	private LineChart <Number, Number> chart_line;
	@FXML
	private NumberAxis xaxis = new NumberAxis();
	@FXML
	private NumberAxis yaxis = new NumberAxis();
	private TrainingViewController model;      
	@FXML
	private AnchorPane view;
	@FXML
	private LineChart.Series<Number,Number> series;

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

		System.out.println("error update called");
	}

	private void addDummyDataToErrorList() {
		Random r = new Random();
		for (int i = 0; i < 13; i++) {
			errors.add(r.nextDouble() * 123);
		}
	}

	private void buildGraph() {

//		addDummyDataToErrorList();


		chart_line.setId("Error Chart");
		chart_line.setTitle("Error Chart");
		xaxis.setAutoRanging(true);
		yaxis.setAutoRanging(true);
		xaxis.setForceZeroInRange(false);

		series = new XYChart.Series<Number, Number>();       
		series.setName("dummy series");

		for (int i = 0; i < errors.size(); i++) {
			series.getData().add(new XYChart.Data<Number, Number>(i, errors.get(i)));


		}

		chart_line.getData().add(series);
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

