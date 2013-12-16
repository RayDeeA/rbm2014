package de.htw.iconn.fx.decomposition.rbm;

public abstract class ARBMAdapter implements IRBM{

	protected IRBM rbm;

	protected ARBMAdapter(IRBM rbm) {
		this.rbm = rbm;
	}

        @Override
	public void train(double[][] data, int maxEpoche, boolean binarizeHidden, boolean binarizeVisible) {
		rbm.train(data, maxEpoche, binarizeHidden, binarizeVisible);
	}

        @Override
	public double error(double[][] data, boolean binarizeHidden, boolean binarizeVisible) {
		return rbm.error(data, binarizeHidden, binarizeVisible);
	}

        @Override
	public abstract double[][] getHidden(double[][] data, boolean binarizeHidden);
        
        @Override
        public abstract double[][] getVisible(double[][] data, boolean binarizeVisible);

        @Override
	public double[][] getWeights() {
		return rbm.getWeights();
	}
}
