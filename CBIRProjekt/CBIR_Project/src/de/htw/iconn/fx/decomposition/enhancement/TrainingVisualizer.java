package de.htw.iconn.fx.decomposition.enhancement;

public class TrainingVisualizer implements IRBMTrainingEnhancement {

	private final int updateInterval;

	public TrainingVisualizer(int updateInterval) {
		super();
		this.updateInterval = updateInterval;
	}

	@Override
	public void action(RBMInfoPackage info) {
		
	}

	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

}
