/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import java.net.URL;
import java.util.ResourceBundle;

import de.htw.cbir.RBMWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class RunHiddenController implements Initializable {
    @FXML
    private Button btn_loadNewTestImage;
    @FXML
    private Button btn_runHidden;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_Input;
    
    private RunHiddenModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new RunHiddenModel();
    }
    
    public void setRBMWrapper(RBMWrapper rbmWrapper) {
    	this.model.setRbmWrapper(rbmWrapper);
    }

    @FXML
    private void btn_loadNewTestImageAction(ActionEvent event) {
        Image image = this.model.openFile4();
        if(!image.isError()) {
            this.imgv_Input.setImage(image);
            btn_runHidden.setDisable(false);
        } else {
            System.out.println("error");
        }
    }

    @FXML
    private void btn_runHiddenAction(ActionEvent event) {
        this.model.runHidden();
    }
    
    
}
