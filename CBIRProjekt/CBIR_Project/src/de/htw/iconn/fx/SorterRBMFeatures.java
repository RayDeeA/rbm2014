package de.htw.iconn.fx;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.ARBMFeature;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;


public class SorterRBMFeatures extends ASorter {

	private ARBMFeature rbmWrapper;
	
	public SorterRBMFeatures(Pic[] pics, ForkJoinPool pool, ARBMFeature rbmWrapper) {
		super(pics, pool);
		this.rbmWrapper = rbmWrapper;
	}

	///////////////////////////////////////////
	// visualize the feature data as image
	//
	 @Override
	 protected BufferedImage getFeatureImage(Pic image) {

	  double[] fv = image.getFeatureVector();
	  int height = 256, width = fv.length;
	  int [] pixels = new int [width * height];
	  BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	  
	  for (int y = 0; y <  height; y++) {
	   for (int x = 0; x < width; x++) {
	    int i = y * width + x;
	    double val = fv[x]*height;
	    pixels[i] = (val > height-1 - y) ? 0xFFFFFFFF : 0xFF777777;
	   }    
	  }

	  bi.setRGB(0, 0, width, height, pixels, 0, width);
	  return bi;
	 }

	@Override
	protected double[] getFeatureVector(Pic image) {
		return rbmWrapper.getHidden(image);
	}

	@Override
	public double getDistance(double[] fv1, double[] fv2) {
		return getL1Dist(fv1, fv2);
	}

	@Override
	public String getName() {
		return "SorterRBMWrapper";
	}
}
