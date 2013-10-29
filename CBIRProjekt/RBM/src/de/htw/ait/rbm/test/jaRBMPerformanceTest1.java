package de.htw.ait.rbm.test;
import java.util.Random;

import com.syvys.jaRBM.RBM;
import com.syvys.jaRBM.RBMImpl;
import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LinearLayer;
import com.syvys.jaRBM.Layers.StochasticBinaryLayer;
import com.syvys.jaRBM.RBMLearn.CDStochasticRBMLearner;

import de.htw.ait.rbm.Matrix;

/**
 * http://jarbm.cvs.sourceforge.net/viewvc/jarbm/RBM/src/com/syvys/jaRBM/Main.java?view=markup
 * 
 * @author Neiko
 *
 */
public class jaRBMPerformanceTest1 {

	static int max_epochs = 1000;
	
	static int num_visible = 10;
	static int num_hidden = 3;
	
	static double learningRate = 0.1;
	static double momentum = 0.6;

	public static void main(String[] args) {
		
		long nanosec = System.nanoTime();
		
		String[] names = new String[] {"cold", "ice", "warm", "sun", "hot", "winter", "water", "ocean", "fish", "weather"};
		// cold, ice, warm, sun, hot, winter, water, ocean, fish, weather
		//   1,   2,   3,    4,   5,    6,     7,      8,     9,   10
		
		double[][] trainingData = new double[][] {
			{1,1,0,0,0,0,0,0,0,0}, // cold, ice
			{0,1,0,0,0,1,0,0,0,0}, 
			{1,0,0,0,0,0,0,0,0,1}, 
			{0,0,0,0,0,1,0,0,0,1}, 
			{0,1,0,0,0,1,0,0,0,1},
			
			{0,0,1,0,0,0,0,0,0,1}, // warm
			{0,0,1,1,0,0,0,0,0,1},
			{0,0,1,0,1,0,0,0,0,0},
			{0,0,0,1,1,0,0,0,0,0},
			
			{0,1,0,0,0,0,1,0,0,0}, // wasser
			{0,0,0,0,0,0,1,0,1,0},
			{0,0,0,0,0,0,0,1,1,0},
			{0,0,0,0,0,0,1,1,1,0},
			{0,0,0,1,0,0,1,1,0,0},
		};

		com.syvys.jaRBM.Math.Matrix.FORCE_SINGLE_THREADED_MULTIPLICATION  = false;
		
		Layer visibleLayer = new LinearLayer(num_visible);
		Layer hiddenLayer = new StochasticBinaryLayer(num_hidden);
		
		RBMImpl rbm = new RBMImpl(visibleLayer, hiddenLayer, learningRate, 1, momentum);
//		rbm.setLearningRate(learningRate);
//		rbm.setMomentum(momentum);
		
		System.out.println("recalling before learning: ");
		System.out.println("visible activities: ");
		//com.syvys.jaRBM.Math.Matrix.printMatrix(rbm1.getVisibleActivitiesFromHiddenData(rbm1.getHiddenActivitiesFromVisibleData(trainingData)));
		
		
		// zuf�llige Gewichte
		Random rnd = new Random();
		double[][] weightUpdates = new double[visibleLayer.getNumUnits()][hiddenLayer.getNumUnits()];
        for (int i = 0; i < weightUpdates.length; i++) 
            for (int j = 0; j < weightUpdates[i].length; j++) 
            	weightUpdates[i][j] = rnd.nextGaussian();
		CDStochasticRBMLearner.updateWeights(rbm, weightUpdates);
		
		// trainieren
		for (int epochs = 0; epochs < max_epochs; epochs++) {
			for (double[] ds : trainingData) {
				CDStochasticRBMLearner.Learn(rbm, ds);
			}
	        System.out.println("Epoch " + epochs + ": " + error(rbm, trainingData));
		}
		
		// ------------------------------------------------------------------------------------------
		// ------------------------------ Test gegen anderes Beispiel -------------------------------
		// ------------------------------------------------------------------------------------------	
		double[][] test = new double[][]{{0,0,0,1,0,0,1,1,0,0}}; //wasser
		double[][] hidden_data = rbm.getHiddenActivitiesFromVisibleData(test);
		double[][] reconstructed_data = rbm.getVisibleActivitiesFromHiddenData(hidden_data);
		Matrix.mprint(hidden_data);
		
		double[][] test1 = new double[][]{{1,1,0,0,0,0,0,0,0,0}}; // kalt
		double[][] hidden_data1 = rbm.getHiddenActivitiesFromVisibleData(test1);
		double[][] reconstructed_data1 = rbm.getVisibleActivitiesFromHiddenData(hidden_data1);
		Matrix.mprint(hidden_data1);
		
		double[][] test2 = new double[][]{{0,0,1,0,1,0,0,0,0,0}}; // warm
		double[][] hidden_data2 = rbm.getHiddenActivitiesFromVisibleData(test2);
		double[][] reconstructed_data2 = rbm.getVisibleActivitiesFromHiddenData(hidden_data2);
		Matrix.mprint(hidden_data2);
		
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		System.out.println("\n\n");
		System.out.println("Finde die passende Gruppe zu Kalt");
		double[][] cold = new double[][]{{1,0,0,0,0,0,0,0,0,0}};
		double[][] hidden_data_cold = rbm.getHiddenActivitiesFromVisibleData(cold);
		Matrix.mprint(hidden_data_cold);
		extractMaximum(hidden_data_cold);
		
		System.out.println("Gruppe:");
		Matrix.mprint(hidden_data_cold);
		double[][] reconstructed_data_cold = rbm.getVisibleActivitiesFromHiddenData(hidden_data_cold);
		
		Matrix.mprint(reconstructed_data_cold);
		System.out.println("Folge werte verwenden:");
		for (int i = 0; i < reconstructed_data_cold[0].length; i++) {
			if(reconstructed_data_cold[0][i] > 0.5)
				System.out.print(names[i]+",");
		}
		System.out.println();
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
	}

	public static double error(RBM rbm, double[][] trainingData) 
	{
		double[][] hiddenActivities = rbm.getHiddenActivitiesFromVisibleData(trainingData);
	    double[][] negPhaseVisible = rbm.getVisibleActivitiesFromHiddenData(hiddenActivities);
	    return com.syvys.jaRBM.Math.Matrix.getMeanSquaredError(trainingData, negPhaseVisible);
	}
	
	private static void extractMaximum(double[][] data)
	{
		double max = 0; 
		int index = 0;
		for (double[] ds : data) {
			for (int i = 0; i < ds.length; i++) {
				if(max < ds[i])
				{
					max = ds[i];
					index = i;
				}
			}
			
			for (int i = 0; i < ds.length; i++) {
				ds[i] = 0;
			}
			ds[index] = 1;
		}
	}
}
