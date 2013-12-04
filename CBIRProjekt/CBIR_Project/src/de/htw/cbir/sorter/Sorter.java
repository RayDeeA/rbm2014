package de.htw.cbir.sorter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;

@Deprecated
public abstract class Sorter {
	
	protected Pic[] images;
	protected Settings settings;
	private ForkJoinPool pool;
	
	public Sorter(Pic[] images, Settings settings, ForkJoinPool pool) {
		this.images = images;
		this.settings = settings;
		this.pool = pool;
		settings.addChangeListener((ActionListener)EventHandler.create(ActionListener.class, this, "settingsChanged", ""));
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
	protected abstract double[] getFeatureVector(Pic image);
	public abstract double getDistance(double[] fv1, double[] fv2); 
	public abstract String getName();
	public abstract void settingsChanged(ActionEvent e);
}
