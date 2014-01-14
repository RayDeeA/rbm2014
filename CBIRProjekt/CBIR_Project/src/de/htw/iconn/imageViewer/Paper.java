package de.htw.iconn.imageViewer;

import java.util.ArrayList;

import de.htw.iconn.imageViewer.drawables.ADrawable;
import math.geom2d.Vector2D;

public class Paper {

  private Vector2D size = new Vector2D();
  private final ArrayList<ADrawable> drawables = new ArrayList<ADrawable>();
  
  Paper() {
    
  }
  
  public void setSize(Vector2D s) {
    size = s;
  }

  public void addDrawable(ADrawable d){
    drawables.add(d);
  }

  public ArrayList<ADrawable> getDrawables() {
    return drawables;
  }
  
  public Vector2D getSize() {
    return size;
  }  
}
