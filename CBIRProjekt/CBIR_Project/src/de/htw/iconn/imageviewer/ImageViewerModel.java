package de.htw.iconn.imageviewer;

import javafx.scene.canvas.GraphicsContext;

import com.badlogic.gdx.math.Vector2;

import de.htw.iconn.image.Pic;
import de.htw.iconn.imageviewer.drawables.Image;

public class ImageViewerModel {

  private final ImageViewerController controller;

  private Projector                   projector;

  ImageViewerModel(ImageViewerController controller) {
    this.controller = controller;

    Paper paper = new Paper();
    paper.addDrawable(new Image(new Pic()));

    Camera camera1 = new Camera(paper);

    GraphicsContext gc = controller.canvas.getGraphicsContext2D();
    this.projector = new Projector(gc, camera1, new Vector2(600, 400));
    getProjector().draw();
  }

  public Projector getProjector() {
    return projector;
  }
}
