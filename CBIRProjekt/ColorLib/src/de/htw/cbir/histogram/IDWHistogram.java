package de.htw.cbir.histogram;

import java.awt.image.BufferedImage;

import de.htw.color.ColorConverter;
import de.htw.color.ColorMetric;
import de.htw.color.ColorConverter.ColorSpace;

public class IDWHistogram {

	private double[][] histogramDataPoints;
	private double[] histogramBins;
	private double pixelDistancePower; //Default: 5.0
	private double histogramBinPower; 
	private ColorSpace colorSpace;
	
	public static double[][] getEmpiricalDataPoints() {
		double[][] dp = new double[][] {
				{38,38,38}, 
				{98,98,98},
				{36,157,39},
				{139,140,0},
				{217,98,0},
				{225,55,54},
				{159,38,156},
				{56,55,224},
				{0,140,141},
				{158,158,158},
				{96,217,99},
				{199,200,31},
				{255,158,37},
				{255,115,114},
				{219,98,216},
				{116,115,255},
				{30,200,201},
				{218,218,218}
			};
		return dp;
	}
	
	public IDWHistogram(double[][] histogramDataPoints, double pixelDistancePower, double histogramBinPower, ColorSpace colorSpace) {
		this.colorSpace = colorSpace;
		this.histogramDataPoints = histogramDataPoints;
		this.histogramBins = new double[histogramDataPoints.length];
		this.pixelDistancePower = pixelDistancePower;
		this.histogramBinPower = histogramBinPower;
	}
	
	public double[] getBins() {
		return histogramBins;
	}
	
	public void analyseImage(BufferedImage bi) {
		
		int width = bi.getWidth();
		int height = bi.getHeight();

		// besorge die Pixel des Bildes
		int[] rgbValues = new int[width * height];
		bi.getRGB(0, 0, width, height, rgbValues, 0, width);

		// Grenzen zum Bestimmen des Innenbereichs eines Bildes
		int w2 = width/2,  w4 = (int) (width*0.212), h2 = height/2, h4 = (int) (height*0.25); 
		
		// Summe der Gewichte (innere Bereiche des Bildes werden staerker gewichtet) 
		double sumW = 0;			
		
		// berechne das Histogram
		for (int y = 0; y < height; y++) {
			boolean isYcenter = Math.abs(y-h2) < h4;
			for (int x = 0; x < width; x++) {
				int pos = y * width + x;
				double weight = (float) ((Math.abs(x-w2) < w4 && isYcenter) ? 3.249f : 1);
				sumW += weight;
				analyseColor(rgbValues[pos], weight);
			}
		}
		
		// normalisiere noch die Werte.
		normalizeBins(sumW);
	}
	
	public void analyseColor(int rgb, double weight) {
		int r = (rgb >> 16) & 255;
		int g = (rgb >>  8) & 255;
		int b = (rgb      ) & 255;
		analyseColor(r, g, b, weight);
	}
	
	/**
	 * wi = 1/d(x, xi)p
	 * p = potenz
	 * d() = distance 
	 * 
	 * wi / summe_j(wi(xj))
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void analyseColor(double r, double g, double b, double rgbWeight) {
		
		double[] YCbCr = ColorConverter.rgb2AdvYCbCr(r, g, b);
		
		// berechne alle Werte
		double divisor = 0;
		double[] wi = new double[histogramDataPoints.length];
		for (int i = 0; i < histogramDataPoints.length; i++) {
			double[] hdp = histogramDataPoints[i];
			
			double dist = 0;
			switch (colorSpace) {
				case AdvYCbCr:
					// Distance in Kai's YCbCr Farbraum
					dist = ColorMetric.getL2Distance(YCbCr[0], YCbCr[1], YCbCr[2], hdp[0], hdp[1], hdp[2]);
					break;
				case RGB:
					// Distance im RGB Farbraum
					dist = ColorMetric.getL2Distance(r, g, b, hdp[0], hdp[1], hdp[2]);
					break;
				case Genetic:
					// Distance im RGB Farbraum
					dist = ColorMetric.getGeneticSolutionDistance(new double[] {r, g, b}, hdp);
					break;
				default:
					break;
			}
			
			if(dist == 0) {
				histogramBins[i]++;
				return;
			}
			
			wi[i] = 1 / Math.pow(dist, pixelDistancePower);
			divisor += wi[i];
		}
		
		// gebe jeden HistogramBin einen Anteil
		for (int i = 0; i < histogramDataPoints.length; i++) {
			double weight = wi[i] / divisor;
			histogramBins[i] += weight * rgbWeight;
		}
	}

	/**
	 * Histgramm normieren und potenzieren (damit kleinere Häufigkeiten verstärkt werden)
	 * 
	 * @param length
	 */
	public void normalizeBins(double length) {
		for (int i = 0; i < histogramBins.length; i++) {
			double val = histogramBins[i] / length;
			histogramBins[i] = Math.pow(val, histogramBinPower); //(0.65*Math.pow(val, 0.34))
		}
	}
}
