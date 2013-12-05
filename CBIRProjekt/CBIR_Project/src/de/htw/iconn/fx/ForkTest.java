package de.htw.iconn.fx;

import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.concurrent.RecursiveAction;

import de.htw.cbir.model.Pic;

public class ForkTest extends RecursiveAction {

	private static final long serialVersionUID = 7409860587315610289L;
	protected static int sThreshold = 100;
	
	private Pic[] images;
    private int start;
    private int length;
    private double[] MAPs;
    private Evaluation evaluation;
    private TIntDoubleHashMap lookup;
    private PrecisionRecallTable table;
    
	public ForkTest(Evaluation evaluation, TIntDoubleHashMap lookup, PrecisionRecallTable table, Pic[] images, int start, int length, double[] MAPs) {
		this.images = images;
		this.start = start;
		this.length = length;
		this.MAPs = MAPs;
		this.evaluation = evaluation;
		this.lookup = lookup;
		this.table = table;
	}
	
    protected void computeDirectly() {
        for (int index = start; index < start + length; index++) {
        	Pic image = images[index];
        	double map = evaluation.test(image, index, lookup, table);
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
	    
	    invokeAll(new ForkTest(evaluation, lookup, table, images, start, split, MAPs),
	              new ForkTest(evaluation, lookup, table, images, start + split, length - split, MAPs));
	}
}
