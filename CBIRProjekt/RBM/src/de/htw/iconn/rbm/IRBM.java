package de.htw.iconn.rbm;

import de.htw.iconn.rbm.functions.ILogistic;

public interface IRBM {

	public void train(double[][] trainingData, int max_epochs );
	public double error(double[][] trainingData);
	public double[][] run_visible(double[][] userData);
	public double[][] run_hidden(double[][] hiddenData);

	public void setWeightsWithBias(double[][] weights);
	public double[][][] getWeights();
	public double[][][] getWeightsWithBias();
	
	public int getInputSize();
	public int getOutputSize();
	public double getLearnRate();
	public ILogistic getLogisticFunction();

}
