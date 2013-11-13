package de.htw.iconn.rbm;

public interface IRBM {

	public void train(double[][] trainingData, int max_epochs );
	public double error(double[][] trainingData);
	public double[][] run_visual(double[][] userData);
	public double[][] run_hidden(double[][] hiddenData);

	public void setWeightsWithBias(double[][] weights);
	public double[][] getWeights();
	public double[][] getWeightsWithBIAS();
	
	public int getInputSize();
	public int getOutputSize();

}
