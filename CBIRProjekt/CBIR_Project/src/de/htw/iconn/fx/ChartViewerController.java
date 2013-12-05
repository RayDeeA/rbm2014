/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
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

    @FXML
    private AnchorPane view;
    
    private final int width = 500, height = 400, margin_top = 60, margin_right = 300, margin_bottom = 20, margin_left = 20;
    private final int p = 2, r = 3;
    private final Vector<float[][]> graphs = new Vector<>();
    private Vector<String> titles = new Vector<String>();
    @FXML
        private LineChart<Number, Number> cha_MAP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void addGraph(float[][] pUeberR, String title) {
        graphs.add(pUeberR);
        titles.add(title);
        // redraw();
    }

    public void draw() {
        // show data
        
        for (int i = 0; i < graphs.size(); i++) {

            XYChart.Series series = new XYChart.Series();
            series.setName(titles.elementAt(i));

            float[][] tmpGraph = graphs.elementAt(i);

            ObservableList<XYChart.Data<Float, Float>> tmp = series.getData();
            for (float[] fs : tmpGraph) {
                tmp.add(new XYChart.Data(fs[0], fs[1]));
            }
            series.setData(tmp);
            cha_MAP.getData().add(series);
        }
    }

    @Override
    public Node getView() {
        return view;
    }

}
