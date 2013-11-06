package iconn.htw.main;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import de.htw.iconn.rbm.IRBM;


public class RBMOriginal implements IRBM {
    
	private boolean debug = true;
	
    private int numHidden;
    private int numVisible;
    private int numHiddenWithBias;
    private int numVisibleWithBias;
    private double learningRate;
    private double[][] randomMatrix;
    
    private double error;
    
    private Random randomGenerator = new Random();
    
    private double[][] weights;
    
    public RBMOriginal(int numVisbible, int numHidden, double learningRate, double[][] weights) {
    	
		this.numHidden = numHidden;
		this.numVisible = numVisbible;
		this.numHiddenWithBias = numHidden + 1;
		this.numVisibleWithBias = numVisbible + 1;
		this.learningRate = learningRate;
		
		this.weights = new double[numVisibleWithBias][numHiddenWithBias];
	    for(int v = 1; v < numVisibleWithBias; v++) {
	    	for(int h = 1; h < numHiddenWithBias; h++) {
	    		this.weights[v][h] = weights[v -1][h - 1];
	    	}
	    }
    }
    
	public RBMOriginal(int numVisbible, int numHidden, double learningRate) {
		this.numHidden = numHidden;
		this.numVisible = numVisbible;
		this.numHiddenWithBias = numHidden + 1;
		this.numVisibleWithBias = numVisbible + 1;
		this.learningRate = learningRate;
		
		
		this.weights = new double[numVisibleWithBias][numHiddenWithBias];
	    for(int v = 1; v < numVisibleWithBias; v++) {
	    	for(int h = 1; h < numHiddenWithBias; h++) {
	    		weights[v][h] = this.learningRate * randomGenerator.nextGaussian();
	    	}
	    }
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
	
	@Override
	public double error(double[][] trainingData) {
		
		
		
		return error;
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs) {
		
		Printer.printMatrix("weights", weights);
		/*
	    Train the machine.

	    Parameters
	    ----------
	    data: A matrix where each row is a training example consisting of the states of visible units.    
	    */

		int numberOfExamples = trainingData.length;
		int numberOfChoicesPerExample = trainingData[0].length;

	    // Insert bias units of 1 into the first column.
	    double[][] dataWithBias = new double[numberOfExamples][numberOfChoicesPerExample + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < numberOfChoicesPerExample + 1; c++) {
	    		if(c == 0) {
	    			dataWithBias[r][c] = 1;
	    		} else {
	    			dataWithBias[r][c] = trainingData[r][c-1];
	    		}
	    	}
	    }
	    
	    Printer.printMatrix("DataWithBias", dataWithBias);
	    
	    for (int i = 0; i < max_epochs; i++) {
	    	
	    	// Clamp to the data and sample from the hidden units. 
		    // (This is the "positive CD phase", aka the reality phase.)
	    	double[][] posHiddenActivations = multiplicar(dataWithBias, this.weights);
	    	Printer.printMatrix("posHiddenActivations", posHiddenActivations);
	    	
	    	int rLength = dataWithBias.length;
	    	int cLength = this.weights[0].length;
	    	
	    	double[][] posHiddenProbs = logistic(posHiddenActivations);
	    	Printer.printMatrix("posHiddenProbs", posHiddenProbs);
	    	/*
	    	double[][] posHiddenStates = new double[rLength][cLength];
		    for(int r = 0; r < rLength; r++) {
		    	for(int c = 0; c < cLength; c++) {
		    		posHiddenStates[r][c] = (posHiddenProbs[r][c] > randomMatrix[r][c]) ? 1 : 0; 
		    	}
		    }
		    */
		    
		    double[][] dataWithBiasT = transposeMatrix(dataWithBias);
		    
		    double[][] posAssociations = multiplicar(dataWithBiasT, posHiddenProbs);
	    	Printer.printMatrix("posAssociations", posAssociations);
		    
		    
		    double[][] weightsT = transposeMatrix(this.weights);
		    
		    double[][] negVisibleActivations = multiplicar(posHiddenProbs, weightsT);
	    	Printer.printMatrix("negVisibleActivations", negVisibleActivations);
		    
		    double[][] negVisibleProbs = logistic(negVisibleActivations);
		    
		    for(int r = 0; r < negVisibleProbs.length; r++) {
		    	for(int c = 0; c < negVisibleProbs[0].length; c++) {
		    		if(c==0) negVisibleProbs[r][c] = 1;
		    	}
		    }
	    	Printer.printMatrix("negVisibleProbs", negVisibleProbs);
		    
		    double[][] negHiddenActivations = multiplicar(negVisibleProbs, this.weights);
	    	Printer.printMatrix("negHiddenActivations", negHiddenActivations);
		    
		    double[][] negHiddenProbs = logistic(negHiddenActivations);
	    	Printer.printMatrix("negHiddenProbs", negHiddenProbs);
		    
		    double[][] negVisibleProbsT = transposeMatrix(negVisibleProbs);
		    
		    double[][] negAssociations = multiplicar(negVisibleProbsT, negHiddenProbs);
	    	Printer.printMatrix("negAssociations", negAssociations);
		    
		    // Update weights
		    for(int r = 0; r < weights.length; r++) {
		    	for(int c = 0; c < weights[0].length; c++) {
		    		weights[r][c] += this.learningRate * ((posAssociations[r][c] - negAssociations[r][c]) / numberOfExamples);
//		    		weights[r][c] += (posAssociations[r][c] - negAssociations[r][c]);
		    	}
		    }
	    	Printer.printMatrix("weights", weights);
	    	System.out.println(numberOfExamples);
		    
		    error = 0;
		    for(int r = 0; r < negVisibleProbs.length; r++) {
		    	for(int c = 0; c < negVisibleProbs[0].length; c++) {
		    		error += (dataWithBias[r][c] - negVisibleProbs[r][c]) * (dataWithBias[r][c] - negVisibleProbs[r][c]);
		    	}
		    }
		    System.out.println(error);

	    }

	}
	
