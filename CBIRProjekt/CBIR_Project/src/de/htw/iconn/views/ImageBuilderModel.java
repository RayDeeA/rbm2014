package de.htw.iconn.views;

import de.htw.iconn.main.BenchmarkModel;

public class ImageBuilderModel {

    private final ImageBuilderController controller;
	private BenchmarkModel benchmarkModel;
	
    private boolean useHiddenStates;
	private boolean useVisibleStates;
    
	public ImageBuilderModel(ImageBuilderController controller) {
        this.controller = controller;
	}
    
	public void setBenchmarkModel(BenchmarkModel benchmarkModel) {
		this.benchmarkModel = benchmarkModel;
	}
	
    public void setUseHiddenStates(boolean useHiddenStates) {
        this.useHiddenStates = useHiddenStates;
    }

    public void setUseVisibleStates(boolean useVisibleStates) {
        this.useVisibleStates = useVisibleStates;
    }

}
