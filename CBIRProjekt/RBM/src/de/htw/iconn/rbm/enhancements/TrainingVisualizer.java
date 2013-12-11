package de.htw.iconn.rbm.enhancements;

import de.htw.cbir.gui.RBMVisualizationFrame;

public class TrainingVisualizer implements IRBMTrainingEnhancement {

	private final int updateInterval;
	private final RBMVisualizationFrame frame;

	public TrainingVisualizer(RBMVisualizationFrame frame, int updateInterval) {
		super();
		this.updateInterval = updateInterval;
		this.frame = frame;
	}

	@Override
	public void action(RBMInfoPackage info) {
		frame.updatePanel(info.getWeights(), info.getError(), 0);
	}

	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

}
