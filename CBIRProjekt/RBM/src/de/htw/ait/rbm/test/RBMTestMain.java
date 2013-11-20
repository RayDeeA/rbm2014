package de.htw.ait.rbm.test;
import de.htw.ait.rbm.Matrix;
import de.htw.ait.rbm.RBMNico;


public class RBMTestMain {

	static double[][] trainingData = {
//		{1,1,1,0,0,0},
//		{1,0,1,0,0,0},
//		{1,1,1,0,0,0},
//		{0,0,1,1,1,0}, 
//		{0,0,1,1,0,0},
//		{0,0,1,1,1,0}

		{1,1,1,0,0,0},
		{1,1,1,0,0,0},
		{1,0,1,1,1,1},
		{1,1,1,1,1,1},
		{0,0,1,1,1,0},
//		{0,0,1,1,0,0},
//		{0,0,1,1,1,0},
		
//		{1,1,0.5,0,0,0},
//		{1,1,1,1,1,1},
//		{0,0,1,1,0,0},
//		{0,0,1,1,1,0}
	};
	
	static int num_hidden = 2;
	
	static int max_epochs = 5000;

	public static void main(String[] args) {
		
		RBMNico rbm = new RBMNico(trainingData[0].length, num_hidden, 0.1);
		
		rbm.train(trainingData, max_epochs);
	
		double[][] useData = {{1,1,1,0,0,0}}; // trainingData;
		
		System.out.println("Desired:");
		Matrix.mprint(useData);
		
		System.out.println("Hidden Data:");
		double[][] hidden_data = rbm.run_visual(useData);
		
		System.out.println("Visual activities:");
		double[][] visual_data = rbm.run_hidden(hidden_data);
		Matrix.mprint(visual_data);		
	}

}
