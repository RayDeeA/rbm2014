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

  private final Stage viewStage = new Stage();

  private ImageViewerModel model;
  
  @FXML
  private AnchorPane  view;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
//    Parent root = (Parent) this.getView();
//    Scene scene = new Scene(root, 600, 400);

    System.out.println("suuuup");
    Group root = new Group();
    Canvas canvas = new Canvas(600, 400);
    GraphicsContext gc = canvas.getGraphicsContext2D();    
    gc.setFill(Color.GREEN);
    gc.setStroke(Color.BLUE);
    gc.setLineWidth(5);
    gc.strokeLine(40, 10, 10, 40);
    gc.fillOval(10, 60, 30, 30);
    gc.strokeOval(60, 60, 30, 30);
    gc.fillRoundRect(110, 60, 30, 30, 10, 10);
    
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

      }
    };

    scene.widthProperty().addListener(onResize);
    scene.heightProperty().addListener(onResize);

    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
      }
    });
    
    scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouse) {
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
        double delta = scroll.getDeltaY();
      }
    });
    
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent t) {
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
