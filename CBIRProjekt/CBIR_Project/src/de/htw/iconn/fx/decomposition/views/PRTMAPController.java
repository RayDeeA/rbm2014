package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.AController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gregor Altastädt
 */
public class PRTMAPController extends AController implements EventHandler {

    @FXML
    private AnchorPane view;

    @FXML
    private ImageView imgView;

    private PRTMAPModel model;
    private final Stage prtmapStage = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Parent root = (Parent) this.getView();
        Scene scene = new Scene(root, 600, 400);
        prtmapStage.setTitle("Map Viewer");
        prtmapStage.setScene(scene);
        prtmapStage.setOnCloseRequest(this);

        this.model = new PRTMAPModel(this);
        this.update();
    }

    @Override
    public void update() {
    }

    @Override
    public Node getView() {
        return this.view;
    }

    public PRTMAPModel getModel() {
        return this.model;
    }

    @Override
    public void handle(Event t) {
    }

    public void hide() {
        prtmapStage.hide();
    }

    public void show() {
        prtmapStage.show();
    }
}
