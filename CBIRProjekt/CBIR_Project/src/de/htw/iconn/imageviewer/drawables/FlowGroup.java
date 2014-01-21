package de.htw.iconn.imageviewer.drawables;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import com.badlogic.gdx.math.Vector2;

public class FlowGroup extends ADrawable {
	Canvas canvas;

	public FlowGroup(ArrayList<ADrawable> e, Canvas p) {
		canvas = p;
		drawables = e;
		pos.set(0, 0);
	}

	@Override
	public void draw(GraphicsContext gc, Vector2 offset, double zoom) {

		int pCount = drawables.size();
		double r = canvas.getWidth() / canvas.getHeight();
		int yCount = (int) Math.ceil(Math.sqrt(pCount / r));
		int xCount = (int) Math.floor(r * yCount);

		for (int i = 0; i < yCount; i++) {
			for (int j = 0; j < xCount; j++) {

				int index = i * xCount + j;

				float width = (float) (canvas.getWidth() / xCount);
				Vector2 tmpSize = new Vector2(width - 5, width - 5);

				if (index < pCount) {
					drawables.get(index).setPos(
							new Vector2(j * width, i * width));
					drawables.get(index).draw(gc, offset, zoom, tmpSize);
				}
			}
		}
	}

	public Vector2 getPos() {
		return new Vector2(0, 0);
	}

	public Vector2 getSize() {
		return new Vector2((float) canvas.getWidth(),
				(float) canvas.getHeight());
	}

	@Override
	public void draw(GraphicsContext gc, Vector2 offset, double zoom,
			Vector2 newSize) {

	}
}
