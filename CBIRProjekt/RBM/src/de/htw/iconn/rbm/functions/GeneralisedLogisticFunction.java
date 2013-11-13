package de.htw.iconn.rbm.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class GeneralisedLogisticFunction extends MatrixFunctions implements ILogistic {
	
	/*
	http://en.wikipedia.org/wiki/Generalised_logistic_function
	A + (K-A) / ((1+exp(-B *x)) 1/v)
	
	A: the lower asymptote;
	K: the upper asymptote. If A=0 then K is called the carrying capacity;
	B: the growth rate;
	ν>0 : affects near which asymptote maximum growth occurs.
	*/
	
	@Override
	public DoubleMatrix function(DoubleMatrix m) {
		return function(m, 0, 1, 1);
	}
	
	public DoubleMatrix function(DoubleMatrix m, double A, double K, double B) {
		final DoubleMatrix negM = m.neg().mmul(B);
		final DoubleMatrix negExpM = MatrixFunctions.exp(negM);
		final DoubleMatrix negExpPlus1M = negExpM.add(1.0);
		
		final DoubleMatrix oneDivideNegExpPlusOneM = MatrixFunctions.pow(negExpPlus1M, -1 * (A + (K-A))); 
		
		return oneDivideNegExpPlusOneM;
	}

}
