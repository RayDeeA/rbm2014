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

public class RBMVisualizationFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	private static JFrame mainFrame;
	private static JPanel menuPanel;
	// subs of menu
	private static JButton loadButton;
	private static JSlider slider;

	private static JPanel graphicsPanel;
	// subs of graphics
	private RBMVisualizationPanel visPanel;

	private JPanel dataPanel;
	// subs of error
	private JLabel errorLabel;
	private JLabel mapLabel;
	private String errorText;
	private String mapText;

	// 
	private CBIRController controller;

	private final static int width = 800;
	private final static int height = 400;
	private final static int menuHeight = 42;
	private final static int maxImageWidth = 300;
	private final static int maxImageHeight = 500;
	private final static int idealHeight = 150;
	private final static int idealWidth = 784;
	private final static int border = 13;

	private Boolean visPanelOnly;

	public RBMVisualizationFrame(Boolean visPanelOnly){		
		//		super(new BorderLayout(border, border));
		super();
		this.visPanelOnly = visPanelOnly;
		this.setSize(idealWidth, height);
		this.setPreferredSize(new Dimension(width, height));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0,13,0,0);

		if (!visPanelOnly) {
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
			slider.setPreferredSize(new Dimension(width/2, menuHeight));
			slider.setMinimum(0);
			slider.setMaximum(100);
			slider.setMajorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setToolTipText("slide to see the training history");

			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {		
				}
			});
			slider.setVisible(true);

			menuPanel = new JPanel(new GridBagLayout());
			menuPanel.add(loadButton, c);
			menuPanel.add(slider, c);
			TitledBorder menuTitle = new TitledBorder(BorderFactory.createEtchedBorder(),
					"RBM menu", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
					new Font("Sans", Font.PLAIN, 11));
			menuPanel.setBorder(menuTitle);
			this.add(menuPanel, BorderLayout.NORTH);
			menuPanel.setVisible(true);
		}

		graphicsPanel = new JPanel(new BorderLayout(border, border));		
		TitledBorder graphicsTitle = new TitledBorder(BorderFactory.createEtchedBorder(),
				"Visualization", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Sans", Font.PLAIN, 11));
		graphicsPanel.setBorder(graphicsTitle);
		visPanel = new RBMVisualizationPanel(this, new Dimension(idealWidth, idealHeight));
		visPanel.setPreferredSize(new Dimension(idealWidth, idealHeight));
		graphicsPanel.add(visPanel, BorderLayout.CENTER);
		graphicsPanel.setPreferredSize(new Dimension(width, height+menuHeight));
		this.add(graphicsPanel, BorderLayout.CENTER);

		if(!visPanelOnly) {
			dataPanel = new JPanel(new GridBagLayout());
			TitledBorder errorTitle = new TitledBorder(BorderFactory.createEtchedBorder(),
					"Data", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
					new Font("Sans", Font.PLAIN, 11));
			dataPanel.setBorder(errorTitle);
			errorLabel = new JLabel("", JLabel.LEFT);
			errorLabel.setPreferredSize(new Dimension(width/2, 23));
			errorLabel.setText("Error: " + errorText);
			errorLabel.setVisible(true);
			mapLabel = new JLabel("", JLabel.LEFT);
			mapLabel.setPreferredSize(new Dimension(width/2, 23));
			mapLabel.setText("maP: " + mapText);
			mapLabel.setVisible(true);
			dataPanel.add(errorLabel, c);
			dataPanel.add(mapLabel, c);
			this.add(dataPanel, BorderLayout.SOUTH);
		}

		// display the window.
		//		mainFrame.pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);

		visPanel.setVisible(true);
		this.setVisible(true);


	}

	public void setControllerRef(CBIRController contr) {
		this.controller = contr;
	}

	public static void main(String args[]) {
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

		RBMVisualizationFrame newContentPane = new RBMVisualizationFrame(true);
//				newContentPane.setOpaque(true); //content panes must be opaque
		mainFrame.setContentPane(newContentPane);

		// display the window.
		mainFrame.pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		mainFrame.setLocation((screenSize.width - mainFrame.getWidth()) / 2, (screenSize.height - mainFrame.getHeight()) / 2);
		mainFrame.setVisible(true);        
	}

	public void updatePanel(double[][] weights, double error, double map){
		errorText = Double.toString(error);
		mapText = Double.toString(map);
		visPanel.update(weights);
		//		errorLabel.setText("Error: " + errorText); 
		//		mapLabel.setText("maP: " + mapText);
		this.pack();
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
