package de.htw.cbir.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import de.htw.cbir.CBIRController;
import de.htw.cbir.model.PrecisionRecallTable;
import de.htw.cbir.model.Settings.SettingOption;

public class CBIRUI  {

	// Fenstertitel festlegen
	private static final String str = "IR Project";
	
	// anfägnliche Fenstergröße
	private static final int frameSizeX = 500; 
	private static final int frameSizeY = 500;
	
	// GUI Elemente
	private JFrame frame;
	private RBMVisualizationFrame rbmFrame;
	private ImageGrid grid;
	
	// allgemeine Variablen
	private CBIRController controller;
	
	public CBIRUI(CBIRController controller, RBMVisualizationFrame rbmFrame) {
		this.controller = controller;
		this.rbmFrame = rbmFrame;
		
		// Hauptfenster
		frame = createMainFrame();
		
		// Komponente die die Bilder darstellt
		grid = new ImageGrid(controller, frameSizeX, frameSizeY);
		frame.add(grid);
		
		// lass alles zeichnen
		repaint();
		PrecisionRecallTable.initializeGraph();
		
	}
	
	/**
	 * Alles neuzeichnen
	 */
	public void repaint() {
		// zeichne die Bilder
		grid.doDrawing();
	}
	
	/**
	 * Erstelle das Hauptfenster
	 * @return
	 */
	private JFrame createMainFrame() {
		JFrame jFrame = new JFrame(str);
		jFrame.setJMenuBar(createMenuBar());
		jFrame.setSize(frameSizeX, frameSizeY);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		return jFrame;
	}
	
	private JMenuBar createMenuBar() {
		
		// Menubar 
		JMenuBar menuBar = new JMenuBar();
		
		// Menu "Algorithm"
		JMenu methodMenu = new JMenu("Algorithm");
		ButtonGroup buttonGroup = new ButtonGroup();
		String[] methodNames = controller.getSorterNames();
		for (String methodName : methodNames) {
			JRadioButtonMenuItem mI_methodName = new JRadioButtonMenuItem(methodName,true);
			mI_methodName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeSorter(e);
				}
			});
			methodMenu.add(mI_methodName);
			buttonGroup.add(mI_methodName);
		}
		menuBar.add(methodMenu);
		
		// Menu "Testen"
		JMenu testMenu = new JMenu("Testen");
		
		JMenuItem mI_all = new JMenuItem("Alle");
		mI_all.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.triggerTests(e);
			}
		});
		testMenu.add(mI_all);
		
		
		int index = 0;
		for (String groupName : controller.getImageManager().getGroupNames()) {
			JMenuItem mI_group = new JMenuItem(groupName);
			mI_group.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.triggerTests(e);
				}
			});
			testMenu.add(mI_group);
			
			// brich nach 20 ab
			if(index++ > 20) break;
		}
		menuBar.add(testMenu);
		
		// Menu "Einstellungen"
		JMenu settingsMenu = new JMenu("Einstellungen");
		
		// Menupunkt "Helligkeit"
		JMenu m_lumValue = new JMenu("Lum Wert");
		final DoubleJSlider lumSlider = DoubleJSlider.createDoubleJSlider(0, 5, 0, 1);
		lumSlider.setMajorTickSpacing(1);
		lumSlider.setMinorTickSpacing(0.1);
		lumSlider.setPaintTicks(true);
		lumSlider.setPaintLabels(true);
		controller.getSettings().bind(SettingOption.LUM, lumSlider);
		m_lumValue.add(lumSlider);
		settingsMenu.add(m_lumValue);
		
		menuBar.add(settingsMenu);
		
		
		// Menu "Automatisch"
		JMenu autmaticMenu = new JMenu("Automatisch");
		
		String[] automaticNames = new String[] { "Finde besten Lum-Wert", "Finde ColorSpace Distance", "Finde Genetic Histogram", "Finde RBM Weights", "reduziere den RBM Fehler" };
		for (String automaticName : automaticNames) {
			JMenuItem mI_group = new JMenuItem(automaticName);
			mI_group.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.triggerAutomaticTests(e);
				}
			});
			autmaticMenu.add(mI_group);
		}
		
		menuBar.add(autmaticMenu);
		
		return menuBar;
	}

	public int askNumber(String question) {
		String s = (String)JOptionPane.showInputDialog(frame,
                question, "Input number", JOptionPane.PLAIN_MESSAGE);
		return Integer.parseInt(s);
	}
	
	public double askDouble(String question) {
		String s = (String)JOptionPane.showInputDialog(frame,
                question, "Input number", JOptionPane.PLAIN_MESSAGE);
		s = s.replace(",", ".");
		return Double.parseDouble(s);
	}
	
	public RBMVisualizationFrame getRBMVis(){
		return this.rbmFrame;
	}

}
