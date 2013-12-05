package de.htw.cbir;

import java.nio.file.Path;

import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;

public abstract class ARBMFeature {

	// Anzahl der Eingangs und Ausgangsneuronen
	protected int inputSize;
	protected int outputSize;
	protected double learnRate;

	// Datananalyse
	protected double dimensionMin[], dimensionMax[];

	protected IRBM rbm;

	protected ARBMFeature(int inputSize, int outputSize, double learnRate) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.learnRate = learnRate;
		this.rbm = new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());
	}

	protected ARBMFeature(int inputSize, int outputSize, IRBM rbm) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.rbm = rbm;
	}

	/**
	 * 
	 * @param inputSize
	 * @param outputSize
	 */
	public abstract ARBMFeature shallowCopy();

	public void train(Pic[] images, int maxEpoche) {
		double[][] trainingsData = createTrainingsData(images);
		if (maxEpoche > 0)
			rbm.train(trainingsData, maxEpoche);
	}

	public double getError(Pic[] images) {
		double[][] trainingsData = createTrainingsData(images);

		return rbm.error(trainingsData);// Math.sqrt(error / trainingsData.length / (inputSize + 1));
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
    public abstract double[] getVisible(double[] hiddenData);

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
