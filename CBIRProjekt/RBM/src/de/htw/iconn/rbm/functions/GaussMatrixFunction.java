package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class GaussMatrixFunction extends MatrixFunctions implements ILogistic {
	
	// e^(-x^2)
	
	public  DoubleMatrix function(DoubleMatrix m) {
		
		final DoubleMatrix mPow2 = MatrixFunctions.pow(m, 2.0);
		final DoubleMatrix negMPow2 = mPow2.neg();
		final DoubleMatrix ePowNegMPow2 = MatrixFunctions.exp(negMPow2);
		 
		return ePowNegMPow2;
		
	}
	
}