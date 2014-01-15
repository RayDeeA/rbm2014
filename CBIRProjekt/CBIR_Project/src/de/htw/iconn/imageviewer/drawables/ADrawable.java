package de.htw.iconn.imageviewer.drawables;

import javafx.scene.canvas.GraphicsContext;

import com.badlogic.gdx.math.Vector2;

public abstract class ADrawable {

	protected final Vector2 pos = new Vector2();
	protected final Vector2 size = new Vector2();

	public abstract void draw(GraphicsContext gc, Vector2 offset, double zoom);

	public Vector2 getPos() {
		return pos;
	}

	public Vector2 getSize() {
		return size;
	}
}
