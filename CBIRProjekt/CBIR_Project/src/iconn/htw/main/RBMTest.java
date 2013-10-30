package iconn.htw.main;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;


public class RBMTest {
    
	private boolean debug = true;
	
    private int numHidden;
    private int numVisible;
    private int numHiddenWithBias;
    private int numVisibleWithBias;
    private double learningRate;
    private double[][] randomMatrix;
    
    private Random randomGenerator = new Random();
    
    private double[][] weights;
    	
	public RBMTest(int numVisbible, int numHidden, double learningRate) {
		this.numHidden = numHidden;
		this.numVisible = numVisbible;
		this.numHiddenWithBias = numHidden + 1;
		this.numVisibleWithBias = numVisbible + 1;
		this.learningRate = learningRate;
		
		/*
		this.weights = new double[numVisibleWithBias][numHiddenWithBias];
	    for(int v = 1; v < numVisibleWithBias; v++) {
	    	for(int h = 1; h < numHiddenWithBias; h++) {
	    		weights[v][h] = 0.1 * randomGenerator.nextGaussian();
	    	}
	    }
	    */
		
		this.weights = new double[][]{
				{0.000,	0.000,	0.000},
				{0.000,	-0.274,	0.002},
				{0.000,	-0.107,	0.007},
				{0.000,	-0.177,	-0.007},
				{0.000,	-0.059,	-0.152},
				{0.000,	-0.004,	-0.053},
				{0.000,	-0.115,	-0.085}
		};
		
		
		
		int rLength = numVisible;
		int cLength = numHiddenWithBias;
		
    	randomMatrix = new double[][] {
				{0.138,	0.664,	0.156},
				{0.925,	0.786,	0.910},
				{0.341,	0.548,	0.872},
				{0.337,	0.813,	0.237},
				{0.582,	0.631,	0.275},
				{0.523,	0.428,	0.880}
    	};
	    
	    printMatrix("weights:", weights);
	    printMatrix("Random matrix:", randomMatrix);
	}
	
