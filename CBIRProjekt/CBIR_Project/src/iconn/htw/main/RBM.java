package iconn.htw.main;

public class RBM {

	private final IFloatFunction sigmoidfunc;
	private final IFloatFunction activationfunc;
	private final IRandomFunction random;
	private final IRandomFunction activationRandom;
	
	private final float learningRate = 0.1f;
	
	private Matrix weights;
	
	
	public RBM(int visible, int hidden) {
		sigmoidfunc = new DefaultSigmoid();
		activationfunc = new DefaultActivation();
		random = new GaussianRandom(0.1f, 0.0f);
		activationRandom = new DefaultRandom(1.0f, 0.0f);

		this.weights = Matrix.createRandomMatrix(visible, hidden, random).expandfirstColumnWithZeros().expandfirstRowWithZeros();//.mult(learningRate);
	}
	
	public void runVisible(final Vector data) {
		
	}
	
	public void train(final Matrix data, final int epochs) {

		final Matrix dataWithBias = data.expandfirstColumnWithOnes();
		
		for(int i = 0; i < epochs; i++) {
			
			final Matrix positiveHiddenProbs = dataWithBias.mult(weights).applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix hiddenStates = positiveHiddenProbs.isGreaterThan(Matrix.createRandomMatrix(
							positiveHiddenProbs.getHeight(),
							positiveHiddenProbs.getWidth(), activationRandom));
			
			final Matrix positiveFeedback = dataWithBias.trans().mult(positiveHiddenProbs);
			
			final Matrix negativeVisibleProbs = hiddenStates.mult(weights.trans())
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix negativeHiddenProbs = negativeVisibleProbs.mult(weights)
					.applyFloatFunction(activationfunc).applyFloatFunction(sigmoidfunc);
			
			final Matrix negativeFeedback = negativeVisibleProbs.trans().mult(negativeHiddenProbs);
			
			weights = weights.add(positiveFeedback.subt(negativeFeedback).mult(learningRate / dataWithBias.getHeight()));
			
		}
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
	
	public static String arrayToString(final float[] array) {
		String str = "";
		
		for (int i = 0; i < array.length; i++) {
			str += array[i] + (i != array.length -1 ? ", " : "");
		}
		return str;
	}
	
	public static void main(String[] args) {
		
		final RBM rbm = new RBM(5,3);
		System.out.println(rbm.weights.toString()); 
			rbm.train(new Matrix(new float[][]{{1,1,1,1,1}, {1,0,1,0,1}, {1,1,1,0,0}}), 1000);
		System.out.println(rbm.weights.toString());
	}

}
