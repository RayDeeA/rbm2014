package de.htw.iconn.imageViewer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import com.badlogic.gdx.math.Vector2;

import de.htw.iconn.imageViewer.drawables.ADrawable;

public class Projector {

  Camera          camera;

  Vector2         mPos = new Vector2(0, 0);
  Vector2         size = new Vector2(0, 0);
  Vector2         pos  = new Vector2(0, 0);

  GraphicsContext gc;

  private boolean isDown;

  private Vector2 lastMousePosition = new Vector2(0, 0);

  Projector(GraphicsContext g, Camera c, Vector2 s) {
    camera = c;
    size = s;
    gc = g;

    draw();
  }

  void zoomFitCamera(double factor) {
    float w, h;

    w = getSize().x / camera.getPaper().getSize().x;
    h = getSize().y / camera.getPaper().getSize().y;

    camera.setZoomFactor(factor * Math.min(w, h));
  }

  void onKeyPressed(KeyEvent e) {
    System.out.println(e.getCharacter());
  }

  Vector2 getMousePos(MouseEvent e) {
    float a = (float) e.getX();
    float b = (float) e.getY();
    return new Vector2(a, b);
  }

  Vector2 getMousePos(ScrollEvent e) {
    float a = (float) e.getX();
    float b = (float) e.getY();
    return new Vector2(a, b);
  }

  void onResize(int w, int h) {
    setSize(new Vector2(w, h));
  }

  void onMouseWheel(ScrollEvent e) {
    Vector2 mpos = getMousePos(e);
    Vector2 a = (mpos.add(camera.getRelPos())).mul((float) (1 / camera.getZoomFactor()));

    if (e.getDeltaY() < 0)
      camera.setZoomFactor(camera.getZoomFactor() * 1.1);
    else
      camera.setZoomFactor(camera.getZoomFactor() / 1.1);

    Vector2 newPos = (a.mul((float) camera.getZoomFactor())).sub(getMousePos(e));
    camera.setRelPos(newPos);
  }

  void onMouseDown(MouseEvent e) {
    isDown = true;
    lastMousePosition.set(getMousePos(e));
  }

  void onMouseMove(MouseEvent e) {
    if (isDown) {
      Vector2 offset = lastMousePosition.sub(getMousePos(e));
      camera.setPos(camera.getPos().add(offset.mul((float) (1 / camera.getZoomFactor()))));
      lastMousePosition.set(getMousePos(e));
    }
  }

  void onMouseUp(MouseEvent e) {
    isDown = false;
  }

  void centerCamera() {
    Vector2 tmp = getSize().sub(camera.getPaper().getSize().mul((float) camera.getZoomFactor()));
    camera.getPos().set(tmp.mul(-1.0f).mul((float) (1 / (2 * camera.getZoomFactor()))));
  }

  void draw() {

    for (ADrawable d : camera.getPaper().getDrawables()) {
      d.draw(gc, camera.getPos(), camera.getZoomFactor());
    }
  }

  void setSize(Vector2 s) {
    size.set(s);
  }

  Vector2 getSize() {
    return size;
  }

  void setPos(Vector2 p) {
    pos.set(p);
  }

  Vector2 getPos() {
    return pos;
  }
}
