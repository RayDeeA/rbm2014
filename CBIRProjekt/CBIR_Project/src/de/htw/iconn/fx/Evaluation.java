package de.htw.iconn.fx;

import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.ImagePair;
import de.htw.cbir.model.Pic;

public class Evaluation {

	private Pic[] images;
	private ASorter sorter;
	private ForkJoinPool pool;
        private SimpleRBMModel model;
	
	public Evaluation(SimpleRBMModel model, ForkJoinPool pool) {
                this.model = model;
		this.pool = pool;
                this.images = model.getImageManager().getImages(true);
                this.sorter = model.getSorter();
	}

	/**
	 * Berechnet die Mean Average Precision ��ber alle Bilder
	 * 
	 * @param sorter
	 * @return
	 */
	public void testAll() {
		final TIntDoubleHashMap lookup = createDistanceLookupTable();
		final PrecisionRecallTable table = new PrecisionRecallTable(true, model.getSorter().getName(), "all");
		
		// starte die komplexe Analyse 
		table.start(images.length);
		
		// berechne die Average Precision f��r jedes Bild aus
//		long start = System.currentTimeMillis();
		
		// Java 8: ohne Lambda Expression
		// paralle Berechnungen
		double[] averagePrecisions = new double[images.length];
		ForkTest ft = new ForkTest(this, lookup, table, images, 0, images.length, averagePrecisions);
		pool.invoke(ft);
		
		double mAP = 0;
		for (int i = 0; i < averagePrecisions.length; i++)
			mAP += averagePrecisions[i];
		mAP /= images.length;
                
                this.model.setmAP(mAP);
		
//		long stop = System.currentTimeMillis();
//		System.out.println("MAP: "+MAP+" took "+(stop-start)+"ms");
		
		// beende die Analyse und zeige eventuell Ergebnisse
		this.model.setPrTable(table.generatePRTable());
                System.out.println( "");
	}
	
	/**
	 * Ermittle die Mean Average Precision f��r alle angegebenen Bilder 
	 * 
	 * @param queryImages
	 * @return
	 */
	public double test(String imageGroup) {
		final TIntDoubleHashMap lookup = createDistanceLookupTable();
		PrecisionRecallTable table = new PrecisionRecallTable(false, sorter.getName(), imageGroup);
		
                Pic[] queryImages = this.model.getImageManager().getImageInGroup(imageGroup).toArray(new Pic[0]);
		// starte die komplexe Analyse 
		table.start(queryImages.length);
				
		double[] averagePrecisions = new double[queryImages.length];
		ForkTest ft = new ForkTest(this, lookup, table, queryImages, 0, queryImages.length, averagePrecisions);
		pool.invoke(ft);
		// berechne die Average Precision f��r jedes Bild aus
		double MAP = 0;
		for (int i = 0; i < queryImages.length; i++)
			MAP += test(queryImages[i], i, table);
		MAP /= images.length;
		
		table.finish();
                this.model.setmAP(MAP);
		this.model.setPrTable(table.generatePRTable());
		// analyse
		
                System.out.println("s");
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
	 * Liefert eine Liste aller Bilder sorter nach deren ��hnlichkeit zum Querybild
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
		
		// sortiere alle Bilder nach der ������hnlichkeit zum Querybild 
		ImagePair[] result = sortBySimilarity(queryImage);
		
		// berechne die eigentliche Average Precision
		return table.analyse(result, num);
	}  
	
	/**
	 * Vergleiche das Query Bild mit allen Bildern im 
	 * ImageManager und ermittle die jeweilige Distanz
	 * der Bilder ��ber die Lookup Tabelle.
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
	 * besorgt aus der Lookup Tabelle die Distanz f��r die beiden Ids
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
	 * Erzeuge eine Lookup Tabelle f��r alle Distanzen.
	 * So das diese nur einmal berechnet werden m��ssen.
	 * 
	 * @return
	 */
	private TIntDoubleHashMap createDistanceLookupTable() {
		Pic[] allImages = this.model.getImageManager().getImages(true);
		int initialCapacity = (allImages.length-1)*allImages.length/2;
		TIntDoubleHashMap map = new TIntDoubleHashMap(initialCapacity);
		
		for (int i = 0; i < allImages.length-1; i++) {
			Pic p1 = allImages[i];
			for (int j = i+1; j < allImages.length; j++) {
				Pic p2 = allImages[j];
				int key =  (p1.getId() << 16) | p2.getId();
				double dist = this.model.getSorter().getDistance(p1.getFeatureVector(), p2.getFeatureVector());
				map.put(key, dist);
			}
		}
		
		return map;
	}
}
