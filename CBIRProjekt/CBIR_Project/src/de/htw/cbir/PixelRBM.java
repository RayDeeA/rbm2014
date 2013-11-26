package de.htw.cbir;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.lcs.feature2opt.FeatureVector2opt;

public class PixelRBM extends RBMWrapper {
	
	// Anzahl der Eingangs und Ausgangsneuronen
	private int inputSize;
	private int outputSize;
	private double learnRate;
	
	private IRBM rbm;
	
	/**
	 * 
	 * @param inputSize Amount of pixels in scaline order
	 * @param outputSize
	 */
	public PixelRBM(int inputSize, int outputSize, double learnRate) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.learnRate = learnRate;
		this.rbm = new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());
	}
	
	public PixelRBM(int inputSize, int outputSize, IRBM rbm) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.rbm = rbm;
	}
	
	public PixelRBM shallowCopy() {
		IRBM newRBM = new RBMJBlas(inputSize, outputSize, learnRate, rbm.getWeights()[0], new DefaultLogisticMatrixFunction());
		return new PixelRBM(inputSize, outputSize, newRBM);
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
	 * Für jedes Bild besorge die 15 Byte DCT Daten.
	 * Normalisiere das Gesamtergebnis.
	 * 
	 * @param images
	 * @return
	 */
	protected double[][] createTrainingsData(Pic[] images) {
		double[][] result = new double[images.length][inputSize];
		
		// Berechne fuer alle Bilder die DCT Koeffi
		for (int i = 0; i < images.length; i++) {
			BufferedImage bi = images[i].getDisplayImage();
			
			int[] pixels = new int[bi.getWidth() * bi.getHeight()];
			bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, bi.getWidth());
			
			float[] fvFloat = new float[pixels.length];
			
			for(int j = 0; j < inputSize; j++) {
				int argb = pixels[j];
				
				int r = (argb >> 16) & 0xFF;
				int g = (argb >>  8) & 0xFF;
				int b = (argb      ) & 0xFF;
				
				int pixel = (r + g + b) / 3;
				fvFloat[j] = pixel / 255.0f;
			}
			
			for (int j = 0; j < inputSize; j++) {
				result[i][j] = fvFloat[j];
			}
		}
		
		return result;
	}	
	
	public double[] getHidden(Pic image) {
		BufferedImage bi = image.getDisplayImage();

		int[] pixels = new int[bi.getWidth() * bi.getHeight()];
		bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, bi.getWidth());
		
		float[] fvFloat = new float[pixels.length];
		
		for(int i = 0; i < inputSize; i++) {
			int argb = pixels[i];
			
			int r = (argb >> 16) & 0xFF;
			int g = (argb >>  8) & 0xFF;
			int b = (argb      ) & 0xFF;
			
			int pixel = (r + g + b) / 3;
			fvFloat[i] = pixel / 255.0f;
		}
		
		double[][] useData = new double[1][inputSize];
		for (int i = 0; i < inputSize; i++)
			useData[0][i] = fvFloat[i];
		
		// ermittle die hidden Neurons		
		double[][] hidden_data = rbm.run_visible(useData);
		return hidden_data[0];
	}

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
