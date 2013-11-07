package de.htw.cbir.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.*;

import javax.swing.*;

public class RBMVisualizationPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private double[][] weights;
	private RBMVisualizationFrame frame;
	private Dimension dim;
	private int cellHeight;
	private int cellWidth;

	public RBMVisualizationPanel(RBMVisualizationFrame frame, Dimension dim){
		super();
		this.frame = frame;
		this.dim = dim;
		this.setSize(frame.getSize());
		this.addMouseListener(this);
		//		this.setToolTipText("tooltip");
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		//		Dimension  dim = frame.getSize();
		g.fillRect(0,0, dim.width, dim.height);
		if(weights != null) {
			double[][] weights = relativateWeights(this.weights);
			cellHeight = this.dim.height / (weights.length );
			cellWidth = this.dim.width / (weights[0].length);

			float rc = 0;
			float gc = 0;
			float bc = 0;

			for (int i = 0; i < weights[0].length; i++) {
				for (int j = 0; j < weights.length; j++) {
					float  current = (float)weights[j][i];
					if(current > 0) {
						rc = 1.0f - current;
						gc = 1.0f;
						bc = 1.0f - current;		
					}
					else {
						current = 1 + current;
						rc = 1.0f;
						gc = current;
						bc = current;
					}
					g.setColor(new Color(rc, gc, bc));
					g.fillRect((i * cellWidth), (j * cellHeight), cellWidth, cellHeight);
				}
			} 
		}
	}

	public void update(double[][] weights){
		this.weights = weights;
		Dimension  dim = frame.getSize();
		this.paintImmediately(0, 0, dim.width, dim.height);
	}

	private double[][] relativateWeights(double[][] weights) {
		final double[][] result = new double[weights.length][weights[0].length];
		double max = 0;
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				final double currentAbs = Math.abs(weights[i][j]);
				if(currentAbs > max) {
					max = currentAbs;
				}
			}
		}
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = Math.max(-1, Math.min(1, (weights[i][j] / 5.0f)));
			}
		}
		return result;
	}

	private double getCellWeight(int x, int y) {
		int cellX = x / cellWidth;
		int cellY = y / cellHeight;

		double r = 0.0;
		try {
			return weights[cellY][cellX];
		} catch (IndexOutOfBoundsException e){
			System.out.println("out of bounds");
		}
		return r;
	}

	//	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		String mousePos = "mousePos x: " + x + " y: " + y;
		String mouseWeight = "weight: " + this.getCellWeight(x, y);
		frame.setTooltip(mouseWeight);
		System.out.println(mouseWeight);
	}

	//	@Override
	public void mousePressed(MouseEvent e) {
	}

	//	@Override
	public void mouseReleased(MouseEvent e) {
	}

	//	@Override
	public void mouseEntered(MouseEvent e) {
	}

	//	@Override
	public void mouseExited(MouseEvent e) {
	}

}
