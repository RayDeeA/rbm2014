package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class RectifierMatrixFunction extends MatrixFunctions implements ILogistic {

	// log(1 + e^x);
	
	public  DoubleMatrix function(DoubleMatrix m) {
		
		final DoubleMatrix ExpM = MatrixFunctions.exp(m);
		final DoubleMatrix ExpPlus1M = ExpM.add(1.0);
		final DoubleMatrix LogExpPlusOneM = MatrixFunctions.log10(ExpPlus1M); 
		
		return LogExpPlusOneM;
	}
}