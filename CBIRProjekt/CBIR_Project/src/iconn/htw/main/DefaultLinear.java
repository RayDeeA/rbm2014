package iconn.htw.main;

public class DefaultLinear implements IFloatFunction {

	@Override
	public double function(double x) {
		return x;
	}
	
	public double function(double x, double min, double max) {
		return (x-min)/(max-min);
	}

}