	public void printMatrix(String title, double[][] m){
	    try{
	    	DecimalFormat f = new DecimalFormat("#0.000");
	    	
	        int rows = m.length;
	        int cols = m[0].length;
	        
	        String str = title + "\n" + "|\t";

	        for(int r=0;r<rows;r++){
	            for(int c=0;c<cols;c++){
	                str += f.format(m[r][c]) + "\t";
	            }

	            System.out.println(str + "|");
	            str = "|\t";
	        }

	    }catch(Exception e){System.out.println("Matrix is empty!!");}
	}
	

	
	public double[][] logistic(double[][] matrix) {
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		double[][] result = new double[rows][cols];
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				result[r][c] = 1.0 / (1.0 + Math.exp(-matrix[r][c]));
			}
		}
		
		return result;
	}

	public static double[][] transposeMatrix(double [][] matrix){
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		
        double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[j][i] = matrix[i][j];
            	
        return result;
    }

	public double[][] multiplicar(double[][] matrixA, double[][] matrixB) {

        int aRows = matrixA.length;
        int aCols = matrixA[0].length;
        int bRows = matrixB.length;
        int bCols = matrixB[0].length;

        if (aCols != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aCols + " did not match B:Columns " + bRows + ".");
        }

        double[][] result = new double[aRows][bCols];

        for (int r = 0; r < aRows; r++) { // aRow
            for (int c = 0; c < bCols; c++) { // bColumn
                for (int k = 0; k < aCols; k++) { // aColumn
                    result[r][c] += matrixA[r][k] * matrixB[k][c];
                }
            }
        }

        return result;
    }
	
	public void train(double[][] data, int maxEpochs) {
		/*
	    Train the machine.

	    Parameters
	    ----------
	    data: A matrix where each row is a training example consisting of the states of visible units.    
	    */

		int numberOfExamples = data.length;
		int numberOfChoicesPerExample = data[0].length;

	    // Insert bias units of 1 into the first column.
	    double[][] dataWithBias = new double[numberOfExamples][numberOfChoicesPerExample + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < numberOfChoicesPerExample + 1; c++) {
	    		if(c == 0) {
	    			dataWithBias[r][c] = 1;
	    		} else {
	    			dataWithBias[r][c] = data[r][c-1];
	    		}
	    	}
	    }
	    
	    printMatrix("Data With Bias:", dataWithBias);
	    
	    for (int i = 0; i < maxEpochs; i++) {
	    	
	    	// Clamp to the data and sample from the hidden units. 
		    // (This is the "positive CD phase", aka the reality phase.)
	    	double[][] posHiddenActivations = multiplicar(dataWithBias, this.weights);
	    	if(i == maxEpochs-1) printMatrix("posHiddenActivations:", posHiddenActivations);
	    	
	    	int rLength = dataWithBias.length;
	    	int cLength = this.weights[0].length;
	    	
	    	double[][] posHiddenProbs = logistic(posHiddenActivations);
	    	if(i == maxEpochs-1) printMatrix("posHiddenProbs:", posHiddenProbs);
	    	
	    	double[][] posHiddenStates = new double[rLength][cLength];
		    for(int r = 0; r < rLength; r++) {
		    	for(int c = 0; c < cLength; c++) {
		    		posHiddenStates[r][c] = (posHiddenProbs[r][c] > randomMatrix[r][c]) ? 1 : 0; 
		    	}
		    }
		    if(i == maxEpochs-1) printMatrix("posHiddenStates:", posHiddenStates);
		    
		    double[][] dataWithBiasT = transposeMatrix(dataWithBias);
		    if(i == maxEpochs-1) printMatrix("dataWithBiasT:", dataWithBiasT);
		    
		    double[][] posAssociations = multiplicar(dataWithBiasT, posHiddenProbs);
		    if(i == maxEpochs-1) printMatrix("posAssociations:", posAssociations);
		    
		    
		    
		    double[][] weightsT = transposeMatrix(this.weights);
		    if(i == maxEpochs-1) printMatrix("weightsT:", weightsT);
		    
		    double[][] negVisibleActivations = multiplicar(posHiddenStates, weightsT);
		    if(i == maxEpochs-1) printMatrix("negVisibleActivations:", negVisibleActivations);
		    
		    double[][] negVisibleProbs = logistic(negVisibleActivations);
		    if(i == maxEpochs-1) printMatrix("negVisibleProbs:", negVisibleProbs);
		    
		    for(int r = 0; r < negVisibleProbs.length; r++) {
		    	for(int c = 0; c < negVisibleProbs[0].length; c++) {
		    		if(c==0) negVisibleProbs[r][c] = 1;
		    	}
		    }
		    if(i == maxEpochs-1) printMatrix("negVisibleProbs (repaired weights):", negVisibleProbs);
		    
		    double[][] negHiddenActivations = multiplicar(negVisibleProbs, this.weights);
		    if(i == maxEpochs-1) printMatrix("negHiddenActivations:", negHiddenActivations);
		    
		    double[][] negHiddenProbs = logistic(negHiddenActivations);
		    if(i == maxEpochs-1) printMatrix("negHiddenProbs:", negHiddenProbs);
		    
		    double[][] negVisibleProbsT = transposeMatrix(negVisibleProbs);
		    if(i == maxEpochs-1) printMatrix("negVisibleProbsT:", negVisibleProbsT);
		    
		    double[][] negAssociations = multiplicar(negVisibleProbsT, negHiddenProbs);
		    if(i == maxEpochs-1) printMatrix("negAssociations:", negAssociations);
		    
		    // Update weights
		    for(int r = 0; r < weights.length; r++) {
		    	for(int c = 0; c < weights[0].length; c++) {
		    		weights[r][c] += this.learningRate * ((posAssociations[r][c] - negAssociations[r][c]) / numberOfExamples);
		    	}
		    }
		    if(i == maxEpochs-1) printMatrix("weights:", weights);
		    
		    double error = 0;
		    for(int r = 0; r < negVisibleProbs.length; r++) {
		    	for(int c = 0; c < negVisibleProbs[0].length; c++) {
		    		error += (dataWithBias[r][c] - negVisibleProbs[r][c]) * (dataWithBias[r][c] - negVisibleProbs[r][c]);
		    	}
		    }
		    
		    System.out.format("Epoch %s: error is %s \n", i, error);

	    }
	    
	    printMatrix("final weights", weights);

	}
	
	private double[][] runVisible(double[][] data) {
		/*
	    Assuming the RBM has been trained (so that weights for the network have been learned),
	    run the network on a set of visible units, to get a sample of the hidden units.

	    Parameters
	    ----------
	    data: A matrix where each row consists of the states of the visible units.

	    Returns
	    -------
	    hidden_states: A matrix where each row consists of the hidden units activated from the visible
	    units in the data matrix passed in.
		*/
		
		int numberOfExamples = data.length;
		int numberOfChoicesPerExample = data[0].length;
		
		double[][] hiddenStates = new double[numberOfExamples][this.numHidden + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < this.numHidden + 1; c++) {
	    		hiddenStates[r][c] = 1;
	    	}
	    }
		// printMatrix("hiddenStates:", hiddenStates);
		
	    // Insert bias units of 1 into the first column.
	    double[][] dataWithBias = new double[numberOfExamples][numberOfChoicesPerExample + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < numberOfChoicesPerExample + 1; c++) {
	    		if(c == 0) {
	    			dataWithBias[r][c] = 1;
	    		} else {
	    			dataWithBias[r][c] = data[r][c-1];
	    		}
	    	}
	    }
	   // printMatrix("dataWithBias:", dataWithBias);
	    
	    // Calculate the activations of the hidden units.
	    double[][] hiddenActivations = multiplicar(dataWithBias, this.weights);
	   // printMatrix("hiddenActivations:", hiddenActivations);
		
	    // Calculate the probabilities of turning the hidden units on.
	    double[][] hiddenProbs = logistic(hiddenActivations);
	    // printMatrix("hiddenProbs:", hiddenProbs);
	    
	    // Turn the hidden units on with their specified probabilities.
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < this.numHidden + 1; c++) {
	    		hiddenStates[r][c] = (hiddenProbs[r][c] > randomGenerator.nextDouble()) ? 1 : 0; 
	    	}
	    }
	    // printMatrix("hiddenStates:", hiddenProbs);

	    
	    double[][] hiddenStatesWithoutBias = new double[numberOfExamples][this.numHidden];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 1; c < this.numHidden + 1; c++) {
	    		hiddenStatesWithoutBias[r][c - 1] = hiddenStates[r][c];
	    	}
	    }

	    // Ignore the bias units.
	    return hiddenStatesWithoutBias;
	}
	
	private double[][] runHidden(double[][] data) {

		int numberOfExamples = data.length;
		int numberOfChoicesPerExample = data[0].length;
	    
	    // Create a matrix, where each row is to be the visible units (plus a bias unit)
		double[][] visibleStates = new double[numberOfExamples][this.numVisible + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < this.numVisible + 1; c++) {
	    		visibleStates[r][c] = 1;
	    	}
	    }
	    
	    // Insert bias units of 1 into the first column of data.
	    double[][] dataWithBias = new double[numberOfExamples][numberOfChoicesPerExample + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < numberOfChoicesPerExample + 1; c++) {
	    		if(c == 0) {
	    			dataWithBias[r][c] = 1;
	    		} else {
	    			dataWithBias[r][c] = data[r][c-1];
	    		}
	    	}
	    }
	    
	    double[][] weightsT = transposeMatrix(weights);
	    // Calculate the activations of the visible units.
	    double[][] visibleActivations = multiplicar(dataWithBias, weightsT);
	  
	    // Calculate the probabilities of turning the visible units on.
	    double[][] visibleProbs = this.logistic(visibleActivations);
	    
	    // Turn the visible units on with their specified probabilities.
	    for(int r = 0; r < visibleStates.length; r++) {
	    	for(int c = 0; c < visibleStates[0].length; c++) {
	    		visibleStates[r][c] = visibleProbs[r][c] > randomGenerator.nextDouble() ? 1 : 0;
	    	}
	    }
	    
	    printMatrix("visible states", visibleStates);
	    
	    // Ignore bias
	    double[][] visibleStatesWithoutBias = new double[numberOfExamples][this.numVisible];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 1; c < this.numVisible + 1; c++) {
	    		visibleStatesWithoutBias[r][c - 1] = visibleStates[r][c];
	    	}
	    }
	    
	    return visibleStatesWithoutBias;
	    
	}


	public static void main(String[] args) {
		RBMTest rbm = new RBMTest(6, 2, 0.1f);

		double data[][] = {
						// Alice: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
						{ 1, 1, 1, 0, 0, 0 },
						// Bob: (Harry Potter = 1, Avatar = 0, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). SF/fantasy fan, but doesn't like Avatar.
						{ 1, 0, 1, 0, 0, 0 },
						// Carol: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
						{ 1, 1, 1, 0, 0, 0 },
						// David: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
						{ 0, 0, 1, 1, 1, 0 },
						// Eric: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 0, Glitter = 0). Oscar winners fan, except for Titanic.
						{ 0, 0, 1, 1, 0, 0 },
						// Fred: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
						{ 0, 0, 1, 1, 1, 0 },
	    			   };

		rbm.train(data, 1);
		rbm.printMatrix("Weights", rbm.weights);
		
		double user[][] = {
				// Gregory: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 0, 0, 1, 1, 1, 0 },
				{ 1, 1, 1, 0, 0, 0 }
		};
		
		double hidden[][] = {
				{ 1, 0},
				{ 0, 1}
		};
		
		for(int i = 0; i < 10; i++) {
			rbm.printMatrix("User", user);
			double[][] result1 = rbm.runVisible(user);
			rbm.printMatrix("Result", result1);
			double[][] result2 = rbm.runHidden(result1);
			rbm.printMatrix("Check", result2);
			System.out.println("");
		}
		
	}

}
