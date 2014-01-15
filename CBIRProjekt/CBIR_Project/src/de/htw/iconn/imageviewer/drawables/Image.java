package de.htw.iconn.imageviewer.drawables;

import com.badlogic.gdx.math.Vector2;

import javafx.scene.canvas.GraphicsContext;
import de.htw.iconn.image.Pic;

public class Image extends ADrawable {
  javafx.scene.image.Image image;
  Vector2                  pos, size;

  public Image(Pic i) {
    // image = i;
    
    image = new javafx.scene.image.Image("http://i.imgur.com/n0yfX0B.png");
    
    size = new Vector2(image.widthProperty().floatValue(), image.heightProperty().floatValue());
    pos = new Vector2(0, 0);    
  }

  @Override
  public void draw(GraphicsContext gc, Vector2 offset, double zoom) {
    gc.drawImage(image, pos.x * zoom - offset.x, pos.y * zoom - offset.y, size.x, size.y);
  }

}
