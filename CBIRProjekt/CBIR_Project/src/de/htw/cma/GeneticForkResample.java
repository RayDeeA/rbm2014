package de.htw.cma;

import fr.inria.optimization.cmaes.CMAEvolutionStrategy;

import java.util.concurrent.RecursiveAction;

public class GeneticForkResample extends RecursiveAction {

	private static final long serialVersionUID = 7409860587315610289L;
	protected static int sThreshold = 100;
	
    private int start;
    private int length;
    private GeneticDCTRBMError geneticAlgo;
    private CMAEvolutionStrategy cma;
    private double[][] population;
    
	public GeneticForkResample(GeneticDCTRBMError geneticAlgo, CMAEvolutionStrategy cma, double[][] population, int start, int length) {
		this.start = start;
		this.length = length;
		this.cma = cma;
		this.geneticAlgo = geneticAlgo;
		this.population = population;
	}
	
    protected void computeDirectly() {
        for (int index = start; index < start + length; index++) {	
        	while (!geneticAlgo.isFeasible(population[index]))
        		population[index] = cma.resampleSingle(index);
        }
    }
    
	@Override
	protected void compute() {
	    if (length < sThreshold) {
	        computeDirectly();
	        return;
	    }
	    
	    int split = length / 2;
	    
	    invokeAll(new GeneticForkResample(geneticAlgo, cma, population, start, split),
	              new GeneticForkResample(geneticAlgo, cma, population, start + split, length - split));
	}
}
