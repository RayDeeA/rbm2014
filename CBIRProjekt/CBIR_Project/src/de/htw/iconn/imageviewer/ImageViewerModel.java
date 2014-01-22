package de.htw.iconn.imageviewer;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import com.badlogic.gdx.math.Vector2;

import de.htw.iconn.image.ImageManager;
import de.htw.iconn.image.Pic;
import de.htw.iconn.imageviewer.drawables.ADrawable;
import de.htw.iconn.imageviewer.drawables.FlowGroup;
import de.htw.iconn.imageviewer.drawables.Image;

public class ImageViewerModel {

  private final ImageViewerController controller;

  Camera                              camera;
  Paper                               paper;

  Vector2                             pos               = new Vector2(0, 0);

  Canvas                              canvas;
  GraphicsContext                     gc;

  private Vector2                     lastMousePosition = new Vector2(0, 0);


  ImageViewerModel(ImageViewerController controller) {
    this.controller = controller;
    canvas = controller.canvas;

    setSize(new Vector2(600, 400));

    gc = canvas.getGraphicsContext2D();

    paper = new Paper();

    camera = new Camera();
    
    zoomFitCamera(.9f);
    centerCamera();

    draw();
  }
  
	public void setImages(Pic[] images) {
		java.util.Arrays.sort(images);

		paper = new Paper();
		ArrayList<ADrawable> elements = new ArrayList<>();
		for (Pic p : images) {
			elements.add(new Image(p));
		}
		paper.addDrawable(new FlowGroup(elements, canvas));
		paper.autoSize();
		
		zoomFitCamera(.9f);
		centerCamera();

	    draw();
  }

  public void setImages(ImageManager imageManager) {
	Pic[] images = imageManager.getImages();
	setImages(images);
  }
  
  void zoomFitCamera(double factor) {
    float w, h;

    w = getSize().x / paper.getSize().x;
    h = getSize().y / paper.getSize().y;

    camera.setZoomFactor((float) (factor * Math.min(w, h)));
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

    if (e.getDeltaY() > 0)
      camera.setZoomFactor(camera.getZoomFactor() * 1.1f);
    else
      camera.setZoomFactor(camera.getZoomFactor() / 1.1f);

    Vector2 newPos = (a.mul(camera.getZoomFactor())).sub(getMousePos(e));
    
    camera.setRelPos(newPos);
  }

  void onMouseDown(MouseEvent e) {
    lastMousePosition.set(getMousePos(e));
  }

  void onMouseDragging(MouseEvent e) {
    Vector2 offset = lastMousePosition.sub(getMousePos(e));
    camera.setPos(camera.getPos().add(offset.mul(1 / camera.getZoomFactor())));
    lastMousePosition.set(getMousePos(e));
  }

  void centerCamera() {
    Vector2 desiredPaperSize = paper.getSize().mul(camera.getZoomFactor());
    Vector2 tmp = desiredPaperSize.sub(getSize());
    camera.setPos(tmp.mul(1 / (2 * camera.getZoomFactor())));
  }

  void draw() {
    gc.fillRect(0, 0, getSize().x, getSize().y);

    for (ADrawable d : paper.getDrawables()) {
      d.draw(gc, camera.getPos(), camera.getZoomFactor());
    }
  }

  void setSize(Vector2 s) {
    canvas.setWidth(s.x);
    canvas.setHeight(s.y);
  }

  Vector2 getSize() {
    return new Vector2((float) canvas.getWidth(), (float) canvas.getHeight());
  }

  void setPos(Vector2 p) {
    pos.set(p);
  }

  Vector2 getPos() {
    return pos;
  }
}
