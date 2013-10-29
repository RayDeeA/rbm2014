package de.htw.ait.rbm.test;
import java.util.Random;

import com.syvys.jaRBM.RBM;
import com.syvys.jaRBM.RBMImpl;
import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LinearLayer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import com.syvys.jaRBM.Math.Matrix;
import com.syvys.jaRBM.RBMLearn.CDStochasticRBMLearner;


public class jaRBMTest {

	static double learningRate = 0.1;
	static double momentum = 0.6;
	static int hiddenUnits = 2;
	static int MAX_EPOCHE = 1000;
	static double multiplicator = 4.;
	
	public static void main(String[] args) {
		testRBM();
		
		System.out.println("test");
		System.exit(1);
	}
	
	 public static void testRBM() {
	        
//	        double[] d1 = {0., 0, 0};
//	        double[] d2 = {-1., 0., 1};
//	        double[] d3 = {0., -1., 1};
//	        double[] d4 = {1., 1., 0};
	      	double[] d1 = {0., 0., 0};
	        double[] d2 = {1., 0., 1};
	        double[] d3 = {0., 1., 1};
	        double[] d4 = {1., 1., 0};
	        double[][] batchd = {d1, d2, d3, d4};
	        
	        //batchd = batchbinarydata;
	        batchd = Matrix.multiply(batchd, multiplicator);
	        
	        int visibleUnits = batchd[0].length;
	        
	        Layer visibleLayer = new LinearLayer(visibleUnits);
	        //Layer visibleLayer = new LogisticLayer(visibleUnits);
	        //Layer visibleLayer = new StochasticContinuousLayer(visibleUnits, 5, -5);
	        
	        
	        //Layer hiddenLayer = new LinearLayer(hiddenUnits);
	        //Layer hiddenLayer = new StochasticLinearLayer(hiddenUnits);
	        Layer hiddenLayer = new LogisticLayer(hiddenUnits);
//	        Layer hiddenLayer = new StochasticBinaryLayer(hiddenUnits);
	        //Layer hiddenLayer = new StochasticContinuousLayer(hiddenUnits, 1, -1);
	        
	        
	        RBMImpl rbm1 = new RBMImpl(visibleLayer, hiddenLayer);
	        rbm1.setLearningRate(learningRate);
	        rbm1.setMomentum(momentum);
	        //System.out.print("---------------asdfasdf"); Matrix.printArray(data1);
	        
			// zufällige Gewichte
			Random rnd = new Random();
			double[][] weightUpdates = new double[visibleLayer.getNumUnits()][hiddenLayer.getNumUnits()];
	        for (int i = 0; i < weightUpdates.length; i++) 
	            for (int j = 0; j < weightUpdates[i].length; j++) 
	            	weightUpdates[i][j] = rnd.nextGaussian();
			CDStochasticRBMLearner.updateWeights(rbm1, weightUpdates);
	        
	        System.out.println("recalling before learning: ");
	        System.out.println("visible activities: ");
	        Matrix.printMatrix(rbm1.getVisibleActivitiesFromHiddenData(rbm1.getHiddenActivitiesFromVisibleData(batchd)));

	        for (int epochs = 0; epochs < MAX_EPOCHE; epochs++) {
	            CDStochasticRBMLearner.Learn(rbm1, batchd[0]);
	            CDStochasticRBMLearner.Learn(rbm1, batchd[1]);
	            CDStochasticRBMLearner.Learn(rbm1, batchd[2]);
	            CDStochasticRBMLearner.Learn(rbm1, batchd[3]);
	            System.out.println("Epoch " + epochs + ": " + error(rbm1, batchd));
	        }
	        System.out.println("error: " + error(rbm1, batchd));

	        System.out.println("");
	        System.out.println("hidden activities: ");
	        Matrix.printMatrix(rbm1.getHiddenActivitiesFromVisibleData(batchd));
	        
	        System.out.println("recalling after learning: ");
	        System.out.println("visible activities: ");
	        Matrix.printMatrix(rbm1.getVisibleActivitiesFromHiddenData(rbm1.getHiddenActivitiesFromVisibleData(batchd)));
	        
	        System.out.println("\nDesired: ");
	        Matrix.printMatrix(batchd);
	        
	        
	    }
	    

	public static double error(RBM rbm, double[][] trainingData) 
	{
		double[][] hiddenActivities = rbm.getHiddenActivitiesFromVisibleData(trainingData);
	    double[][] negPhaseVisible = rbm.getVisibleActivitiesFromHiddenData(hiddenActivities);
	    return com.syvys.jaRBM.Math.Matrix.getMeanSquaredError(trainingData, negPhaseVisible);
	}
}
