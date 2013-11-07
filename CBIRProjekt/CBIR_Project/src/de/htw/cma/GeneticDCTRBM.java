package de.htw.cma;

import java.nio.file.Paths;
import java.util.Random;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.DCTRBM;
import de.htw.cbir.gui.RBMVisualizationFrame;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public class GeneticDCTRBM implements IObjectiveFunction{

	private static Random rand = new Random();
	private String imageSetName;
	
	private CBIREvaluation eval;
	private double[] initialValues;
	private double stdDev = 5;
	private DCTRBM rbm;
	
	public GeneticDCTRBM(DCTRBM rbm, String imageSetName, CBIREvaluation evalulation) {
		this.imageSetName	= imageSetName;
		this.rbm 			= rbm;
		this.eval			= evalulation;
		this.initialValues = getInitialRandomValues();
	}
	
	private double[] getInitialRandomValues() {
		int num_visible = rbm.getVisibleCount();
		int num_hidden = rbm.getHiddenCount();
		
		// initial zuf��llige Gewichte
		double[][] weights = new double[num_visible+1][num_hidden+1];
		for (int v = 1; v < num_visible+1; v++) 
			for (int h = 1; h < num_hidden+1; h++) 
				weights[v][h] = 0.1*rand.nextGaussian();
		
		return convert(weights);
	}

	public void run(RBMVisualizationFrame frame) {
		IObjectiveFunction fitfun = this;
		
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		cma.setDimension(initialValues.length); // overwrite some loaded properties
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
			//System.out.println("MAP: "+(1-bestFitness));
			cma.writeToDefaultFiles();
			int outmod = 10;
			
			// error of 42 added because of signature of update method to write on label
			frame.update(convert(cma.getBestRecentX()), 42.0);
			if (cma.getCountIter() % (10*outmod) == 1) {
				cma.printlnAnnotation(); // might write file as well
				save(cma.getBestX(), frame);
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
	
	private void save(double[] input, RBMVisualizationFrame frame) {

		// speichere die besten gewichte
		double[][] weights = convert(input);
		//rbm.setWeights(weights);
		// error of 42 added because of signature of update method to write on label
		frame.update(weights, 42.0);
		
		int num_visible = rbm.getVisibleCount();
		int num_hidden = rbm.getHiddenCount();
		String name = num_visible+"x"+num_hidden+"RBM.rbm";
		rbm.save(Paths.get("solutions/"+imageSetName+"/"+name));
	}
	
	private double[] convert(double[][] weights) {
		int numV = rbm.getVisibleCount() + 1;
		int numH = rbm.getHiddenCount() + 1;				
		double[] vals = new double[numV * numH];
		for (int v = 0, pos = 0; v < numV; v++)
			for (int h = 0; h < numH; h++, pos++)
				vals[pos] = weights[v][h];
		return vals;
	}
	
	private double[][] convert(double[] input) {
		int numV = rbm.getVisibleCount() + 1;
		int numH = rbm.getHiddenCount() + 1;		
		double[][] weights = new double[numV][numH];
		for (int v = 0, pos = 0; v < numV; v++)
			for (int h = 0; h < numH; h++, pos++)
				weights[v][h] = input[pos];
		return weights;
	}
	
	@Override
	public double valueOf (double[] input) {
		double[][] weights = convert(input);
		rbm.setWeights(weights);
		eval.getSorter().getFeatureVectors();
		return 1 - eval.testAll(false, "ColorSpaceDistance");
	}
		
	@Override
	public boolean isFeasible(double[] x) {
		return true; 
	}
	
}
