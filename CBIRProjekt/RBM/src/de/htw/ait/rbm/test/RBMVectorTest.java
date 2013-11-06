package de.htw.ait.rbm.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.htw.ait.rbm.RBMVector;

public class RBMVectorTest {

	static String[] names = new String[] {"cold", "ice", "warm", "sun", "hot", "winter", "water", "ocean", "fish", "weather"};
	// cold, ice, warm, sun, hot, winter, water, ocean, fish, weather
	//   1,   2,   3,    4,   5,    6,     7,      8,     9,   10
	
	static int[] WATER = new int[]{0,0,0,1,0,0,1,1,0,0}; // sun, water, ocean
	static int[] ICECOLD = new int[]{1,1,0,0,0,0,0,0,0,0}; // cold, ice
	static int[] COLD = new int[]{1,0,0,0,0,0,0,0,0,0}; // cold
	static int[] WARM = new int[]{0,0,1,0,1,0,0,0,0,0}; // warm, hot
	
	public static void main(String[] args) {
		String filename = "rbm.data";
		RBMVectorTest rbm = new RBMVectorTest();
		
		// trainieren
		long nanosec = System.nanoTime();
		rbm.train();
		System.out.println("Errorrate: "+rbm.error());
		System.out.println("Time:" + (System.nanoTime() - nanosec) / 1000000 +"ms");
		
		// testen
		List<String> waterList = rbm.find(convertIntToBool(WATER));
		System.out.println("sun, water, ocean -> " + StringUtils.join(waterList));
		List<String> icecoldList = rbm.find(convertIntToBool(ICECOLD));
		System.out.println("cold, ice -> " + StringUtils.join(icecoldList));
		List<String> coldList = rbm.find(convertIntToBool(COLD));
		System.out.println("cold -> " + StringUtils.join(coldList));
		List<String> warmList = rbm.find(convertIntToBool(WARM));
		System.out.println("warm, hit -> " + StringUtils.join(warmList));
		
		// speichere RBM
		rbm.save(filename);
		
		//----------------------- laden des RBM ---------------------
		System.out.println("\nLade RBM und teste es.");
		RBMVectorTest rbmTest = new RBMVectorTest();
		rbmTest.load(filename);
		
		// testen
		List<String> waterList1 = rbmTest.find(convertIntToBool(WATER));
		System.out.println("sun, water, ocean -> " + StringUtils.join(waterList1));
		List<String> icecoldList1 = rbmTest.find(convertIntToBool(ICECOLD));
		System.out.println("cold, ice -> " + StringUtils.join(icecoldList1));
		List<String> coldList1 = rbmTest.find(convertIntToBool(COLD));
		System.out.println("cold -> " + StringUtils.join(coldList1));
		List<String> warmList1 = rbmTest.find(convertIntToBool(WARM));
		System.out.println("warm, hit -> " + StringUtils.join(warmList1));
	}
	
	public static boolean[] convertIntToBool(int[] values)
	{
		boolean[] result = new boolean[values.length];
		for (int i = 0; i < values.length; i++)
			result[i] = (values[i] == 1);
		return result;
	}
	
	
	//----------------------------------------------------------------------------------------------------
	//----------------------------------- klassen variablen und methoden ---------------------------------
	//----------------------------------------------------------------------------------------------------
	int max_epochs = 10000;
	
	int num_visible = 10;
	int num_hidden = 2;
	
	double learningRate = 0.1;
	double momentum = 0.6;

	double[][] trainingData = new double[][] {
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
	
	RBMVector rbm = null;
	
	public RBMVectorTest()
	{
		rbm = new RBMVector(num_visible, num_hidden, learningRate);
	}
	
	public void save(String filename) {
		rbm.save(filename);
	}
	
	public void load(String filename) {
		rbm = RBMVector.load(filename);
	}
	
	public void train()
	{		
		// training ��ber epochen mit gegebenen daten
		for (int e = 0; e < max_epochs; e++) {
			for (double[] td : trainingData) {
				rbm.train(td);
			}
		}
	}
	
	public double error()
	{
		double error = 0;
		for (double[] td : trainingData)
			error += rbm.error(td);
		return error;
	}

	private List<String> find(boolean[] testdata) 
	{
		List<String> result = new ArrayList<String>();
		
		// calc hidden data
		double[] hidden_data = rbm.run_visual(testdata);
		activate(hidden_data);
		
		// recreate visible data
		double[] reconstructed_data = rbm.run_hidden(hidden_data);
		for (int i = 0; i < reconstructed_data.length; i++)
			if(reconstructed_data[i] > 0.5)
				result.add(names[i]);
		
		return result;
	}
	
	
	private static void activate(double[] data)
	{
		for (int i = 0; i < data.length; i++) {
			data[i] = (data[i] < 0.5) ? 0 : 1;
		}
	}
	
}
