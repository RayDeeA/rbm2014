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

		final Matrix dataWithBias = data.expandfirstColumnWithOnes();
		
		for(int i = 0; i < epochs; i++) {
			
			final Matrix positiveHiddenProbs = dataWithBias.mult(weights).applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix hiddenStates = positiveHiddenProbs.isGreaterThan(Matrix.createRandomMatrix(positiveHiddenProbs.getHeight(), positiveHiddenProbs.getWidth(), activationRandom));
			
			final Matrix positiveFeedback = dataWithBias.trans().mult(positiveHiddenProbs);
			
			
			
			final Matrix negativeVisibleProbs = hiddenStates.mult(weights.trans())
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc).fillFirstColumnWithOnes();
			
			final Matrix negativeHiddenProbs = negativeVisibleProbs.mult(weights)
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix negativeFeedback = negativeVisibleProbs.trans().mult(negativeHiddenProbs);
			
			weights = weights.add(positiveFeedback.subt(negativeFeedback).mult(learningRate / dataWithBias.getHeight()));
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
		visibleProbs = visibleProbs.isGreaterThan(Matrix.createRandomMatrix(visibleProbs.getHeight(), visibleProbs.getWidth(), activationRandom));
		
		return visibleProbs.removefirstColumn();
	}
	
	public Matrix useVisible(final Matrix data) {
		final Matrix hiddenProbs = data.mult(weights).
				applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
		
		final Matrix hiddenStates = hiddenProbs.isGreaterThan(Matrix.createRandomMatrix(
									hiddenProbs.getHeight(),
									hiddenProbs.getWidth(), activationRandom));
		return hiddenStates;
	}
	
	public Matrix useHidden(final Matrix data) {
		final Matrix visibleProbs = data.mult(weights.trans()).
				applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
		
		final Matrix visibleStates = visibleProbs.isGreaterThan(Matrix.createRandomMatrix(
									visibleProbs.getHeight(),
									visibleProbs.getWidth(), activationRandom));
		return visibleStates;
	}
	
	public static String arrayToString(final double[] array) {
		String str = "";
		
		for (int i = 0; i < array.length; i++) {
			str += array[i] + (i != array.length -1 ? ", " : "");
		}
		return str;
	}
	
	public static void main(String[] args) {
		final RBM rbm = new RBM(6,2);
		//System.out.println(rbm.weights.toString()); 
		rbm.train(new Matrix(new double[][]{{1, 1, 1, 0, 0, 0}, {1, 0, 1, 0, 0, 0}, {1, 1, 1, 0, 0, 0}, {0, 0, 1, 1, 1, 0}, {0, 0, 1, 1, 0, 0}, {0, 0, 1, 1, 1, 0}}), 50000);
		//System.out.println(rbm.weights.toString());
		
		Matrix userInput = new Matrix( 
				new double[][]{
						{1,1,1,0,0,0},
						{0,0,0,1,1,0}
					}
				);
		Matrix hiddenStates = rbm.runVisible(userInput);
		
		System.out.println(userInput);
		System.out.println(hiddenStates);
		System.out.println(rbm.runHidden(hiddenStates));
	}

}
