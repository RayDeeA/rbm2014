package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class BasicSigmoidMatrixFunction extends MatrixFunctions implements ILogistic {

	// http://en.wikipedia.org/wiki/Generalised_logistic_function
	// 0.1 + (0.9-0.1) / ((1+1*exp(-0.25 *x)) 1/1) 
	
	@Override
	public DoubleMatrix function(DoubleMatrix m) {
		return function(m, 0.3, 0.7, 0.75);
	}
	
	public DoubleMatrix function(DoubleMatrix m, double lowerAsymptote, double upperAsymptote, double growthRate) {
		final DoubleMatrix negM = m.neg().mmul(growthRate);
		final DoubleMatrix negExpM = MatrixFunctions.exp(negM);
		final DoubleMatrix negExpPlus1M = negExpM.add(1.0);
		
		final DoubleMatrix oneDivideNegExpPlusOneM = MatrixFunctions.pow(negExpPlus1M, -1 * (lowerAsymptote + (upperAsymptote-lowerAsymptote))); 
		
		return oneDivideNegExpPlusOneM;
	}

}
