package de.htw.cbir.gui;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class RBMVisualizationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private double[][] weights;
	private RBMVisualizationFrame frame;
	
	public RBMVisualizationPanel(RBMVisualizationFrame frame){
		super();
		this.frame = frame;
		this.setSize(frame.getSize());
	}

	public void paintComponent(Graphics g) {
		
		g.setColor(Color.BLACK);
		Dimension  dim = frame.getSize();
		g.fillRect(0,0, dim.width, dim.height);

		if(weights != null) {
			
			final float cellHeight = dim.height / (float)(weights.length + 1);
			final float cellWidth = dim.width / (float)(weights[0].length + 1);
			
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
					
					g.fillRect((int)(i * cellWidth), (int)(j * cellHeight), (int)cellWidth, (int)cellHeight);
					
				}
			} 
		}
	}
	
	public void update(double[][] weights){
		
		this.weights = relativateWeights(weights);

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
			
				result[i][j] = weights[i][j]/max;
			}
		}
		
		return result;
	}

}
