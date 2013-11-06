package de.htw.iconn.rbm.functions;

import static org.junit.Assert.*;

import org.jblas.DoubleMatrix;
import org.junit.Test;

public class MatrixFunctionTests {

	@Test
	public void test() {
		double delta = .01;

		DoubleMatrix matNegTen = new DoubleMatrix(new double[][] { { -10 } });
		DoubleMatrix matNegPointFive = new DoubleMatrix(new double[][] { { -.5 } });
		DoubleMatrix matZero = new DoubleMatrix(new double[][] { { 0 } });
		DoubleMatrix matPosPointFive = new DoubleMatrix(new double[][] { { .5 } });
		DoubleMatrix matPosTen = new DoubleMatrix(new double[][] { { 10 } });

		// DefaultLogisticMatrixFunction
		DefaultLogisticMatrixFunction defaultLogisticMatrixFunction = new DefaultLogisticMatrixFunction();
		assert2D(new double[][] { { 0 } }, defaultLogisticMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { .377 } }, defaultLogisticMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { .5 } }, defaultLogisticMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .622 } }, defaultLogisticMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 1 } }, defaultLogisticMatrixFunction.function(matPosTen).toArray2(), delta);
		
		// RectifierMatrixFunction
		RectifierMatrixFunction rectifierMatrixFunction = new RectifierMatrixFunction();
		assert2D(new double[][] { { 0 } }, rectifierMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { .205 } }, rectifierMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { .301 } }, rectifierMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .423 } }, rectifierMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 4.34 } }, rectifierMatrixFunction.function(matPosTen).toArray2(), delta);
		
		
		
		
	}

	private void assert2D(double[][] expecteds, double[][] actuals, double delta) {
		for (int i = 0; i < actuals.length; i++) {
			assertArrayEquals(expecteds[i], actuals[i], delta);
		}
	}
}
