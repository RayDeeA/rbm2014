package de.htw.ait.rbm;

import java.util.Random;

import de.htw.iconn.rbm.IRBM;


public class RBMBla implements IRBM {

	int num_visible = 6;
	int num_hidden = 2;
	double learning_rate = 0.1;
	private static Random random = new Random();
	
	double[][] weights; 
	
	public RBMBla(int numVisible, int numHidden, double learningRate) {
		num_visible =   numVisible;
		num_hidden =    numHidden;
		learning_rate = learningRate;
		
		// initial zuf������llige Gewichte		
		weights = new double[num_visible+1][num_hidden+1];
		for (int v = 1; v < num_visible+1; v++) 
			for (int h = 1; h < num_hidden+1; h++) 
				weights[v][h] = 0.1*random.nextGaussian();
	}

	public RBMBla(int numVisible, int numHidden, double learningRate, double[][] weights) {
		this.num_visible =   numVisible;
		this.num_hidden =    numHidden;
		this.learning_rate = learningRate;		
		this.weights = weights;
	}

	@Override
	public void train(double[][] trainingData, int max_epochs ) {
		
		int num_examples = trainingData.length; 
		
		// Insert bias units of 1 into the first column.
		double[][] data = new double[num_examples][trainingData[0].length+1];
	
		for (int i = 0; i < data.length; i++) 
			for (int j = 0; j < data[0].length; j++) 
				data[i][j] =  (j == 0) ? 1 : trainingData[i][j-1];
		
		//double[][] pos_hidden_states; 
		double[][] neg_visible_probs = null;
		
		for (int e = 0; e < max_epochs; e++) {
			// Clamp to the data and sample from the hidden units. 
		    // (This is the "positive CD phase", aka the reality phase.)			
			// CD = constrastive divergence or "approximate gradient descent"
			double[][] pos_hidden_probs = Matrix.multiply(data, weights);
			logistic(pos_hidden_probs);
			
			
//			pos_hidden_probs = getRandomStatesFromProbs(pos_hidden_probs);
			// Quantisiere Hart
//			for (int i = 0; i < pos_hidden_probs.length; i++)
//				for (int j = 0; j < pos_hidden_probs[i].length; j++)
//					pos_hidden_probs[i][j] = (pos_hidden_probs[i][j] < 0.5) ? 0 : 1;

			// bias again
			for (int k = 0; k < pos_hidden_probs.length; k++) 
				pos_hidden_probs[k][0] = 1;
			
			// Note that we're using the activation *probabilities* of the hidden states, not the hidden states       
		    // themselves, when computing associations. We could also use the states; see section 3 of Hinton's 
		    // "A Practical Guide to Training Restricted Boltzmann Machines" for more.
			double[][] pos_associations = Matrix.multiplyTN(data, pos_hidden_probs);
			
			// Reconstruct the visible units and sample again from the hidden units.
		    // (This is the "negative CD phase", aka the daydreaming phase.)
			neg_visible_probs = Matrix.multiplyNT(pos_hidden_probs, weights);
			logistic(neg_visible_probs);

			// bias again
			for (int k = 0; k < neg_visible_probs.length; k++) 
				neg_visible_probs[k][0] = 1;
			
			double[][] neg_hidden_probs = Matrix.multiply(neg_visible_probs, weights);
			logistic(neg_hidden_probs);
			
			// Note, again, that we're using the activation *probabilities* 
			// when computing associations, not the states themselves.
			double[][] neg_associations = Matrix.multiplyTN(neg_visible_probs, neg_hidden_probs);
			
			// Update weights
			for (int v = 0; v < num_visible+1; v++) 
				for (int h = 0; h < num_hidden+1; h++) 
					weights[v][h] += learning_rate * (pos_associations[v][h] - neg_associations[v][h]) / num_examples;
			
			// Error output every 10th iteration
			if(e % 10 == 0)
			{
				double error = 0;
				for (int i = 0; i < data.length; i++) 
					for (int j = 0; j < data[0].length; j++) {
						double d = data[i][j] - neg_visible_probs[i][j];
						error += d*d;
					}
			
				System.out.println("Epoche "+e+" Error is " + error);
			}
				
		}
		double error = 0;
		for (int i = 0; i < data.length; i++) 
			for (int j = 0; j < data[0].length; j++) {
				double d = data[i][j] - neg_visible_probs[i][j];
				error += d*d;
			}
	
		System.out.println("Error is " + error);	
		
		System.out.println("weights");
		//Matrix.mprint(weights);
	}
	
