package de.htw.iconn.rbm;

public interface IRBM {

	public void train(double[][] trainingData, int max_epochs );
	public double error(double[][] trainingData);
	public double[][] run_visual(double[][] userData);
	public double[][] run_hidden(double[][] hiddenData);

	public void setWeights(double[][] weights);
	public double[][] getWeights();

}
