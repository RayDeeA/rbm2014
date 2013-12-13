package de.htw.iconn.fx.decomposition.logistic;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class TanHMatrixFunction extends MatrixFunctions implements ILogistic {
	
	// (tanh(x) + 1) / 2
	
	public  DoubleMatrix function(DoubleMatrix m) {

		//final DoubleMatrix tanhM = MatrixFunctions.tanh(m);
		final DoubleMatrix tanhM = MatrixFunctions.tanh(m).add(1).div(2);
		 
		return tanhM;
	}
	
}