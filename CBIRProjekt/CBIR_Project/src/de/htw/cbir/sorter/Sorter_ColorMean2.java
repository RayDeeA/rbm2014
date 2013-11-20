package de.htw.cbir.sorter;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.model.Settings.SettingOption;

public class Sorter_ColorMean2 extends Sorter
{

	public Sorter_ColorMean2(Pic[] pics, Settings settings, ForkJoinPool pool) {
		super(pics, settings, pool);
	}

	@Override
	public double[] getFeatureVector(Pic image)  
	{
		BufferedImage bi = image.getDisplayImage();

		int width  = bi.getWidth();
		int height = bi.getHeight();

		int [] rgbValues = new int[width * height];

		bi.getRGB(0, 0, width, height, rgbValues, 0, width);

		double[] featureVector = new double[3];

		// loop over the block
		int r = 0; int g = 0; int b = 0; int sum = 0;

		for(int y=0; y < height; y++) 
		{
			for (int x=0 ; x<width ; x++) 
			{
				int pos = y*width + x;
				r +=  (rgbValues[pos] >> 16) & 255;
				g +=  (rgbValues[pos] >>  8) & 255;
				b +=  (rgbValues[pos]      ) & 255;

				sum++;
			}	
		}
				
		r /= sum;
		g /= sum;
		b /= sum;				
				
		double lum = (r+g+b)/3; 
		featureVector[0] = lum; 
		featureVector[1] = settings.getLuminance()*(lum-b);
		featureVector[2] = settings.getLuminance()*(lum-r);
				
		return featureVector;
	}


	//////////////////////////////////////////////////////////////////////////////
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
	public double getDistance(double[] fv1, double[] fv2) {
		return getL1Dist(fv1, fv2);
	}

	@Override
	public String getName() {
		return "ColorMean2 (Sat: "+settings.getLuminance()+")";
	}

	@Override
	public void settingsChanged(ActionEvent e) {
		SettingOption option = Settings.SettingOption.valueOf(e.getActionCommand());
		if(option == Settings.SettingOption.LUMINANCE)
			getFeatureVectors();
	}
}
