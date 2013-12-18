package de.htw.iconn.fx.decomposition;

import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.iconn.fx.decomposition.tools.Pic;

public abstract class ASorter {
	
	protected Pic[] images;
	private ForkJoinPool pool;
	
	public ASorter(Pic[] images, ForkJoinPool pool) {
		this.images = images;
		this.pool = pool;
	}
	
	
	public void getFeatureVectors() {
		getFeatureVectors(this.images);
	}
	
	public void getFeatureVectors(Pic[] images) {
		ForkSorterCalculations fsc = new ForkSorterCalculations(this, images, 0, images.length);
		pool.invoke(fsc);
	}
	
	/**
	 * Euklidische Distanz
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static double getEuclideanDist(double[] val1, double[] val2) {
		double dist = 0;
		for (int i = 0; i < val2.length; i++) {
			double buff = val1[i] - val2[i];
			dist += buff * buff;
		}
		return dist;
	}	
	
	public static double getL1Dist(double[] val1, double[] val2) {
		double dist = 0;
		for (int i = 0; i < val2.length; i++)
			dist += Math.abs(val1[i] - val2[i]);
		return dist;
	}	
	
	protected abstract BufferedImage getFeatureImage(Pic image);
	public abstract double getDistance(double[] fv1, double[] fv2); 
	public abstract String getName();
}
