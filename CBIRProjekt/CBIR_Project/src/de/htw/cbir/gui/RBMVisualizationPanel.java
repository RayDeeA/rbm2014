package de.htw.cbir.gui;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class RBMVisualizationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private RBMVisualizationFrame frame;
	
	public RBMVisualizationPanel(RBMVisualizationFrame frame){
		super();
		this.frame = frame;
		this.setSize(frame.frameSize());
	}

	public void paintComponent(Graphics g){
		//g.setColor(Color.RED);
		//g.drawImage(image, x, y, observer);
	}
	
	public void update(BufferedImage image){
		this.image = image;
		this.repaint();	
	}

}
