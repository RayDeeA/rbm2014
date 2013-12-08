package de.htw.ait.rbm.test;
import de.htw.ait.rbm.RBMNico;


public class RBMPerformanceTest {

	static int num_hidden = 20;
	static int max_epochs = 500;
	
	static int num_samples = 100;
	static int size_sample = 60;

	public static void main(String[] args) {
		
		long nanosec = System.nanoTime();
		
		double[][] trainingData = new double[num_samples][size_sample];
		for (int i = 0; i < trainingData.length; i++) {
			for (int j = 0; j < trainingData[0].length; j++) {
				trainingData[i][j] = (int)(Math.random() + 0.5); // ganze zahl 1 oder 0
			}
		}
		
		
		RBMNico rbm = new RBMNico(trainingData[0].length, num_hidden, 0.1);
		rbm.train(trainingData, max_epochs, false, false);
	
		double[][] hidden_data = rbm.run_visible(trainingData, false);
		double[][] reconstructed_data = rbm.run_hidden(hidden_data, false);
		
		double diff = 0;
		for (int i = 0; i < trainingData.length; i++) {
			for (int j = 0; j < trainingData[0].length; j++) {
				diff += (trainingData[i][j] == Math.round(reconstructed_data[i][j])) ? 0 : 1;
			}
		}
	
		System.out.println("Difference: " + (diff/(num_samples*size_sample)));
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		long nanosec1 = System.nanoTime();
		double[][] reconstructed_data1 = rbm.run_hidden(hidden_data, false);
		System.out.println("Reconstruction Time:" + (System.nanoTime() - nanosec1)+"ns");
	}

}
