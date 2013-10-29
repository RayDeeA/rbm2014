package de.htw.interpolation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Locale;

import de.htw.color.ColorMetric;
import de.htw.color.DistanceInterface;

public class RGBInterpolation implements DistanceInterface {

	// RGB Koordinaten eines 3D Würfels
	private double[][][][] values; 
	private int steps;
	private int delta;
	
	public RGBInterpolation(int size) {
		this.steps = size;
		this.delta = 256/size;
		this.values = new double[size][size][size][3];
		
		initialise();
	}
	
	public RGBInterpolation(int size, double[][][][] vals) {
		this.steps = size;
		this.delta = 256/size;
		this.values = vals;
	}
	
	
	
	public int getSteps() {
		return steps;
	}

	public int getDelta() {
		return delta;
	}

	public double[] getValueAt(int rIndex, int gIndex, int bIndex) {
		return values[rIndex][gIndex][bIndex];
	}
	
	public double[][][][] getValues() {
		return values;
	}
	
	public double[][][][] getValuesCopy() {
	    double[][][][] target = new double[steps][steps][steps][3];
	    for (int z = 0; z < steps; z++) {
		    for (int y = 0; y < steps; y++) {
		    	for (int x = 0; x < steps; x++) {
		    		target[z][y][x][0] = values[z][y][x][0];
		    		target[z][y][x][1] = values[z][y][x][1];
		    		target[z][y][x][2] = values[z][y][x][2];
		    	}
		    }
	    }
	    return target;
	}
	
	private void initialise() {
		int dh = delta / 2;
		
		// Distanzen vom aktuelle Punkt zum Ursprung
		// z.B. size = 4 ist delta 64 ersten Werte sind 32 -> 96 -> 160
		for (int rIndex = 0; rIndex < steps; rIndex++)
			for (int gIndex = 0; gIndex < steps; gIndex++)
				for (int bIndex = 0; bIndex < steps; bIndex++) {
					int r = dh + rIndex * delta;
					int g = dh + gIndex * delta;
					int b = dh + bIndex * delta;
					values[rIndex][gIndex][bIndex] = new double[] { r,g,b };
				}
					
	}
	

	public static RGBInterpolation load(Path file) {		
		try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
			Object list = ois.readObject();		
			double[][][][] v = (double[][][][]) list;
			int s = v.length; 
			return new RGBInterpolation(s, v);
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public void save(Path file) {
		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
			oos.writeObject(this.values);
		} catch (Exception e) { e.printStackTrace(); }		
	}
	
	@Override
	public double getDistanceBetween(double[] fv1, double[] fv2) {
		//  Interpoliere anhand der Positionen im RGB Raum, 
		// die Entfernung zur Z,Y,X Achse im Distance Raum
		double p1[] = interpolateAt(fv1[0], fv1[1], fv1[2]);
		double p2[] = interpolateAt(fv2[0], fv2[1], fv2[2]);

		return ColorMetric.getL2Distance(p1, p2);
	}

	/**
	 * Die übergebenen Parameter entsprechen Positionen im RGB Raum.
	 * Jene Indizies können direkt übernommen werden und in values[][][][3]
	 * verwendet werden. Diese enthalten jeweils 3 Koordinaten (Z,Y,X)
	 * 	
	 * @param rIndex
	 * @param gIndex
	 * @param bIndex
	 * @return
	 */
	private double[] interpolateAt(double rIndex, double gIndex, double bIndex) {
		int r1L = (int)((rIndex-delta/2)/delta); 
		if (r1L == steps-1) r1L--;
		double h1 = (rIndex-delta/2)/(double)delta - r1L;

		int g1L = (int)((gIndex-delta/2)/delta);
		if (g1L == steps-1) g1L--;
		double v1 = (gIndex-delta/2)/(double)delta - g1L;

		int b1L = (int)((bIndex-delta/2)/delta);
		if (b1L == steps-1) b1L--;
		double t1 = (bIndex-delta/2)/(double)delta - b1L;
		
		double[] val = new double[3];
		double[] h_1 = {1-h1, h1};
		double[] v_1 = {1-v1, v1};
		double[] t_1 = {1-t1, t1};

		for (int r1 = 0; r1 <= 1; r1++) {
			for (int g1 = 0; g1 <= 1; g1++) {
				for (int b1 = 0; b1 <= 1; b1++) {
					val[0] += h_1[r1] * v_1[g1] * t_1[b1] * values[r1L+r1][g1L+g1][b1L+b1][0];
					val[1] += h_1[r1] * v_1[g1] * t_1[b1] * values[r1L+r1][g1L+g1][b1L+b1][1];
					val[2] += h_1[r1] * v_1[g1] * t_1[b1] * values[r1L+r1][g1L+g1][b1L+b1][2];
				}
			}
		}

		return val;
	}
	

	public void printRGBValues() {
		Locale.setDefault(Locale.US);
		DecimalFormat df = new DecimalFormat("#0.00");
		
		double[] highest = new double[3];
	    for (int z = 0; z < steps; z++) {
		    for (int y = 0; y < steps; y++) {
		    	for (int x = 0; x < steps; x++) {
		    		double[] rgb = values[z][y][x];
		    		if(highest[0] < rgb[0]) highest[0] = rgb[0];
		    		if(highest[1] < rgb[1]) highest[1] = rgb[1];
		    		if(highest[2] < rgb[2]) highest[2] = rgb[2];
		    	}
		    }
	    }

	    for (int z = 0; z < steps; z++) {
		    for (int y = 0; y < steps; y++) {
		    	for (int x = 0; x < steps; x++) {
		    		double[] rgb = values[z][y][x];
		    		double r = Math.min(255, rgb[0] / highest[0] * 255);
		    		double g = Math.min(255, rgb[1] / highest[1] * 255);
		    		double b = Math.min(255, rgb[2] / highest[2] * 255);
		    		System.out.println(df.format(r)+" "+df.format(g)+" "+df.format(b));
		    	}
		    }
	    }
	}
}
