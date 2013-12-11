/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class RBMSettingsWeightsController extends AController {
    @FXML
    private AnchorPane view;
    private RBMSettingsWeightsModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsWeightsModel();
    }    

    @FXML
    private void btn_setWeightsRandomAction(ActionEvent event) {
        this.model.setInitializedWeights(true);
    }

    @FXML
    private void btn_loadWeightsAction(ActionEvent event) {
        this.model.setInitializedWeights(false);
    }

    @FXML
    private void btn_saveCurrentWeightsAction(ActionEvent event) {
        this.model.setInitializedWeights(false);
    }

    @Override
    public Node getView() {
        return view;
    }
    public RBMSettingsWeightsModel getModel() {
        return this.model;
    }
}
