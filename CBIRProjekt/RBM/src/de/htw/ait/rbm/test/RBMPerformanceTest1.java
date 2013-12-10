package de.htw.ait.rbm.test;
import de.htw.ait.rbm.Matrix;
import de.htw.ait.rbm.RBMNico;
import de.htw.ait.rbm.RBMVector;


/**
 * http://jarbm.cvs.sourceforge.net/viewvc/jarbm/RBM/src/com/syvys/jaRBM/Main.java?view=markup
 * 
 * @author Neiko
 *
 */
public class RBMPerformanceTest1 {

	static int max_epochs = 20000;
	
	static int num_visible = 10;
	static int num_hidden = 2;
	
	static double learningRate = 0.6;
	static double momentum = 0.6;
	
	static double[][] WATER = new double[][]{{0,0,0,1,0,0,1,1,0,0}}; // sun, water, ocean
	static double[][] ICECOLD = new double[][]{{1,1,0,0,0,0,0,0,0,0}}; // cold, ice
	static double[][] COLD = new double[][]{{1,0,0,0,0,0,0,0,0,0}}; // cold
	static double[][] WARM = new double[][]{{0,0,1,0,1,0,0,0,0,0}}; // warm, hot
	
	static String[] names = new String[] {"cold", "ice", "warm", "sun", "hot", "winter", "water", "ocean", "fish", "weather"};
	// cold, ice, warm, sun, hot, winter, water, ocean, fish, weather
	//   1,   2,   3,    4,   5,    6,     7,      8,     9,   10

	static double[][] trainingData = new double[][] {
			{1,1,0,0,0,0,0,0,0,0}, // kalt
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
		
	
	public static void main(String[] args) {
		
		long nanosec = System.nanoTime();
		
		newFunc(trainingData, names, nanosec);
		long diff0 = (System.nanoTime() - nanosec);
		nanosec = System.nanoTime();
		
//		singleOld(trainingData, names, nanosec);
		long diff = (System.nanoTime() - nanosec);
		nanosec = System.nanoTime();
		
//		old(trainingData, names, nanosec);
		long diff1 = (System.nanoTime() - nanosec);

		System.out.println();
		System.out.println("new time: "+(diff0/1000000)+"ms");
		System.out.println("singleold time: "+(diff/1000000)+"ms");
		System.out.println("Old time:"+(diff1/1000000)+"ms");
	}
	
	private static void newFunc(double[][] trainingData, String[] names, long nanosec) 
	{
		RBMVector rbm = new RBMVector(num_visible, num_hidden, 0.1);
		
		// training ���ber epochen mit gegebenen daten
		for (int e = 0; e < max_epochs; e++) {
			double error = 0;
			for (double[] td : trainingData) {
				rbm.train(td);
		
				if(e % 1000 == 0)
					error += rbm.error(td);
			}
			
			if(e % 1000 == 0)
				System.out.println("Epoche "+e+" Error is " + error);
		}
		
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		find(rbm, COLD, "cold");
		find(rbm, ICECOLD, "cold, ice");
		find(rbm, WATER, "sun, water, ocean");
		find(rbm, WARM, "warm, hot");
	}
	
	private static void find(RBMVector rbm, double[][] testdata, String groupName) {
		// besorge die versteckten daten 
		System.out.println("\n\n");
		System.out.println("Finde die passende Gruppe zu "+groupName);
		double[] hidden_data_cold = rbm.run_visual(testdata[0]);
		Matrix.mprint(hidden_data_cold);
		
		// probiere die aktivierung mit dem maximum
		double[] hidden_data_cold_max = hidden_data_cold.clone();
		extractMaximum(hidden_data_cold_max);
		double[] reconstructed_data_cold_max = rbm.run_hidden(hidden_data_cold_max);
		System.out.println("Maximum aktivierung brachte folgende Werte:");
		for (int i = 0; i < reconstructed_data_cold_max.length; i++)
			if(reconstructed_data_cold_max[i] > 0.5)
				System.out.print(names[i]+",");
		System.out.println();
		
		// probiere die aktivierung mit runden
		double[] hidden_data_cold_round = hidden_data_cold.clone();
		activate(hidden_data_cold_round);
		double[] reconstructed_data_cold_round = rbm.run_hidden(hidden_data_cold_round);
		System.out.println("Aktivierung mit runden brachte folgende Werte:");
		for (int i = 0; i < reconstructed_data_cold_round.length; i++)
			if(reconstructed_data_cold_round[i] > 0.5)
				System.out.print(names[i]+",");
	}
	
	
	private static void singleOld(double[][] trainingData, String[] names, long nanosec)
	{
		RBMNico rbm = new RBMNico(trainingData[0].length, num_hidden, 0.1);
		
		// training ���ber epochen mit gegebenen daten
		for (int e = 0; e < max_epochs; e++) {
			double error = 0;
			for (double[] td : trainingData) {
				double[][] data = new double[][] {td};
				rbm.train(data);
		
				if(e % 10 == 0)
					error += rbm.error(data, false, false);
			}
			
			if(e % 10 == 0)
				System.out.println("Epoche "+e+" Error is " + error);
		}
		
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		System.out.println("\n\n");
		System.out.println("Finde die passende Gruppe zu Kalt");
		double[][] hidden_data_cold = rbm.run_visible(COLD, false);
		extractMaximum(hidden_data_cold);
		
		System.out.println("Gruppe:");
		Matrix.mprint(hidden_data_cold);
		double[][] reconstructed_data_cold = rbm.run_hidden(hidden_data_cold, false);
		
		Matrix.mprint(reconstructed_data_cold);
		System.out.println("Folge werte verwenden:");
		for (int i = 0; i < reconstructed_data_cold[0].length; i++) {
			if(reconstructed_data_cold[0][i] > 0.5)
				System.out.print(names[i]+",");
		}
	}
	
	private static void old(double[][] trainingData, String[] names, long nanosec) 
	{
		RBMNico rbm = new RBMNico(trainingData[0].length, num_hidden, 0.1);
		rbm.train(trainingData, max_epochs, false, false);
		double error = rbm.error(trainingData, false, false);
		System.out.println("calc Error "+error);
		
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		System.out.println("\n\n");
		System.out.println("Finde die passende Gruppe zu Kalt");
		double[][] hidden_data_cold = rbm.run_visible(COLD, false);
		extractMaximum(hidden_data_cold);
		
		System.out.println("Gruppe:");
		Matrix.mprint(hidden_data_cold);
		double[][] reconstructed_data_cold = rbm.run_hidden(hidden_data_cold, false);
		
		Matrix.mprint(reconstructed_data_cold);
		System.out.println("Folge werte verwenden:");
		for (int i = 0; i < reconstructed_data_cold[0].length; i++) {
			if(reconstructed_data_cold[0][i] > 0.5)
				System.out.print(names[i]+",");
		}
	}
	
	private static void activate(double[] data)
	{
		for (int i = 0; i < data.length; i++) {
			data[i] = (data[i] < 0.5) ? 0 : 1;
		}
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

	private static void extractMaximum(double[] data)
	{
		double max = 0; 
		int index = 0;
		
		for (int i = 0; i < data.length; i++) {
			if(max < data[i])
			{
				max = data[i];
				index = i;
			}
		}
			
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
		data[index] = 1;
	}
}
