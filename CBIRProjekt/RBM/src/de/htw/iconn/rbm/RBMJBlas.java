package de.htw.iconn.rbm;

import java.util.Random;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import de.htw.iconn.rbm.functions.ILogistic;


public class RBMJBlas implements IRBM {
    
    private double learnRate;
    private final ILogistic logisticFunction;
    
    private double error;
    
    private DoubleMatrix weights;
    
	public RBMJBlas(int numVisible, int numHidden, double learningRate, ILogistic sigmoid) {
		this(numVisible, numHidden, learningRate, sigmoid, false, 0);
	}
	
	public RBMJBlas(int numVisible, int numHidden, double learningRate, ILogistic sigmoid, boolean useSeed, int seed) {
		this.learnRate = learningRate;
		this.logisticFunction = sigmoid;
		
		if(useSeed) {
			Random random = new Random(seed);
			double[][] weights = new double[numVisible][numHidden];
			for (int v = 0; v < numVisible; v++) 
				for (int h = 0; h < numHidden; h++) 
					weights[v][h] = 0.1 * random.nextGaussian();
			
			this.weights = new DoubleMatrix(weights);
		} else {
			this.weights = DoubleMatrix.randn(numVisible, numHidden).mmul(0.1);
		}
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
	}
	
    public RBMJBlas(int numVisible, int numHidden, double learnRate, double[][] weights, ILogistic logisticFunction) {
		this.learnRate = learnRate;
		this.logisticFunction = logisticFunction;
		
		this.weights = new DoubleMatrix(weights);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
    }
	
