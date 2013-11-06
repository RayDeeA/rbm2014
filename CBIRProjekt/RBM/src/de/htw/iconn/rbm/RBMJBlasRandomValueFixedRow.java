package de.htw.iconn.rbm;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.jblas.util.Random;

import de.htw.iconn.rbm.functions.ILogistic;


public class RBMJBlasRandomValueFixedRow implements IRBM {
    
    private double learningRate;
    private final ILogistic sigmoid;
    
    private double error;
    private int randomFactorEachXEpoch;
    
    private DoubleMatrix weights;
    
    public RBMJBlasRandomValueFixedRow(int numVisbible, int numHidden, double learningRate, double[][] weights, ILogistic sigmoid, int randomFactorEachXEpoch) {
		this.learningRate = learningRate;
		this.sigmoid = sigmoid;
		this.randomFactorEachXEpoch = randomFactorEachXEpoch;
		
		this.weights = new DoubleMatrix(weights);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 	1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		
    }
    
	public RBMJBlasRandomValueFixedRow(int numVisbible, int numHidden, double learningRate, ILogistic sigmoid, int randomFactorEachXEpoch) {
		this.learningRate = learningRate;
		this.sigmoid = sigmoid;
		this.randomFactorEachXEpoch = randomFactorEachXEpoch;
		
		
		this.weights = DoubleMatrix.randn(numVisbible, numHidden).mmul(learningRate);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 	1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
	}

	
	@Override
	public double error(double[][] trainingData) {
		return error;
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs) {
		
//		Printer.printMatrix("JBLAS Weights", weights);
		
		
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
		
//		Printer.printMatrix("JBLAS DataWithBias", dataWithBias);
		
	    for (int i = 0; i < max_epochs; i++) {
	    	
	    	if(i % randomFactorEachXEpoch == 0) {
	    		int randomRow = 5;
	    		
	    		double max = this.weights.max();
	    		double min = this.weights.min();
	    		
	    		for(int c = 0; c < this.weights.getColumns(); c++) {
	    			double randomValue = min + (max - min) * Random.nextDouble();
	    			this.weights.put(randomRow, c, randomValue);
	    		}

	    	}
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	  
	    	final DoubleMatrix posHiddenProbs = sigmoid.function(posHiddenActivations);  	
	    	
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenProbs);
		    
		    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
		    
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
	    
	    final DoubleMatrix hiddenProbsWithoutBias = hiddenProbs.getRange(0,hiddenProbs.getRows(), 1, hiddenProbs.getColumns());

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
	     
	    // Ignore bias
		final DoubleMatrix visibleProbsWithoutBias = visibleProbs.getRange(0,visibleProbs.getRows(), 1, visibleProbs.getColumns());

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
