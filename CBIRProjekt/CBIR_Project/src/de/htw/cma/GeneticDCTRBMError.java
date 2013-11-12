package de.htw.cma;

import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.DCTRBM;
import de.htw.cbir.ImageManager;
import de.htw.cbir.gui.RBMVisualizationFrame;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;

public class GeneticDCTRBMError {

	private static Random rand = new Random();
	private ImageManager imageManager;
	
	private CBIREvaluation eval;
	private double[] initialValues;
	private double stdDev = 0.05;
	private DCTRBM rbm;
	private ForkJoinPool pool;
	
	public GeneticDCTRBMError(DCTRBM rbm, ImageManager imageManager, CBIREvaluation evalulation, ForkJoinPool pool) {
		this.imageManager	= imageManager;
		this.rbm 			= rbm;
		this.eval			= evalulation;
		this.pool 			= pool;
		this.initialValues 	= getInitialRandomValues();
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

	public void run() {
		
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		cma.setDimension(initialValues.length); // overwrite some loaded properties
		cma.setInitialX(initialValues); // in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(stdDev); // also a mandatory setting 
		cma.options.stopFitness = 1e-14;       // optional setting

		// initial output to files
		cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

		// iteration loop
		while(cma.stopConditions.getNumber() == 0) {

            // --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of solutions
			
			// erstelle nur g��ltige candidaten
			GeneticForkResample ft = new GeneticForkResample(this, cma, pop, 0, pop.length);
			pool.invoke(ft);
			
			for(int i = 0; i < pop.length; ++i)
				while (!isFeasible(pop[i]))
					pop[i] = cma.resampleSingle(i);
				
            // compute fitness/objective value	
			double[] currentFitnesses = valueOfParallel(pop); // fitfun.valueOf() is to be minimized
			cma.updateDistribution(currentFitnesses);         // pass fitness array to update search distribution
            // --- end core iteration step ---

			// output to files and console 
			cma.writeToDefaultFiles();
			System.out.println("-------------------------");
			MAP(cma.getBestX());
			save(cma.getBestX());
			System.out.println("Fitnes: "+(1-valueOf(cma.getBestX())));
			System.out.println("RBM raw Error: "+getRawError(cma.getBestX()));
		}
		// evaluate mean value as it is the best estimator for the optimum
		cma.setFitnessOfMeanX(valueOf(cma.getMeanX())); // updates the best ever solution 

		// final output
		cma.writeToDefaultFiles(1);
		cma.println();
		cma.println("Terminated due to");
		for (String s : cma.stopConditions.getMessages()) 
			cma.println("  " + s);
		cma.println("best function value " + cma.getBestFunctionValue() + " at evaluation " + cma.getBestEvaluationNumber());
	}
	
	private void MAP(double[] input) {
		double[][] weights = convert(input);
		rbm.setWeights(weights);
		
		eval.getSorter().getFeatureVectors();
		double map = eval.testAll(false, "DCT RBM Error reduction");
		System.out.println("MAP: "+map);
	}

	private void save(double[] input) {

		// speichere die besten gewichte
		double[][] weights = convert(input);
		rbm.setWeights(weights);
		
		int num_visible = rbm.getVisibleCount();
		int num_hidden = rbm.getHiddenCount();
		String name = num_visible+"x"+num_hidden+"RBM.rbm";
		rbm.save(Paths.get("solutions/"+imageManager.getImageSetName()+"/"+name));
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
	
	public double[][] convert(double[] input) {
		int numV = rbm.getVisibleCount() + 1;
		int numH = rbm.getHiddenCount() + 1;		
		double[][] weights = new double[numV][numH];
		for (int v = 0, pos = 0; v < numV; v++)
			for (int h = 0; h < numH; h++, pos++)
				weights[v][h] = input[pos];
		return weights;
	}
	
	public double getRawError (double[] input) {
		double[][] weights = convert(input);
		rbm.setWeights(weights);
		return rbm.getRawError(imageManager.getImages());
	}

	
	public double valueOf (double[] input) {
		DCTRBM testRBM = rbm.shallowCopy();
		double[][] weights = convert(input);
		testRBM.setWeights(weights);
		double err = testRBM.getError(imageManager.getImages());
		//System.out.println("error"+err);
		return err;
	}
	
	public boolean isFeasible(double[] x) {
		return true; 
	}

	public double[] valueOfParallel(double[][] input) {
		double[] fitnessValues = new double[input.length];
		GeneticForkFitness ft = new GeneticForkFitness(this, input, 0, input.length, fitnessValues);
		pool.invoke(ft);
		return fitnessValues;
	}
}
