package de.htw.cbir;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.gui.CBIRUI;
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
import de.htw.cma.GeneticDCTRBMError;
import de.htw.cma.GeneticHistogram;
import de.htw.cma.GeneticDCTRBM;
import de.htw.color.ColorConverter.ColorSpace;
import iconn.htw.sorter.*;

public class CBIRController {

	private Settings settings;
	private ImageManager imageManager;
	
	private CBIRUI ui;
	private Sorter sorter;
	private ForkJoinPool pool;
	
	public CBIRController(Settings settings, ImageManager imageManager) {
		this.settings = settings;
		this.imageManager = imageManager;
		this.pool = new ForkJoinPool();
		
		// GUI Elemente
		this.ui = new CBIRUI(this);
	}
	
	public void changeSorter(ActionEvent e) {
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();
		if(cmd.equalsIgnoreCase("ColorMean"))
			sorter = new Sorter_ColorMean(allImages, settings, pool);
		else if(cmd.equalsIgnoreCase("ColorMean2"))
			sorter = new Sorter_ColorMean2(allImages, settings, pool);
		else if(cmd.equalsIgnoreCase("IDW Histogram")) {
//			double power = ui.askDouble("Power");
//			double[][] histogramDataPoints = IDWHistogram.getEmpiricalDataPoints();
//			IDWHistogramFactory factory = new IDWHistogramFactory(histogramDataPoints, ColorSpace.Genetic, power, 0.34);
			
			IDWHistogramFactory factory = IDWHistogramFactory.load(new File("solutions//WebImages_71x6//10Histogram.gh").toPath());
			factory.printINPPMFormat();
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);
		} else if(cmd.equalsIgnoreCase("FV15DCT")) {
			sorter = new Sorter_FV15DCT(allImages, settings, pool);
		} else if(cmd.equalsIgnoreCase("DCTRBM")) {
			DCTRBM dctRBM = new DCTRBM(15, 10);
			dctRBM.train(allImages, 0);
			System.out.println("error "+ dctRBM.getError(allImages));
			System.out.println("raw error "+ dctRBM.getRawError(allImages));
			dctRBM.train(allImages, 3000);
			System.out.println("error "+ dctRBM.getError(allImages));
			System.out.println("raw error "+ dctRBM.getRawError(allImages));

			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		}  else if(cmd.equalsIgnoreCase("DCT_CJ")) {
			sorter = new Sorter_DCT_CJ(allImages, settings, pool);
		}
		
		sorter.getFeatureVectors();
	}
	
	
	/**
	 * TODO: füge hier weitere Sortiermöglichkeiten hinzu
	 * Alle hier genannten Sorter Namen werden im Sortiermenu angezeigt
	 * 
	 * @return SorterNamen
	 */
	public String[] getSorterNames() {
		return new String[]  {  "None", "ColorMean", "ColorMean2", "IDW Histogram", "FV15DCT", "DCTRBM", "DCT_CJ" };
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
		if(sorter == null) {
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
		System.out.println("sortmethod:"+sorter.getName());		
		System.out.println("meanAveragePrecision:" + ap + " - Time: " + (System.currentTimeMillis() - milliSec) +"ms");
		
		// wende die Reihnfolge an und zeige sie dem Benutzer
		for (int i = 0; i < sortedArray.length; i++)
			sortedArray[i].getSearchImage().setRank(i);
		ui.repaint();
	}

	public void triggerTests(ActionEvent e) {
		
		// wurde bereits ein Sortieralgorithmus ausgewählt
		if(sorter == null) {
			System.out.println("No sorting algorithm selected");
			return;
		}
		
		// evaluiere (durch MAP Wert) den Sortieralgorithmus
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();
		CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);
		
		// welche Teste sollen durchgeführt werden
		if(cmd.equals("Alle")) {
			eval.testAll(true, cmd);
		} else {
			Pic[] queryImages = imageManager.getImageInGroup(cmd).toArray(new Pic[0]);
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
		
		
		if(cmd.equalsIgnoreCase("Finde besten Lum-Wert")) {
			Settings sett = new Settings();
			String cmdStr = "ColorMean2";
			for (int i = 0; i < 50; i+=2) {
				
				// berechne die Featurevektoren
				sett.setLumValue((double)i / 10);
				sorter = new Sorter_ColorMean2(allImages, sett, pool);
				sorter.getFeatureVectors();
				
				// berechne den MAP
				CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);
				eval.testAll(true, cmdStr);
			}
		} else if(cmd.equalsIgnoreCase("Finde ColorSpace Distance")) {
			sorter = new Sorter_RGBInterpolation(allImages, settings, pool);
			sorter.getFeatureVectors();
			
			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticColorDistance csd = new GeneticColorDistance(4, imageManager.getImageSetName(), evalulation);
			csd.run();
		} else if(cmd.equalsIgnoreCase("Finde Genetic Histogram")) {
			
			IDWHistogramFactory factory = new IDWHistogramFactory(ColorSpace.AdvYCbCr, 5, 0.34);
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);
		
			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticHistogram gh = new GeneticHistogram(3, imageManager.getImageSetName(), evalulation, factory);
			gh.run();
		} else if(cmd.equalsIgnoreCase("Finde RBM Weights")) {
			
			int inputSize = 15;
			int outputSize = 10;
			
			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und 
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0); 
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);
		
			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticDCTRBM gh = new GeneticDCTRBM(rbm, imageManager.getImageSetName(), evalulation);
			gh.run();
		} else if(cmd.equalsIgnoreCase("reduziere den RBM Fehler")) {
			
			int inputSize = 15;
			int outputSize = 10;
			
			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und 
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0); 
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);
		
			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager, evalulation, pool);
			gh.run();
		}
	}	

}
