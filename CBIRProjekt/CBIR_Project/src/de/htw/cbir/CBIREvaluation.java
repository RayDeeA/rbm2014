package de.htw.cbir;

import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.ImagePair;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.PrecisionRecallTable;
import de.htw.cbir.sorter.Sorter;

public class CBIREvaluation {

	private Pic[] images;
	private Sorter sorter;
	private ForkJoinPool pool;
	
	public CBIREvaluation(Sorter sorter, Pic[] images, ForkJoinPool pool) {
		this.images = images;
		this.sorter = sorter;
		this.pool = pool;
	}
	
	public Sorter getSorter() {
		return sorter;
	}

	/**
	 * Berechnet die Mean Average Precision über alle Bilder
	 * 
	 * @param sorter
	 * @return
	 */
	public double testAll(boolean displayResult, String description) {
		final TIntDoubleHashMap lookup = createDistanceLookupTable(sorter);
		final PrecisionRecallTable table = new PrecisionRecallTable(displayResult, sorter.getName(), description);
		
		// starte die komplexe Analyse 
		table.start(images.length);
		
		// berechne die Average Precision für jedes Bild aus
//		long start = System.currentTimeMillis();
		
		// Java 8: ohne Lambda Expression
		// paralle Berechnungen
		double[] MAPs = new double[images.length];
		CBIRForkTest ft = new CBIRForkTest(this, lookup, table, images, 0, images.length, MAPs);
		pool.invoke(ft);
		
		double MAP = 0;
		for (int i = 0; i < MAPs.length; i++)
			MAP += MAPs[i];
		MAP /= images.length;
		
//		long stop = System.currentTimeMillis();
//		System.out.println("MAP: "+MAP+" took "+(stop-start)+"ms");
		
		// beende die Analyse und zeige eventuell Ergebnisse
		table.finish();
		
		return MAP;
	}
	
	/**
	 * Ermittle die Mean Average Precision für alle angegebenen Bilder 
	 * 
	 * @param queryImages
	 * @return
	 */
	public double test(Pic[] queryImages, boolean displayResult, String description) {
		PrecisionRecallTable table = new PrecisionRecallTable(displayResult, sorter.getName(), description);
		
		// starte die komplexe Analyse 
		table.start(queryImages.length);
				
		// berechne die Average Precision für jedes Bild aus
		double MAP = 0;
		for (int i = 0; i < queryImages.length; i++)
			MAP += test(queryImages[i], i, table);
		MAP /= images.length;
		
		// analyse
		table.finish();
		
		return MAP;
	}
	
	/**
	 * Ermittle die Average Precision
	 * 
	 * @param queryImage
	 * @param displayResult zeige den Verlauf des Precision Recall Graphen
	 * @return
	 */
	public double test(Pic queryImage) {
		return test(queryImage, 0, new PrecisionRecallTable(false, sorter.getName(), "Id: "+queryImage.getId()));
	}

	/**
	 * Liefert eine Liste aller Bilder sorter nach deren Ähnlichkeit zum Querybild
	 * 
	 * @param queryImage
	 * @return
	 */
	public ImagePair[] sortBySimilarity(Pic queryImage) {
		Pic[] allImages = images;
		ImagePair[] result = new ImagePair[allImages.length];
		
		// durchlaufe alle Bilder
		for (int i = 0; i < allImages.length; i++) {
			Pic searchImage = allImages[i];
			double distance = sorter.getDistance(queryImage.getFeatureVector(), searchImage.getFeatureVector());
			result[i] = new ImagePair(queryImage, searchImage, distance);
		}
		
		// sortiere die Suchbilder nach der Distance zum Query Bild
		Arrays.sort(result);
		
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param queryImage
	 * @param table
	 * @return
	 */
	private double test(Pic queryImage, int num, PrecisionRecallTable table) {
		
		// sortiere alle Bilder nach der Ähnlichkeit zum Querybild 
		ImagePair[] result = sortBySimilarity(queryImage);
		
		// berechne die eigentliche Average Precision
		return table.analyse(result, num);
	}  
	
	/**
	 * Vergleiche das Query Bild mit allen Bildern im 
	 * ImageManager und ermittle die jeweilige Distanz
	 * der Bilder über die Lookup Tabelle.
	 * 
	 * Berechne die Average Precision mithilfe der Distanz.
	 * 
	 * @param testImages
	 * @param lookup
	 * @return
	 */
	public double test(Pic queryImage, int num, TIntDoubleHashMap lookup, PrecisionRecallTable table) {
		Pic[] allImages = images;
		ImagePair[] result = new ImagePair[allImages.length];
		
		// durchlaufe alle Bilder
		for (int i = 0; i < allImages.length; i++) {
			Pic searchImage = allImages[i];
			double distance = readDistanceFor(queryImage.getId(), searchImage.getId(), lookup);
			result[i] = new ImagePair(queryImage, searchImage, distance);
		}
		
		// sortiere die Suchbilder nach der Distance zum Query Bild
		Arrays.sort(result);
		
		// berechne die eigentliche Average Precision
		return table.analyse(result, num);
	}    
	
	/**
	 * besorgt aus der Lookup Tabelle die Distanz für die beiden Ids
	 * 
	 * @param id1
	 * @param id2
	 * @param lookup
	 * @return
	 */
	private static double readDistanceFor(int id1, int id2, TIntDoubleHashMap lookup) {
		if(id1 == id2) return 0;
		if(id1 < id2) return lookup.get((id1 << 16) | id2);
		return lookup.get((id2 << 16) | id1);
	}
	
	/**
	 * Erzeuge eine Lookup Tabelle für alle Distanzen.
	 * So das diese nur einmal berechnet werden müssen.
	 * 
	 * @return
	 */
	private TIntDoubleHashMap createDistanceLookupTable(Sorter sorter) {
		Pic[] allImages = images;
		int initialCapacity = (allImages.length-1)*allImages.length/2;
		TIntDoubleHashMap map = new TIntDoubleHashMap(initialCapacity);
		
		for (int i = 0; i < allImages.length-1; i++) {
			Pic p1 = allImages[i];
			for (int j = i+1; j < allImages.length; j++) {
				Pic p2 = allImages[j];
				int key =  (p1.getId() << 16) | p2.getId();
				double dist = sorter.getDistance(p1.getFeatureVector(), p2.getFeatureVector());
				map.put(key, dist);
			}
		}
		
		return map;
	}
}
