package de.htw.iconn.fx.decomposition.rbm;

public abstract class ARBMAdapter {

	protected IRBM rbm;

	protected ARBMAdapter(IRBM rbm) {
		this.rbm = rbm;
	}

	public void train(double[][] data, int maxEpoche, boolean binarizeHidden, boolean binarizeVisible) {
		rbm.train(data, maxEpoche, binarizeHidden, binarizeVisible);
	}

	public double getError(double[][] data, boolean binarizeHidden, boolean binarizeVisible) {
		return rbm.error(data, binarizeHidden, binarizeVisible);
	}

	public double getRawError(double[][] data, boolean binarizeHidden, boolean binarizeVisible) {
		return rbm.error(data, binarizeHidden, binarizeVisible);
	}

	public abstract double[][] getHidden(double[][] data, boolean binarizeHidden);
    public abstract double[][] getVisible(double[][] data, boolean binarizeVisible);

	public double[][] getWeights() {
		return rbm.getWeights();
	}

    public IRBM getRBM() {
        return rbm;
    }
}
