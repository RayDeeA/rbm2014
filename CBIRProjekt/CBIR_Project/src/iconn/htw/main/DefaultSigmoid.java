package iconn.htw.main;

public class DefaultSigmoid implements IFloatFunction {

	@Override
	public float function(float x) {
		return (float) (1.0 / (1.0 + Math.exp(-x)));
	}

}
