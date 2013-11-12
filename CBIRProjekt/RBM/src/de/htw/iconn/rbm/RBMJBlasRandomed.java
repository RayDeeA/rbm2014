package de.htw.iconn.rbm;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import de.htw.iconn.rbm.functions.ILogistic;


public class RBMJBlasRandomed implements IRBM {
    
    private double learningRate;
    private final ILogistic sigmoid;
    
    
    private double offset = .01;
    
    private double error;
    
    
    private DoubleMatrix weights;
    
    public RBMJBlasRandomed(int numVisbible, int numHidden, double learningRate, double[][] weights, ILogistic sigmoid) {
		this.learningRate = learningRate;
		this.sigmoid = sigmoid;
		
		this.weights = new DoubleMatrix(weights);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 	1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		
    }
    
	public RBMJBlasRandomed(int numVisbible, int numHidden, double learningRate, ILogistic sigmoid) {
		this.learningRate = learningRate;
		this.sigmoid = sigmoid;
		
		
		this.weights = DoubleMatrix.randn(numVisbible, numHidden).mmul(learningRate);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 	1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
	}

	
	@Override
	public double error(double[][] trainingData) {
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
		
    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
    	
    	final DoubleMatrix posHiddenProbs = sigmoid.function(posHiddenActivations);  	
    	
    	// Attempt to find a equivalent to:
    	// hidden_states[:,:] = hidden_probs > np.random.rand(num_examples, self.num_hidden + 1).
    	
    	// this will adjust the values by using a random matrix
    	// instead of    (hiddenState = p > r ? 1 : 0;) // binarized
    	// this will use (hiddenState = (p + r) / 2;)   // dragged
    	
    	double[][] randomMatrix = DoubleMatrix.rand(trainingData.length, trainingData[0].length +1).toArray2();
    	
		double[][] tmpPosHiddenStates = posHiddenProbs.dup().toArray2();
		for (int y = 0; y < tmpPosHiddenStates.length; y++) {
			for (int x = 0; x < tmpPosHiddenStates[y].length; x++) {				
				// (p + r) / 2
				// ez eg.: .6, .9 => .75
				if(tmpPosHiddenStates[y][x] > randomMatrix[y][x])
					tmpPosHiddenStates[y][x] += offset;
				else
					tmpPosHiddenStates[y][x] -= offset;			
			}
		}
		
		final DoubleMatrix posHiddenStates = new DoubleMatrix(tmpPosHiddenStates);
		
	    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
	    
	    final DoubleMatrix negVisibleProbs = sigmoid.function(negVisibleActivations);
	    
	    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
	    		     
		
	    double currentError = MatrixFunctions.pow(dataWithBias.sub(negVisibleProbs), 2.0).sum();
	    
		
		return currentError;
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs) {
		
//		Printer.printMatrix("JBLAS Weights", weights);
		
		
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
		
//		Printer.printMatrix("JBLAS DataWithBias", dataWithBias);
	    
	    for (int i = 0; i < max_epochs; i++) {
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	    	final DoubleMatrix posHiddenProbs = sigmoid.function(posHiddenActivations);  	
	    	
	    	
    	 	// Attempt to find a equivalent to:
	    	// hidden_states[:,:] = hidden_probs > np.random.rand(num_examples, self.num_hidden + 1).
	    	
	    	// this will adjust the values by using a random matrix
	    	// instead of    (hiddenState = p > r ? 1 : 0;) // binarized
	    	// this will use (hiddenState = (p + r) / 2;)   // dragged
	    	
	    	double[][] randomMatrix = DoubleMatrix.rand(trainingData.length, trainingData[0].length).toArray2();
	    	
			double[][] tmpPosHiddenStates = posHiddenProbs.dup().toArray2();
			for (int y = 0; y < tmpPosHiddenStates.length; y++) {
				for (int x = 0; x < tmpPosHiddenStates[y].length; x++) {				
					// (p + r) / 2
					// ez eg.: .6, .9 => .75
										
					if(tmpPosHiddenStates[y][x] > randomMatrix[y][x])
						tmpPosHiddenStates[y][x] += offset;
					else
						tmpPosHiddenStates[y][x] -= offset;						
				}
			}
			
			final DoubleMatrix posHiddenStates = new DoubleMatrix(tmpPosHiddenStates);
			
	    	// use posHiddenStates
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenProbs);

	    	// use posHiddenStates
		    final DoubleMatrix negVisibleActivations = posHiddenStates.mmul(this.weights.transpose());
		    
			// end of new stuff --
		    
		    final DoubleMatrix negVisibleProbs = sigmoid.function(negVisibleActivations);
		    		    
		    
		    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
		    
		    
		    final DoubleMatrix negHiddenActivations = negVisibleProbs.mmul(this.weights);		    
		   
		    final DoubleMatrix negHiddenProbs = sigmoid.function(negHiddenActivations);	    
		    
		    final DoubleMatrix negAssociations = negVisibleProbs.transpose().mmul(negHiddenProbs);	 
		    
		    // Update weights
		    this.weights.addi( ( posAssociations.sub(negAssociations) ).mul(this.learningRate / data.getRows() ) );
		    error = MatrixFunctions.pow(dataWithBias.sub(negVisibleProbs), 2.0).sum();
		    

//		    Printer.printMatrix("JBLAS posHiddenActivations", posHiddenActivations);
//		    Printer.printMatrix("JBLAS posHiddenProbs", posHiddenProbs);
//		    Printer.printMatrix("JBLAS posAssociations", posAssociations);
//		    Printer.printMatrix("JBLAS negVisibleActivations", negVisibleActivations);
//		    Printer.printMatrix("JBLAS negVisibleProbs", negVisibleProbs);
//		    Printer.printMatrix("JBLAS negHiddenActivations", negHiddenActivations);
//		    Printer.printMatrix("JBLAS negHiddenProbs", negHiddenProbs);
//		    Printer.printMatrix("JBLAS negAssociations", negAssociations);		
//		    
//		    Printer.printMatrix("JBLAS weights", weights);
//		    System.out.println(data.getRows());
		    
		    System.out.println(error);
	    }

	}
	
	@Override
	public double[][] run_visual(double[][] userData) {

		DoubleMatrix data = new DoubleMatrix(userData);
		
		// Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
	    
	    // Calculate the activations of the hidden units.
	    final DoubleMatrix hiddenActivations = dataWithBias.mmul(this.weights);
		
	    // Calculate the probabilities of turning the hidden units on.
	    final DoubleMatrix hiddenProbs = sigmoid.function(hiddenActivations);
//	    final DoubleMatrix hiddenProbs = hiddenActivations;
	    double[][] randomMatrix = DoubleMatrix.rand(userData.length, userData[0].length+1).toArray2();
    	
		double[][] tmpPosHiddenStates = hiddenProbs.dup().toArray2();
		for (int y = 0; y < tmpPosHiddenStates.length; y++) {
			for (int x = 0; x < tmpPosHiddenStates[y].length; x++) {				
				// (p + r) / 2
				// ez eg.: .6, .9 => .75
									
				if(tmpPosHiddenStates[y][x] > randomMatrix[y][x])
					tmpPosHiddenStates[y][x] += offset;
				else
					tmpPosHiddenStates[y][x] -= offset;						
			}
		}
		
		final DoubleMatrix posHiddenStates = new DoubleMatrix(tmpPosHiddenStates);
	    final DoubleMatrix hiddenProbsWithoutBias = posHiddenStates.getRange(0,posHiddenStates.getRows(), 1, posHiddenStates.getColumns());

	    // Ignore the bias units.
	    return hiddenProbsWithoutBias.toArray2();
	}
	
	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		
		DoubleMatrix data = new DoubleMatrix(hiddenData);
	    
	    // Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);

	    // Calculate the activations of the visible units.
		DoubleMatrix visibleActivations = dataWithBias.mmul(weights.transpose());
	  
	    // Calculate the probabilities of turning the visible units on.
		DoubleMatrix visibleProbs = sigmoid.function(visibleActivations);
		
	    double[][] randomMatrix = DoubleMatrix.rand(hiddenData.length, hiddenData[0].length+1).toArray2();
    	
		double[][] tmpPosHiddenStates = visibleProbs.dup().toArray2();
		for (int y = 0; y < tmpPosHiddenStates.length; y++) {
			for (int x = 0; x < tmpPosHiddenStates[y].length; x++) {				
				// (p + r) / 2
				// ez eg.: .6, .9 => .75
									
				if(tmpPosHiddenStates[y][x] > randomMatrix[y][x])
					tmpPosHiddenStates[y][x] += offset;
				else
					tmpPosHiddenStates[y][x] -= offset;						
			}
		}
		
		final DoubleMatrix posHiddenStates = new DoubleMatrix(tmpPosHiddenStates);
		
	    // Ignore bias
		final DoubleMatrix visibleProbsWithoutBias = posHiddenStates.getRange(0,posHiddenStates.getRows(), 1, posHiddenStates.getColumns());

	    return visibleProbsWithoutBias.toArray2();
	    
	}
	
	@Override
	public void setWeights(double[][] weights) {
		this.weights = new DoubleMatrix(weights);
	}
	
	@Override
	public double[][] getWeights() {
		return (this.weights.getRange(1, weights.getRows(), 1, weights.getColumns())).toArray2();
	}
	
	@Override
	public double[][] getWeightsWithBIAS() {
		return this.weights.toArray2();
	}

}