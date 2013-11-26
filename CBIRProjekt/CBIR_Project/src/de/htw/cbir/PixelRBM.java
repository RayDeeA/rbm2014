package de.htw.cbir;

import java.awt.image.BufferedImage;

import de.htw.cbir.model.Pic;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;

public class PixelRBM extends RBMWrapper {

	/**
	 * 
	 * @param inputSize
	 *            Amount of pixels in scaline order
	 * @param outputSize
	 */
	public PixelRBM(int inputSize, int outputSize, double learnRate) {
		super(inputSize, outputSize, learnRate);
	}

	public PixelRBM(int inputSize, int outputSize, IRBM rbm) {
		super(inputSize, outputSize, rbm);
	}

	public RBMWrapper shallowCopy() {
		IRBM newRBM = new RBMJBlas(inputSize, outputSize, learnRate,
				rbm.getWeights()[0], new DefaultLogisticMatrixFunction());
		return new PixelRBM(inputSize, outputSize, newRBM);
	}

	/**
	 * Erstelle ein Trainingsarray mit den Daten aller Bilder. Für jedes Bild
	 * besorge die 15 Byte DCT Daten. Normalisiere das Gesamtergebnis.
	 * 
	 * @param images
	 * @return
	 */
	protected double[][] createTrainingsData(Pic[] images) {
		double[][] result = new double[images.length][inputSize];

		for (int i = 0; i < images.length; i++) {
			BufferedImage bi = images[i].getDisplayImage();

			int[] pixels = new int[bi.getWidth() * bi.getHeight()];
			bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0,
					bi.getWidth());

			float[] fvFloat = new float[pixels.length];

			for (int j = 0; j < inputSize; j++) {
				int argb = pixels[j];

				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb) & 0xFF;

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

		for (int i = 0; i < inputSize; i++) {
			int argb = pixels[i];

			int r = (argb >> 16) & 0xFF;
			int g = (argb >> 8) & 0xFF;
			int b = (argb) & 0xFF;

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
}
