package de.htw.cbir.gui;

import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.functions.ILogistic;


public class VisWrapper implements IRBM{
	
	public VisWrapper(RBMVisualizationFrame visframe) {
		
	}

	@Override
	public void train(double[][] trainingData, int max_epochs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double error(double[][] trainingData) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[][] run_visible(double[][] userData) {
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
	public double[][][] getWeights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][][] getWeightsWithBias() {
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

}
