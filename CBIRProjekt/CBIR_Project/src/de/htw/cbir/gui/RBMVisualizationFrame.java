package de.htw.cbir.gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.htw.cbir.CBIRController;

public class RBMVisualizationFrame extends JPanel{

	private static final long serialVersionUID = 1L;

	private static JFrame mainFrame;
	private static JPanel menuPanel;
	// subs of menu
	private static JButton loadButton;
	private static JSlider slider;
	
	private static JPanel graphicsPanel;
	// subs of graphics
	private RBMVisualizationPanel visPanel;
	private RBMVisualizationPanel trainingPanel;
	private RBMVisualizationPanel hiddenPanel;

	private JPanel errorPanel;
	// subs of error
	private JLabel errorLabel;
	private String errorText;
	private String weightText;
	
	// 
	private CBIRController controller;

	private final static int width = 900;
	private final static int height = 600;
	private final static int menuHeight = 60;
	private static final int maxImageWidth = 300;
	private static final int maxImageHeight = 500;
	private static final int border = 13;
	
	public RBMVisualizationFrame(){		
		super(new BorderLayout(border, border));
		this.setSize(new Dimension(width, height));			
		
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
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
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
		
		menuPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,13,0,0);
		menuPanel.add(loadButton, c);
		menuPanel.add(slider, c);
		TitledBorder menuTitle = new TitledBorder(BorderFactory.createEtchedBorder(),
				"RBM menu", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Sans", Font.PLAIN, 11));
		menuPanel.setBorder(menuTitle);
		this.add(menuPanel, BorderLayout.NORTH);
		
		graphicsPanel = new JPanel(new BorderLayout(border, border));		
		visPanel = new RBMVisualizationPanel(this, new Dimension(maxImageWidth, maxImageHeight));
		trainingPanel = new RBMVisualizationPanel(this, new Dimension(maxImageWidth, maxImageHeight));
		hiddenPanel = new RBMVisualizationPanel(this, new Dimension(maxImageWidth, maxImageHeight));
		graphicsPanel.add(visPanel, BorderLayout.WEST);
		graphicsPanel.add(trainingPanel, BorderLayout.CENTER);
		graphicsPanel.add(hiddenPanel, BorderLayout.EAST);
		graphicsPanel.setPreferredSize(new Dimension(width, height+menuHeight));
		this.add(graphicsPanel, BorderLayout.CENTER);
		
		
		TitledBorder errorTitle = new TitledBorder(BorderFactory.createEtchedBorder(),
				"Error", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Sans", Font.PLAIN, 11));
//		.setBorder(menuTitle);
		errorLabel = new JLabel("", JLabel.CENTER);
		errorLabel.setPreferredSize(new Dimension(width, 23));
		errorLabel.setVisible(true);
		this.add(errorLabel, BorderLayout.SOUTH);
		
		// display the window.
//		this.pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		
		menuPanel.setVisible(true);
		visPanel.setVisible(true);
		trainingPanel.setVisible(true);
		hiddenPanel.setVisible(true);
		this.setVisible(true);


	}
	
	public void setControllerRef(CBIRController contr) {
		mainFrame = new JFrame("RBM Visualization");
		this.controller = contr;
	}

	public static void main(String args[]) {
//		final RBMVisualizationFrame mainFrame = new RBMVisualizationFrame();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});		
	}
	
	private static void createAndShowGUI() {
		// create and setup the window
		mainFrame = new JFrame("RBM Visualization");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new RBMVisualizationFrame();
		newContentPane.setOpaque(true); //content panes must be opaque
		mainFrame.setContentPane(newContentPane);

		// display the window.
		mainFrame.pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		mainFrame.setLocation((screenSize.width - mainFrame.getWidth()) / 2, (screenSize.height - mainFrame.getHeight()) / 2);
		mainFrame.setVisible(true);        
	}
	

	public void update(double[][] weights, double error){
		errorText = Double.toString(error);
		visPanel.update(weights);
		errorLabel.setText("Error: " + errorText); 
		mainFrame.pack();
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

}
