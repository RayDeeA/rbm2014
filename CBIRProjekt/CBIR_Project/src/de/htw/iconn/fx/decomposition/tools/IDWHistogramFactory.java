package de.htw.iconn.fx.decomposition.tools;

import de.htw.iconn.fx.decomposition.tools.ColorConverter.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IDWHistogramFactory {

	private ColorSpace colorSpace;
	private double[][] histogramDataPoints;
	private double[][] histogramRGBDataPoints;
	private double pixelDistancePower;
	private double histogramBinPower;
	
	public IDWHistogramFactory(ColorSpace colorSpace, double pixelDistancePower, double histogramBinPower) {
		this.colorSpace = colorSpace;
		this.pixelDistancePower = pixelDistancePower;
		this.histogramBinPower = histogramBinPower;
	}
	
	public double getPixelDistancePower() {
		return pixelDistancePower;
	}

	public double getHistogramBinPower() {
		return histogramBinPower;
	}
	
	public int getHistogramBinSize() {
		return histogramRGBDataPoints.length;
	}

	public IDWHistogramFactory(double[][] histogramDataPoints, ColorSpace colorSpace, double pixelDistancePower, double histogramBinPower) {
		this(colorSpace, pixelDistancePower, histogramBinPower);
		setHistogramDataPoints(histogramDataPoints);
	}
	
	public void setHistogramDataPoints(double[][] histogramDataPoints) {
		this.histogramRGBDataPoints = histogramDataPoints;
		this.histogramDataPoints = ColorConverter.convertRGBTo(histogramDataPoints, colorSpace);
	}
	
	public IDWHistogram analyseImage(BufferedImage bi) {
		IDWHistogram histo = new IDWHistogram(histogramDataPoints, pixelDistancePower, histogramBinPower, colorSpace);
		histo.analyseImage(bi);		
		return histo;		
	}
	
	public double[][] getRGBDataPoints() {
		return histogramRGBDataPoints;
	}
	
	public double getDistance(double[] featureVector1, double[] featureVector2) {
		return ColorMetric.getL1Distance(featureVector1, featureVector2);
	}

	public void save(Path path) {
		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
			oos.writeObject(this.histogramRGBDataPoints);
			oos.writeObject(this.colorSpace);
			oos.writeObject(this.pixelDistancePower);
			oos.writeObject(this.histogramBinPower);				
		} catch (Exception e) { e.printStackTrace(); }		
	}
	
	public static IDWHistogramFactory load(Path path) {
		try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {	
			double[][] hdp = (double[][]) ois.readObject();	
			ColorSpace cs = (ColorSpace) ois.readObject();	
			Double pdp = (Double) ois.readObject();
			Double hbp = (Double) ois.readObject(); 
			return new IDWHistogramFactory(hdp, cs, pdp, hbp);
		} catch (Exception e) { e.printStackTrace(); }
		return null;	
	}

	public void printDataPoints() {
		for (int i = 0; i < histogramRGBDataPoints.length; i++) {
			double[] s = histogramRGBDataPoints[i];
			System.out.println((int)s[0]+" "+(int)s[1]+" "+(int)s[2]);
		}
	}

	public void printINPPMFormat() {
		
		//P3
		//# The P3 means colors are in ASCII, then 3 columns and 2 rows,
		//# then 255 for max color, then RGB triplets
		//3 2
		//255
		//255   0   0     0 255   0     0   0 255
		//255 255   0   255 255 255     0   0   0
		
		System.out.println("P3");
		System.out.println("1 "+histogramRGBDataPoints.length);
		System.out.println("255");
		for (int i = 0; i < histogramRGBDataPoints.length; i++) {
			double[] s = histogramRGBDataPoints[i];
			System.out.println((int)s[0]+" "+(int)s[1]+" "+(int)s[2]);
		}
	}
}
