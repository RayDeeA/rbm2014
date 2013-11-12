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
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.RBMJBlasRandomed;
import de.htw.iconn.rbm.RBMJBlasSeparatedWeights;
import de.htw.iconn.rbm.functions.BasicSigmoidMatrixFunction;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
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

	public CBIRController(Settings settings, ImageManager imageManager) {
		this.settings = settings;
		this.imageManager = imageManager;
		this.pool = new ForkJoinPool();

		this.visualizationFrame = new RBMVisualizationFrame();
		// GUI Elemente
		this.ui = new CBIRUI(this, this.visualizationFrame);
	}

	public void changeSorter(ActionEvent e) {
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

			IDWHistogramFactory factory = IDWHistogramFactory.load(new File(
					"solutions//WebImages_71x6//10Histogram.gh").toPath());
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

			for (int i = 0; i < 30; i++) {
				dctRBM.train(allImages, 100);
				visualizationFrame.update(dctRBM.getWeights(), error);
			}

			System.out.println("error " + dctRBM.getError(allImages));
			System.out.println("raw error " + dctRBM.getRawError(allImages));
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCT_CJ")) {
			sorter = new Sorter_DCT_CJ(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("RBMJBlas_Sigmoid")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new DefaultLogisticMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("RBMJBlasRandomed_Sigmoid")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlasRandomed(inputSize, outputSize, learnRate,
					new DefaultLogisticMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_RM")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 0.1;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new DefaultLogisticMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_CJ")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 0.1;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlasSeparatedWeights(inputSize, outputSize,
					learnRate, new DefaultLogisticMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_DefaultLogisticMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new DefaultLogisticMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_RectifierMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new RectifierMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_TanHMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new TanHMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_GaussMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new GaussMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearClippedMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new LinearClippedMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearUnclippedMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new LinearUnclippedMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd
				.equalsIgnoreCase("DCTRBM_LinearInterpolatedMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 0.1;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new LinearInterpolatedMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_HardClipMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 1.0;
			int epochs = 10000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new HardClipMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_BasicSigmoidMatrixFunction")) {
			int inputSize = 15;
			int outputSize = 10;
			double learnRate = 0.1;
			int epochs = 20000;
			int updateFrequency = 100;
			IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate,
					new BasicSigmoidMatrixFunction());
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM_MU")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_RC")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_SR")) {

		}

		sorter.getFeatureVectors();
	}

	public String[] getSorterNames() {

		return new String[] { "None", "ColorMean", "ColorMean2",
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
				"DCTRBM_SR", "RBMJBlasRandomed_Sigmoid" };
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
		CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);
		long milliSec = System.currentTimeMillis();

		// Sortere das alle Bilder nach dem Querybild
		ImagePair[] sortedArray = eval.sortBySimilarity(queryImage);
		double ap = PrecisionRecallTable.calcMeanAveragePrecision(sortedArray);

		// logge die Ergebnisse
		System.out.println("sortmethod:" + sorter.getName());
		System.out.println("meanAveragePrecision:" + ap + " - Time: "
				+ (System.currentTimeMillis() - milliSec) + "ms");

		// wende die Reihnfolge an und zeige sie dem Benutzer
		for (int i = 0; i < sortedArray.length; i++)
			sortedArray[i].getSearchImage().setRank(i);
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
		CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);

		// welche Teste sollen durchgeführt werden
		if (cmd.equals("Alle")) {
			eval.testAll(true, cmd);
		} else {
			Pic[] queryImages = imageManager.getImageInGroup(cmd).toArray(
					new Pic[0]);
			eval.test(queryImages, true, cmd);
		}
	}

	/**
	 * Wird getriggert wenn einer der automatischen Tests aufgerufen wird.
	 * 
	 * @param e
	 */
	public void triggerAutomaticTests(ActionEvent e) {

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
				CBIREvaluation eval = new CBIREvaluation(sorter, allImages,
						pool);
				eval.testAll(true, cmdStr);
			}
		} else if (cmd.equalsIgnoreCase("Finde ColorSpace Distance")) {
			sorter = new Sorter_RGBInterpolation(allImages, settings, pool);
			sorter.getFeatureVectors();

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages,
					pool);
			GeneticColorDistance csd = new GeneticColorDistance(4,
					imageManager.getImageSetName(), evalulation);
			csd.run();
		} else if (cmd.equalsIgnoreCase("Finde Genetic Histogram")) {

			IDWHistogramFactory factory = new IDWHistogramFactory(
					ColorSpace.AdvYCbCr, 5, 0.34);
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages,
					pool);
			GeneticHistogram gh = new GeneticHistogram(3,
					imageManager.getImageSetName(), evalulation, factory);
			gh.run();
		} else if (cmd.equalsIgnoreCase("Finde RBM Weights")) {

			int inputSize = 15;
			int outputSize = 10;

			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages,
					pool);
			GeneticDCTRBM gh = new GeneticDCTRBM(rbm,
					imageManager.getImageSetName(), evalulation);
			gh.run(visualizationFrame);
		} else if (cmd.equalsIgnoreCase("reduziere den RBM Fehler")) {

			int inputSize = 15;
			int outputSize = 10;

			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages,
					pool);
			GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager,
					evalulation, pool);
			gh.run();
		}
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
