package de.htw.cma;

import java.util.concurrent.RecursiveAction;

public class GeneticForkFitness extends RecursiveAction {

	private static final long serialVersionUID = 7409860587315610289L;
	protected static int sThreshold = 100;
	
    private int start;
    private int length;
    private double[] fitnessValues;
    private GeneticDCTRBMError geneticAlgo;
    private double[][] weightValues;
    
	public GeneticForkFitness(GeneticDCTRBMError geneticAlgo, double[][] weightValues, int start, int length, double[] fitnessValues) {
		this.start = start;
		this.length = length;
		this.fitnessValues = fitnessValues;
		this.geneticAlgo = geneticAlgo;
		this.weightValues = weightValues;
	}
	
    protected void computeDirectly() {
        for (int index = start; index < start + length; index++) {
        	double[] weights = weightValues[index];
        	double fitness = geneticAlgo.valueOf(weights);
        	fitnessValues[index] = fitness;
        }
    }
    
	@Override
	protected void compute() {
	    if (length < sThreshold) {
	        computeDirectly();
	        return;
	    }
	    
	    int split = length / 2;
	    
	    invokeAll(new GeneticForkFitness(geneticAlgo, weightValues, start, split, fitnessValues),
	              new GeneticForkFitness(geneticAlgo, weightValues, start + split, length - split, fitnessValues));
	}
}
