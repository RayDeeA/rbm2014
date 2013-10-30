package iconn.htw.main;
import java.util.Random;


public class DefaultRandom implements IRandomFunction {

	private final Random random;
	private final float scale;
	private final float offset;

	public DefaultRandom(long seed, float scale, float offset) {

		this.random = new Random(seed);
		this.scale = scale;
		this.offset = offset;
	}
	
	public DefaultRandom(float scale, float offset) {

		this.random = new Random();
		this.scale = scale;
		this.offset = offset;
	}


	@Override
	public double nextNumber() {
		return scale * random.nextDouble() + offset;
	}

}
