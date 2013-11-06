package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class LinearUnclippedMatrixFunction extends MatrixFunctions implements
		ILogistic {

	// linear and no clipping

	public DoubleMatrix function(DoubleMatrix m) {
		return m.dup();
	}

}