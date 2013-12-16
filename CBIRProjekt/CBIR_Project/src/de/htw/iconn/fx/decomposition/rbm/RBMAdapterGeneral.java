package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.awt.image.BufferedImage;

public class RBMAdapterGeneral extends ARBMAdapter {

	public RBMAdapterGeneral(IRBM rbm) {
		super(rbm);
	}

	public double[][] getHidden(double[][] data, boolean binarizeHidden) {
		return rbm.getHidden(data, binarizeHidden);
	}
	
    public double[][] getVisible(double[][] data, boolean useVisibleStates) {
    	return rbm.getVisible(data, useVisibleStates);
    }
}
