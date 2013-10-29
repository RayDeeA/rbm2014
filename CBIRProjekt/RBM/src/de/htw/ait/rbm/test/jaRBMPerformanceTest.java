package de.htw.ait.rbm.test;
import com.syvys.jaRBM.RBMImpl;
import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LinearLayer;
import com.syvys.jaRBM.Layers.StochasticBinaryLayer;
import com.syvys.jaRBM.RBMLearn.CDStochasticRBMLearner;

/**
 * http://jarbm.cvs.sourceforge.net/viewvc/jarbm/RBM/src/com/syvys/jaRBM/Main.java?view=markup
 * 
 * @author Neiko
 *
 */
public class jaRBMPerformanceTest {

	static int num_hidden = 20;
	static int max_epochs = 200;
	
	static int num_samples = 10000;
	static int size_sample = 60;

	public static void main(String[] args) {
		
		long nanosec = System.nanoTime();
		
		double[][] trainingData = new double[num_samples][size_sample];
		for (int i = 0; i < trainingData.length; i++) {
			for (int j = 0; j < trainingData[0].length; j++) {
				trainingData[i][j] = (int)(Math.random() + 0.5); // ganze zahl 1 oder 0
			}
		}
		
		Layer visibleLayer = new LinearLayer(size_sample);
		Layer hiddenLayer = new StochasticBinaryLayer(size_sample+1);
		
		RBMImpl rbm1 = new RBMImpl(visibleLayer, hiddenLayer);
		rbm1.setLearningRate(0.1);
		rbm1.setMomentum(0.6);
		
		System.out.println("recalling before learning: ");
		System.out.println("visible activities: ");
		//com.syvys.jaRBM.Math.Matrix.printMatrix(rbm1.getVisibleActivitiesFromHiddenData(rbm1.getHiddenActivitiesFromVisibleData(trainingData)));

		for (int epochs = 0; epochs < max_epochs; epochs++)
			CDStochasticRBMLearner.Learn(rbm1, trainingData);
			//System.out.println("Epoch " + epochs + ": " + CDStochasticRBMLearner.Learn(rbm1, trainingData));
		
		System.out.println("error: " + CDStochasticRBMLearner.Learn(rbm1, trainingData));
		
		double[][] reconstructed_data = rbm1.getVisibleActivitiesFromHiddenData(rbm1.getHiddenActivitiesFromVisibleData(trainingData));
		
		double diff = 0, wrong_samples = 0;
		for (int i = 0; i < trainingData.length; i++) {
			boolean founddiff = false;
			for (int j = 0; j < trainingData[0].length; j++) {
				
				if(trainingData[i][j] != Math.round(reconstructed_data[i][j]))
				{
					founddiff= true;
					
					diff ++;
				}
			}
			
			if(founddiff)
				wrong_samples++;
		}
	
		System.out.println("Difference: " + (diff/(num_samples*size_sample)) +" -  wrong samples: "+(wrong_samples/num_samples));
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
	}

}
