/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author GDur
 */
public class ChartViewerController implements Initializable, IFXController {

    private final int precisionIndex = 2, recallIndex = 3;
    @FXML
    private AnchorPane view;

    @FXML
    private LineChart<Number, Number> cha_PRTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void addGraph(float[][] pUeberR, String title, double map) {
        XYChart.Series tmpGraph = new XYChart.Series();

        
        // set title of graph
        tmpGraph.setName(String.format(Locale.ENGLISH, "%s mAP = %6.3f", title,  map));        

        // transfer the float array data to the tmpGraph
        for (int i = 0; i < pUeberR[precisionIndex].length; i++) {

            double x = pUeberR[recallIndex][i];
            double y = pUeberR[precisionIndex][i];

            tmpGraph.getData().add(new XYChart.Data(x, y));
        }

        // finally add and draw the tmpGraph to the PRChart
        cha_PRTable.getData().add(tmpGraph);
    }

    @Override
    public Node getView() {
        return view;
    }
    
    // methods to use this chartview for error plotting while training
    
    public void useAsErrorChart() {
    	this.cha_PRTable.setTitle("Error");
    }
    
    
}
