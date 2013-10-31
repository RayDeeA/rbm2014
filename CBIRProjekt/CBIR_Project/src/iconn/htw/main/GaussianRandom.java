package iconn.htw.main;
import java.util.Random;


public class GaussianRandom implements IRandomFunction {

	private final Random random;
	private final float scale;
	private final float offset;


	public GaussianRandom(long seed, float scale, float offset) {

		this.random = new Random(seed);
		this.scale = scale;
		this.offset = offset;
	}
	
	public GaussianRandom(float scale, float offset) {

		this.random = new Random();
		this.scale = scale;
		this.offset = offset;
	}


	@Override
	public double nextNumber() {
		return scale *  random.nextGaussian() + offset;
	}
}
