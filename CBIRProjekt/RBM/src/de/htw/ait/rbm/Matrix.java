package de.htw.ait.rbm;

import java.util.Locale;


public class Matrix {

	// multiply normal * normal
	public static double[][] multiply(double[][] m1, double[][] m2) {
		int m1rows = m1.length;
		int m1cols = m1[0].length;
		int m2rows = m2.length;
		int m2cols = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[][] result = new double[m1rows][m2cols];

		// multiply
		for (int i=0; i<m1rows; i++)
			for (int j=0; j<m2cols; j++)
				for (int k=0; k<m1cols; k++)
					result[i][j] += m1[i][k] * m2[k][j];

		return result;
	}
	
	public static double[] multiply(double[] m1, double[][] m2) {
		int m1cols = m1.length;
		int m2rows = m2.length;
		int m2cols = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[] result = new double[m2cols];

		// multiply
		for (int j=0; j<m2cols; j++)
			for (int k=0; k<m1cols; k++)
				result[j] += m1[k] * m2[k][j];

		return result;
	}
	
	// multiply transpose * normal
	public static double[][] multiplyTN(double[][] m1, double[][] m2) {
		int m1cols = m1.length;
		int m1rows = m1[0].length;
		int m2rows = m2.length;
		int m2cols = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[][] result = new double[m1rows][m2cols];

		// multiply
		for (int i=0; i<m1rows; i++)
			for (int j=0; j<m2cols; j++)
				for (int k=0; k<m1cols; k++)
					result[i][j] += m1[k][i] * m2[k][j];

		return result;
	}
	
	public static double[] multiplyTN(double[] m1, double[][] m2) {
		int m1cols = m1.length;
		int m2rows = m2.length;
		int m2cols = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[] result = new double[m2cols];

		// multiply
		for (int j=0; j<m2cols; j++)
			for (int k=0; k<m1cols; k++)
				result[j] += m1[k] * m2[k][j];

		return result;
	}
	
	/**
	 * m1 wird zum spaltenvektor 
	 * m2 bleibt zeilenvektor
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static double[][] multiplyTN(double[] m1, double[] m2) {
		int m1rows = m1.length;
		int m2cols = m2.length;
		double[][] result = new double[m1rows][m2cols];

		// multiply
		for (int r=0; r<m1rows; r++)
			for (int c=0; c<m2cols; c++)
				result[r][c] = m1[r] * m2[c];

		return result;
	}
	
	// multiply normal * transpose
	public static double[][] multiplyNT(double[][] m1, double[][] m2) {
		int m1rows = m1.length;
		int m1cols = m1[0].length;
		int m2cols = m2.length;
		int m2rows = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[][] result = new double[m1rows][m2cols];

		for (int i=0; i<m1rows; i++)
			for (int j=0; j<m2cols; j++)
				for (int k=0; k<m1cols; k++)
					result[i][j] += m1[i][k] * m2[j][k];

		return result;
	}
	
	// multiply normal * transpose
	public static double[] multiplyNT(double[] m1, double[][] m2) {

		int m1cols = m1.length;
		int m2cols = m2.length;
		int m2rows = m2[0].length;
		if (m1cols != m2rows)
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		double[] result = new double[m2cols];

			for (int j=0; j<m2cols; j++)
				for (int k=0; k<m1cols; k++)
					result[j] += m1[k] * m2[j][k];

		return result;
	}

	/** Matrix print.
	 */
	public static void mprint(double[][] a) {
		int rows = a.length;
		int cols = a[0].length;
		System.out.println("array["+rows+"]["+cols+"] = {");
		for (int i=0; i<rows; i++) {
			System.out.print("{");
			for (int j=0; j<cols; j++)
				System.out.printf(Locale.ENGLISH, " %8.6f,", a[i][j]);
			System.out.println("},");
		}
		System.out.println("};");
	}

	/** Matrix print.
	 */
	public static void mprint(double[] a) {
		int rows = a.length;
		System.out.print("array["+rows+"] = {");
			for (int j=0; j<rows; j++)
				System.out.printf(Locale.ENGLISH, " %8.6f,", a[j]);
		System.out.println("};");
	}
	public static void mprint1(double[][] a) {
		int rows = a.length;
		int cols = a[0].length;
		System.out.println("array["+rows+"]["+cols+"] = {");
		for (int i=0; i<rows; i++) {
			System.out.print("{");
			for (int j=0; j<cols; j++)
				System.out.printf(Locale.ENGLISH, " %d,", (int)Math.round(a[i][j]));
			System.out.println("},");
		}
		System.out.println("};");
	}
	public static void mprint1(double[] a) {
		int rows = a.length;
		System.out.println("array["+rows+"] = {");
		for (int i=0; i<rows; i++) {
			System.out.printf(Locale.ENGLISH, " %d,", (int)Math.round(a[i]));
		}
		System.out.println("};");
	}

	public static void main(String[] argv) {

		double x[][] = {
				{ 3, 2, 3 },
				{ 5, 9, 8 },
		};
		double y[][] = {
				{ 4, 7 },
				{ 9, 3 },
				{ 8, 1 },
		};
		double z[][] = Matrix.multiply(x, y);
		Matrix.mprint(x);
		Matrix.mprint(y);
		Matrix.mprint(z);

	}
}
