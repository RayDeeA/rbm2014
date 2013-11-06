package de.htw.cbir.gui;

import java.awt.*;
import javax.swing.*;

public class RBMVisualizationFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private RBMVisualizationPanel panel;
	
	private final int width = 600;
	private final int height = 600;
	
	public RBMVisualizationFrame(){		
		super("RBMVisualization");
		
		this.setSize(new Dimension(width, height));
			
		this.setVisible(true);
		panel = new RBMVisualizationPanel(this);
		this.add(panel);
		
	}

	public void update(double[][] weights){
		panel.update(weights);
	}

}
