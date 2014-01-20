package de.htw.iconn.imageviewer.drawables;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

import com.badlogic.gdx.math.Vector2;

import de.htw.iconn.imageviewer.Paper;

public class FlowGroup extends ADrawable {
  Paper                paper;
  ArrayList<ADrawable> elements;
  Vector2              pos, size;

  public FlowGroup(ArrayList<ADrawable> e, Paper p) {
    paper = p;
    elements = e;
  }

  @Override
  public void draw(GraphicsContext gc, Vector2 offset, double zoom) {
    for (ADrawable d : elements) {
      d.draw(gc, offset, zoom);
    }
  }

  public Vector2 getPos() {
    return new Vector2(0, 0);
  }

  public Vector2 getSize() {
    return paper.getSize().cpy();
  }
}
