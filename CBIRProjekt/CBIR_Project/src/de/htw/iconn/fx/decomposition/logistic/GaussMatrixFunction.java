package de.htw.iconn.fx.decomposition.logistic;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class GaussMatrixFunction extends MatrixFunctions implements ILogistic {
	
	// e^(-x^2)
	
    /**
     *
     * @param m
     * @return
     */
    	
        @Override
	public  DoubleMatrix function(DoubleMatrix m) {
		
		final DoubleMatrix mPow2 = MatrixFunctions.pow(m, 2.0);
		final DoubleMatrix negMPow2 = mPow2.neg();
		final DoubleMatrix ePowNegMPow2 = MatrixFunctions.exp(negMPow2);
		 
		return ePowNegMPow2;
		
	}
	
}