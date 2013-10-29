package de.htw.cbir.sorter;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.lcs.feature2opt.FeatureVector2opt;


public class Sorter_FV15DCT extends Sorter {

	public Sorter_FV15DCT(Pic[] pics, Settings settings, ForkJoinPool pool) {
		super(pics, settings, pool);
	}

	///////////////////////////////////////////
	// visualize the feature data as image
	//
	@Override
	protected BufferedImage getFeatureImage(Pic image) {

		int w = 1;
		int h = 1;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		double[] featureVector = image.getFeatureVector();
		int r = (int) featureVector[0];
		int g = (int) featureVector[1];
		int b = (int) featureVector[2];

		pixels[0] = (0xFF << 24) | (r << 16) | (g << 8) | b;

		BufferedImage bThumb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		bThumb.setRGB(0, 0, w, h, pixels, 0, w);

		big.drawImage(bThumb, 0, 0, w, h, null);
		big.dispose();
		return bi;
	}

	@Override
	protected double[] getFeatureVector(Pic image) {
		
		BufferedImage bi = image.getDisplayImage();
		float[] fvFloat = FeatureVector2opt.getFeatureVectorDCT(bi);

		double[] featureVector = new double[fvFloat.length];
		for (int i = 0; i < fvFloat.length; i++) 
			featureVector[i] = fvFloat[i];

		return featureVector;
	}

	@Override
	public double getDistance(double[] fv1, double[] fv2) {
		return getEuclideanDist(fv1, fv2);
	}

	@Override
	public String getName() {
		return "FV15DCT";
	}

	@Override
	public void settingsChanged(ActionEvent e) {
	}
}
