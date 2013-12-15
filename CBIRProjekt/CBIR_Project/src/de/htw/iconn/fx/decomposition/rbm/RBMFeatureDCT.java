package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.awt.image.BufferedImage;
import de.htw.lcs.feature2opt.FeatureVector2opt;

public class RBMFeatureDCT extends ARBMFeature {

	public RBMFeatureDCT(int inputSize, int outputSize, double learnRate) {
		super(inputSize, outputSize, learnRate);
	}

	public RBMFeatureDCT(int inputSize, int outputSize, IRBM rbm) {
		super(inputSize, outputSize, rbm);
	}

	public ARBMFeature shallowCopy() {
		IRBM newRBM = new RBMJBlas(inputSize, outputSize, learnRate,
				rbm.getWeights(), new DefaultLogisticMatrixFunction());
		return new RBMFeatureDCT(inputSize, outputSize, newRBM);
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

		// die minimalen und maximalen Werte pro Dimension
		dimensionMin = new double[inputSize];
		dimensionMax = new double[inputSize];

		// Berechne fuer alle Bilder die DCT Koeffi
		for (int i = 0; i < images.length; i++) {
			BufferedImage bi = images[i].getDisplayImage();

			int[] array = new int[bi.getWidth() * bi.getHeight()];

			bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), array, 0,
					bi.getWidth());

			float[] fvFloat = FeatureVector2opt.getFeatureVectorDCT(bi);

			// es werden nicht immer alle DCT Koeffi benoetigt
			for (int j = 0; j < inputSize; j++) {
				float val = fvFloat[j];

				// analysiere die Daten
				if (val < dimensionMin[j])
					dimensionMin[j] = val;
				if (val > dimensionMax[j])
					dimensionMax[j] = val;

				result[i][j] = val;
			}
		}

		// normalisiere die Daten
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				double val = (result[i][j] + Math.abs(dimensionMin[j]))
						/ (0.0000000001 + Math.abs(dimensionMin[j]) + Math
								.abs(dimensionMax[j]));
				result[i][j] = val;
			}
		}

		return result;
	}

	public double[] getHidden(Pic image, boolean useHiddenStates) {

		BufferedImage bi = image.getDisplayImage();
		float[] fvFloat = FeatureVector2opt.getFeatureVectorDCT(bi);

		// verwende nur die Wichtigen DCT Koeffi
		double[][] useData = new double[1][inputSize];
		for (int j = 0; j < inputSize; j++)
			useData[0][j] = (fvFloat[j] + Math.abs(dimensionMin[j]))
					/ (Math.abs(dimensionMin[j]) + Math.abs(dimensionMax[j]));

		// ermittle die hidden Neurons
		double[][] hidden_data = rbm.run_visible(useData, useHiddenStates);
		return hidden_data[0];
	}
	
	public double[] getVisible(double[] hiddenData, boolean useVisibleStates) {
		throw new UnsupportedOperationException();
	}
}
