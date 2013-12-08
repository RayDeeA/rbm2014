package de.htw.iconn.rbm;

import java.util.LinkedList;

import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;


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
	
	public RBMCascade(double learningRate, DefaultLogisticMatrixFunction logistic, int ...inputsizes) {
		
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
			data = rbms[i].run_visible(data, false);
		}
	}

	@Override
	public double error(double[][] trainingData) {
		double sumError = 0;
		
		double[][] data = trainingData;
		for (int i = 0; i < rbms.length; i++) {
			sumError += rbms[i].error(data);
			data = rbms[i].run_visible(data, false);
		}
		
		return sumError;
	}

	@Override
	public double[][] run_visible(double[][] userData, boolean useHiddenStates) {
		
		double[][] data = userData;
		for (int i = 0; i < rbms.length; i++) {

			data = rbms[i].run_visible(data, useHiddenStates);
		}
		return data;
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates) {
		
		double[][] data = hiddenData;
		for (int i = 0; i < rbms.length; i++) {

			data = rbms[i].run_hidden(data, useVisibleStates);
		}
		return data;
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {

	}

	@Override
	public double[][][] getWeights() {
		LinkedList<double[][]> weights = new LinkedList<>();
		
		for (IRBM rbm : rbms) {
			if(rbm instanceof RBMCascade) {
				double [][][] cascadeWeights = ((RBMCascade)rbm).getWeights();
				for (double[][] ds : cascadeWeights) {
					weights.add(ds);
				}
			}
			else {
				weights.add(rbm.getWeights()[0]);
			}
		}
		
		return (double[][][])weights.toArray();
	}

	@Override
	public double[][][] getWeightsWithBias() {
		LinkedList<double[][]> weights = new LinkedList<>();
		
		for (IRBM rbm : rbms) {
			if(rbm instanceof RBMCascade) {
				double [][][] cascadeWeights = ((RBMCascade)rbm).getWeightsWithBias();
				for (double[][] ds : cascadeWeights) {
					weights.add(ds);
				}
			}
			else {
				weights.add(rbm.getWeightsWithBias()[0]);
			}
		}
		
		return (double[][][])weights.toArray();
	}

	@Override
	public int getInputSize() {
		return rbms[0].getInputSize();
	}

	@Override
	public int getOutputSize() {
		return rbms[rbms.length - 1].getOutputSize();
	}

	@Override
	public double getLearnRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ILogistic getLogisticFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBias() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double[][] daydream(int numberOfSamples) {
		// TODO Auto-generated method stub
		return null;
	}

}
