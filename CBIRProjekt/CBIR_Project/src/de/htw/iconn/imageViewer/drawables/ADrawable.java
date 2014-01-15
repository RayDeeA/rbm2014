package de.htw.iconn.imageViewer.drawables;

import javafx.scene.canvas.GraphicsContext;

import com.badlogic.gdx.math.Vector2;

public abstract class ADrawable {

  private final Vector2 pos = new Vector2();
  private final Vector2 size = new Vector2();
  public abstract void draw(GraphicsContext gc, Vector2 offset, double zoom);
}
