package de.htw.iconn.fx.decomposition.rbm;

public interface IRBM {

	public void train(double[][] data, int maxEpochs, boolean binarizeHidden, boolean binarizeVisible);
	public double error(double[][] data, boolean binarizeHidden, boolean binarizeVisible);
	public double[][] getHidden(double[][] data, boolean binarizeHidden);
	public double[][] getVisible(double[][] data, boolean binarizeVisible);
	
	public double[][] getWeights();

}
