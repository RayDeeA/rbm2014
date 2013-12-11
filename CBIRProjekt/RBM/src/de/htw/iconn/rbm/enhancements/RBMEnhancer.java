package de.htw.iconn.rbm.enhancements;

import java.util.LinkedList;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.functions.ILogistic;

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
                this.info = new RBMInfoPackage(0, rbm.getWeights()[0], 0);
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
		boolean updateModel = true;		
		for (int i = 0; i < max_epochs; i++) {
			updateModel = true;
			rbm.train(trainingData, 1, useHiddenStates, useVisibleStates);
			
			for (IRBMTrainingEnhancement enhancement : this.traningEnhancements) {
				if(i % enhancement.getUpdateInterval() == 0) {
					if(updateModel) {
						this.info.setError(rbm.error(trainingData, false, false));
                                                this.info.setWeights(rbm.getWeights()[0]);
                                                this.info.setEpochs(i);
						updateModel = false;
					}
					enhancement.action(this.info);
				}
			}
		}
		
		for (IRBMEndTrainingEnhancement enhancement : this.endEnhancements) {
			enhancement.action(this.info);
		}
	}

	@Override
	public double error(double[][] trainingData, boolean useHiddenStates, boolean useVisibleStates) {
		return rbm.error(trainingData, useHiddenStates, useVisibleStates);
	}

	@Override
	public double[][] run_visible(double[][] userData, boolean useHiddenStates) {
		return rbm.run_visible(userData, useHiddenStates);
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates) {
		return rbm.run_hidden(hiddenData, useVisibleStates);
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {
		rbm.setWeightsWithBias(weights);
	}

	@Override
	public double[][][] getWeights() {
		return rbm.getWeights();
	}

	@Override
	public double[][][] getWeightsWithBias() {
		return rbm.getWeightsWithBias();
	}

	@Override
	public int getInputSize() {
		return rbm.getInputSize();
	}

	@Override
	public int getOutputSize() {
		return rbm.getOutputSize();
	}

	@Override
	public double getLearnRate() {
		return rbm.getLearnRate();
	}

	@Override
	public ILogistic getLogisticFunction() {
		return rbm.getLogisticFunction();
	}

	@Override
	public boolean hasBias() {
		return rbm.hasBias();
	}
	
	public IRBM getRBM() {
		return rbm;
	}

}
