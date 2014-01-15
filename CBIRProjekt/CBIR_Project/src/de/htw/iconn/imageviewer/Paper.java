package de.htw.iconn.imageviewer;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.htw.iconn.imageviewer.drawables.ADrawable;

public class Paper {

  private Vector2 size = new Vector2();
  private final ArrayList<ADrawable> drawables = new ArrayList<ADrawable>();
  
  Paper() {
    
  }
  
  public void setSize(Vector2 s) {
    size.set(s);
  }

  public void addDrawable(ADrawable d){
    drawables.add(d);
  }

  public ArrayList<ADrawable> getDrawables() {
    return drawables;
  }
  
  public Vector2 getSize() {
    return size;
  }  
}
