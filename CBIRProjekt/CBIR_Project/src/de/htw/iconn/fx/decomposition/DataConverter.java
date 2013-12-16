package de.htw.iconn.fx.decomposition;

import java.awt.image.BufferedImage;

import de.htw.iconn.fx.decomposition.tools.ImageScaler;
import de.htw.iconn.fx.decomposition.tools.Pic;

public class DataConverter {

	public static double[][] generatePixelIntensityData(Pic[] pics, int edgeLength) {
		
		double[][] data = new double[pics.length][edgeLength * edgeLength];
		
		for (int i = 0; i < pics.length; i++) {
			ImageScaler imageScaler = new ImageScaler(pics[i].getDisplayImage());
			BufferedImage scaledImage = imageScaler.scale(edgeLength);
			int[] pixels = scaledImage.getRGB(0,0,edgeLength,edgeLength,null,0,edgeLength);
			
			for (int p = 0; p < pixels.length; p++) {
				int argb = pixels[p];

				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = (argb) & 0xFF;
				
				double intensity = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0f;
				
				data[i][p] = intensity;
			}
			
		}
		
		return data;
	}
	
}
