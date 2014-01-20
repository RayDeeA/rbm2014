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
    
    int pCount = elements.size();
    double r = paper.getSize().x / paper.getSize().y;
    int yCount = (int) Math.ceil(Math.sqrt(pCount / r));
    int xCount = (int) Math.ceil(r * yCount);
    
    for (int i = 0; i < yCount; i++) {
      for (int j = 0; j < xCount; j++) {
        
        int index = i * yCount + j;
        Vector2 tmpSize = new Vector2(paper.getSize().x / xCount, paper.getSize().x / xCount);
        
        elements.get(index).draw(gc, offset, zoom, tmpSize);
      }
    }
  }

  public Vector2 getPos() {
    return new Vector2(0, 0);
  }

  public Vector2 getSize() {
    return paper.getSize().cpy();
  }

  @Override
  public void draw(GraphicsContext gc, Vector2 offset, double zoom, Vector2 newSize) {
    
  }
}
