package de.htw.iconn.rbm;

import de.htw.iconn.rbm.functions.ILogistic;

public class RBMLogger implements IRBM{
	private IRBM rbm;
	private int numVisible, numHidden;
	private double learningRate;
	private String logisticName;
	private String rbmName;
	
	public RBMLogger(int numVisible, int numHidden, double learningRate, ILogistic sigmoid) {
		this.rbm = new RBMJBlas(numVisible, numHidden, learningRate, sigmoid);
		this.numVisible = numVisible;
		this.numHidden = numHidden;
		this.learningRate = learningRate;
		this.logisticName = sigmoid.getClass().getSimpleName();
		this.rbmName = this.rbm.getClass().getSimpleName();
	}
	
	public RBMLogger(int numVisible, int numHidden, double learningRate, double[][] weights, ILogistic sigmoid) {
		this.rbm = new RBMJBlas(numVisible, numHidden, learningRate, weights, sigmoid);
		this.numVisible = numVisible;
		this.numHidden = numHidden;
		this.learningRate = learningRate;
		this.logisticName = sigmoid.getClass().getSimpleName();
		this.rbmName = this.rbm.getClass().getSimpleName();
	}
	
	public RBMLogger(IRBM rbm){
		this.rbm = rbm;
		this.numVisible = rbm.getInputSize();
		this.numHidden = rbm.getOutputSize();
		this.learningRate = 0;
		this.logisticName = "NA";
		this.rbmName = this.rbm.getClass().getSimpleName();
	}

	@Override
	public void train(double[][] trainingData, int max_epochs) {
		rbm.train(trainingData, max_epochs);		
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
		return rbm.run_visible(hiddenData);
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

}
