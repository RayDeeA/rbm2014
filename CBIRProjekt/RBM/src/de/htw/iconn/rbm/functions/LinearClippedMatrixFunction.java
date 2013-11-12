package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class LinearClippedMatrixFunction extends MatrixFunctions implements
		ILogistic {

	// linear but clipped (0 .. 1)

	public DoubleMatrix function(DoubleMatrix m) {
		double[][] duplicateM = m.dup().mul(0.5).add(0.5).toArray2();
		for (int y = 0; y < duplicateM.length; y++) {
			for (int x = 0; x < duplicateM[y].length; x++) {
				if (duplicateM[y][x] < -1)
					duplicateM[y][x] = -1;
				
				if (duplicateM[y][x] > 1)
					duplicateM[y][x] = 1;
			}
		}
		return new DoubleMatrix(duplicateM);
	}

}