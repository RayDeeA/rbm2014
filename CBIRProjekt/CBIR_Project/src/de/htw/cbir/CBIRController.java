package de.htw.cbir;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.gui.CBIRUI;
import de.htw.cbir.gui.RBMVisualizationFrame;
import de.htw.cbir.histogram.IDWHistogramFactory;
import de.htw.cbir.model.ImagePair;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.PrecisionRecallTable;
import de.htw.cbir.model.Settings;
import de.htw.cbir.sorter.Sorter;
import de.htw.cbir.sorter.Sorter_ColorMean;
import de.htw.cbir.sorter.Sorter_ColorMean2;
import de.htw.cbir.sorter.Sorter_DCTRBM;
import de.htw.cbir.sorter.Sorter_FV15DCT;
import de.htw.cbir.sorter.Sorter_IDWHistogram;
import de.htw.cbir.sorter.Sorter_RGBInterpolation;
import de.htw.cma.GeneticColorDistance;
import de.htw.cma.GeneticDCTRBM;
import de.htw.cma.GeneticDCTRBMError;
import de.htw.cma.GeneticHistogram;
import de.htw.color.ColorConverter.ColorSpace;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.IRBMLogger;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.RBMLogger;
import de.htw.iconn.rbm.RBMLoggerVisualizer;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.GeneralisedLogisticFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearInterpolatedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.SquareRootLogistic;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;
import de.htw.iconn.sorter.Sorter_DCT_CJ;

@Deprecated
public class CBIRController {

	private Settings settings;
	private ImageManager imageManager;

	private CBIRUI ui;
	private final RBMVisualizationFrame visualizationFrame;
	private ArrayList<RBMVisualizationFrame> visframelist;

	private Sorter sorter;
	private ForkJoinPool pool;
	private IRBM rbm;
	private RBMFeatureDCT dctRBM;
	private CBIREvaluation evaluation;
	private CBIREvaluationModel evaluationModel;

	private boolean useDCTRBM = true;
	
	public CBIRController(Settings settings, ImageManager imageManager) {
		this.settings = settings;		
		this.imageManager = imageManager;
		this.pool = new ForkJoinPool();

		this.visualizationFrame = new RBMVisualizationFrame(true);
		this.visualizationFrame.setControllerRef(this);
		// GUI Elemente
		this.ui = new CBIRUI(this, this.visualizationFrame);
		
		this.evaluationModel = new CBIREvaluationModel(imageManager.getImageCount());
	}
	
