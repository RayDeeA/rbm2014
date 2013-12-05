/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author dvarul
 */
public class TrainingViewController implements Initializable, IFXController {

    private ArrayList<Double> errors = new ArrayList<Double>();
    
    final int width = 500, height = 400, margin_top = 60, margin_right = 300, margin_bottom = 20, margin_left = 20;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public Node getView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
