/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Cristea
 */
public class Visualization_ViewerController implements Initializable {
    @FXML
    private AnchorPane viz_viewer;
    @FXML
    private GridPane grid_panel;
    
    private int dimX = 0;
    private int dimY = 0;
    private double[][] weights;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void setWeightData(double[][] w){
        this.weights = w ;
    }
    
    public void setDimensions(int x, int y){
        dimX = x;
        dimY = y;
     }
     
    public void draw(){
    
       Text rank4 = new Text("Test");;
       grid_panel.add(rank4, 0 , 0);
       grid_panel.setGridLinesVisible(false);
       
       /*for (int i = 0; i < x; i++) {
         ColumnConstraints column = new ColumnConstraints(100);
        grid_panel.getColumnConstraints().add(column);
         
         for (int j = 0; j < y; j++) {
             RowConstraints rows = new RowConstraints(10);
         grid_panel.getRowConstraints().add(rows);
     }*/
    }
 
       
    
    

    
   
    
    
    
   
}
