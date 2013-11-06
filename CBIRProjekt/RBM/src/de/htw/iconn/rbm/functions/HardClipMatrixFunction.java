package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class HardClipMatrixFunction extends MatrixFunctions implements
		ILogistic {

	// 

	public DoubleMatrix function(DoubleMatrix m) {
		double[][] duplicateM = m.dup().toArray2();
		for (int y = 0; y < duplicateM.length; y++) {
			for (int x = 0; x < duplicateM[y].length; x++) {
				if (duplicateM[y][x] < 0)
					duplicateM[y][x] = 0;
				else
					duplicateM[y][x] = 1;
			}
		}
		return new DoubleMatrix(duplicateM);
	}

}