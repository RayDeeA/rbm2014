
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.views;

import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkModel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class ImageBuilderController extends AController {
	
    @FXML
    private ToggleButton btn_hiddenStates;
    @FXML
    private ToggleButton btn_visibleStates;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private AnchorPane view;

	ImageBuilderModel model;
	
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new ImageBuilderModel(this);
        this.update();
    }
    
    @FXML
    private void btn_hiddenStatesAction(ActionEvent event) {
        this.model.setUseHiddenStates(this.btn_hiddenStates.isSelected());
    }

    @FXML
    private void btn_visibleStatesAction(ActionEvent event) {
        this.model.setUseVisibleStates(this.btn_visibleStates.isSelected());
    }
    
    public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
    	this.model.setBenchmarkModel(benchmarkModel);
    }

    @Override
    public Node getView() {
        return this.view;
    }

    @Override
    public void update() {

    }

}
