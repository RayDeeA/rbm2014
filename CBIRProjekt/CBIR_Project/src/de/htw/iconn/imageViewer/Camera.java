package de.htw.iconn.imageViewer;

import com.badlogic.gdx.math.Vector2;

public class Camera {
  private final Vector2  pos        = new Vector2(0, 0);

  private double zoomFactor = 1.0;
  private Paper  paper;

  Camera(Paper p) {
    setPaper(p);
  }

  void setPaper(Paper p) {
    paper = p;
  }

  void move(Vector2 offset) {
    setPos(getPos().sub(offset));
  }

  void setPos(Vector2 offset) {
    pos.set(offset);
  }

  void setRelPos(Vector2 offset) {
    getPos().set(offset.mul((float) (1.0 / getZoomFactor())));
  }

  Vector2 getRelPos() {
    return getPos().mul((float) getZoomFactor());
  }

  public double getZoomFactor() {
    return zoomFactor;
  }

  public void setZoomFactor(double zoomFactor) {
    this.zoomFactor = zoomFactor;
  }

  public Paper getPaper() {
    return paper;
  }

  public Vector2 getPos() {
    return pos;
  }
}
