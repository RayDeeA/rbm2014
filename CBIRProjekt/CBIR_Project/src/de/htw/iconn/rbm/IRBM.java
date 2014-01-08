package de.htw.iconn.rbm;

public interface IRBM {

	public void train(float[][] data, int maxEpochs, boolean binarizeHidden, boolean binarizeVisible);
	public float error(float[][] data, boolean binarizeHidden, boolean binarizeVisible);
	public float[][] getHidden(float[][] data, boolean binarizeHidden);
	public float[][] getVisible(float[][] data, boolean binarizeVisible);
	
	public float[][] getWeights();

}
