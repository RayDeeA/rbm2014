package de.htw.cbir.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class VisTrainingPanel extends JPanel implements MouseListener{
	
	private RBMVisualizationFrame frame;
	private Dimension dim;

	public VisTrainingPanel(RBMVisualizationFrame frame, Dimension dim){
		super();
		this.frame = frame;
		this.dim = dim;
		this.setSize(frame.getSize());
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}