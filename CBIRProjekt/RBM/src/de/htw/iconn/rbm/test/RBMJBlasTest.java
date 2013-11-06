package de.htw.iconn.rbm.test;

import static org.junit.Assert.*;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.RBMOriginal;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;

import org.jblas.DoubleMatrix;
import org.junit.Test;

public class RBMJBlasTest {

	@Test
	public void test() {
		final double delta = 0.01;
		
		final int numVisible = 5;
		final int numHidden = 2;
		final int epochs = 1;
		 
		final double[][] trainingData = new double[][]{
				// Alice: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 1, 1, 1, 0, 0 },
				// Bob: (Harry Potter = 1, Avatar = 0, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). SF/fantasy fan, but doesn't like Avatar.
				{ 1, 0, 1, 0, 0 },
				// Carol: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 1, 1, 1, 0, 0 },
				// David: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
				{ 0, 0, 0, 1, 1 },
				// Eric: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 0, Glitter = 0). Oscar winners fan, except for Titanic.
				{ 0, 0, 0, 1, 0 },
				// Fred: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
				{ 0, 0, 0, 1, 1 },
		};
		
		final double learningRate = 0.1;
		final DoubleMatrix initialWeights = DoubleMatrix.rand(numVisible, numHidden);
		
		RBMOriginal rbm = new RBMOriginal(numVisible, numHidden, learningRate, initialWeights.dup().toArray2());
		RBMJBlas rbmJ = new RBMJBlas(numVisible, numHidden, learningRate, initialWeights.dup().toArray2(), new DefaultLogisticMatrixFunction());
		
		rbm.train(trainingData, 1);
		rbmJ.train(trainingData, epochs);
		
		assert2D(rbmJ.getWeightsWithBIAS(), rbm.getWeights(), delta);
		
		
	}
	
	private void assert2D(double[][] expecteds, double[][] actuals, double delta) {		
		for (int i = 0; i < actuals.length; i++) {
			assertArrayEquals(expecteds[i], actuals[i], delta);
			
		}
	}

}
