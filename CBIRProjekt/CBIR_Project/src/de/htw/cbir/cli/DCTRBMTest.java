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
import de.htw.cma.GeneticDCTRBM;


public class DCTRBMTest {

	public static void main(String[] args) {
		
		// lade alle Bilder aus dem Ordner
		ImageManager imageManager = new ImageManager();
		imageManager.loadImages(new File("images//WebImages_71x6"));
		
		// keine Besonderen Settings sind n��tig
		Settings settings = new Settings();
		
		// erstelle die Sortierart und das Histogram ein
		Pic[] allImages = imageManager.getImages();

		ForkJoinPool pool = new ForkJoinPool();
		
		// ------------------- neu erstellen --------------s---------
		int inputSize = 15;
		int outputSize = 10;
		
		DCTRBM rbm = new DCTRBM(inputSize, outputSize);
		// nur damit die Datenanalysiert werden und 
		// eine Normalisierung sp��ter stattfinden kann
		rbm.train(allImages, 0); 
		Sorter_DCTRBM sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);
	
		CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool, new CBIREvaluationModel());
		GeneticDCTRBM gh = new GeneticDCTRBM(rbm, imageManager.getImageSetName(), evalulation);
		//gh.run();
		
		
		//--------------------- laden -----------------------		

		
	}

}
