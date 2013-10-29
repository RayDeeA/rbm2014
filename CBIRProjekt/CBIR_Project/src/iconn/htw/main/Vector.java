package iconn.htw.main;

public final class Vector {
	private final float[] coeff;

	public Vector(final float[] coeff) {
		super();
		this.coeff = coeff;
		if(coeff.length == 0) throw new IllegalArgumentException();
	}
	
	public static Vector createWithRandomNumbers(final int size, final IRandomFunction random, final float scale) {
		final float[] randomCoeff = new float[size];
		for(int i = 0; i < size; i++) {
			randomCoeff[i] = random.nextNumber() * scale;
		}
		return new Vector(randomCoeff);
	}
	
	public static Vector createWithRandomNumbers(final int size, final IRandomFunction random) {
		return createWithRandomNumbers(size, random, 1.0f);
	}
	
	public static Vector createZero(final int length) {
		return new Vector(new float[length]);
	}
	
	public float dot(final Vector v) {

		float result = 0;
		
		if(v.coeff.length == this.coeff.length) {
			for(int i = coeff.length -1; i >= 0; i--) {
				result += coeff[i] * v.coeff[i];
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		return result;
	}
	
	public Vector applyFloatFunction(final IFloatFunction func) {
		final float[] result =  new float[coeff.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = func.function(coeff[i]);
		}
		
		return new Vector(result);
	}
	
	public Vector add(final Vector v) {
		
		final float[] result = new float[coeff.length];
		
		if(v.coeff.length == this.coeff.length) {
			for(int i = coeff.length -1; i >= 0; i--) {
				result[i] = coeff[i] + v.coeff[i];
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		return new Vector(result);
	}
	
	public Vector subt(final Vector v) {
		
		final float[] result = new float[coeff.length];
		
		if(v.coeff.length == this.coeff.length) {
			for(int i = coeff.length -1; i >= 0; i--) {
				result[i] = coeff[i] - v.coeff[i];
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		return new Vector(result);
	}
	
	public Vector negate() {

		final float[] result = new float[coeff.length];
		
		for(int i = coeff.length -1; i >= 0; i--) {
			result[i] = -coeff[i];
		}

		return new Vector(result);
	}
	
	public Vector getZeroVector() {
		return new Vector(new float[coeff.length]);
	}
	
	public int getLength() {
		return coeff.length;
	}
	
	public float[] getCoefficients() {
		return coeff.clone();
	}
	
}
