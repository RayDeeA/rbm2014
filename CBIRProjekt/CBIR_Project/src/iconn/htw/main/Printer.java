package iconn.htw.main;

import java.text.DecimalFormat;

import org.jblas.DoubleMatrix;

public class Printer {
	
	
	public static void printMatrix(final String name, final double[][] m) {
		
		DecimalFormat f = new DecimalFormat("#0.000");

		String str = name + "\n";
		for (int i = 0; i < m.length; i++) {
			str += "[";
			for (int j = 0; j < m[0].length; j++) {
			
				str += f.format(m[i][j]);
				
				if(j != m[0].length - 1)
					str +="\t";
			}
			str += "]\n\n";
		}
		System.out.println(str);
	}
	
	public static void printMatrix(final String name, final DoubleMatrix m) {
		Printer.printMatrix(name, m.toArray2());
	}

}
