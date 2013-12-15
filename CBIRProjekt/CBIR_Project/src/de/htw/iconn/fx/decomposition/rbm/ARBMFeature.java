package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.nio.file.Path;

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

	public void train(Pic[] images, int maxEpoche, boolean useHiddenStates, boolean useVisibleStates) {
		double[][] trainingsData = createTrainingsData(images);
		if (maxEpoche > 0)
			rbm.train(trainingsData, maxEpoche, useHiddenStates, useVisibleStates);
	}

	public double getError(Pic[] images, boolean useHiddenStates, boolean useVisibleStates) {
		double[][] trainingsData = createTrainingsData(images);

		return rbm.error(trainingsData, useHiddenStates, useVisibleStates);// Math.sqrt(error / trainingsData.length / (inputSize + 1));
	}

	public double getRawError(Pic[] images, boolean useHiddenStates, boolean useVisibleStates) {
		double[][] trainingsData = createTrainingsData(images);
		return rbm.error(trainingsData, useHiddenStates, useHiddenStates);
	}

	/**
	 * 
	 * @param images
	 * @return
	 */
	protected abstract double[][] createTrainingsData(Pic[] images);

	public abstract double[] getHidden(Pic image, boolean useHiddenStates);
        public abstract double[] getVisible(double[] hiddenData, boolean useVisibleStates);

	public int getVisibleCount() {
		return inputSize;
	}

	public int getHiddenCount() {
		return outputSize;
	}

	public double[][] getWeights() {
		return rbm.getWeights();
	}

	public void setWeights(double[][] weights) {
		rbm.setWeights(weights);
	}

	public void save(Path path) {

	}

	public void printWeightAnalyse() {
		System.out.println("weights");
		double[][] weights = rbm.getWeights();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++)
				System.out.printf("%6.2f ", weights[i][j]);
			System.out.println();
		}
	}
        
        public IRBM getRBM() {
            return rbm;
        }

		public Double getErrorAsDouble() {
			// TODO Auto-generated method stub
			return 42.23;
		}
}
