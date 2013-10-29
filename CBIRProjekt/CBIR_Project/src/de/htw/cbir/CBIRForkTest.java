package de.htw.cbir;

import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.concurrent.RecursiveAction;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.PrecisionRecallTable;

public class CBIRForkTest extends RecursiveAction {

	private static final long serialVersionUID = 7409860587315610289L;
	protected static int sThreshold = 100;
	
	private Pic[] images;
    private int start;
    private int length;
    private double[] MAPs;
    private CBIREvaluation eval;
    private TIntDoubleHashMap lookup;
    private PrecisionRecallTable table;
    
	public CBIRForkTest(CBIREvaluation eval, TIntDoubleHashMap lookup, PrecisionRecallTable table, Pic[] images, int start, int length, double[] MAPs) {
		this.images = images;
		this.start = start;
		this.length = length;
		this.MAPs = MAPs;
		this.eval = eval;
		this.lookup = lookup;
		this.table = table;
	}
	
    protected void computeDirectly() {
        for (int index = start; index < start + length; index++) {
        	Pic image = images[index];
        	double map = eval.test(image, index, lookup, table);
        	MAPs[index] = map;
        }
    }
    
	@Override
	protected void compute() {
	    if (length < sThreshold) {
	        computeDirectly();
	        return;
	    }
	    
	    int split = length / 2;
	    
	    invokeAll(new CBIRForkTest(eval, lookup, table, images, start, split, MAPs),
	              new CBIRForkTest(eval, lookup, table, images, start + split, length - split, MAPs));
	}
}
