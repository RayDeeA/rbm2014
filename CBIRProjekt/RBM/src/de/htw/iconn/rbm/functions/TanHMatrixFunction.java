package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class TanHMatrixFunction extends MatrixFunctions implements ILogistic {
	
	// tanh(x)
	
	public  DoubleMatrix function(DoubleMatrix m) {
		
		final DoubleMatrix tanhM = MatrixFunctions.tanh(m);
		 
		return tanhM;
	}
	
}