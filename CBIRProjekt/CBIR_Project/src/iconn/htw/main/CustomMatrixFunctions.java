package iconn.htw.main;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;


public class CustomMatrixFunctions extends MatrixFunctions {
	
	// 1.0 / (1.0 + Math.exp(-m_ij)
	
	public static DoubleMatrix sigmoid(DoubleMatrix m) {
		
		final DoubleMatrix negM = m.neg();
		final DoubleMatrix negExpM = MatrixFunctions.exp(negM);
		final DoubleMatrix negExpPlus1M = negExpM.add(1.0);
		final DoubleMatrix OneDivideNegExpPlusOneM = MatrixFunctions.pow(negExpPlus1M, -1.0); 
		
		
		return OneDivideNegExpPlusOneM;
		
	}
	
}
