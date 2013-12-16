package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import java.util.Random;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class RBMJBlas implements IRBM {
    
    private double learnRate;
    private final ILogistic logisticFunction;
    
    private double error;
    
    private DoubleMatrix weights;
	
	public RBMJBlas(int inputSize, int outputSize, double learningRate, ILogistic logisticFunction, boolean useSeed, int seed, double[][] weights) {
		this.learnRate = learningRate;
		this.logisticFunction = logisticFunction;
		
		if(weights == null) {
			if(useSeed) {
				Random random = new Random(seed);
				double[][] weightsTemp = new double[inputSize][outputSize];
				for (int v = 0; v < inputSize; v++) 
					for (int h = 0; h < outputSize; h++) 
						weightsTemp[v][h] = 0.1 * random.nextGaussian();
				
				this.weights = new DoubleMatrix(weightsTemp);
			} else {
				this.weights = DoubleMatrix.randn(inputSize, outputSize).mmul(0.1);
			}
		} else {
			this.weights = new DoubleMatrix(weights);
		}
		
		final DoubleMatrix oneVectorCol = DoubleMatrix.zeros(this.weights.getRows(), 1);
		final DoubleMatrix oneVectorRow = DoubleMatrix.zeros(1, this.weights.getColumns() + 1);
		
		this.weights = DoubleMatrix.concatHorizontally(oneVectorCol, this.weights);
		this.weights = DoubleMatrix.concatVertically(oneVectorRow, this.weights);
	}

	
	@Override
	public double error(double[][] trainingData, boolean binarizeHidden, boolean binarizeVisible) {
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
		
    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
    	
    	DoubleMatrix posHiddenNodes = logisticFunction.function(posHiddenActivations);
    	
	    if(binarizeHidden) {
	    	double[][] randomMatrix = DoubleMatrix.rand(posHiddenNodes.getRows(), posHiddenNodes.getColumns()).toArray2();
	    	
			double[][] tmpHiddenStates = posHiddenNodes.dup().toArray2();
			for (int y = 0; y < tmpHiddenStates.length; y++) {
				for (int x = 0; x < tmpHiddenStates[y].length; x++) {
					tmpHiddenStates[y][x] = (tmpHiddenStates[y][x] > randomMatrix[y][x]) ? 1 : 0;			
				}
			}
			posHiddenNodes = new DoubleMatrix(tmpHiddenStates);
	    } 	
    	
	    posHiddenNodes.putColumn(0, DoubleMatrix.ones(posHiddenNodes.getRows(), 1)); 	
	    
	    final DoubleMatrix negVisibleActivations = posHiddenNodes.mmul(this.weights.transpose());
	    
	    DoubleMatrix negVisibleNodes = logisticFunction.function(negVisibleActivations);
	    
	    if(binarizeVisible) {
	    	double[][] randomMatrix = DoubleMatrix.rand(negVisibleNodes.getRows(), negVisibleNodes.getColumns()).toArray2();
	    	
			double[][] tmpVisibleStates = negVisibleNodes.dup().toArray2();
			for (int y = 0; y < tmpVisibleStates.length; y++) {
				for (int x = 0; x < tmpVisibleStates[y].length; x++) {				
					tmpVisibleStates[y][x] = (tmpVisibleStates[y][x] > randomMatrix[y][x]) ? 1 : 0;		
				}
			}
			negVisibleNodes = new DoubleMatrix(tmpVisibleStates);
	    } 
	    
	    negVisibleNodes.putColumn(0, DoubleMatrix.ones(negVisibleNodes.getRows(), 1));
		
		return Math.sqrt(MatrixFunctions.pow(dataWithBias.sub(negVisibleNodes), 2.0).sum() / trainingData.length / weights.getRows());
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs, boolean binarizeHidden, boolean binarizeVisible) {
		DoubleMatrix data = new DoubleMatrix(trainingData);
		
		final DoubleMatrix oneVector = DoubleMatrix.ones(data.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, data);
	    
	    for (int i = 0; i < max_epochs; i++) {
	    	
	    	final DoubleMatrix posHiddenActivations = dataWithBias.mmul(this.weights);
	    	
	    	DoubleMatrix posHiddenNodes = logisticFunction.function(posHiddenActivations);
	    	
		    if(binarizeHidden) {
		    	double[][] randomMatrix = DoubleMatrix.rand(posHiddenNodes.getRows(), posHiddenNodes.getColumns()).toArray2();
		    	
				double[][] tmpHiddenStates = posHiddenNodes.dup().toArray2();
				for (int y = 0; y < tmpHiddenStates.length; y++) {
					for (int x = 0; x < tmpHiddenStates[y].length; x++) {
						tmpHiddenStates[y][x] = (tmpHiddenStates[y][x] > randomMatrix[y][x]) ? 1 : 0;		
					}
				}
				posHiddenNodes = new DoubleMatrix(tmpHiddenStates);
		    }
		    
		    posHiddenNodes.putColumn(0, DoubleMatrix.ones(posHiddenNodes.getRows(), 1));
	    	
	    	final DoubleMatrix posAssociations = dataWithBias.transpose().mmul(posHiddenNodes);
		    
		    final DoubleMatrix negVisibleActivations = posHiddenNodes.mmul(this.weights.transpose());
		    
		    DoubleMatrix negVisibleNodes = logisticFunction.function(negVisibleActivations);
		    
		    if(binarizeVisible) {
		    	double[][] randomMatrix = DoubleMatrix.rand(negVisibleNodes.getRows(), negVisibleNodes.getColumns()).toArray2();
		    	
				double[][] tmpVisibleStates = negVisibleNodes.dup().toArray2();
				for (int y = 0; y < tmpVisibleStates.length; y++) {
					for (int x = 0; x < tmpVisibleStates[y].length; x++) {				
						tmpVisibleStates[y][x] = (tmpVisibleStates[y][x] > randomMatrix[y][x]) ? 1 : 0;		
					}
				}
				negVisibleNodes = new DoubleMatrix(tmpVisibleStates);
		    }
		    
		    negVisibleNodes.putColumn(0, DoubleMatrix.ones(negVisibleNodes.getRows(), 1));
		    
		    final DoubleMatrix negHiddenActivations = negVisibleNodes.mmul(this.weights);		    
		   
		    final DoubleMatrix negHiddenProbs = logisticFunction.function(negHiddenActivations);
		    
		    final DoubleMatrix negAssociations = negVisibleNodes.transpose().mmul(negHiddenProbs);	 
		    
		    // Update weights
		    this.weights.addi( ( posAssociations.sub(negAssociations) ).mul(this.learnRate / data.getRows() ) );
		    error = Math.sqrt(MatrixFunctions.pow(dataWithBias.sub(negVisibleNodes), 2.0).sum() / trainingData.length / weights.getRows());
		    
		    //System.out.println(error);
	    }
	    System.out.println(error);
	}
	
	@Override
	public double[][] getHidden(double[][] data, boolean binarizeHidden) {

		DoubleMatrix dataMatrix = new DoubleMatrix(data);
		
		// Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(dataMatrix.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, dataMatrix);
	    
	    // Calculate the activations of the hidden units.
	    final DoubleMatrix hiddenActivations = dataWithBias.mmul(this.weights);
		
	    // Calculate the probabilities of turning the hidden units on.
	    DoubleMatrix hiddenNodes = logisticFunction.function(hiddenActivations);
	    //final DoubleMatrix hiddenProbs = hiddenActivations;
	    
	    if(binarizeHidden) {
	    	double[][] randomMatrix = DoubleMatrix.rand(hiddenNodes.getRows(), hiddenNodes.getColumns()).toArray2();
	    	
			double[][] tmpHiddenStates = hiddenNodes.dup().toArray2();
			for (int y = 0; y < tmpHiddenStates.length; y++) {
				for (int x = 0; x < tmpHiddenStates[y].length; x++) {				
					tmpHiddenStates[y][x] = (tmpHiddenStates[y][x] > randomMatrix[y][x]) ? 1 : 0;			
				}
			}
			hiddenNodes = new DoubleMatrix(tmpHiddenStates);
	    }
	    
	    final DoubleMatrix hiddenNodesWithoutBias = hiddenNodes.getRange(0,hiddenNodes.getRows(), 1, hiddenNodes.getColumns());
	    
	    // Ignore the bias units.
	    return hiddenNodesWithoutBias.toArray2();
	}
	
	@Override
	public double[][] getVisible(double[][] data, boolean binarizeVisible) {
		
		DoubleMatrix dataMatrix = new DoubleMatrix(data);
	    
	    // Insert bias units of 1 into the first column of data.
		final DoubleMatrix oneVector = DoubleMatrix.ones(dataMatrix.getRows(), 1);
		final DoubleMatrix dataWithBias = DoubleMatrix.concatHorizontally(oneVector, dataMatrix);

	    // Calculate the activations of the visible units.
		final DoubleMatrix visibleActivations = dataWithBias.mmul(weights.transpose());
	  
	    // Calculate the probabilities of turning the visible units on.
		DoubleMatrix visibleNodes = logisticFunction.function(visibleActivations);
		
		if(binarizeVisible) {
	    	double[][] randomMatrix = DoubleMatrix.rand(visibleNodes.getRows(), visibleNodes.getColumns()).toArray2();
	    	
			double[][] tmpVisibleStates = visibleNodes.dup().toArray2();
			for (int y = 0; y < tmpVisibleStates.length; y++) {
				for (int x = 0; x < tmpVisibleStates[0].length; x++) {				
					tmpVisibleStates[y][x] = (tmpVisibleStates[y][x] > randomMatrix[y][x]) ? 1 : 0;		
				}
			}
			
			visibleNodes = new DoubleMatrix(tmpVisibleStates);
		}
	     
	    // Ignore bias
		final DoubleMatrix visibleNodesWithoutBias = visibleNodes.getRange(0,visibleNodes.getRows(), 1, visibleNodes.getColumns());
		
	    return visibleNodesWithoutBias.toArray2();
	    
	}
	
	@Override
	public double[][] getWeights() {
		return this.weights.toArray2();
	}

}
