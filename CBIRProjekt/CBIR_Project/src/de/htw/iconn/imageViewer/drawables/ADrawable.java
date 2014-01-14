package de.htw.iconn.imageViewer.drawables;

import math.geom2d.Vector2D;

public abstract class ADrawable {

  private final Vector2D pos = new Vector2D();
  private final Vector2D size = new Vector2D();
  public abstract void draw(Vector2D offset, double zoom);
//  draw(CanvasRenderingContext2D ctx, Vector2D offset, double zoom);
}
