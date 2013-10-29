package de.htw.ait.rbm.test;
import java.util.Random;

import de.htw.ait.rbm.Matrix;


public class RBM_orig {

	int num_visible = 6;
	int num_hidden = 2;
	double learning_rate = 0.1;
	Random random;
	
	double[][] weights; 
//	= {
//			{ 3.092749, -0.307868, 0.220275,},
//			{ 1.125749, 3.742721, -9.014602,},
//			{ 1.128419, 3.740775, -9.013852,},
//			{ 5.864249, 4.559970, 2.403509,},
//			{ -5.936808, 2.658933, 6.884548,},
//			{ -8.166085, 3.452035, 0.512441,},
//			{ -6.866478, 2.591355, -6.878207,},
//			};
	
	double[][] weightsT; 
	
	public RBM_orig(int numVisible, int numHidden, double learningRate) {
		super();
		num_visible =   numVisible;
		num_hidden =    numHidden;
		learning_rate = learningRate;
		
		// initial zufällige Gewichte
		random = new Random((long) (1000*Math.random())); // 7 // 
		
		weights = new double[num_visible+1][numHidden+1];
		for (int v = 1; v < num_visible+1; v++) {
			for (int h = 1; h < num_hidden+1; h++) {
				weights[v][h] = 0.2*random.nextGaussian();
			}
		}
		
		// transpose weights
		weightsT = new double[num_hidden+1][num_visible+1];
		for (int v = 0; v < num_visible+1; v++) 
			for (int h = 0; h < num_hidden+1; h++) 
				weightsT[h][v] = weights[v][h];
	}


