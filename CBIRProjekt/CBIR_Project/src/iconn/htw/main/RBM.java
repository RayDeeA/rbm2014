package iconn.htw.main;

public class RBM {

	private final IFloatFunction sigmoidfunc;
	private final IFloatFunction activationfunc;
	private final IRandomFunction random;
	private final IRandomFunction activationRandom;
	
	private final double learningRate = 0.1f;
	
	private Matrix weights;
	
	
	public RBM(int visible, int hidden) {
		sigmoidfunc = new DefaultSigmoid();
		activationfunc = new DefaultActivation();
		random = new GaussianRandom(0.1f, 0.0f);
		activationRandom = new DefaultRandom(1.0f, 0.0f);

		weights = Matrix.createRandomMatrix(visible, hidden, random).expandfirstColumnWithZeros().expandfirstRowWithZeros();//.mult(learningRate);
	}
	
	public void train(final Matrix data, final int epochs) {
		final double maxMeanSquareError = 0.00002; 
		double currentMeanSquareError = Double.MAX_VALUE;
		
		final Matrix dataWithBias = data.expandfirstColumnWithOnes();
		
		for (int i = 0; i < epochs; i++) {
			
			final Matrix positiveHiddenProbs = dataWithBias.mult(weights).applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix hiddenStates = positiveHiddenProbs.isGreaterThan(Matrix.createRandomMatrix(positiveHiddenProbs.getHeight(), positiveHiddenProbs.getWidth(), activationRandom));
			
			final Matrix positiveFeedback = dataWithBias.trans().mult(positiveHiddenProbs);
			
			
			
			final Matrix negativeVisibleProbs = hiddenStates.mult(weights.trans())
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc).fillFirstColumnWithOnes();
			
			final Matrix negativeHiddenProbs = negativeVisibleProbs.mult(weights)
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix negativeFeedback = negativeVisibleProbs.trans().mult(negativeHiddenProbs);
			
			weights = weights.add(positiveFeedback.subt(negativeFeedback).mult(learningRate / dataWithBias.getHeight()));
			
			currentMeanSquareError = dataWithBias.subt(negativeVisibleProbs).applyFloatFunction(new Square()).sumValues();
			//System.out.println("Mean Square Error:  " + currentMeanSquareError);
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
		rbm.train(trainData, 5000);
		for (int i = 0; i < testCount; i++) {
			Matrix result = rbm.runHidden(rbm.runVisible(userInput));
			
			System.out.println(result);
			
			similar += userInput.hasCommonValues(result);
		}
		System.out.println("Common Values in percent:  " + similar / testCount);
	}

}
