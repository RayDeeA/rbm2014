package de.htw.cbir.gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class RBMVisualizationFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private RBMVisualizationPanel panel;
	
	public RBMVisualizationFrame(){		
		super("RBMVisualization");
		panel = new RBMVisualizationPanel(this);
		this.setSize(new Dimension(600, 600));
		this.add(panel);	
		this.setVisible(true);
	}
	
	public Dimension frameSize(){
		return new Dimension(this.getWidth(), this.getHeight());
	}

	public void update(BufferedImage image){
		panel.update(image);
	}
}
