package de.htw.cbir.cli;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.ImageManager;
import de.htw.cbir.histogram.IDWHistogramFactory;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.sorter.Sorter;
import de.htw.cbir.sorter.Sorter_IDWHistogram;
import de.htw.cma.GeneticHistogram;


public class IDWHistogramTest {

	public static void main(String[] args) {
		
		int histogramBins = 15;
		if(args.length > 0)
			histogramBins = Integer.parseInt(args[0]);
		
		// lade alle Bilder aus dem Ordner
		ImageManager imageManager = new ImageManager();
		imageManager.loadImages(new File("images//colors"));
		
		// keine Besonderen Settings sind nötig
		Settings settings = new Settings();
		
		// erstelle die Sortierart und das Histogram ein
		Pic[] allImages = imageManager.getImages();
		ForkJoinPool pool = new ForkJoinPool();
		
		
		// ------------------- neu erstellen -----------------------
//		IDWHistogramFactory factory = new IDWHistogramFactory(ColorSpace.AdvYCbCr, 5, 0.34);
//		Sorter sorter = new Sorter_IDWHistogram(allImages, settings, factory);
//	
//		// berechne für das Histogram gute Histogram Punkte mit Hilfe von ES
//		CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages);
//		GeneticHistogram gh = new GeneticHistogram(histogramBins, imageManager.getImageSetName(), evalulation, factory);
//		gh.run();
		
		
		//--------------------- laden -----------------------		
		IDWHistogramFactory factory = IDWHistogramFactory.load(new File("solutions//WebImages_71x6//10Histogram.gh").toPath());
		factory.printINPPMFormat();
		Sorter sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);
		
		// berechne für das Histogram gute Histogram Punkte mit Hilfe von ES
		CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
		GeneticHistogram gh = new GeneticHistogram(factory.getRGBDataPoints(), 0.1, imageManager.getImageSetName(), evalulation, factory);
		gh.run();
		
	}

}