	public void train(double[][] trainingData, int max_epochs ) {
		
		int num_examples = trainingData.length; 
		
		// Insert bias units of 1 into the first column.
		double[][] data = new double[num_examples][trainingData[0].length+1];
		double[][] dataT = new double[trainingData[0].length+1][num_examples];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if(j == 0) 
					data[i][j] = dataT[j][i] =1;
				else
					data[i][j] = dataT[j][i] =trainingData[i][j-1];
			}
		}
		
		double[][] pos_hidden_states; 
		
		for (int e = 0; e < max_epochs; e++) {
//			System.out.println("weights.T");
//			Matrix.mprint(weightsT);
			
			// Clamp to the data and sample from the hidden units. 
		    // (This is the "positive CD phase", aka the reality phase.)
			
			// a_i = Sum w_ij * x_i 
			// pos_hidden_activations = np.dot(data, self.weights)
			double[][] pos_hidden_probs = Matrix.multiply(data, weights);
			//  pos_hidden_probs = self._logistic(pos_hidden_activations)
			logistic(pos_hidden_probs);
//			System.out.println("pos_hidden_probs");
//			Matrix.mprint(pos_hidden_probs);
			
			// !!!
			for (int k = 0; k < pos_hidden_probs.length; k++) 
				pos_hidden_probs[k][0] = 1;
			
			pos_hidden_states = getRandomStatesFromProbs(pos_hidden_probs);
//			System.out.println("pos_hidden_probs");
//			Matrix.mprint(pos_hidden_states);
			
			// Note that we're using the activation *probabilities* of the hidden states, not the hidden states       
		    // themselves, when computing associations. We could also use the states; see section 3 of Hinton's 
		    // "A Practical Guide to Training Restricted Boltzmann Machines" for more.
			// pos_associations = np.dot(data.T, pos_hidden_probs)
			double[][] pos_associations = Matrix.multiply(dataT, pos_hidden_probs);
//			System.out.println("pos_associations");
//			Matrix.mprint(pos_associations);
			
			
			// Reconstruct the visible units and sample again from the hidden units.
		    // (This is the "negative CD phase", aka the daydreaming phase.)
			
			// neg_visible_activations = np.dot(pos_hidden_states, self.weights.T)
			double[][] neg_visible_probs = Matrix.multiply(pos_hidden_states, weightsT);
			logistic(neg_visible_probs);
//			System.out.println("neg_visible_probs  ########");
//			Matrix.mprint(neg_visible_probs);
			
			// neg_visible_probs[:,0] = 1 # Fix the bias unit.
			for (int k = 0; k < neg_visible_probs.length; k++) 
				neg_visible_probs[k][0] = 1;
//			Matrix.mprint(neg_visible_probs);
			
			// neg_hidden_activations = np.dot(neg_visible_probs, self.weights)
			double[][] neg_hidden_probs = Matrix.multiply(neg_visible_probs, weights);
//			System.out.println("neg_hidden_activations");
//			Matrix.mprint(neg_hidden_probs);
			logistic(neg_hidden_probs);
//			System.out.println("neg_hidden_probs");
//			Matrix.mprint(neg_hidden_probs);
			
			// Note, again, that we're using the activation *probabilities* 
			// when computing associations, not the states themselves.
			
			// transpose neg_visible_probs
			double[][] neg_visible_probsT = new double[neg_visible_probs[0].length][neg_visible_probs.length];
			for (int v = 0; v < neg_visible_probsT.length; v++) 
				for (int h = 0; h < neg_visible_probsT[0].length; h++) 
					neg_visible_probsT[v][h] = neg_visible_probs[h][v];
//			System.out.println("neg_visible_probsT");
//			Matrix.mprint(neg_visible_probsT);
			
			// neg_associations = np.dot(neg_visible_probs.T, neg_hidden_probs)
			double[][] neg_associations = Matrix.multiply(neg_visible_probsT, neg_hidden_probs);
//			Matrix.mprint(neg_associations);
			
			// Update weights.
			for (int v = 0; v < num_visible+1; v++) 
				for (int h = 0; h < num_hidden+1; h++) 
					weights[v][h] += learning_rate * (pos_associations[v][h] - neg_associations[v][h]) / num_examples;
			//Matrix.mprint(weights);
			
			// transpose weights
			for (int v = 0; v < num_visible+1; v++) 
				for (int h = 0; h < num_hidden+1; h++) 
					weightsT[h][v] = weights[v][h];
			
			// error = np.sum((data - neg_visible_probs) ** 2)			
			double error = 0;
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					double d = data[i][j] - neg_visible_probs[i][j];
					error += d*d;
				}
			}
			System.out.println("Epoch " + e + ": error is " + error);			
		}
		System.out.println("weights.T");
		Matrix.mprint(weightsT);
	}
	
	
	private double[][] getRandomStatesFromProbs(double[][] posHiddenProbs) {
		
		double[][] states = new double[posHiddenProbs.length][posHiddenProbs[0].length];
		
		for (int i = 0; i < states.length; i++) 
			for (int j = 0; j < states[0].length; j++) 
				states[i][j] = (posHiddenProbs[i][j] > random.nextDouble()) ? 1 : 0;
		
		return states;
	}


	// 1 / (1 + e^-x) 
	void logistic (double[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = 1. / ( 1 + Math.exp(-m[i][j]) );
			}
		}
	}

	/*	Assuming the RBM has been trained (so that weights for the network have been learned),
    	run the network on a set of visible units, to get a sample of the hidden units.
    
    	Parameters:
    	data: A matrix where each row consists of the states of the visible units.
    
    	Returns:
    	hidden_states: A matrix where each row consists of the hidden units activated from the visible
    	units in the data matrix passed in.
	*/
	public double[][] run_visual(double[][] userData) {
		
		 int num_examples = userData.length;
		 
		 // Create a matrix, where each row is to be the hidden units (plus a bias unit)
		 // sampled from a training example.
		 // hidden_states = np.ones((num_examples, self.num_hidden + 1))
		 double[][] hidden_states = new double[num_examples][num_hidden+1];
		 for (int i = 0; i < hidden_states.length; i++) 
			 for (int j = 0; j < hidden_states[0].length; j++) 
				 hidden_states[i][j] = 1;

		 // Insert bias units of 1 into the first column of data.
		 //   data = np.insert(data, 0, 1, axis = 1)
		 double[][] data = new double[num_examples][userData[0].length+1];
		 for (int i = 0; i < data.length; i++) 
			 for (int j = 0; j < data[0].length; j++) {
				 if(j == 0) 
					 data[i][j] = 1;
				 else
					 data[i][j] = userData[i][j-1];
			 }
		 //Matrix.mprint(data);
		 
		 // Calculate the activations of the hidden units.
		 // hidden_activations = np.dot(data, self.weights)
		 double[][] hidden_probs = Matrix.multiply(data, weights);
		 
		 // Calculate the probabilities of turning the hidden units on.
		 // hidden_probs = self._logistic(hidden_activations)
		 logistic(hidden_probs);
		 //Matrix.mprint(hidden_probs);
		 
		 // Turn the hidden units on with their specified probabilities.
		 // hidden_states[:,:] = hidden_probs > np.random.rand(num_examples, self.num_hidden + 1)
		 hidden_states = getRandomStatesFromProbs(hidden_probs);
		 
		 double[][] hidden_states_ = new double[num_examples][num_hidden];
		 for (int i = 0; i < hidden_states_.length; i++) 
			 for (int j = 0; j < hidden_states_[0].length; j++) 
				 hidden_states_[i][j] = hidden_states[i][j+1];
		 
		 Matrix.mprint(hidden_states_);
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
		 double[][] visible_probs = Matrix.multiply(data, weightsT); 
		 logistic(visible_probs);
		 
		 double[][] visible_states = getRandomStatesFromProbs(visible_probs);
		 double[][] visible_states_ = new double[visible_states.length][visible_states[0].length-1];
		 for (int i = 0; i < visible_states_.length; i++) 
			 for (int j = 0; j < visible_states_[0].length; j++) 
				 visible_states_[i][j] = visible_states[i][j+1];
		 //Matrix.mprint(visible_states_);
		 return visible_states_;
	}
	
}
