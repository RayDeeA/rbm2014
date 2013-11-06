package de.htw.iconn.main;

public class DefaultGauss implements IFloatFunction {

	public double function(double x, double sigma) {
	    
	    // 1/(sqrt(2 * pi * sigma))
		double OneDivSqrt2PISigma = (float) (1.0f / (Math.sqrt(2 * Math.PI) * sigma));
	    
	    // 2 * sigma^2
		double TwoTimes2SigmaPow2 = (float) (2 * sigma * sigma);
	    
	    // x^2
		double distancePow2 = x * x;
		 
	    // 1/(sqrt(2 * pi * sigma)) * e ^ (-(x)^2/(2 * sigma^2))
	    return OneDivSqrt2PISigma * Math.exp((float)(-(distancePow2))/TwoTimes2SigmaPow2);
	}

	@Override
	public double function(double x) {
		return function(x, 1.0f);
	}

}
