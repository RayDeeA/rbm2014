package de.htw.iconn.rbm.enhancements;

import java.util.LinkedList;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.functions.ILogistic;

public class RBMObserver implements IRBM {
	
	private final IRBM rbm;
	private final LinkedList<IRBMTrainingEnhancement> traningEnhancements;
	private final LinkedList<IRBMEndTrainingEnhancement> endEnhancements;
	private final CBIREvaluationModel evaluationModel;

	
	public RBMObserver(IRBM rbm, CBIREvaluationModel evaluationModel) {
		super();
		this.rbm = rbm;
		this.evaluationModel = evaluationModel;
		this.traningEnhancements = new LinkedList<>();
		this.endEnhancements = new LinkedList<>();
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
	public void train(double[][] trainingData, int max_epochs) {
		
		for (int i = 0; i < max_epochs; i++) {
			rbm.train(trainingData, 1);
			for (IRBMTrainingEnhancement enhancement : this.traningEnhancements) {
				if(i % enhancement.getUpdateInterval() == 0) {
					enhancement.action(evaluationModel);
				}
			}
		}
		
		for (IRBMEndTrainingEnhancement enhancement : this.endEnhancements) {
			enhancement.action(evaluationModel);
		}
	}

	@Override
	public double error(double[][] trainingData) {
		return rbm.error(trainingData);
	}

	@Override
	public double[][] run_visible(double[][] userData) {
		return rbm.run_visible(userData);
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		return rbm.run_hidden(hiddenData);
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
