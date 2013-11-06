package de.htw.iconn.visualisation.processing;

import processing.core.PApplet;

public class RBM_Visualisation{

	PApplet p; // The parent PApplet that we will render ourselves onto

	int rectSize = 20;
	private int mW;
	private int mH;

	public RBM_Visualisation(PApplet parent, int mH, int mW) {
		this.mH = mH;
		this.mW = mW;
		this.p = parent;

	}

	public void draw(double[][] matrix) {
		rectSize = Math.min(p.height / mH, p.width / mW);

		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[y].length; x++) {
				p.stroke(30);

				double tmp = Math.tanh(matrix[y][x]);
				float r, g, b;
				r = g = b = 0.0f;

				if (tmp < 0) {
					r = 1;
					g = 1 - (float) (Math.abs(tmp));
					b = g;
				} else if (tmp > 0) {
					g = 1;
					r = 1 - (float) (Math.abs(tmp));
					b = r;
				} else {
					r = 1;
					g = 1;
					b = 1;
				}

				p.fill(r * 255, g * 255, b * 255);
				p.rect(x * rectSize, y * rectSize, rectSize, rectSize);
			}
		}
	}
}