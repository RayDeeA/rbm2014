package de.htw.iconn.rbm;

import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;


public class RBMCascade implements IRBM {

	private final IRBM[] rbms;
	
	public RBMCascade(IRBM ...rbms) {
		
		int lastRBMSize = rbms[0].getInputSize();
		for (IRBM rbm : rbms) {
			if(rbm.getInputSize() == lastRBMSize) {
				lastRBMSize = rbm.getOutputSize();
			}
			else throw new IllegalArgumentException("InputSize: " + rbm.getInputSize()+ " differs from last outputsize: " + lastRBMSize);
		}
		
		this.rbms = rbms;
		
	}
	
	public RBMCascade(int learningRate, DefaultLogisticMatrixFunction logistic, int ...inputsizes) {
		
		rbms = new IRBM[inputsizes.length];
		
		int lastRBMSize = inputsizes[0];
		for (int i = 1; i < inputsizes.length; i++) {
			rbms[i] = new RBMJBlas(lastRBMSize, inputsizes[i], learningRate, logistic);
		}
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs) {
		
		double[][] data = trainingData;
		
		for (int i = 0; i < rbms.length; i++) {
			rbms[i].train(data, max_epochs);
			data = rbms[i].run_visual(data);
		}
	}

	@Override
	public double error(double[][] trainingData) {
		double sumError = 0;
		
		double[][] data = trainingData;
		for (int i = 0; i < rbms.length; i++) {
			sumError += rbms[i].error(data);
			data = rbms[i].run_visual(data);
		}
		
		return sumError;
	}

	@Override
	public double[][] run_visual(double[][] userData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {
		// TODO Auto-generated method stub

	}

	@Override
	public double[][] getWeights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] getWeightsWithBIAS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInputSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutputSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
