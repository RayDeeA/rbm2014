package de.htw.iconn.rbm;

import de.htw.iconn.rbm.functions.ILogistic;

public interface IRBM {

	public void train(double[][] trainingData, int max_epochs, boolean useHiddenStates, boolean useVisibleStates );
	public double error(double[][] trainingData);
	public double[][] run_visible(double[][] userData, boolean useHiddenStates);
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates);
	public double[][] daydream(int numberOfSamples);
	
	public void setWeightsWithBias(double[][] weights);
	public double[][][] getWeights();
	public double[][][] getWeightsWithBias();
	
	public int getInputSize();
	public int getOutputSize();
	public double getLearnRate();
	public ILogistic getLogisticFunction();
	public boolean hasBias();

}
