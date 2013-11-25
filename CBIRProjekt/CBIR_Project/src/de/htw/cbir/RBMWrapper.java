package de.htw.cbir;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.lcs.feature2opt.FeatureVector2opt;

public abstract class RBMWrapper {
	
	// Anzahl der Eingangs und Ausgangsneuronen
	private int inputSize;
	private int outputSize;
	private double learnRate;
	
	private IRBM rbm;
	
	/**
	 * 
	 * @param inputSize
	 * @param outputSize
	 */
	public abstract RBMWrapper shallowCopy();
	
	public void train(Pic[] images, int maxEpoche) {
		double[][] trainingsData = createTrainingsData(images);
		if(maxEpoche > 0)
			rbm.train(trainingsData, maxEpoche);
	}
	
	
	public double getError(Pic[] images) {
		double[][] trainingsData = createTrainingsData(images);
		double error = rbm.error(trainingsData);
		return Math.sqrt(error / trainingsData.length / (inputSize+1));
	}
	
	public double getRawError(Pic[] images) {
		double[][] trainingsData = createTrainingsData(images);
		return rbm.error(trainingsData);
	}
	
	/**
	 * 
	 * @param images
	 * @return
	 */
	protected abstract double[][] createTrainingsData(Pic[] images);
	
	public abstract double[] getHidden(Pic image);

	public int getVisibleCount() {
		return inputSize;
	}

	public int getHiddenCount() {
		return outputSize;
	}
	
	public double[][] getWeights() {
		return rbm.getWeights()[0];
	}

	public void setWeights(double[][] weights) {
		rbm.setWeightsWithBias(weights);
	}

	public void save(Path path) {
		
	}

	public void printWeightAnalyse() {
		System.out.println("weights");
		double[][] weights = rbm.getWeights()[0];
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++)
				System.out.printf("%6.2f ", weights[i][j]);
			System.out.println();
		}
	}
}
