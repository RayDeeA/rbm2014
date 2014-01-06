package de.htw.iconn.rbm;

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
	public double[][] getHidden(double[][] data, boolean binarizeHidden) {
		return rbm.getHidden(data, binarizeHidden);
	}
	
        @Override
        public double[][] getVisible(double[][] data, boolean binarizeVisible) {
            return rbm.getVisible(data, binarizeVisible);
        }

        @Override
	public double[][] getWeights() {
		return rbm.getWeights();
	}
}
