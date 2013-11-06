package de.htw.iconn.main;

public class Rectifier implements IFloatFunction {

    /** 
     * Rectifier Function
     * @link http://en.wikipedia.org/wiki/Rectifier_(neural_networks)
     * @param min double to max double 
     * @return double value between 0 and +ininity
     */
	
	@Override
	public double function(double x) {
		return (double) Math.log(1 + Math.exp(x));
	}

}
 