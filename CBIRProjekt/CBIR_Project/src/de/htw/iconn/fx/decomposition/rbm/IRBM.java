package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.ILogistic;

public interface IRBM {

	public void train(double[][] trainingData, int max_epochs, boolean useHiddenStates, boolean useVisibleStates );
	public double error(double[][] trainingData, boolean useHiddenStates, boolean useVisibleStates);
	public double[][] run_visible(double[][] userData, boolean useHiddenStates);
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates);
	
	public void setWeights(double[][] weights);
	public double[][] getWeights();
	
	public int getInputSize();
	public int getOutputSize();
	public double getLearnRate();
	public ILogistic getLogisticFunction();
	public boolean hasBias();

}
