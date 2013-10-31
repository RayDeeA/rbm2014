package iconn.htw.main;

public class RBM {

	private final IFloatFunction sigmoidfunc;
	private final IRandomFunction random;
	private final IRandomFunction activationRandom;
	
	private final double learningRate = 0.1f;
	
	private Matrix weights;
	
	
	public RBM(int visible, int hidden) {
		sigmoidfunc = new DefaultSigmoid();
		random = new GaussianRandom(0.1f, 0.0f);
		activationRandom = new DefaultRandom(1.0f, 0.0f);

		weights = Matrix.createRandomMatrix(visible, hidden, random).expandfirstColumnWithZeros().expandfirstRowWithZeros();//.mult(learningRate);
	}
	
	public void train(final Matrix data, final int epochs) {
		
		final Matrix dataWithBias = data.expandfirstColumnWithOnes();
		for (int i = 0; i < epochs; i++) {
			
			final Matrix positiveHiddenActivations = dataWithBias.mult(weights);
			final Matrix positiveHiddenProbalities = positiveHiddenActivations.applyFloatFunction(sigmoidfunc);
			
			final Matrix positiveHiddenStates = positiveHiddenProbalities.isGreaterThan(
					Matrix.createRandomMatrix(
							positiveHiddenProbalities.getHeight(), 
							positiveHiddenProbalities.getWidth(), 
							activationRandom));
			
			final Matrix positiveAssociations = dataWithBias.trans().mult(positiveHiddenProbalities);
			final Matrix negativeVisibleActivations = positiveHiddenStates.mult(weights.trans());
			final Matrix negativeVisibleProbabilities = negativeVisibleActivations.applyFloatFunction(sigmoidfunc);
			final Matrix negativeHiddenActivations = negativeVisibleProbabilities.mult(weights);
			final Matrix negativeHiddenProbabilities = negativeHiddenActivations.applyFloatFunction(sigmoidfunc);	
			final Matrix negativeAssociations = negativeVisibleProbabilities.trans().mult(negativeHiddenProbabilities);
			
			weights = weights.add(positiveAssociations.subt(negativeAssociations).mult(1 / data.getHeight()).mult(learningRate));
			
//			System.out.println("Mean Square Error:  " + dataWithBias.subt(negativeVisibleProbabilities).applyFloatFunction(new Square()).sumValues());
		}
	}
	
	public Matrix runVisible(Matrix data) {
		
		int numberOfExamples = data.getHeight();
		int numberOfChoicesPerExample = data.getWidth();
		
		Matrix hiddenStates = Matrix.createMatrixFilledWithNumber(numberOfExamples, numberOfChoicesPerExample + 1, 1.0f);
		Matrix dataWithBias = data.expandfirstColumnWithOnes();
		
		Matrix hiddenProbs = dataWithBias.mult(this.weights).applyFloatFunction(sigmoidfunc);
		hiddenStates = hiddenProbs.isGreaterThan(Matrix.createRandomMatrix(hiddenProbs.getHeight(), hiddenProbs.getWidth(), activationRandom));
		
		return hiddenStates.removefirstColumn();
	}
	
	public Matrix runHidden(Matrix data) {
		
		int numberOfExamples = data.getHeight();
		int numberOfChoicesPerExample = data.getWidth();
		
		Matrix visibleStates = Matrix.createMatrixFilledWithNumber(numberOfExamples, numberOfChoicesPerExample + 1, 1.0f);
		Matrix dataWithBias = data.expandfirstColumnWithOnes();

		Matrix visibleProbs = dataWithBias.mult(this.weights.trans()).applyFloatFunction(sigmoidfunc);
		visibleStates = visibleProbs.isGreaterThan(Matrix.createRandomMatrix(visibleProbs.getHeight(), visibleProbs.getWidth(), activationRandom));
		
		return visibleStates.removefirstColumn();
	}
	
	public static void main(String[] args) {
		
		double similar = 0.0;
		int testCount = 10;
		
		final Matrix trainData = new Matrix(
				new double[][]{
						// Alice: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
						{ 1, 1, 1, 0, 0 },
						// Bob: (Harry Potter = 1, Avatar = 0, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). SF/fantasy fan, but doesn't like Avatar.
						{ 1, 0, 1, 0, 0 },
						// Carol: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
						{ 1, 1, 1, 0, 0 },
						// David: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
						{ 0, 0, 1, 1, 1 },
						// Eric: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 0, Glitter = 0). Oscar winners fan, except for Titanic.
						{ 0, 0, 1, 1, 0 },
						// Fred: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator = 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
						{ 0, 0, 1, 1, 1 },
				});
		
		Matrix userInput = new Matrix( 
				new double[][]{
						{ 0, 0, 1, 1, 1 },
						{ 1, 1, 0, 0, 0 }
					}
				);
		
		final RBM rbm = new RBM(5,2); 
		rbm.train(trainData, 1_000);
		for (int i = 0; i < testCount; i++) {
			Matrix result = rbm.runHidden(rbm.runVisible(userInput));
			
			System.out.println(result);
			
			similar += userInput.hasCommonValues(result);
		}
		System.out.println("Common Values in percent:  " + similar / testCount);
	}

}
