/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition.views;

import java.util.Locale;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Gregor Altstädt
 */
public class PRTMAPModel {

    private final PRTMAPController controller;

    private LineChart<Number, Number> cha_PRTable;

    PRTMAPModel(PRTMAPController controller) {
        this.controller = controller;
    }

    public void addGraph(float[][] pUeberR, String title, double map) {
        XYChart.Series tmpGraph = new XYChart.Series();

        // set title of graph
        tmpGraph.setName(String.format(Locale.ENGLISH, "%s mAP = %6.3f", title, map));

        int precisionIndex = 2, recallIndex = 3;
        // transfer the float array data to the tmpGraph
        for (int i = 0; i < pUeberR[precisionIndex].length; i++) {

            double x = pUeberR[recallIndex][i];
            double y = pUeberR[precisionIndex][i];

            tmpGraph.getData().add(new XYChart.Data(x, y));
        }

        // finally add and draw the tmpGraph to the PRChart
        cha_PRTable.getData().add(tmpGraph);
    }

    void clearTable() {
        if (cha_PRTable != null) {
            cha_PRTable.getData().clear();
        }
    }
}
