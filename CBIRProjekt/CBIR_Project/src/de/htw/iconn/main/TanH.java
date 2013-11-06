package de.htw.iconn.main;

public class TanH implements IFloatFunction {

	/**
	 * TanH Function
	 * 
	 * @link http://fooplot.com/#W3sidHlwZSI6MCwiZXEiOiJ0YW5oKHgpIiwiY29sb3IiOiIjMDMwM0ZGIn0seyJ0eXBlIjoxMDAwLCJzaXplIjpbNjQ5LDM5OV19XQ--
	 * @param min double to max double
	 * @return double value between -1 and 1
	 */

	@Override
	public double function(double x) {
		return (double) Math.tanh(x);
	}
 
}
