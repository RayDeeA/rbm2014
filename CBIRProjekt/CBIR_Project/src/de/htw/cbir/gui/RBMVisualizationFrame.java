package de.htw.cbir.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.htw.cbir.CBIRController;

public class RBMVisualizationFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static RBMVisualizationPanel visPanel;
	private static JPanel menuPanel;

	private static JButton saveButton;
	private static JButton loadButton;
	private static JSlider slider;
	private JLabel errorLabel;
	private JPanel errorPanel;
	private String errorText;
	private String weightText;
	private CBIRController controller;

	private final static int width = 600;
	private final static int height = 800;
	private final static int menuHeight = 60;

	public RBMVisualizationFrame(){		
		super("RBMVisualization");
		this.createAndShowGUI();

	}
	
	public void setControllerRef(CBIRController contr) {
		this.controller = contr;
	}

	public static void main(String args[]) {
		final RBMVisualizationFrame frame = new RBMVisualizationFrame();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.createAndShowGUI();
			}
		});		
	}

	private void createAndShowGUI() {


		//		frame = new JFrame(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(width, height));			


		menuPanel = new JPanel(new BorderLayout(23, 23));
		visPanel = new RBMVisualizationPanel(this, new Dimension(width, width));

		// save
		saveButton = new JButton("save");
		saveButton.setPreferredSize(new Dimension(width/3, menuHeight));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}        	
		});	
		saveButton.setVisible(true);

		// load
		loadButton = new JButton("load");
		loadButton.setPreferredSize(new Dimension(width/3, menuHeight));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}        	
		});
		loadButton.setVisible(true);

		// slider history
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.setPreferredSize(new Dimension(width/3, menuHeight));
		slider.setMinimum(0);
		slider.setMaximum(100);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {		
			}
		});
		slider.setVisible(true);

		menuPanel.add(saveButton, BorderLayout.WEST);
		menuPanel.add(loadButton, BorderLayout.CENTER);
		menuPanel.add(slider, BorderLayout.EAST);

		visPanel.setPreferredSize(new Dimension(width, width));

		this.add(menuPanel, BorderLayout.NORTH);
		this.add(visPanel, BorderLayout.CENTER);

		errorLabel = new JLabel("", JLabel.CENTER);
		errorLabel.setPreferredSize(new Dimension(width, 23));
		errorLabel.setVisible(true);
		this.add(errorLabel, BorderLayout.SOUTH);

		menuPanel.setVisible(true);
		visPanel.setVisible(true);
		this.setVisible(true);
	}

	public void update(double[][] weights, double error){
		errorText = Double.toString(error);
		visPanel.update(weights);
		errorLabel.setText("Error: " + errorText); 
		this.pack();

	}

	//	frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter()

	/**
	 * method that saves the current visualization data
	 */
	private void saveFile() {

		this.controller.saveButtonPressed();
		
	}

	/**
	 * method that loads a xml file to restore a visualization
	 */
	private File loadFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML (*.XML, *.xml)", "XML", "xml");
		chooser.setFileFilter(filter);
		int ret = chooser.showOpenDialog(this);
		if(ret == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
		return null;		
	}

	/**
	 * method that goes back an forth in the visualization history
	 */
	private void history() {

	}

	public void setTooltip(String pos) {
		this.weightText = pos;
		this.errorLabel.setText(weightText);
		this.pack();
	}
}
