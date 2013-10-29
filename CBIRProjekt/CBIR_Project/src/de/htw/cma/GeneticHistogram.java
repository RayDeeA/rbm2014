package de.htw.cma;

import java.nio.file.Paths;
import java.util.Random;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.histogram.IDWHistogramFactory;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public class GeneticHistogram implements IObjectiveFunction{

	private static Random rand = new Random();
	private String imageSetName;
	private IDWHistogramFactory factory;
	private CBIREvaluation eval;
	private int size;
	private double[] initialValues;
	private double stdDev = 5;
	
	public GeneticHistogram(int size, String imageSetName, CBIREvaluation evalulation, IDWHistogramFactory factory) {
		this.imageSetName = imageSetName;
		this.factory = factory;
		this.eval = evalulation;
		this.size = size;
		this.initialValues = getInitialRandomValues();
	}
	
	public GeneticHistogram(double[][] dps, double stdDev, String imageSetName, CBIREvaluation evalulation, IDWHistogramFactory factory) {
		this.imageSetName = imageSetName;
		this.factory = factory;
		this.eval = evalulation;
		this.size = dps.length;
		this.initialValues = convert(dps);
		this.stdDev = stdDev;
	}
	
	public void run() {
		IObjectiveFunction fitfun = this;
		
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		cma.setDimension(size * 3); // overwrite some loaded properties
		cma.setInitialX(initialValues); // in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(stdDev); // also a mandatory setting 
		cma.options.stopFitness = 1e-14;       // optional setting

		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];

		// initial output to files
		cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

		// iteration loop
		while(cma.stopConditions.getNumber() == 0) {

			double bestFitness = 0;
			
            // --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of solutions
			for(int i = 0; i < pop.length; ++i) {    // for each candidate solution i
            	// a simple way to handle constraints that define a convex feasible domain  
            	// (like box constraints, i.e. variable boundaries) via "blind re-sampling" 
            	                                       // assumes that the feasible domain is convex, the optimum is  
				while (!fitfun.isFeasible(pop[i]))     //   not located on (or very close to) the domain boundary,  
					pop[i] = cma.resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are  
                                                       //   sufficiently small to prevent quasi-infinite looping here
                // compute fitness/objective value	
				double currentFitness = fitfun.valueOf(pop[i]); // fitfun.valueOf() is to be minimized
				if(bestFitness < currentFitness)
					bestFitness = currentFitness;				
				fitness[i] = currentFitness;
			}
			cma.updateDistribution(fitness);         // pass fitness array to update search distribution
            // --- end core iteration step ---

			// output to files and console 
			System.out.println("MAP: "+(1-bestFitness));
			cma.writeToDefaultFiles();
//			int outmod = 10;
//			if (cma.getCountIter() % (10*outmod) == 1) {
				cma.printlnAnnotation(); // might write file as well
				save(cma.getBestX());
//			}
//			if (cma.getCountIter() % outmod == 1)
				cma.println(); 
		}
		// evaluate mean value as it is the best estimator for the optimum
		cma.setFitnessOfMeanX(fitfun.valueOf(cma.getMeanX())); // updates the best ever solution 

		// final output
		cma.writeToDefaultFiles(1);
		cma.println();
		cma.println("Terminated due to");
		for (String s : cma.stopConditions.getMessages()) 
			cma.println("  " + s);
		cma.println("best function value " + cma.getBestFunctionValue() + " at evaluation " + cma.getBestEvaluationNumber());
	}
	
	
	private double[] getInitialRandomValues() {
		
		int pos = 0;
		double[] result = new double[size*3];
		
		for (int rIndex = 0; rIndex < size; rIndex++) {
			result[pos++] = rand.nextInt(255);
			result[pos++] = rand.nextInt(255);
			result[pos++] = rand.nextInt(255);
		}
		return result;
	}
	
//	private double[] getInitialValues() {
//		
//		int pos = 0;
//		int delta = 256/steps;
//		double dh = delta / 2;
//		double[] result = new double[steps*steps*steps*3];
//		
//		// Distanzen vom aktuelle Punkt zum Ursprung
//		// z.B. size = 4 ist delta 64 ersten Werte sind 32 -> 96 -> 160
//		for (int rIndex = 0; rIndex < steps; rIndex++)
//			for (int gIndex = 0; gIndex < steps; gIndex++)
//				for (int bIndex = 0; bIndex < steps; bIndex++) {
//					result[pos++] = dh + rIndex * delta;
//					result[pos++] = dh + gIndex * delta;
//					result[pos++] = dh + bIndex * delta;
//				}
//		return result;
//	}
	
	private void save(double[] input) {
		String name = size+"Histogram.gh";
		double[][] histogramDataPoints = convert(input);
		factory.setHistogramDataPoints(histogramDataPoints);
		factory.save(Paths.get("solutions/"+imageSetName+"/"+name));
	}
	
	private double[] convert(double[][] input) {
		double[] vals = new double[input.length * 3];
		for (int z = 0, pos = 0; z < input.length; z++)
			for (int s = 0; s < 3; s++, pos++)
				vals[pos] = input[z][s];
		return vals;
	}
	
	private double[][] convert(double[] input) {
		// wandle das input Array in ein 2D Array um
		double[][] vals = new double[size][3];
		for (int z = 0, pos = 0; z < size; z++)
			for (int s = 0; s < 3; s++, pos++)
				vals[z][s] = input[pos];
		
		// wendere die Werte und berechne 
		return vals;
	}
	
	@Override
	public double valueOf (double[] input) {
		double[][] histogramDataPoints = convert(input);
		factory.setHistogramDataPoints(histogramDataPoints);
		eval.getSorter().getFeatureVectors();
		return 1 - eval.testAll(false, "ColorSpaceDistance");
	}
		
	@Override
	public boolean isFeasible(double[] x) {
		for (double d : x) 
			if(d < 0 || d > 255)
				return false;
		return true; 
	}
	
}
