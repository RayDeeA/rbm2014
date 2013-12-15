package de.htw.iconn.fx.decomposition.logistic;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class TanhOfXPow3MatrixFunction extends MatrixFunctions implements ILogistic {

	// tanh(x)

	public DoubleMatrix function(DoubleMatrix m) {

		final DoubleMatrix mPow3 = MatrixFunctions.pow(m, 3);
		final DoubleMatrix tanhMPow3 = MatrixFunctions.tanh(mPow3);
		final DoubleMatrix tanhM = MatrixFunctions.tanh(tanhMPow3).add(1).div(2);

		return tanhM;
	}

}