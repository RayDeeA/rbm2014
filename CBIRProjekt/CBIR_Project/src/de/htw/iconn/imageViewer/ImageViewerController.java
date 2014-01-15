package de.htw.iconn.imageViewer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import de.htw.iconn.main.AController;

public class ImageViewerController extends AController implements EventHandler {

  private final Stage      viewStage = new Stage();

  private ImageViewerModel model;

  @FXML
  private AnchorPane       view;
  Canvas                   canvas;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {

    Group root = new Group();
    canvas = new Canvas(600, 400);

    root.getChildren().add(canvas);
    Scene scene = new Scene(root, 600, 400);

    viewStage.setScene(scene);
    viewStage.show();
    viewStage.setOnCloseRequest(this);

    model = new ImageViewerModel(this);

    // add event listener:
    ChangeListener<Number> onResize = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newHeight) {
        ImageViewerController.this.model.getProjector().draw();
      }
    };

    scene.widthProperty().addListener(onResize);
    scene.heightProperty().addListener(onResize);

    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
        ImageViewerController.this.model.getProjector().onMouseDown(mouse);
        ImageViewerController.this.model.getProjector().draw();
      }
    });

    scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
        ImageViewerController.this.model.getProjector().onMouseUp(mouse);
        ImageViewerController.this.model.getProjector().draw();
      }
    });

    scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
      }
    });

    scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
        ImageViewerController.this.model.getProjector().onMouseMove(mouse);
        ImageViewerController.this.model.getProjector().draw();
      }
    });
    scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
      }
    });

    scene.setOnScroll(new EventHandler<ScrollEvent>() {
      @Override
      public void handle(ScrollEvent scroll) {
        ImageViewerController.this.model.getProjector().onMouseWheel(scroll);
        ImageViewerController.this.model.getProjector().draw();
      }
    });

    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent k) {
        ImageViewerController.this.model.getProjector().onKeyPressed(k);
        ImageViewerController.this.model.getProjector().draw();
      }
    });

  }

  @Override
  public Node getView() {
    return view;
  }

  @Override
  public void handle(Event arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
  }

}
