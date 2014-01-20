package de.htw.iconn.imageviewer.drawables;

import com.badlogic.gdx.math.Vector2;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import de.htw.iconn.image.Pic;

public class Image extends ADrawable {
	javafx.scene.image.Image image;
	Vector2 pos, size;

	public Image(Pic i) {

	  // from Pic
	  image = SwingFXUtils.toFXImage(i.getDisplayImage(), null);
	  
	  // from internet
//    image = new javafx.scene.image.Image("http://i.imgur.com/n0yfX0B.png");
	  
	  // local
//    image = new javafx.scene.image.Image(getClass().getResourceAsStream("./logo.png"));

		size = new Vector2((float) image.getWidth(), (float) image.getHeight());
		pos = new Vector2(0, 0);
	}

	@Override
	public void draw(GraphicsContext gc, Vector2 offset, double zoom) {
		gc.drawImage(image, (pos.x - offset.x) * zoom, zoom * (pos.y - offset.y), size.x * zoom, size.y * zoom);
	}

	public Vector2 getPos() {
		return pos.cpy();
	}

	public Vector2 getSize() {
		return size.cpy();
	}

  @Override
  public void draw(GraphicsContext gc, Vector2 offset, double zoom, Vector2 newSize) {
    gc.drawImage(image, (pos.x - offset.x) * zoom, zoom * (pos.y - offset.y), newSize.x * zoom, newSize.y * zoom);
  }
}
