package de.htw.iconn.rbm;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import de.htw.iconn.rbm.functions.ILogistic;


public class RBMJBlasSeparatedWeights implements IRBM {
    
    private double learningRate;
    private final ILogistic sigmoid;
    
    private double error;
    
    private DoubleMatrix weights;
    private DoubleMatrix backWeights;
    
    public RBMJBlasSeparatedWeights(double[][] weights, double learningRate, ILogistic sigmoid) {
    	this.learningRate = learningRate;
		this.sigmoid = sigmoid;
		
		this.weights = new DoubleMatrix(weights);
		this.backWeights = DoubleMatrix.randn(weights.length, weights[0].length).mmul(learningRate);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		this.backWeights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.backWeights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		this.backWeights = this.backWeights.transpose();
    }
    
	public RBMJBlasSeparatedWeights(int numVisible, int numHidden, double learningRate, ILogistic sigmoid) {
		this.learningRate = learningRate;
		this.sigmoid = sigmoid;
	
		this.weights = DoubleMatrix.randn(numVisible, numHidden).mmul(learningRate);
		this.backWeights = DoubleMatrix.randn(numVisible, numHidden).mmul(learningRate);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		this.backWeights = DoubleMatrix.concatHorizontally(oneVectorCol, this.backWeights);
		this.backWeights = DoubleMatrix.concatVertically(oneVectorRow, this.backWeights);
		this.backWeights = this.backWeights.transpose();
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
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	    	final DoubleMatrix posHiddenProbs = sigmoid.function(posHiddenActivations);  	
	    	
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenProbs);
		    
		    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.backWeights); //oneVectorCol at front or end?
		    
		    final DoubleMatrix negVisibleProbs = sigmoid.function(negVisibleActivations);		    
		    
		    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
		    
		    final DoubleMatrix negHiddenActivations = negVisibleProbs.mmul(this.weights);		    
		   
		    final DoubleMatrix negHiddenProbs = sigmoid.function(negHiddenActivations);	    
		    
		    final DoubleMatrix negAssociations = negVisibleProbs.transpose().mmul(negHiddenProbs);	 
		    
		    // Update weights
		    this.weights.addi( ( posAssociations.sub(negAssociations) ).mul(this.learningRate / data.getRows() ) );
		    this.backWeights.addi( ( posAssociations.sub(negAssociations) ).mul(this.learningRate / data.getRows() ).transpose() );
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
	public double[][] run_visible(double[][] userData) {

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
		DoubleMatrix visibleActivations = dataWithBias.mmul(backWeights);
	  
	    // Calculate the probabilities of turning the visible units on.
		DoubleMatrix visibleProbs = sigmoid.function(visibleActivations);
	     
	    // Ignore bias
		final DoubleMatrix visibleProbsWithoutBias = visibleProbs.getRange(0,visibleProbs.getRows(), 1, visibleProbs.getColumns());

	    return visibleProbsWithoutBias.toArray2();
	    
	}
	
	@Override
	public void setWeightsWithBias(double[][] weights) {
		this.weights = new DoubleMatrix(weights);
	}
	
	@Override
	public double[][][] getWeights() {
		return new double[][][]{(this.weights.getRange(1, weights.getRows(), 1, weights.getColumns())).toArray2(),
				(this.backWeights.getRange(1, backWeights.getRows(), 1, backWeights.getColumns())).toArray2()};
	}
	
	@Override
	public double[][][] getWeightsWithBias() {
		return new double[][][]{this.weights.toArray2(), this.backWeights.toArray2()};
	}

	@Override
	public int getInputSize() {
		return weights.getRows();
	}

	@Override
	public int getOutputSize() {
		return weights.getColumns();
	}

}
