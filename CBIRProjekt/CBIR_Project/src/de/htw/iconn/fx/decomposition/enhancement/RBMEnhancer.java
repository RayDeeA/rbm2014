package de.htw.iconn.fx.decomposition.enhancement;

import de.htw.iconn.fx.decomposition.rbm.IRBM;
import java.util.LinkedList;

public class RBMEnhancer implements IRBM {
	
	private final IRBM rbm;
	private final LinkedList<IRBMTrainingEnhancement> traningEnhancements;
	private final LinkedList<IRBMEndTrainingEnhancement> endEnhancements;
        private final RBMInfoPackage info;
	
	public RBMEnhancer(IRBM rbm) {
		super();
		this.rbm = rbm;
		this.traningEnhancements = new LinkedList<>();
		this.endEnhancements = new LinkedList<>();
                this.info = new RBMInfoPackage(0, rbm.getWeights(), 0);
	}

	
	public boolean addEnhancement(IRBMEnhancement enhancement) {
		
		boolean added = false;
		
		if(enhancement instanceof IRBMTrainingEnhancement) {
                            
			traningEnhancements.add((IRBMTrainingEnhancement) enhancement);
			added = true;
		}
		
		if(enhancement instanceof IRBMEndTrainingEnhancement) {
			endEnhancements.add((IRBMEndTrainingEnhancement) enhancement);
			added = true;
		}
		
		return added;	
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs, boolean useHiddenStates, boolean useVisibleStates) {
		boolean updateModel;		
		for (int i = 0; i < max_epochs; i++) {
			updateModel = true;
			rbm.train(trainingData, 1, useHiddenStates, useVisibleStates);
			
			for (IRBMTrainingEnhancement enhancement : this.traningEnhancements) {
				if(i % enhancement.getUpdateInterval() == 0) {
					if(updateModel) {
						updateModel = false;
                        setInfo(rbm, trainingData, i, useHiddenStates, useVisibleStates);
					}
					enhancement.action(this.info);
				}
			}
		}
		setInfo(rbm, trainingData, max_epochs, useHiddenStates, useVisibleStates);
		for (IRBMEndTrainingEnhancement enhancement : this.endEnhancements) {
			enhancement.action(this.info);
		}
	}

        private void setInfo(IRBM rbm, double[][] trainingData, int epochs, boolean useHiddenStates, boolean useVisibleStates) {
                this.info.setError(rbm.error(trainingData, useHiddenStates, useVisibleStates));
                this.info.setWeights(rbm.getWeights());
                this.info.setEpochs(epochs);
        }
	@Override
	public double error(double[][] trainingData, boolean useHiddenStates, boolean useVisibleStates) {
		return rbm.error(trainingData, useHiddenStates, useVisibleStates);
	}

	@Override
	public double[][] getHidden(double[][] userData, boolean useHiddenStates) {
		return rbm.getHidden(userData, useHiddenStates);
	}

	@Override
	public double[][] getVisible(double[][] hiddenData, boolean useVisibleStates) {
		return rbm.getVisible(hiddenData, useVisibleStates);
	}

	@Override
	public double[][] getWeights() {
		return rbm.getWeights();
	}

}
