package de.htw.cma;

import java.nio.file.Paths;

import de.htw.cbir.CBIREvaluation;
import de.htw.color.ColorMetric;
import de.htw.interpolation.RGBInterpolation;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public class GeneticColorDistance implements IObjectiveFunction{

	private String imageSetName;
	private CBIREvaluation eval;
	private int steps;
	private int delta;
	
	public GeneticColorDistance(int size, String imageSetName, CBIREvaluation evalulation) {
		this.imageSetName = imageSetName;
		this.eval = evalulation;
		this.steps = size;
		this.delta = 256/size;
	}
	
	public void run() {
		IObjectiveFunction fitfun = this;
		
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		cma.setDimension(64 * 3); // overwrite some loaded properties
		cma.setInitialX(getInitialValues()); // in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(5); // also a mandatory setting 
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
//			System.out.println("MAP: "+(1-bestFitness));
			cma.writeToDefaultFiles();
			int outmod = 10;
			if (cma.getCountIter() % (10*outmod) == 1) {
				cma.printlnAnnotation(); // might write file as well
				save(cma.getBestX());
			}
			if (cma.getCountIter() % outmod == 1)
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
	
	private double[] getInitialValues() {
		
		int pos = 0;
		int dh = delta / 2;
		double[] result = new double[steps*steps*steps*3];
		
		// Distanzen vom aktuelle Punkt zum Ursprung
		// z.B. size = 4 ist delta 64 ersten Werte sind 32 -> 96 -> 160
		for (int rIndex = 0; rIndex < steps; rIndex++)
			for (int gIndex = 0; gIndex < steps; gIndex++)
				for (int bIndex = 0; bIndex < steps; bIndex++) {
					result[pos++] = dh + rIndex * delta;
					result[pos++] = dh + gIndex * delta;
					result[pos++] = dh + bIndex * delta;
				}
		return result;
	}
	
	private void save(double[] input) {
		String name = "4x4x4RGB.csa";
		RGBInterpolation gds = convert(input);
		gds.save(Paths.get("solutions/"+imageSetName+"/"+name));
	}
	
	private RGBInterpolation convert(double[] input) {
		// wandle das input Array in ein 4D Array um, 
		// damit es als GeneticSolution verwendet werden kann
		double[][][][] vals = new double[steps][steps][steps][3];
		for (int z = 0, pos = 0; z < steps; z++)
			for (int y = 0; y < steps; y++)
				for (int x = 0; x < steps; x++)
					for (int s = 0; s < 3; s++, pos++)
						vals[z][y][x][s] = input[pos];
		
		// wendere die Werte und berechne 
		return new RGBInterpolation(steps, vals);
	}
	
	@Override
	public double valueOf (double[] input) {
		RGBInterpolation gds = convert(input);
		ColorMetric.setDistanceInterface(gds);
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
