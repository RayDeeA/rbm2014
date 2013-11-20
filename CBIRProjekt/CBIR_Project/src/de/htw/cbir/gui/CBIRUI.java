package de.htw.cbir.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;

import de.htw.cbir.CBIRController;
import de.htw.cbir.model.PrecisionRecallTable;
import de.htw.cbir.model.Settings;
import de.htw.cbir.model.Settings.SettingOption;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.GeneralisedLogisticFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearInterpolatedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;

public class CBIRUI {

	// Fenstertitel festlegen
	private static final String str = "IR Project";

	// anfängliche Fenstergröße
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
	 * 
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
			JRadioButtonMenuItem mI_methodName = new JRadioButtonMenuItem(
					methodName, true);
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

		// Menu "Logistik testen"
		JMenu logisticTestMenu = new JMenu("Logistik testen");
		String[] logisticTestMethodNames = controller.getLogisticsNames();
		for (String logisticTestMethod : logisticTestMethodNames) {
			JRadioButtonMenuItem logisticTestMethodName = new JRadioButtonMenuItem(
					logisticTestMethod, true);
			logisticTestMethodName.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.changeLogisticTest(e);
				}
			});
			logisticTestMenu.add(logisticTestMethodName);
			buttonGroup.add(logisticTestMethodName);
		}
		menuBar.add(logisticTestMenu);

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
			if (index++ > 20)
				break;
		}
		menuBar.add(testMenu);

		// Menu "Einstellungen"
		JMenu settingsMenu = new JMenu("Einstellungen");
		Settings settings = controller.getSettings();
		
		// Menupunkt "Helligkeit"
		JMenu m_lumValue = new JMenu("Lum Wert");
		final DoubleJSlider lumSlider = DoubleJSlider.createDoubleJSlider(0, 5, settings.getLuminance(), 1);
		lumSlider.setMajorTickSpacing(1);
		lumSlider.setMinorTickSpacing(0.1);
		lumSlider.setPaintTicks(true);
		lumSlider.setPaintLabels(true);
		settings.bind(SettingOption.LUMINANCE, lumSlider);
		m_lumValue.add(lumSlider);
		settingsMenu.add(m_lumValue);

		// Menu "Einstellungen->RBM"
		JMenu rbmSettingsMenu = new JMenu("RBM");
					
			// Menupunkt "Einstellungen->RBM->RBM Epochs"
			JMenu m_epochs = new JMenu("Epochs (in K)");
			final JSlider epochsSlider = new JSlider(0, 30, settings.getEpochs() / 1000);
			epochsSlider.setMajorTickSpacing(5);
			epochsSlider.setMinorTickSpacing(1);
			epochsSlider.setPaintTicks(true);
			epochsSlider.setPaintLabels(true);
			settings.bind(SettingOption.EPOCHS, epochsSlider);
			m_epochs.add(epochsSlider);
			rbmSettingsMenu.add(m_epochs);
			
			// Menupunkt "Einstellungen->RBM->Update Frequency"
			JMenu m_updateFrequency = new JMenu("Update Frequency");
			final JSlider updateFrequencySlider = new JSlider(0, 300, settings.getUpdateFrequency());
			updateFrequencySlider.setMajorTickSpacing(100);
			updateFrequencySlider.setMinorTickSpacing(50);
			updateFrequencySlider.setPaintTicks(true);
			updateFrequencySlider.setPaintLabels(true);
			settings.bind(SettingOption.UPDATE_FREQUENCY, updateFrequencySlider);
			m_updateFrequency.add(updateFrequencySlider);
			rbmSettingsMenu.add(m_updateFrequency);
	
			// Menupunkt "Einstellungen->RBM->Learnrate"
			JMenu m_rbmLearnRate = new JMenu("Learnrate");
			final DoubleJSlider rbmLearnRateSlider = DoubleJSlider.createDoubleJSlider(0, 2, settings.getLearnRate(), 1);
			rbmLearnRateSlider.setMajorTickSpacing(0.5);
			rbmLearnRateSlider.setMinorTickSpacing(0.1);
			rbmLearnRateSlider.setPaintTicks(true);
			rbmLearnRateSlider.setPaintLabels(true);
			settings.bind(SettingOption.LEARN_RATE, rbmLearnRateSlider);
			m_rbmLearnRate.add(rbmLearnRateSlider);
			rbmSettingsMenu.add(m_rbmLearnRate);
			
			// Menupunkt "Einstellungen->RBM->Input size"
			JMenu m_rbmInputSize = new JMenu("Input size");
			final JSlider rbmInputSizeSlider = new JSlider(0, 15, settings.getInputSize());
			rbmInputSizeSlider.setMajorTickSpacing(5);
			rbmInputSizeSlider.setMinorTickSpacing(1);
			rbmInputSizeSlider.setPaintTicks(true);
			rbmInputSizeSlider.setPaintLabels(true);
			settings.bind(SettingOption.INPUT_SIZE, rbmInputSizeSlider);
			m_rbmInputSize.add(rbmInputSizeSlider);
			rbmSettingsMenu.add(m_rbmInputSize);
	
			// Menupunkt "Einstellungen->RBM->Output Size"
			JMenu m_rbmOutputSize = new JMenu("Output Size");
			final JSlider rbmOutputSizeSlider = new JSlider(0, 30, settings.getOutputSize());
			rbmOutputSizeSlider.setMajorTickSpacing(5);
			rbmOutputSizeSlider.setMinorTickSpacing(1);
			rbmOutputSizeSlider.setPaintTicks(true);
			rbmOutputSizeSlider.setPaintLabels(true);
			settings.bind(SettingOption.OUTPUT_SIZE, rbmOutputSizeSlider);
			m_rbmOutputSize.add(rbmOutputSizeSlider);
			rbmSettingsMenu.add(m_rbmOutputSize);
			
			JMenu m_Seed = new JMenu("Seed");
	        JRadioButtonMenuItem m_SeedNo = new JRadioButtonMenuItem("NO");
	        m_SeedNo.setSelected(true);
	        m_SeedNo.setHorizontalTextPosition(JMenuItem.RIGHT);
	        JRadioButtonMenuItem m_Seed1 = new JRadioButtonMenuItem("1");
	        m_Seed1.setHorizontalTextPosition(JMenuItem.RIGHT);
	        JRadioButtonMenuItem m_Seed2 = new JRadioButtonMenuItem("2");
	        m_Seed2.setHorizontalTextPosition(JMenuItem.RIGHT);
	        JRadioButtonMenuItem m_Seed3 = new JRadioButtonMenuItem("3");
	        m_Seed3.setHorizontalTextPosition(JMenuItem.RIGHT);
			
	        ButtonGroup group = new ButtonGroup(  );
	        group.add(m_SeedNo);
	        group.add(m_Seed1);
	        group.add(m_Seed2);
	        group.add(m_Seed3);
			m_Seed.add(m_SeedNo);
			m_Seed.add(m_Seed1);
			m_Seed.add(m_Seed2);
			m_Seed.add(m_Seed3);
			
			settings.bind(SettingOption.SEED, m_SeedNo);
			settings.bind(SettingOption.SEED, m_Seed1);
			settings.bind(SettingOption.SEED, m_Seed2);
			settings.bind(SettingOption.SEED, m_Seed3);
			rbmSettingsMenu.add(m_Seed);
			
			JMenu m_Logistic = new JMenu("Logistic");
			ButtonGroup logisticGroup = new ButtonGroup(  );
			String[] logisticNames = controller.getLogisticsNames();
			for(int i = 0; i < logisticNames.length; i++) {
				JRadioButtonMenuItem logisticItem = new JRadioButtonMenuItem(logisticNames[i]);
				if(i == 0) logisticItem.setSelected(true);
				logisticItem.setHorizontalTextPosition(JMenuItem.RIGHT);
				logisticGroup.add(logisticItem);
				m_Logistic.add(logisticItem);
				settings.bind(SettingOption.LOGISTIC_FUNCTION, logisticItem);
			}

			rbmSettingsMenu.add(m_Logistic);

		settingsMenu.add(rbmSettingsMenu);

		menuBar.add(settingsMenu);

		// Menu "Automatisch"
		JMenu autmaticMenu = new JMenu("Automatisch");

		String[] automaticNames = new String[] { "Finde besten Lum-Wert",
				"Finde ColorSpace Distance", "Finde Genetic Histogram",
				"Finde RBM Weights", "reduziere den RBM Fehler" };
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
		String s = (String) JOptionPane.showInputDialog(frame, question,
				"Input number", JOptionPane.PLAIN_MESSAGE);
		return Integer.parseInt(s);
	}

	public double askDouble(String question) {
		String s = (String) JOptionPane.showInputDialog(frame, question,
				"Input number", JOptionPane.PLAIN_MESSAGE);
		s = s.replace(",", ".");
		return Double.parseDouble(s);
	}

	public RBMVisualizationFrame getRBMVis() {
		return this.rbmFrame;
	}

}
