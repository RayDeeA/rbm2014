/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class TrainingViewController implements Initializable, IFXController {

    private ArrayList<Double> errors = new ArrayList<Double>();

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

    public void addGraph() {
    	
    }
    
    public void addErrorToGraph(Double error) {
    	errors.add(error);
    }
    
    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
//        root.getChildren().add(createChart());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

	public void update() {
		// TODO Auto-generated method stub
		
	}
}
