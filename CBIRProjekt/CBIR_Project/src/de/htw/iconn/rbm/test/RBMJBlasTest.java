package de.htw.iconn.rbm.test;

import de.htw.iconn.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.RBMJBlasOpti;
import de.htw.iconn.rbm.StoppingCondition;
import junit.framework.TestCase;
import org.jblas.FloatMatrix;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author Moritz
 */


public class RBMJBlasTest extends TestCase{

    public RBMJBlasTest(String testName) {
        super(testName);
    }  
    public RBMJBlasTest(){}
    
	public void test() {
		final float delta = 0.01f;
		
		final int numVisible = 3;
		final int numHidden = 2;
		final int epochs = 1;
		 
		/*final double[][] trainingData = new double[][]{
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
		};*/
		float[][] trainingData = new float[][]
                {{ 0, 0, 0},{0.5f, 0.5f, 0.5f},{ 1, 1, 1}};
		
                final float learningRate = 0.1f;
		final FloatMatrix initialWeights = FloatMatrix.rand(numVisible, numHidden);
		final int seed = 12345;
		RBMJBlas rbmJ = new RBMJBlas(numHidden, numVisible, learningRate, new DefaultLogisticMatrixFunction(), true, seed, trainingData);
                RBMJBlasOpti rbmO = new RBMJBlasOpti(numHidden, numVisible, learningRate, new DefaultLogisticMatrixFunction(), true, seed, trainingData);
		rbmO.train(trainingData, new StoppingCondition(epochs), false, false);
		rbmJ.train(trainingData, new StoppingCondition(epochs),  false,  false);
		
		assert2D(rbmO.getWeights(), rbmJ.getWeights(), delta);
		
		
	}
	
	private void assert2D(float[][] expecteds, float[][] actuals, float delta) {		
		for (int i = 0; i < actuals.length; i++) {
			assertArrayEquals(expecteds[i], actuals[i], delta);
			
		}
	}

}