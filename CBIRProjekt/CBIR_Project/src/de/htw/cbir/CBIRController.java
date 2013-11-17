package de.htw.cbir;

import java.awt.event.ActionEvent;
import java.io.File;
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
import de.htw.iconn.rbm.RBMJBlasRandomed;
import de.htw.iconn.rbm.RBMJBlasSeparatedWeights;
import de.htw.iconn.rbm.RBMLogger;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.GeneralisedLogisticFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearInterpolatedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;
import de.htw.iconn.sorter.Sorter_DCT_CJ;

public class CBIRController {

	private Settings settings;
	private ImageManager imageManager;

	private double error;
	private double rawError;

	private CBIRUI ui;
	private final RBMVisualizationFrame visualizationFrame;

	private Sorter sorter;
	private ForkJoinPool pool;
	private IRBM rbm;	
	private CBIREvaluation evaluation;
	private CBIREvaluationModel evaluationModel;
	
	private int inputSize = 15;
	private int outputSize = 4;
	private double learnRate = 0.1;
	private int epochs = 10000;
	private int updateFrequency = 100;

	public int getInputSize(){
		return inputSize;
	}
	public int getOutputSize(){
		return outputSize;
	}
	public CBIRController(Settings settings, ImageManager imageManager) {
		this.settings = settings;
		
		evaluationModel = new CBIREvaluationModel();
		settings.setInputSizeValue(inputSize);
		settings.setOutputSizeValue(outputSize);
		
		this.imageManager = imageManager;
		this.pool = new ForkJoinPool();

		this.visualizationFrame = new RBMVisualizationFrame();
		this.visualizationFrame.setControllerRef(this);
		// GUI Elemente
		this.ui = new CBIRUI(this, this.visualizationFrame);
	}
	
	public void changeLogisticTest(ActionEvent e) {
		evaluationModel.reset();
		
		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();

		Pic[] allImages = imageManager.getImages();

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
		}
		
		rbm = new RBMJBlas(inputSize, outputSize, learnRate, logisticFunction);
		DCTRBM dctrbm = new DCTRBM(inputSize, outputSize, rbm);
		// nur damit die Datenanalysiert werden und
		// eine Normalisierung später stattfinden kann
		dctrbm.train(allImages, 0);
		
		this.sorter = new Sorter_DCTRBM(allImages, settings, dctrbm, pool);
		
		evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
		GeneticDCTRBMError gh = new GeneticDCTRBMError(dctrbm, imageManager, evaluation, pool);
		gh.run();
		
	}
	
	public String[] getLogisticsTestNames() {

		return new String[] { 
			"Standard", 
			"Gaussian", 
			"Hard Clip", 
			"Linear Clipped", 
			"Linear Interpolated", 
			"Linear Unclipped (Absolute Value)", 
			"Rectifier", 
			"TanH" 
		};
		
	}
	
	public void changeSorter(ActionEvent e) {
		evaluationModel.reset();
		rbm = null;
		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();
		
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
		} else if (cmd.equalsIgnoreCase("DCTRBM")) {
			DCTRBM dctRBM = new DCTRBM(15, 4);
			dctRBM.train(allImages, 0);

			// made global for update RBMVisualizationFrame.update()
			error = dctRBM.getError(allImages);
			rawError = dctRBM.getRawError(allImages);

			System.out.println("error " + error);
			System.out.println("raw error " + rawError);

			for(int i = 0; i < 30; i++) {	
				dctRBM.train(allImages, 100);
				visualizationFrame.update(dctRBM.getWeights(), error);
			}

			System.out.println("error " + dctRBM.getError(allImages));
			System.out.println("raw error " + dctRBM.getRawError(allImages));
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCT_CJ")) {
			sorter = new Sorter_DCT_CJ(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("RBMJBlas_Sigmoid")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("RBMJBlasRandomed_Sigmoid")) {
			rbm = new RBMLogger(new RBMJBlasRandomed(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_RM")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));			
		} else if (cmd.equalsIgnoreCase("DCTRBM_CJ")) {
			rbm = new RBMLogger(new RBMJBlasSeparatedWeights(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_DefaultLogisticMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_RectifierMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new RectifierMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_TanHMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new TanHMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_GaussMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new GaussMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearClippedMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new LinearClippedMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearUnclippedMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new LinearUnclippedMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearInterpolatedMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new LinearInterpolatedMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_HardClipMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new HardClipMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_BasicSigmoidMatrixFunction")) {
			rbm = new RBMLogger(new RBMJBlas(inputSize, outputSize, learnRate, new GeneralisedLogisticFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_MU")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_RC")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_SR")) {

		}
		if(rbm != null) {
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);			
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		}
		sorter.getFeatureVectors();
	}
	
	public void rbmLog(){
		if(rbm != null && rbm instanceof IRBMLogger){
			IRBMLogger logger = (IRBMLogger)(rbm);
			evaluationModel.setImageSetSize(imageManager.getImageCount());
			logger.log(evaluationModel);
		}
	}

	public String[] getSorterNames() {

		return new String[] { 
			"None", "ColorMean", "ColorMean2",
			"IDW Histogram", "FV15DCT", "DCTRBM", "DCT_CJ",
			"RBMJBlas_Sigmoid", "DCTRBM_RM", "DCTRBM_CJ",
			"DCTRBM_DefaultLogisticMatrixFunction",
			"DCTRBM_RectifierMatrixFunction", "DCTRBM_TanHMatrixFunction",
			"DCTRBM_GaussMatrixFunction",
			"DCTRBM_LinearClippedMatrixFunction",
			"DCTRBM_LinearUnclippedMatrixFunction",
			"DCTRBM_LinearInterpolatedMatrixFunction",
			"DCTRBM_HardClipMatrixFunction",
			"DCTRBM_BasicSigmoidMatrixFunction", "DCTRBM_MU", "DCTRBM_RC",
			"DCTRBM_SR", "RBMJBlasRandomed_Sigmoid" 
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

		Pic[] allImages = imageManager.getImages();
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
		Pic[] allImages = imageManager.getImages();
		evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);

		// welche Teste sollen durchgeführt werden
		if (cmd.equals("Alle")) {
			evaluation.testAll(true, cmd);
		} else {
			Pic[] queryImages = imageManager.getImageInGroup(cmd).toArray(new Pic[0]);
			evaluation.test(queryImages, true, cmd);
		}
		
		rbmLog();
	}

	/**
	 * Wird getriggert wenn einer der automatischen Tests aufgerufen wird.
	 * 
	 * @param e
	 */
	public void triggerAutomaticTests(ActionEvent e) {

		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();

		if (cmd.equalsIgnoreCase("Finde besten Lum-Wert")) {
			Settings sett = new Settings();
			String cmdStr = "ColorMean2";
			for (int i = 0; i < 50; i += 2) {

				// berechne die Featurevektoren
				sett.setLumValue((double) i / 10);
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


			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticDCTRBM gh = new GeneticDCTRBM(rbm, imageManager.getImageSetName(), evaluation);
			gh.run(visualizationFrame);
		} else if (cmd.equalsIgnoreCase("reduziere den RBM Fehler")) {

			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			evaluation = new CBIREvaluation(sorter, allImages, pool, evaluationModel);
			GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager, evaluation, pool);
			gh.run();
		}
		rbmLog();
	}

	private void updateVisualization(int epochs, int updateFrequency,
			DCTRBM dctrbm) {
		int update = epochs / updateFrequency;
		for (int i = 0; i < update; i++) {
			dctrbm.train(imageManager.getImages(), updateFrequency);
			visualizationFrame.update(dctrbm.getWeights(), error);
		}
	}
}
