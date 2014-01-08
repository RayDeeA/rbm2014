package de.htw.iconn.rbm;

public abstract class ARBMAdapter implements IRBM{

	protected IRBM rbm;

	protected ARBMAdapter(IRBM rbm) {
		this.rbm = rbm;
	}

        @Override
	public void train(float[][] data, StoppingCondition stop, boolean binarizeHidden, boolean binarizeVisible) {
		rbm.train(data, stop, binarizeHidden, binarizeVisible);
	}

        @Override
	public float error(float[][] data, boolean binarizeHidden, boolean binarizeVisible) {
		return rbm.error(data, binarizeHidden, binarizeVisible);
	}
        
        @Override
	public float[][] getHidden(float[][] data, boolean binarizeHidden) {
		return rbm.getHidden(data, binarizeHidden);
	}
	
        @Override
        public float[][] getVisible(float[][] data, boolean binarizeVisible) {
            return rbm.getVisible(data, binarizeVisible);
        }

        @Override
	public float[][] getWeights() {
		return rbm.getWeights();
	}
}
