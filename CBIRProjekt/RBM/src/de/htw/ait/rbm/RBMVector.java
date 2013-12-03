package de.htw.ait.rbm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Random;

public class RBMVector implements Serializable {

	private static final long serialVersionUID = 9124521794449502067L;
	
	int num_visible = 6;
	int num_hidden = 2;
	double learning_rate = 0.1;
	double[][] weights;
	
	public RBMVector(int numVisible, int numHidden, double learningRate) {
		super();

		learning_rate = learningRate;
		createWeights(numVisible, numHidden);
	}
	
	private void createWeights(int numVisible, int numHidden)
	{
		num_visible =   numVisible;
		num_hidden =    numHidden;
		
		// initial zufaellige Gewichte
		//random = new Random((long) 7); 
		Random random = new Random((long) (1000*Math.random())); 
		
		weights = new double[num_visible+1][num_hidden+1];
		for (int v = 1; v < num_visible+1; v++) 
			for (int h = 1; h < num_hidden+1; h++) 
				weights[v][h] = 0.1*random.nextGaussian();
	}

	public static RBMVector load(String filename) {
		InputStream fis = null;
		RBMVector rbm = null;

		try {
			fis = new FileInputStream(filename);
			ObjectInputStream o = new ObjectInputStream(fis);
			rbm = (RBMVector) o.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}

		return rbm;
	}
	