	public void train(double[][] trainingData) 
	{	
		int num_examples = trainingData.length; 
		
		// Insert bias units of 1 into the first column.
		double[][] data = new double[num_examples][trainingData[0].length+1];
	
		for (int i = 0; i < data.length; i++) 
			for (int j = 0; j < data[0].length; j++) 
				data[i][j] =  (j == 0) ? 1 : trainingData[i][j-1];
		
		//double[][] pos_hidden_states; 
		double[][] neg_visible_probs = null;
		
		// Clamp to the data and sample from the hidden units. 
		// (This is the "positive CD phase", aka the reality phase.)			
		// CD = constrastive divergence or "approximate gradient descent"
		double[][] pos_hidden_probs = Matrix.multiply(data, weights);
		logistic(pos_hidden_probs);
			
		// bias again
		for (int k = 0; k < pos_hidden_probs.length; k++) 
			pos_hidden_probs[k][0] = 1;
			
		// Note that we're using the activation *probabilities* of the hidden states, not the hidden states       
		// themselves, when computing associations. We could also use the states; see section 3 of Hinton's 
		// "A Practical Guide to Training Restricted Boltzmann Machines" for more.
		double[][] pos_associations = Matrix.multiplyTN(data, pos_hidden_probs);
			
		// Reconstruct the visible units and sample again from the hidden units.
		// (This is the "negative CD phase", aka the daydreaming phase.)
		neg_visible_probs = Matrix.multiplyNT(pos_hidden_probs, weights);
		logistic(neg_visible_probs);

		// bias again
		for (int k = 0; k < neg_visible_probs.length; k++) 
			neg_visible_probs[k][0] = 1;
			
		double[][] neg_hidden_probs = Matrix.multiply(neg_visible_probs, weights);
		logistic(neg_hidden_probs);
			
		// Note, again, that we're using the activation *probabilities* 
		// when computing associations, not the states themselves.
		double[][] neg_associations = Matrix.multiplyTN(neg_visible_probs, neg_hidden_probs);
			
		// Update weights
		for (int v = 0; v < num_visible+1; v++) 
			for (int h = 0; h < num_hidden+1; h++) 
				weights[v][h] += learning_rate * (pos_associations[v][h] - neg_associations[v][h]) / num_examples;
	}

	@Override
	public double error(double[][] trainingData) {
		
		int num_examples = trainingData.length; 
		
		// Insert bias units of 1 into the first column.
		double[][] data = new double[num_examples][trainingData[0].length+1];
	
		for (int i = 0; i < data.length; i++) 
			for (int j = 0; j < data[0].length; j++) 
				data[i][j] =  (j == 0) ? 1 : trainingData[i][j-1];
		
		//double[][] pos_hidden_states; 
		double[][] neg_visible_probs = null;
		
		// Clamp to the data and sample from the hidden units. 
		// (This is the "positive CD phase", aka the reality phase.)			
		// CD = constrastive divergence or "approximate gradient descent"
		double[][] pos_hidden_probs = Matrix.multiply(data, weights);
		logistic(pos_hidden_probs);
			
		// bias again
		for (int k = 0; k < pos_hidden_probs.length; k++) 
			pos_hidden_probs[k][0] = 1;
			
		// Note that we're using the activation *probabilities* of the hidden states, not the hidden states       
		// themselves, when computing associations. We could also use the states; see section 3 of Hinton's 
		// "A Practical Guide to Training Restricted Boltzmann Machines" for more.
		double[][] pos_associations = Matrix.multiplyTN(data, pos_hidden_probs);
			
		// Reconstruct the visible units and sample again from the hidden units.
		// (This is the "negative CD phase", aka the daydreaming phase.)
		neg_visible_probs = Matrix.multiplyNT(pos_hidden_probs, weights);
		logistic(neg_visible_probs);

		// bias again
		for (int k = 0; k < neg_visible_probs.length; k++) 
			neg_visible_probs[k][0] = 1;
			
		double[][] neg_hidden_probs = Matrix.multiply(neg_visible_probs, weights);
		logistic(neg_hidden_probs);
			
		// Note, again, that we're using the activation *probabilities* 
		// when computing associations, not the states themselves.
		double[][] neg_associations = Matrix.multiplyTN(neg_visible_probs, neg_hidden_probs);
			
		// Update weights
		for (int v = 0; v < num_visible+1; v++) 
			for (int h = 0; h < num_hidden+1; h++) 
				weights[v][h] += learning_rate * (pos_associations[v][h] - neg_associations[v][h]) / num_examples;
			
		// Error output every 10th iteration
		double error = 0;
		for (int i = 0; i < data.length; i++) 
			for (int j = 0; j < data[0].length; j++) {
				double d = data[i][j] - neg_visible_probs[i][j];
				error += d*d;
			}
		
		return error;
	}
	
