package de.htw.iconn.rbm.functions.tests;

import static org.junit.Assert.assertArrayEquals;

import org.jblas.DoubleMatrix;
import org.junit.Test;

import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;

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

		// TanHMatrixFunction
		TanHMatrixFunction tanHMatrixFunction = new TanHMatrixFunction();
		assert2D(new double[][] { { -1 } }, tanHMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { -.462 } }, tanHMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { 0 } }, tanHMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .462 } }, tanHMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 1} }, tanHMatrixFunction.function(matPosTen).toArray2(), delta);

		// GaussMatrixFunction
		GaussMatrixFunction gaussMatrixFunction = new GaussMatrixFunction();
		assert2D(new double[][] { { 0 } }, gaussMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { .778 } }, gaussMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { 1 } }, gaussMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .778 } }, gaussMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 0} }, gaussMatrixFunction.function(matPosTen).toArray2(), delta);

		// LinearClippedMatrixFunction
		LinearClippedMatrixFunction linearClippedMatrixFunction = new LinearClippedMatrixFunction();
		assert2D(new double[][] { { -1 } }, linearClippedMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { -.5 } }, linearClippedMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { 0 } }, linearClippedMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .5} }, linearClippedMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 1} }, linearClippedMatrixFunction.function(matPosTen).toArray2(), delta);

		// LinearUnclippedMatrixFunction
		LinearUnclippedMatrixFunction linearUnclippedMatrixFunction = new LinearUnclippedMatrixFunction();
		assert2D(new double[][] { { -10 } }, linearUnclippedMatrixFunction.function(matNegTen).toArray2(), delta);
		assert2D(new double[][] { { -.5 } }, linearUnclippedMatrixFunction.function(matNegPointFive).toArray2(), delta);
		assert2D(new double[][] { { 0 } }, linearUnclippedMatrixFunction.function(matZero).toArray2(), delta);
		assert2D(new double[][] { { .5} }, linearUnclippedMatrixFunction.function(matPosPointFive).toArray2(), delta);
		assert2D(new double[][] { { 10} }, linearUnclippedMatrixFunction.function(matPosTen).toArray2(), delta);
				
	}

	private void assert2D(double[][] expecteds, double[][] actuals, double delta) {
		for (int i = 0; i < actuals.length; i++) {
			assertArrayEquals(expecteds[i], actuals[i], delta);
		}
	}
}
