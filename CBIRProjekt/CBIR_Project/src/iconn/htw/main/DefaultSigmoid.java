package iconn.htw.main;

public class DefaultSigmoid implements IFloatFunction {

	@Override
	public double function(double x) {
		return (double) (1.0 / (1.0 + Math.exp(-x)));
	}

}