	public static double[][] getRandomStatesFromProbs(double[][] posHiddenProbs) {
		
		double[][] states = new double[posHiddenProbs.length][posHiddenProbs[0].length];
		
		for (int i = 0; i < states.length; i++) 
			for (int j = 0; j < states[0].length; j++) 
				states[i][j] = (posHiddenProbs[i][j] > random.nextDouble()) ? 1 : 0;
		
		return states;
	}
	
	void logistic (double[][] m) {
		for (int i = 0; i < m.length; i++) 
			for (int j = 0; j < m[0].length; j++) 
				m[i][j] = 1. / ( 1 + Math.exp(-m[i][j]) ); // 1 / (1 + e^-x) 
	}

	/*	Assuming the RBM has been trained (so that weights for the network have been learned),
    	run the network on a set of visible units, to get a sample of the hidden units.
    
    	Parameters:
    	data: A matrix where each row consists of the states of the visible units.
    
    	Returns:
    	hidden_states: A matrix where each row consists of the hidden units activated from the visible
    	units in the data matrix passed in.
	*/
	
	@Override
	public double[][] run_visual(double[][] userData) {
		
		 int num_examples = userData.length;
		 
		 // Create a matrix, where each row is to be the hidden units (plus a bias unit)
		 // sampled from a training example.
		 double[][] hidden_states = new double[num_examples][num_hidden+1];
		 for (int i = 0; i < hidden_states.length; i++) 
			 for (int j = 0; j < hidden_states[0].length; j++) 
				 hidden_states[i][j] = 1;

		 // Insert bias units of 1 into the first column of data.
		 double[][] data = new double[num_examples][userData[0].length+1];
		 for (int i = 0; i < data.length; i++) 
			 for (int j = 0; j < data[0].length; j++) 
				 data[i][j] = (j == 0) ? 1 : userData[i][j-1];
			 
		 // Calculate the activations of the hidden units.
		 double[][] hidden_probs = Matrix.multiply(data, weights);
		 
		 // Calculate the probabilities of turning the hidden units on.
		 logistic(hidden_probs);
		 
		 // Turn the hidden units on with their specified probabilities.
//		 hidden_states = getRandomStatesFromProbs(hidden_probs);
		 hidden_states = hidden_probs;
		 
		 double[][] hidden_states_ = new double[num_examples][num_hidden];
		 for (int i = 0; i < hidden_states_.length; i++) 
			 for (int j = 0; j < hidden_states_[0].length; j++) 
				 hidden_states_[i][j] = hidden_states[i][j+1];
		 
//		 Matrix.mprint(hidden_states_);
		 return (hidden_states_);
	}

	/* 
	 Assuming the RBM has been trained (so that weights for the network have been learned)
	 run the network on a set of hidden units, to get a sample of the visible units.

	Parameters
	data: A matrix where each row consists of the states of the hidden units.

	Returns
	visible_states: A matrix where each row consists of the visible units activated from the hidden
	units in the data matrix passed in.
	*/
	
	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		
		int num_examples = hiddenData.length;
		
		// Insert bias units of 1 into the first column of data.
	    // data = np.insert(data, 0, 1, axis = 1)
		 double[][] data = new double[num_examples][hiddenData[0].length+1];
		 for (int i = 0; i < data.length; i++) 
			 for (int j = 0; j < data[0].length; j++) {
				 if(j == 0) 
					 data[i][j] = 1;
				 else
					 data[i][j] = hiddenData[i][j-1];
			 }
		 
		 // Calculate the activations of the visible units.
		 // visible_activations = np.dot(data, self.weights.T)
		 double[][] visible_probs = Matrix.multiplyNT(data, weights); 
		 logistic(visible_probs);
		 
		 //double[][] visible_states = getRandomStatesFromProbs(visible_probs);
		 double[][] visible_states = visible_probs;
		 
		 double[][] visible_states_ = new double[visible_states.length][visible_states[0].length-1];
		 for (int i = 0; i < visible_states_.length; i++) 
			 for (int j = 0; j < visible_states_[0].length; j++) 
				 visible_states_[i][j] = visible_states[i][j+1];
		 return visible_states_;
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {
		this.weights = weights;
	}

	@Override
	public double[][] getWeights() {
		return this.weights;
	}
	
	@Override
	public double[][] getWeightsWithBIAS() {
		return this.weights;
	}

	@Override
	public int getInputSize() {

		return num_visible;
	}

	@Override
	public int getOutputSize() {
		
		return num_hidden;
	}
	
}
