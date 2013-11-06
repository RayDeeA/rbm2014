package iconn.htw.main;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import de.htw.iconn.rbm.IRBM;


public class RBM_MU implements IRBM {
    
	private boolean debug = true;
	
    private int numHidden;
    private int numVisible;
    private int numHiddenWithBias;
    private int numVisibleWithBias;
    private double learningRate;
    private double[][] randomMatrix;
    
    private double error;
    
    private Random randomGenerator = new Random();
    
    private DoubleMatrix weights;
    
    public RBM_MU(int numVisbible, int numHidden, double learningRate, double[][] weights) {
		this.numHidden = numHidden;
		this.numVisible = numVisbible;
		this.numHiddenWithBias = numHidden + 1;
		this.numVisibleWithBias = numVisbible + 1;
		this.learningRate = learningRate;
		
		this.weights = new DoubleMatrix(weights);
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 	1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
		
    }
    
	public RBM_MU(int numVisbible, int numHidden, double learningRate) {
		this.numHidden = numHidden;
		this.numVisible = numVisbible;
		this.numHiddenWithBias = numHidden + 1;
		this.numVisibleWithBias = numVisbible + 1;
		this.learningRate = learningRate;
		
		
		this.weights = DoubleMatrix.randn(numVisible, numHidden).mmul(learningRate);
		
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
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	  
	    	final DoubleMatrix posHiddenProbs = CustomMatrixFunctions.sigmoid(posHiddenActivations);  	
	    	
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenProbs);
		    
		    final DoubleMatrix negVisibleActivations = posHiddenProbs.mmul(this.weights.transpose());
		    
		    final DoubleMatrix negVisibleProbs = CustomMatrixFunctions.sigmoid(negVisibleActivations);
		    		    
		    
		    negVisibleProbs.putColumn(0, DoubleMatrix.ones(negVisibleProbs.getRows(), 1));
		    
		    
		    final DoubleMatrix negHiddenActivations = negVisibleProbs.mmul(this.weights);		    
		   
		    final DoubleMatrix negHiddenProbs = CustomMatrixFunctions.sigmoid(negHiddenActivations);	    
		    
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
	    final DoubleMatrix hiddenProbs = CustomMatrixFunctions.sigmoid(hiddenActivations);
	    
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
		DoubleMatrix visibleProbs = CustomMatrixFunctions.sigmoid(visibleActivations);
	    
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
		return this.weights.toArray2();
	}

}
