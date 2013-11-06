package de.htw.cbir;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import de.htw.ait.rbm.RBMBla;
import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultSigmoidMatrixFunction;
import de.htw.lcs.feature2opt.FeatureVector2opt;

public class DCTRBM {

	private static double learnRate = 0.1;
	
	// Datananalyse
	private double dimensionMin[], dimensionMax[];
	
	// Anzahl der Eingangs und Ausgangsneuronen
	private int inputSize;
	private int outputSize;
	
	private IRBM rbm;
	
	/**
	 * 
	 * @param inputSize Gueltig sind 3,6,9,12 oder 15
	 * @param outputSize
	 */
	public DCTRBM(int inputSize, int outputSize) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.rbm = new RBMJBlas(inputSize, outputSize, learnRate, new DefaultSigmoidMatrixFunction());
	}
	
	public DCTRBM(int inputSize, int outputSize, IRBM rbm) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.rbm = rbm;
	}
	
	public DCTRBM shallowCopy() {
		IRBM newRBM = new RBMJBlas(inputSize, outputSize, learnRate, rbm.getWeights(), new DefaultSigmoidMatrixFunction());
		return new DCTRBM(inputSize, outputSize, newRBM);
	}
	
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
	 * Erstelle ein Trainingsarray mit den Daten aller Bilder.
	 * F������r jedes Bild besorge die 15 Byte DCT Daten.
	 * Normalisiere das Gesamtergebnis.
	 * 
	 * @param images
	 * @return
	 */
	private double[][] createTrainingsData(Pic[] images) {
		double[][] result = new double[images.length][inputSize];
		
		// die minimalen und maximalen Werte pro Dimension
		dimensionMin = new double[inputSize];
		dimensionMax = new double[inputSize];
		
		// Berechne fuer alle Bilder die DCT Koeffi
		for (int i = 0; i < images.length; i++) {
			BufferedImage bi = images[i].getDisplayImage();
			float[] fvFloat = FeatureVector2opt.getFeatureVectorDCT(bi);
		
			// es werden nicht immer alle DCT Koeffi benoetigt
			for (int j = 0; j < inputSize; j++) {
				float val = fvFloat[j];
				
				// analysiere die Daten
				if(val < dimensionMin[j]) dimensionMin[j] = val;
				if(val > dimensionMax[j]) dimensionMax[j] = val;
				
				result[i][j] = val;
			}
		}
		
		// normalisiere die Daten
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				double val = (result[i][j] + Math.abs(dimensionMin[j])) / (Math.abs(dimensionMin[j]) + Math.abs(dimensionMax[j]));
				result[i][j] = val;
			}
		}
		
		return result;
	}	
	
	public double[] getHidden(Pic image) {
		
		BufferedImage bi = image.getDisplayImage();
		float[] fvFloat = FeatureVector2opt.getFeatureVectorDCT(bi);
		
		// verwende nur die Wichtigen DCT Koeffi
		double[][] useData = new double[1][inputSize];
		for (int j = 0; j < inputSize; j++)
			useData[0][j] = (fvFloat[j] + Math.abs(dimensionMin[j])) / (Math.abs(dimensionMin[j]) + Math.abs(dimensionMax[j]));
		
		// ermittle die hidden Neurons		
		double[][] hidden_data = rbm.run_visual(useData);
		return hidden_data[0];
	}

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
	
	
	
}
