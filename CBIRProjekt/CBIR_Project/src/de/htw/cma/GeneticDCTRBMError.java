package de.htw.cma;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jblas.DoubleMatrix;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.CBIREvaluationModel;
import de.htw.cbir.RBMFeatureDCT;
import de.htw.cbir.ImageManager;
import de.htw.cbir.ARBMFeature;
import de.htw.iconn.rbm.IRBMLogger;
import fr.inria.optimization.cmaes.CMAEvolutionStrategy;

public class GeneticDCTRBMError {

	private static Random rand = new Random();
	private ImageManager imageManager;

	private CBIREvaluation evaluation;
	private double[] initialValues;
	private double stdDev = 0.05;
	private ARBMFeature rbm;
	private ForkJoinPool pool;

	private double bestMap = 0;
	private double bestError = 0;
	private double[][] bestWeights = null;

	public GeneticDCTRBMError(ARBMFeature rbm, ImageManager imageManager,
			CBIREvaluation evalulation, ForkJoinPool pool) {
		this.imageManager = imageManager;
		this.rbm = rbm;
		this.evaluation = evalulation;
		this.pool = pool;
		this.initialValues = getInitialRandomValues();
	}

	private double[] getInitialRandomValues() {
		int num_visible = rbm.getVisibleCount();
		int num_hidden = rbm.getHiddenCount();

		// initial zufällige Gewichte
		double[][] weights = new double[num_visible + 1][num_hidden + 1];
		for (int v = 1; v < num_visible + 1; v++)
			for (int h = 1; h < num_hidden + 1; h++)
				weights[v][h] = 0.1 * rand.nextGaussian();

		return convert(weights);
	}

	public void run() {

		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		cma.readProperties(); // read options, see file
								// CMAEvolutionStrategy.properties
		cma.setDimension(initialValues.length); // overwrite some loaded
												// properties
		cma.setInitialX(initialValues); // in each dimension, also setTypicalX
										// can be used
		cma.setInitialStandardDeviation(stdDev); // also a mandatory setting
		cma.options.stopFitness = 1e-14; // optional setting

		// initial output to files
		cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files
		
		CBIREvaluationModel evaluationModel = evaluation.getEvaluationModel();
		evaluationModel.setEvaluationType(CBIREvaluationModel.evaluationType.EVOLUTION);
		IRBMLogger logger = evaluationModel.getLogger();
		// iteration loop
		int p = 0;
		double error = 0.0;
		while (cma.stopConditions.getNumber() == 0) {
			// --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of
														// solutions

			// erstelle nur gültige candidaten
			GeneticForkResample ft = new GeneticForkResample(this, cma, pop, 0,
					pop.length);
			pool.invoke(ft);

			for (int i = 0; i < pop.length; ++i)
				while (!isFeasible(pop[i]))
					pop[i] = cma.resampleSingle(i);

			// compute fitness/objective value
			double[] currentFitnesses = valueOfParallel(pop); // fitfun.valueOf()
																// is to be
																// minimized
			cma.updateDistribution(currentFitnesses); // pass fitness array to
														// update search
														// distribution
			// --- end core iteration step ---

			// output to files and console
			cma.writeToDefaultFiles();
			System.out.println("=== " + rbm.getVisibleCount() + "x"
					+ rbm.getHiddenCount() + " - Population: " + p++
					+ "==========");

			double map = MAP(cma.getBestX());
			if (map > bestMap) {
				bestMap = map;
				bestWeights = convert(cma.getBestX());
			}
			error = getRawError(cma.getBestX());
			bestError = Math.max(error, bestError);

			System.out.println("MAP: " + map);
			System.out.println("Best MAP was:" + bestMap);
			System.out.println("Fitness: " + (1 - valueOf(cma.getBestX())));
			System.out.println("RBM raw Error: " + error);

			System.out.println("---------- Best Weights ----------");
			new DoubleMatrix(bestWeights).print();

			System.out.println("");

			save(cma.getBestX());
			
			if(p % evaluationModel.getCsvOutputFrequency() == 0){
				evaluationModel.setMAP(bestMap);
				evaluationModel.setEpochs(p);
				evaluationModel.setError(bestError);
				try {
					logger.stepCsvLog(evaluationModel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(p % evaluationModel.getXmlOutputFrequency() == 0){
				evaluationModel.setWeights2d(bestWeights);
				try {
					logger.stepXmlLogEvolution(evaluationModel);
				} catch (IOException | ParserConfigurationException | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		// evaluate mean value as it is the best estimator for the optimum
		cma.setFitnessOfMeanX(valueOf(cma.getMeanX())); // updates the best ever
														// solution

		// final output
		cma.writeToDefaultFiles(1);
		cma.println();
		cma.println("Terminated due to");
		for (String s : cma.stopConditions.getMessages())
			cma.println("  " + s);
		cma.println("best function value " + cma.getBestFunctionValue()
				+ " at evaluation " + cma.getBestEvaluationNumber());
		
		evaluationModel.setMAP(bestMap);
		evaluationModel.setWeights2d(bestWeights);
		evaluationModel.setEpochs(p);
		evaluationModel.setError(bestError);
	}

	private double MAP(double[] input) {
		double[][] weights = convert(input);
		rbm.setWeights(weights);

		evaluation.getSorter().getFeatureVectors();
		double map = evaluation.testAll(false, "DCT RBM Error reduction");

		return map;
	}

	private void save(double[] input) {

		// speichere die besten gewichte
		double[][] weights = convert(input);
		rbm.setWeights(weights);

		int num_visible = rbm.getVisibleCount();
		int num_hidden = rbm.getHiddenCount();
		String name = num_visible + "x" + num_hidden + "RBM.rbm";
		rbm.save(Paths.get("solutions/" + imageManager.getImageSetName() + "/"
				+ name));
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

	public double getRawError(double[] input) {
		double[][] weights = convert(input);
		rbm.setWeights(weights);
		return rbm.getRawError(imageManager.getImages(true));
	}

	public double valueOf(double[] input) {
		ARBMFeature testRBM = rbm.shallowCopy();
		double[][] weights = convert(input);
		testRBM.setWeights(weights);
		double err = testRBM.getError(imageManager.getImages(true));
		// System.out.println("error"+err);
		return err;
	}

	public boolean isFeasible(double[] x) {
		return true;
	}

	public double[] valueOfParallel(double[][] input) {
		double[] fitnessValues = new double[input.length];
		GeneticForkFitness ft = new GeneticForkFitness(this, input, 0,
				input.length, fitnessValues);
		pool.invoke(ft);
		return fitnessValues;
	}
}
