package de.htw.iconn.fx.decomposition.logistic;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class LinearClippedMatrixFunction extends MatrixFunctions implements
		ILogistic {

	// min(max(0, x), 1)

	public DoubleMatrix function(DoubleMatrix m) {
		double[][] duplicateM = m.dup().mul(0.125).add(0.5).toArray2();
		for (int y = 0; y < duplicateM.length; y++) {
			for (int x = 0; x < duplicateM[y].length; x++) {
				if (duplicateM[y][x] < 0)
					duplicateM[y][x] = 0;
				
				if (duplicateM[y][x] > 1)
					duplicateM[y][x] = 1;
			}
		}
		return new DoubleMatrix(duplicateM);
	}

}