	public void changeLogisticTest(ActionEvent e) {
		evaluationModel.reset();
		
		int inputSize = (useDCTRBM) ? settings.getInputSize() : 28*28;
		int outputSize = settings.getOutputSize();
		double learnRate = settings.getLearnRate();

		Pic[] allImages = imageManager.getImages(true);

		ILogistic logisticFunction = null;
		
		String cmd = e.getActionCommand();
		if (cmd.equalsIgnoreCase("Standard")) {
			logisticFunction = new GeneralisedLogisticFunction();
		} else if (cmd.equalsIgnoreCase("Gaussian")) {
			logisticFunction = new GaussMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Hard Clip")) {
			logisticFunction = new HardClipMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Clipped")) {
			logisticFunction = new LinearClippedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Interpolated")) {
			logisticFunction = new LinearInterpolatedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Unclipped (Absolute Value)")) {
			logisticFunction = new LinearUnclippedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Rectifier")) {
			logisticFunction = new RectifierMatrixFunction();
		} else if (cmd.equalsIgnoreCase("TanH")) {
			logisticFunction = new TanHMatrixFunction();
		} else if (cmd.equalsIgnoreCase("SqareRoot")) {
			logisticFunction = new SquareRootLogistic();
		}
		
		rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, logisticFunction));
		ARBMFeature rbmWrapper = null;
		
		if(useDCTRBM)
			rbmWrapper = new RBMFeatureDCT(inputSize, outputSize, rbm);
		else
			rbmWrapper = new RBMFeaturePixel(inputSize, outputSize, rbm);
		// nur damit die Datenanalysiert werden und
		// eine Normalisierung später stattfinden kann
		rbmWrapper.train(allImages, 0, false, false);
		
		this.sorter = new Sorter_DCTRBM(allImages, settings, rbmWrapper, pool);
		
		evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
		GeneticDCTRBMError gh = new GeneticDCTRBMError(rbmWrapper, imageManager, evaluation, pool);
		if(rbm != null && rbm instanceof IRBMLogger){
			evaluationModel.setLogger((IRBMLogger)rbm);		
		}
		gh.run();
		
		rbmLog();
	}
	
	public String[] getLogisticsNames() {

		return new String[] { 
			"Standard", 
			"Gaussian", 
			"Hard Clip", 
			"Linear Clipped", 
			"Linear Interpolated", 
			"Linear Unclipped (Absolute Value)", 
			"Rectifier", 
			"TanH", 
			"SqareRoot" 
		};
		
	}
	
	public void changeSorter(ActionEvent e) {
		evaluationModel.reset();
		rbm = null;
		
		int inputSize = (this.useDCTRBM) ? settings.getInputSize() : 28*28;
		int outputSize = settings.getOutputSize();
		double learnRate = settings.getLearnRate();
		boolean useSeed = settings.isUseSeed();
		int seed = settings.getSeed();
		ILogistic logisticFunction = settings.getLogisticFunction();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages(true);
		
		if (cmd.equalsIgnoreCase("ColorMean"))
			sorter = new Sorter_ColorMean(allImages, settings, pool);
		else if (cmd.equalsIgnoreCase("ColorMean2"))
			sorter = new Sorter_ColorMean2(allImages, settings, pool);
		else if (cmd.equalsIgnoreCase("IDW Histogram")) {
			// double power = ui.askDouble("Power");
			// double[][] histogramDataPoints =
			// IDWHistogram.getEmpiricalDataPoints();
			// IDWHistogramFactory factory = new
			// IDWHistogramFactory(histogramDataPoints, ColorSpace.Genetic,
			// power, 0.34);

			IDWHistogramFactory factory = IDWHistogramFactory.load(new File("solutions//WebImages_71x6//10Histogram.gh").toPath());
			factory.printINPPMFormat();
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);
		} else if (cmd.equalsIgnoreCase("FV15DCT")) {
			sorter = new Sorter_FV15DCT(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("DCT_CJ")) {
			sorter = new Sorter_DCT_CJ(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_JBlas")) {
			rbm = new RBMLoggerVisualizer(new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, logisticFunction, useSeed, seed)), this.visualizationFrame, this.evaluationModel);
		}
		if(rbm != null) {
			ARBMFeature rbmWrapper = null;
			if(useDCTRBM)
				rbmWrapper = new RBMFeatureDCT(inputSize, outputSize, rbm);
			else
				rbmWrapper = new RBMFeaturePixel(inputSize, outputSize, rbm);
			rbmWrapper.train(imageManager.getImages(true), settings.getEpochs(), false, false);			
			sorter = new Sorter_DCTRBM(allImages, settings, rbmWrapper, pool);
		}
		sorter.getFeatureVectors();
		
		// Store data for logger
		if(rbm != null && rbm instanceof IRBMLogger){
			evaluationModel.setLogger((IRBMLogger)rbm);
			evaluationModel.setUseSeed(useSeed);
			evaluationModel.setSeed(seed);
		}
		rbmLog();
	}
	
	public void rbmLog(){
		System.out.println("RBM Log:");
		IRBMLogger logger = evaluationModel.getLogger();
		if(logger != null){
			if(evaluationModel.getEvaluationType() != CBIREvaluationModel.evaluationType.EVOLUTION){
				if(dctRBM != null){
					evaluationModel.setError(dctRBM.getError(imageManager.getImages(true), false, false));
				}
				if(sorter != null){
					evaluation = new CBIREvaluation(sorter, imageManager.getImages(true), pool, evaluationModel);
					evaluationModel.setMAP(evaluation.testAll(true, "Alle"));
				}
				evaluationModel.setEpochs(settings.getEpochs());
				evaluationModel.setEvaluationType(CBIREvaluationModel.evaluationType.TRAINING);
			}
			try {
				logger.finalCsvLog(evaluationModel);			
			} catch (IOException e) {		
				e.printStackTrace();
				System.err.println("File error on log");
			}
		}
	}

	public String[] getSorterNames() {

		return new String[] { 
			"None", "ColorMean", "ColorMean2", "IDW Histogram", "FV15DCT", "DCT_CJ", "DCTRBM_JBlas"
		};
	}

	public ImageManager getImageManager() {
		return imageManager;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSorter(Sorter sorter) {
		this.sorter = sorter;
	}

	public void sortByImage(Pic queryImage) {

		// wurde bereits ein Sortieralgorithmus ausgewählt
		if (sorter == null) {
			System.out.println("No sorting algorithm selected");
			return;
		}

		Pic[] allImages = imageManager.getImages(true);
		evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
		long milliSec = System.currentTimeMillis();

		// Sortere das alle Bilder nach dem Querybild
		ImagePair[] sortedArray = evaluation.sortBySimilarity(queryImage);
		double ap = PrecisionRecallTable.calcMeanAveragePrecision(sortedArray);

		// logge die Ergebnisse
		System.out.println("sortmethod:" + sorter.getName());
		System.out.println("meanAveragePrecision:" + ap + " - Time: " + (System.currentTimeMillis() - milliSec) + "ms");

		// wende die Reihnfolge an und zeige sie dem Benutzer
		for (int i = 0; i < sortedArray.length; i++) {
			sortedArray[i].getSearchImage().setRank(i);
		}
		ui.repaint();
	}

	public void triggerTests(ActionEvent e) {

		// wurde bereits ein Sortieralgorithmus ausgewählt
		if (sorter == null) {
			System.out.println("No sorting algorithm selected");
			return;
		}

		// evaluiere (durch MAP Wert) den Sortieralgorithmus
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages(true);
		evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);

		// welche Teste sollen durchgeführt werden
		if (cmd.equals("Alle")) {
			evaluation.testAll(true, cmd);
		} else {
			Pic[] queryImages = imageManager.getImageInGroup(cmd).toArray(new Pic[0]);
			evaluation.test(queryImages, true, cmd);
		}
	}

	/**
	 * Wird getriggert wenn einer der automatischen Tests aufgerufen wird.
	 * 
	 * @param e
	 */
	public void triggerAutomaticTests(ActionEvent e) {

		int inputSize = settings.getInputSize();
		int outputSize = settings.getOutputSize();
		double learnRate = settings.getLearnRate();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages(true);

		if (cmd.equalsIgnoreCase("Finde besten Lum-Wert")) {
			Settings sett = new Settings();
			String cmdStr = "ColorMean2";
			for (int i = 0; i < 50; i += 2) {

				// berechne die Featurevektoren
				sett.setLuminance((double) i / 10);
				sorter = new Sorter_ColorMean2(allImages, sett, pool);
				sorter.getFeatureVectors();

				// berechne den MAP
				evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
				evaluation.testAll(true, cmdStr);
			}
		} else if (cmd.equalsIgnoreCase("Finde ColorSpace Distance")) {
			sorter = new Sorter_RGBInterpolation(allImages, settings, pool);
			sorter.getFeatureVectors();

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticColorDistance csd = new GeneticColorDistance(4, imageManager.getImageSetName(), evaluation);
			csd.run();
		} else if (cmd.equalsIgnoreCase("Finde Genetic Histogram")) {
			IDWHistogramFactory factory = new IDWHistogramFactory( ColorSpace.AdvYCbCr, 5, 0.34);
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticHistogram gh = new GeneticHistogram(3, imageManager.getImageSetName(), evaluation, factory);
			gh.run();
		} else if (cmd.equalsIgnoreCase("Finde RBM Weights")) {


			RBMFeatureDCT rbm = new RBMFeatureDCT(inputSize, outputSize, learnRate);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0, false, false);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticDCTRBM gh = new GeneticDCTRBM(rbm, imageManager.getImageSetName(), evaluation);
			gh.run(visualizationFrame);
		} else if (cmd.equalsIgnoreCase("reduziere den RBM Fehler")) {

			RBMFeatureDCT rbm = new RBMFeatureDCT(inputSize, outputSize, learnRate);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0, false, false);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager, evaluation, pool);
			gh.run();
		}
	}
}
