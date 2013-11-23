package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;

public class SquareRootLogistic implements ILogistic {

	@Override
	public DoubleMatrix function(DoubleMatrix m) {
		
		DoubleMatrix md = m.dup();
		double[] data = md.getData();
		double x;
		for (int i = 0; i < data.length; i++) {
			x = data[i] * 0.5;
			data[i] = x/Math.sqrt(x*x +1) * 0.5 + 0.5;
		}
		return md;
	}

}
