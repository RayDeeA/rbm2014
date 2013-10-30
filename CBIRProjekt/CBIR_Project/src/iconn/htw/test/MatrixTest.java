package iconn.htw.test;

import static org.junit.Assert.*;
import iconn.htw.main.Matrix;

import org.junit.Test;

public class MatrixTest {

	@Test
	public void mult1() {
		final Matrix a = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
		final Matrix b = new Matrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
		
		final Matrix c = a.mult(b);

		assertArrayEquals(c.getCoefficients()[0], new double[]{22, 28}, 0.01f);
		assertArrayEquals(c.getCoefficients()[1], new double[]{49, 64}, 0.01f);
	}@Test
	public void mult2() {
		final Matrix a = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
		final Matrix b = new Matrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
		
		final Matrix c = b.mult(a);

		assertArrayEquals(c.getCoefficients()[0], new double[]{9, 12, 15}, 0.01f);
		assertArrayEquals(c.getCoefficients()[1], new double[]{19, 26, 33}, 0.01f);
		assertArrayEquals(c.getCoefficients()[2], new double[]{29, 40, 51}, 0.01f);
	}

}
