package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.awt.image.BufferedImage;

public class RBMFeaturePixel extends ARBMFeature {

	/**
	 * 
	 * @param inputSize
	 *            Amount of pixels in scaline order
	 * @param outputSize
	 */
	public RBMFeaturePixel(int inputSize, int outputSize, double learnRate) {
		super(inputSize, outputSize, learnRate);
	}

	public RBMFeaturePixel(int inputSize, int outputSize, IRBM rbm) {
		super(inputSize, outputSize, rbm);
	}

	public ARBMFeature shallowCopy() {
		IRBM newRBM = new RBMJBlas(inputSize, outputSize, learnRate, rbm.getWeights(), new DefaultLogisticMatrixFunction());
		return new RBMFeaturePixel(inputSize, outputSize, newRBM);
	}

	/**
	 * Erstelle ein Trainingsarray mit den Daten aller Bilder. F��r jedes Bild
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
			bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, bi.getWidth());

			for (int j = 0; j < inputSize; j++) {
				int argb = pixels[j];

				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb) & 0xFF;

				int pixel = (r + g + b) / 3;
				result[i][j] = pixel / 255.0f;
			}
		}
		return result;
	}

	public double[] getHidden(Pic image, boolean useHiddenStates) {
		BufferedImage bi = image.getDisplayImage();

		int[] pixels = new int[bi.getWidth() * bi.getHeight()];
		bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, bi.getWidth());

		double[] fvFloat = new double[pixels.length];

		for (int i = 0; i < pixels.length; i++) {
			int argb = pixels[i];

			int r = (argb >> 16) & 0xFF;
			int g = (argb >> 8) & 0xFF;
			int b = (argb) & 0xFF;

			int pixel = (r + g + b) / 3;
			fvFloat[i] = pixel / 255.0f;
		}

		double[][] useData = new double[1][pixels.length];
		for (int i = 0; i < pixels.length; i++)
			useData[0][i] = fvFloat[i];

		// ermittle die hidden Neurons
		double[][] hidden_data = rbm.run_visible(useData, useHiddenStates);
		return hidden_data[0];
	}
	
    public double[] getVisible(double[] hiddenData, boolean useVisibleStates) {

        double[][] useData = new double[1][hiddenData.length];
        for (int i = 0; i < hiddenData.length; i++) {
            useData[0][i] = hiddenData[i];
        }

        // ermittle die visible Neurons
        double[][] visible_data = rbm.run_hidden(useData, useVisibleStates);

        return visible_data[0];
    }
}