	@Override
	public double error(double[][] trainingData) {
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
		
    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
    	
    	final DoubleMatrix posHiddenProbs = logisticFunction.function(posHiddenActivations);  	
    	
    	posHiddenProbs.putColumn(0, DoubleMatrix.ones(posHiddenProbs.getRows(), 1)); 	
	    
	    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
	    
	    final DoubleMatrix negVisibleProbs = logisticFunction.function(negVisibleActivations);
	    
	    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));	     
		
		return Math.sqrt(MatrixFunctions.pow(dataWithBias.sub(negVisibleProbs), 2.0).sum() / trainingData.length / getInputSize());
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs) {

		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
	    
	    for (int i = 0; i < max_epochs; i++) {
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	    	final DoubleMatrix posHiddenProbs = logisticFunction.function(posHiddenActivations);
	    	
	    	posHiddenProbs.putColumn(0, DoubleMatrix.ones(posHiddenProbs.getRows(), 1));
	    	
	    	double[][] randomMatrix = DoubleMatrix.rand(posHiddenProbs.getRows(), posHiddenProbs.getColumns()).toArray2();
			double[][] tmpPosHiddenStates = posHiddenProbs.dup().toArray2();
			for (int y = 0; y < tmpPosHiddenStates.length; y++) {
				for (int x = 0; x < tmpPosHiddenStates[y].length; x++) {				
					// (p + r) / 2
					// ez eg.: .6, .9 => .75
					if(tmpPosHiddenStates[y][x] > randomMatrix[y][x])
						tmpPosHiddenStates[y][x] = 1;
					else
						tmpPosHiddenStates[y][x] = 0;			
				}
			}
			
			DoubleMatrix posHiddenStates = new DoubleMatrix(tmpPosHiddenStates);
	    	
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenProbs);
		    
		    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
		    
		    final DoubleMatrix negVisibleProbs = logisticFunction.function(negVisibleActivations);
		    		    
		    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
		    
		    final DoubleMatrix negHiddenActivations = negVisibleProbs.mmul(this.weights);		    
		   
		    final DoubleMatrix negHiddenProbs = logisticFunction.function(negHiddenActivations);
		    
		    final DoubleMatrix negAssociations = negVisibleProbs.transpose().mmul(negHiddenProbs);	 
		    
		    // Update weights
		    this.weights.addi( ( posAssociations.sub(negAssociations) ).mul(this.learnRate / data.getRows() ) );
		    error = Math.sqrt(MatrixFunctions.pow(dataWithBias.sub(negVisibleProbs), 2.0).sum() / trainingData.length / getInputSize());
		    
		    //System.out.println(error);
	    }
	    System.out.println(error);
	}
	
	@Override
	public double[][] run_visible(double[][] userData, boolean useHiddenStates) {

		DoubleMatrix data = new DoubleMatrix(userData);
		
		// Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
	    
	    // Calculate the activations of the hidden units.
	    final DoubleMatrix hiddenActivations = dataWithBias.mmul(this.weights);
		
	    // Calculate the probabilities of turning the hidden units on.
	    DoubleMatrix hiddenNodes = logisticFunction.function(hiddenActivations);
	    //final DoubleMatrix hiddenProbs = hiddenActivations;
	    
	    if(useHiddenStates) {
	    	double[][] randomMatrix = DoubleMatrix.rand(hiddenNodes.getRows(), hiddenNodes.getColumns()).toArray2();
	    	
			double[][] tmpHiddenStates = hiddenNodes.dup().toArray2();
			for (int y = 0; y < tmpHiddenStates.length; y++) {
				for (int x = 0; x < tmpHiddenStates[y].length; x++) {				
					// (p + r) / 2
					// ez eg.: .6, .9 => .75
					if(tmpHiddenStates[y][x] > randomMatrix[y][x])
						tmpHiddenStates[y][x] = 1;
					else
						tmpHiddenStates[y][x] = 0;			
				}
			}
			
			hiddenNodes = new DoubleMatrix(tmpHiddenStates);
	    }
	    
	    final DoubleMatrix hiddenNodesWithoutBias = hiddenNodes.getRange(0,hiddenNodes.getRows(), 1, hiddenNodes.getColumns());
	    
	    // Ignore the bias units.
	    return hiddenNodesWithoutBias.toArray2();
	}
	
	@Override
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates) {
		
		DoubleMatrix data = new DoubleMatrix(hiddenData);
	    
	    // Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);

	    // Calculate the activations of the visible units.
		final DoubleMatrix visibleActivations = dataWithBias.mmul(weights.transpose());
	  
	    // Calculate the probabilities of turning the visible units on.
		DoubleMatrix visibleNodes = logisticFunction.function(visibleActivations);
		
		
		if(useVisibleStates) {
	    	double[][] randomMatrix = DoubleMatrix.rand(visibleNodes.getRows(), visibleNodes.getColumns()).toArray2();
	    	
			double[][] tmpVisibleStates = visibleNodes.dup().toArray2();
			for (int y = 0; y < tmpVisibleStates.length; y++) {
				for (int x = 0; x < tmpVisibleStates[0].length; x++) {				
					// (p + r) / 2
					// ez eg.: .6, .9 => .75
					if(tmpVisibleStates[y][x] > randomMatrix[y][x])
						tmpVisibleStates[y][x] = 1;
					else
						tmpVisibleStates[y][x] = 0;			
				}
			}
			
			visibleNodes = new DoubleMatrix(tmpVisibleStates);
		}
	     
	    // Ignore bias
		final DoubleMatrix visibleNodesWithoutBias = visibleNodes.getRange(0,visibleNodes.getRows(), 1, visibleNodes.getColumns());
		
	    return visibleNodesWithoutBias.toArray2();
	    
	}
	
	@Override
	public void setWeightsWithBias(double[][] weights) {
		this.weights = new DoubleMatrix(weights);
	}
	
	@Override
	public double[][][] getWeights() {
		return new double[][][]{(this.weights.getRange(1, weights.getRows(), 1, weights.getColumns())).toArray2()};
	}
	
	@Override
	public double[][][] getWeightsWithBias() {
		return new double[][][]{this.weights.toArray2()};
	}

	@Override
	public int getInputSize() {
		return weights.getRows();
	}

	@Override
	public int getOutputSize() {
		return weights.getColumns();
	}

	@Override
	public double getLearnRate() {
		return this.learnRate;
	}

	@Override
	public ILogistic getLogisticFunction() {
		return this.logisticFunction;
	}

	@Override
	public boolean hasBias() {
		return true;
	}

	@Override
	public double[][] daydream(int numberOfSamples) {

		// sampleSize * inputSize
		double[][] samples = new double[numberOfSamples][this.getInputSize()-1];
		
		// Randomly initialize the visible units once
		DoubleMatrix work = DoubleMatrix.rand(1, this.getInputSize());
		work.putColumn(0, DoubleMatrix.ones(work.getRows(), 1));
		samples[0] = work.getRow(0).toArray();
				
		for (int i = 1; i < numberOfSamples; i++) {

			final DoubleMatrix posHiddenActivations = work.mmul(this.weights);
	    	
	    	final DoubleMatrix posHiddenProbs = logisticFunction.function(posHiddenActivations);
	    	
	    	posHiddenProbs.putColumn(0, DoubleMatrix.ones(posHiddenProbs.getRows(), 1));
		    
		    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
		    
		    final DoubleMatrix negVisibleProbs = logisticFunction.function(negVisibleActivations);
		    		    
		    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
		    
		    work = new DoubleMatrix(negVisibleProbs.toArray2());
		    
		    samples[i] = work.getRow(0).toArray();
		}

		return samples;
	}

}
