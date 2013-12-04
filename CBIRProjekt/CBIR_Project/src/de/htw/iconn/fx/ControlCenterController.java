/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class ControlCenterController implements Initializable, IFXController {
    @FXML
    private MenuItem mnu_newRbm;

    private ControlCenterModel model;
    @FXML
    private AnchorPane view;
    @FXML
    private VBox vbox;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new ControlCenterModel();
        
        // will load the default rbm #hack
        mnu_newRbmAction(new ActionEvent());
    }
    
    private Object loadController(String url) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        loader.load();
        return loader.getController();
    }

    @FXML
    private void mnu_newRbmAction(ActionEvent event) {
        try {
            SimpleRBMController controller = (SimpleRBMController) loadController("SimpleRBM.fxml");
            
            this.model.addChildController(controller);
            Node c = controller.getView();
            ObservableList<Node> children = vbox.getChildren();
            //vbox.getChildren().addAll(c);
            children.addAll(c);
        } catch (IOException ex) {
            System.out.println("Could not load controller");
            Logger.getLogger(ControlCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Node getView() {
        return this.view;
    }
}
