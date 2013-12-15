/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AController;
import de.htw.iconn.fx.decomposition.Chooser;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Moritz
 */
public class RBMSettingsWeightsController extends AController {
    @FXML
    private AnchorPane view;   
    @FXML
    private CheckBox cbx_useBias;
    @FXML
    private CheckBox cbx_useSeed;
    @FXML
    private CheckBox cbx_useBinarizeHidden;
    @FXML
    private CheckBox cbx_useBinarizeVisible;
    @FXML
    private TextField txt_seed;
    
    private RBMSettingsWeightsModel model;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RBMSettingsWeightsModel(this);
        this.update();
    }    

    @FXML
    private void btn_setWeightsRandomAction(ActionEvent event) {
        this.model.setInitializedWeights(true);
    }

    @FXML
    private void btn_loadWeightsAction(ActionEvent event) {
        this.model.loadWeights(Chooser.openFileChooser("RBMLogs"));
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

    @FXML
    private void cbx_useBiasAction(ActionEvent event) {
        this.model.setUseBias(cbx_useBias.isSelected());
    }
    
    @FXML
    private void cbx_useBinarizeHiddenAction(ActionEvent event) {
        this.model.setBinarizeHidden(cbx_useBinarizeHidden.isSelected());
    }
    
    @FXML
    private void cbx_useBinarizeVisibleAction(ActionEvent event) {
        this.model.setBinarizeVisible(cbx_useBinarizeVisible.isSelected());
    }

    @FXML
    private void cbx_useSeedAction(ActionEvent event) {
        this.model.setUseSeed(cbx_useSeed.isSelected());
    }

    @FXML
    private void txt_seedKey(KeyEvent event) {
        this.model.setSeed(Integer.parseInt(txt_seed.getText()));
    }

    @Override
    public void update() {
        this.cbx_useBias.setSelected(this.model.isUseBias());
        this.cbx_useBinarizeHidden.setSelected((this.model.isBinarizeHidden()));
        this.cbx_useBinarizeVisible.setSelected((this.model.isBinarizeVisible()));
        this.cbx_useSeed.setSelected(this.model.isUseSeed());
        this.txt_seed.setText(new Integer(this.model.getSeed()).toString());
    }
}
