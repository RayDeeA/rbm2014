package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class LinearInterpolatedMatrixFunction extends MatrixFunctions implements
		ILogistic {

	// Interpolated between 0 and 1

	public DoubleMatrix function(DoubleMatrix m) {

		double[][] duplicateM = m.dup().toArray2();
		double max = m.max();
		double min = m.min();

		double maxMinusMin = max - min;
		for (int y = 0; y < duplicateM.length; y++) {
			for (int x = 0; x < duplicateM[y].length; x++) {
				duplicateM[y][x] = (duplicateM[y][x] - min) / maxMinusMin;
			}
		}
		return new DoubleMatrix(duplicateM);
	}

}