	public void save(String filename) {
		OutputStream fos = null;

		try {
			fos = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * trainieren mit boolean werten
	 * @param trainingData
	 */
	public void train(boolean[] trainingData) 
	{
		// Insert bias units of 1 into the first column.
		double[] data = new double[trainingData.length+1];
	
		for (int i = 0; i < data.length; i++) 
			data[i] =  (i == 0) ? 1 : (trainingData[i-1] ? 1 : 0);
		
		trainWithBias(data);
	}
	
	public void train(double[] trainingData) 
	{
		// Insert bias units of 1 into the first column.
		double[] data = new double[trainingData.length+1];
	
		for (int i = 0; i < data.length; i++) 
			data[i] =  (i == 0) ? 1 : trainingData[i-1];
		
		trainWithBias(data);
	}
	
	private void trainWithBias(double[] data)
	{
		// Clamp to the data and sample from the hidden units. 
		// (This is the "positive CD phase", aka the reality phase.)			
		// CD = constrastive divergence or "approximate gradient descent"
		// data = 1x10  &  weight = 10x3
		double[] pos_hidden_probs = Matrix.multiply(data, weights);
		logistic(pos_hidden_probs);
			
		// bias again
		pos_hidden_probs[0] = 1;
			
		// Note that we're using the activation *probabilities* of the hidden states, not the hidden states       
		// themselves, when computing associations. We could also use the states; see section 3 of Hinton's 
		// "A Practical Guide to Training Restricted Boltzmann Machines" for more.
		double[][] pos_associations = Matrix.multiplyTN(data, pos_hidden_probs);
			
		// Reconstruct the visible units and sample again from the hidden units.
		// (This is the "negative CD phase", aka the daydreaming phase.)
		double[] neg_visible_probs = Matrix.multiplyNT(pos_hidden_probs, weights);
		logistic(neg_visible_probs);

		// bias again
		neg_visible_probs[0] = 1;
			
		double[] neg_hidden_probs = Matrix.multiply(neg_visible_probs, weights);
		logistic(neg_hidden_probs);
			
		// Note, again, that we're using the activation *probabilities* 
		// when computing associations, not the states themselves.
		double neg_associations[][] = Matrix.multiplyTN(neg_visible_probs, neg_hidden_probs);
			
		// Update weights
		for (int v = 0; v < num_visible+1; v++) 
			for (int h = 0; h < num_hidden+1; h++) 
				weights[v][h] += learning_rate *  (pos_associations[v][h] - neg_associations[v][h]) / (data.length-1);
	}
	
	public double error(double[] trainingData) 
	{
		// Insert bias units of 1 into the first column.
		double[] data = new double[trainingData.length+1];
	
		for (int i = 0; i < data.length; i++)
			data[i] =  (i == 0) ? 1 : trainingData[i-1];
		
		return errorWithBias(data);
	}
	
	public double error(boolean[] trainingData) 
	{
		// Insert bias units of 1 into the first column.
		double[] data = new double[trainingData.length+1];
	
		for (int i = 0; i < data.length; i++)
			data[i] =  (i == 0) ? 1 : (trainingData[i-1] ? 1 : 0);
		
		return errorWithBias(data);
	}
	
	private double errorWithBias(double[] data)
	{

		// Clamp to the data and sample from the hidden units. 
	    // (This is the "positive CD phase", aka the reality phase.)			
		// CD = constrastive divergence or "approximate gradient descent"
		double[] pos_hidden_probs = Matrix.multiply(data, weights);
		logistic(pos_hidden_probs);
		
		// bias again
		pos_hidden_probs[0] = 1;
		
		// Reconstruct the visible units and sample again from the hidden units.
	    // (This is the "negative CD phase", aka the daydreaming phase.)
		double[] neg_visible_probs = Matrix.multiplyNT(pos_hidden_probs, weights);
		logistic(neg_visible_probs);

		// bias again
		neg_visible_probs[0] = 1;
		
		double error = 0;
		for (int i = 0; i < data.length; i++) {
			double d = data[i] - neg_visible_probs[i];
			error += d*d;
		}
		
		return error;
	}

	
	void logistic (double[] m) {
		for (int i = 0; i < m.length; i++) 
			m[i] = 1. / ( 1 + Math.exp(-m[i]) ); // 1 / (1 + e^-x) 
	}
	

	/*	Assuming the RBM has been trained (so that weights for the network have been learned),
    	run the network on a set of visible units, to get a sample of the hidden units.
    
    	Parameters:
    	data: A matrix where each row consists of the states of the visible units.
    
    	Returns:
    	hidden_states: A matrix where each row consists of the hidden units activated from the visible
    	units in the data matrix passed in.
	*/
	public double[] run_visual(double[] userData) 
	{
		// Insert bias units of 1 into the first column of data.
		double[] data = new double[userData.length+1];
			for (int j = 0; j < data.length; j++) 
				data[j] = (j == 0) ? 1 : userData[j-1];
			 
		return run_visualWithBias(data);
	}
	
	public double[] run_visual(boolean[] userData) 
	{
		 // Insert bias units of 1 into the first column of data.
		 double[] data = new double[userData.length+1];
			 for (int j = 0; j < data.length; j++) 
				 data[j] = (j == 0) ? 1 : (userData[j-1] ? 1 : 0);
			 
		return run_visualWithBias(data);
	}
	
	private double[] run_visualWithBias(double[] data)
	{
		 // Calculate the activations of the hidden units.
		 double[] hidden_probs = Matrix.multiply(data, weights);
		 
		 // Calculate the probabilities of turning the hidden units on.
		 logistic(hidden_probs);
		 
		 double[] hidden_states_ = new double[num_hidden];
		 for (int i = 0; i < hidden_states_.length; i++)
				 hidden_states_[i] = hidden_probs[i+1];
		 
		 return (hidden_states_);
	}
	

	/*	Assuming the RBM has been trained (so that weights for the network have been learned)
		run the network on a set of hidden units, to get a sample of the visible units.

		Parameters
		data: A matrix where each row consists of the states of the hidden units.

		Returns
		visible_states: A matrix where each row consists of the visible units activated from the hidden
		units in the data matrix passed in.
	*/	
	public double[] run_hidden(double[] hiddenData) 
	{		
		// Insert bias units of 1 into the first column of data.
	    // data = np.insert(data, 0, 1, axis = 1)
		double[] data = new double[hiddenData.length+1];
		for (int i = 0; i < data.length; i++) {
			if(i == 0) 
				data[i] = 1;
			else
				data[i] = hiddenData[i-1];
		}
		 
		// Calculate the activations of the visible units.
		// visible_activations = np.dot(data, self.weights.T)
		double[] visible_probs = Matrix.multiplyNT(data, weights); 
		logistic(visible_probs);
		 
		 double[] visible_states_ = new double[visible_probs.length-1];
		 for (int i = 0; i < visible_states_.length; i++) 
				 visible_states_[i] = visible_probs[i+1];
		 
		 return visible_states_;
	}

	public int getNumVisible() {
		return num_visible;
	}

	public int getNumHidden() {
		return num_hidden;
	}

	public double getLearningRate() {
		return learning_rate;
	}
	
	
}
