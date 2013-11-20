package de.htw.cbir.sorter;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.DCTRBM;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;


public class Sorter_DCTRBM extends Sorter {

	private DCTRBM dctRGBM;
	
	public Sorter_DCTRBM(Pic[] pics, Settings settings, DCTRBM dctRBM, ForkJoinPool pool) {
		super(pics, settings, pool);
		this.dctRGBM = dctRBM;
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
		return dctRGBM.getHidden(image);
	}

	@Override
	public double getDistance(double[] fv1, double[] fv2) {
		return getL1Dist(fv1, fv2);
	}

	@Override
	public String getName() {
		return "DCTRBM";
	}

	@Override
	public void settingsChanged(ActionEvent e) {
	}
}