	@Override
	public double[][] run_visual(double[][] userData) {
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
		
		int numberOfExamples = userData.length;
		int numberOfChoicesPerExample = userData[0].length;
		
		/*
		double[][] hiddenStates = new double[numberOfExamples][this.numHidden + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < this.numHidden + 1; c++) {
	    		hiddenStates[r][c] = 1;
	    	}
	    }
	    */
		// printMatrix("hiddenStates:", hiddenStates);
		
	    // Insert bias units of 1 into the first column.
	    double[][] dataWithBias = new double[numberOfExamples][numberOfChoicesPerExample + 1];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < numberOfChoicesPerExample + 1; c++) {
	    		if(c == 0) {
	    			dataWithBias[r][c] = 1;
	    		} else {
	    			dataWithBias[r][c] = userData[r][c-1];
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
	    /*
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 0; c < this.numHidden + 1; c++) {
	    		hiddenStates[r][c] = (hiddenProbs[r][c] > randomGenerator.nextDouble()) ? 1 : 0; 
	    	}
	    }
	    */
	    // printMatrix("hiddenStates:", hiddenProbs);

	    
	    double[][] hiddenStatesWithoutBias = new double[numberOfExamples][this.numHidden];
	    for(int r = 0; r < numberOfExamples; r++) {
	    	for(int c = 1; c < this.numHidden + 1; c++) {
	    		hiddenStatesWithoutBias[r][c - 1] = hiddenProbs[r][c];
	    	}
	    }

	    // Ignore the bias units.
	    return hiddenStatesWithoutBias;
	}
	
	@Override
	public double[][] run_hidden(double[][] hiddenData) {

		int numberOfExamples = hiddenData.length;
		int numberOfChoicesPerExample = hiddenData[0].length;
	    
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
	    			dataWithBias[r][c] = hiddenData[r][c-1];
	    		}
	    	}
	    }
	    
	    double[][] weightsT = transposeMatrix(weights);
	    // Calculate the activations of the visible units.
	    double[][] visibleActivations = multiplicar(dataWithBias, weightsT);
	  
	    // Calculate the probabilities of turning the visible units on.
	    double[][] visibleProbs = this.logistic(visibleActivations);
	    
	    // Turn the visible units on with their specified probabilities.
	    /*
	    for(int r = 0; r < visibleStates.length; r++) {
	    	for(int c = 0; c < visibleStates[0].length; c++) {
	    		visibleStates[r][c] = visibleProbs[r][c] > randomGenerator.nextDouble() ? 1 : 0;
	    	}
	    }
	    */
	    
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
	
	@Override
	public void setWeights(double[][] weights) {
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


	public static void main(String[] args) {
		RBMOriginal rbm = new RBMOriginal(6, 2, 0.1f);

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

		rbm.train(data, 1000);
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
		
		for(int i = 0; i < 1; i++) {
			rbm.printMatrix("User", user);
			double[][] result1 = rbm.run_visual(user);
			rbm.printMatrix("Result", result1);
			double[][] result2 = rbm.run_hidden(result1);
			rbm.printMatrix("Check", result2);
			System.out.println("");
		}
		
	}

}
