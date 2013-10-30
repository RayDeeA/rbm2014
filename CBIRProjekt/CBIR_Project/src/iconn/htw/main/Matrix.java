package iconn.htw.main;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Matrix {

	private final float[][] coeff;

	public Matrix(final float[][] coeff) {
		this.coeff = coeff;
	}
	
	static Matrix createRandomMatrix(final int visible, final int hidden, final IRandomFunction random) {
		final float[][] coeff = new float[visible][hidden];
		
		for(int i = 0; i < visible; i++)
			for(int j = 0; j < hidden; j++) {
			coeff[i][j] = random.nextNumber();
		}
		return new Matrix(coeff);
	}
	
	
	public Matrix add(final Matrix m) {
		if(coeff.length != m.coeff.length || coeff[0].length == m.coeff.length) throw new IllegalArgumentException();
		
		final float[][] result = new float[coeff.length][coeff[0].length];
		
		for(int i = 0; i < result.length; i++)
			for(int j = 0; j < result[0].length; j++)
				result[i][j] = coeff[i][j] + m.coeff[i][j];
			
		return new Matrix(result);
	}
	
	public Matrix subt(final Matrix m) {
		if(coeff.length != m.coeff.length || coeff[0].length == m.coeff.length) throw new IllegalArgumentException();
		
		final float[][] result = new float[coeff.length][coeff[0].length];
		
		for(int i = 0; i < result.length; i++)
			for(int j = 0; j < result[0].length; j++)
				result[i][j] = coeff[i][j] - m.coeff[i][j];
			
		return new Matrix(result);
	}
	
	public Matrix applyFloatFunction(final IFloatFunction func) {
		final float[][] result = new float[coeff.length][coeff[0].length];
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = func.function(coeff[i][j]);
			}
		}
		return new Matrix(result);
	}
	
	public Matrix mult(final float x) {
		final float[][] result = new float[coeff.length][coeff[0].length];
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = x * result[i][j];
			}
		}
		return new Matrix(result);
	}
	
	
	public Matrix mult(final Matrix m) {
		if(coeff[0].length != m.coeff.length) throw new IllegalArgumentException();
		
		final float[][] mCoeff = m.coeff;
		final float[][] result = new float[coeff.length][mCoeff[0].length];
		
		for(int i=0; i < coeff.length; i++)   {                     
			for(int j=0; j < result[0].length; j++) {                
				for(int k=0; k < mCoeff.length; k++) {    
					result[i][j] += coeff[i][k] * mCoeff[k][j];
				}
			}
		}
		
		return new Matrix(result);
	}
	
	public Matrix trans() {
		final float[][] result = new float[coeff[0].length][coeff.length];
		
		for(int i=0; i < coeff.length; i++)
			for(int j=0; j < coeff[0].length; j++)
				result[j][i] = coeff[i][j];
		
		return new Matrix(result);
	}
	
	public Vector mult(final Vector v) {
		if(coeff.length != v.getLength()) throw new IllegalArgumentException();	
		
		final float[] result = new float[v.getLength()];
		
		for(int i = 0; i < coeff[0].length; i++) {
			
		}
			
		return new Vector(result);
		
	}

	
	public Matrix isGreaterThan(final Matrix m) {
		final float[][] result = new float[coeff.length][coeff[0].length];

		final float[][] mCoeff = m.coeff;
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = (coeff[i][j] > mCoeff[i][j]) ? 1.0f : 0.0f;
			}
		}
		
		return new Matrix(result);
	}
	
	public float[][] getCoefficients() {
		final float[][] result = new float[coeff.length][coeff[0].length];
		
		for(int i = 0; i < coeff.length; i++) {
			result[i] = coeff[i].clone();
		}
		return result;
	}
	
	public Matrix expandfirstRowWithZeros() {
		final float[][] result = new float[coeff.length + 1][coeff[0].length];
		
		for (int i = 1; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) { 
				result[i][j] = coeff[i - 1][j];
			}
		}
		
		return new Matrix(result);
	}
	
	public Matrix expandfirstColumnWithZeros() {
		final float[][] result = new float[coeff.length][coeff[0].length + 1];
		

		for (int i = 0; i < result.length; i++) {
			for (int j = 1; j < result[0].length; j++) { 
				result[i][j] = coeff[i][j - 1];
			}
		}
		
		return new Matrix(result);
	}
	
	public Matrix expandfirstColumnWithOnes() {
		final float[][] result = new float[coeff.length][coeff[0].length + 1];
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) { 
				result[i][j] = (j == 0) ? 1 : coeff[i][j - 1];
			}
		}
		
		return new Matrix(result);
	}
	
	public int getHeight() {
		return coeff.length;
	}
	
	public int getWidth() {
		return coeff[0].length;
	}
	
	@Override
	public String toString() {
    	DecimalFormat f = new DecimalFormat("#0.000");

		String str = "";
		for (int i = 0; i < coeff.length; i++) {
			str += "[";
			for (int j = 0; j < coeff[0].length; j++) {
			
				str += f.format(coeff[i][j]);
				
				if(j != coeff[0].length - 1)
					str +="\t";
			}
			str += "]\n";
		}
		return str;
	}
}
