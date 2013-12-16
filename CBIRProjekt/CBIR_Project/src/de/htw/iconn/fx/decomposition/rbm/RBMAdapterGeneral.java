package de.htw.iconn.fx.decomposition.rbm;

public class RBMAdapterGeneral extends ARBMAdapter {

	public RBMAdapterGeneral(IRBM rbm) {
		super(rbm);
	}

        @Override
	public double[][] getHidden(double[][] data, boolean binarizeHidden) {
		return rbm.getHidden(data, binarizeHidden);
	}
	
        @Override
    public double[][] getVisible(double[][] data, boolean useVisibleStates) {
    	return rbm.getVisible(data, useVisibleStates);
    }
}
