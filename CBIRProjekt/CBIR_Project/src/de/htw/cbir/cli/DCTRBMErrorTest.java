package de.htw.cbir.cli;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.CBIREvaluation;
import de.htw.cbir.CBIREvaluationModel;
import de.htw.cbir.DCTRBM;
import de.htw.cbir.ImageManager;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.sorter.Sorter_DCTRBM;
import de.htw.cma.GeneticDCTRBMError;


public class DCTRBMErrorTest {

	public static void main(String[] args) {
		
		boolean load = false;
		if(args.length > 0)
			load = Boolean.parseBoolean(args[0]);
		
		String imageSetName = "WebImages_71x6";
		int inputSize = 15;
		int outputSize = 10;
		double learnRate = 0.1;
		
		if(args.length > 1)
			imageSetName = args[1];
		else if(args.length > 2)
			inputSize = Integer.parseInt(args[2]);
		else if(args.length > 3)
			outputSize = Integer.parseInt(args[3]);
		else if(args.length > 4)
			learnRate = Double.parseDouble(args[4]);
		
		
		// lade alle Bilder aus dem Ordner
		String dir = "images//"+imageSetName;
		System.out.println("Load images from "+dir);
		ImageManager imageManager = new ImageManager();
		imageManager.loadImages(new File(dir));
		
		// keine Besonderen Settings sind nötig
		Settings settings = new Settings();
		
		// erstelle die Sortierart und das Histogram ein
		Pic[] allImages = imageManager.getImages(true);

		ForkJoinPool pool = new ForkJoinPool();
		DCTRBM rbm = null;
		
		if(load == false) {
			// ------------------- neu erstellen ----------------------
			System.out.println("Create new RBM");
			rbm = new DCTRBM(inputSize, outputSize, learnRate);
			// nur damit die Datenanalysiert werden und 
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);	
		} else {
			//--------------------- laden -----------------------		
			System.out.println("Load RBM");
			String name = inputSize+"x"+outputSize+"RBMError.rbm";
			//rbm = DCTRBMChen.load(Paths.get("solutions/"+imageSetName+"/"+name));
		}
		
		// verwende die RBM in den Sorter und der ES. Teste die Ergebnisse der ES.
		Sorter_DCTRBM sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);
		CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool, new CBIREvaluationModel());
		GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager, evalulation, pool);
		gh.run();
	}

}
