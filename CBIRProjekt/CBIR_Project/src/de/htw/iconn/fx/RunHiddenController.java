/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import java.net.URL;
import java.util.ResourceBundle;

import de.htw.cbir.ARBMFeature;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Radek
 */
public class RunHiddenController implements Initializable, IFXController {
    @FXML
    private Button btn_loadNewTestImage;
    @FXML
    private Button btn_runHidden;
    @FXML
    private ImageView imgv_Result;
    @FXML
    private ImageView imgv_Input;
    @FXML
    private AnchorPane view;
    
    private RunHiddenModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new RunHiddenModel();
    }
    
    public void setRBMFeature(ARBMFeature rbmFeature) {
    	this.model.setRbmFeature(rbmFeature);
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
        this.imgv_Result.setImage(this.model.runHidden());
    }

	@Override
	public Node getView() {
		return this.view;
	}
}
