package de.htw.cbir.sorter;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.histogram.IDWHistogram;
import de.htw.cbir.histogram.IDWHistogramFactory;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

public class Sorter_IDWHistogram extends Sorter {

	private IDWHistogramFactory factory;
	
	public Sorter_IDWHistogram(Pic[] images, Settings settings, IDWHistogramFactory histoFactory, ForkJoinPool pool) {
		super(images, settings, pool);
		this.factory = histoFactory;
	}

	@Override
	protected BufferedImage getFeatureImage(Pic image) {
		double[][] histogramDataPoints = factory.getRGBDataPoints();
		int width = histogramDataPoints.length;
		int height = 256;

		double[] bins = image.getHistogram().getBins();
		int [] pixels = new int [width * height];
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y <  height; y++) {
			for (int x = 0; x < width; x++) {
				int i = y * width + x;
				double val = bins[x]*height;

				if ( val > height-1 - y ) {
					pixels[i] =  0xFF000000 | ((int)histogramDataPoints[x][0] << 16) | ((int)histogramDataPoints[x][1] << 8) | (int)histogramDataPoints[x][2];
				}
				else
					pixels[i] =  0xFF777777;
			}				
		}

		bi.setRGB(0, 0, width, height, pixels, 0, width);
		return bi;
	}

	@Override
	protected double[] getFeatureVector(Pic image) {
		IDWHistogram histo = factory.analyseImage(image.getDisplayImage());				
		image.setHistogram(histo);
		return histo.getBins();
	}

	@Override
	public double getDistance(double[] fv1, double[] fv2) {
		return factory.getDistance(fv1, fv2);
	}

	@Override
	public String getName() {
		double power = factory.getPixelDistancePower();
		int size = factory.getHistogramBinSize();
		return "IDW Histogram PixelPow: "+power+", HistoPow: 0.34, Bins: "+size;
	}

	@Override
	public void settingsChanged(ActionEvent e) {

	}
}
