package de.htw.cbir;

import java.nio.file.Paths;

import de.htw.cbir.model.Settings;
import de.htw.color.ColorMetric;
import de.htw.interpolation.RGBInterpolation;

public class CBIRMain {

	
	public static void main(String[] args) {
		
		// die Einstellungen im gesamten Projekt
		Settings settings = new Settings();
		
		// lade die Bilder
		ImageManager imageManager = new ImageManager();
		imageManager.load();
		
		// Zeige die GUI an
		CBIRController controller = new CBIRController(settings, imageManager);
		
		// setzte die Standart interpolation
		String setName = imageManager.getImageSetName();
		RGBInterpolation cube = RGBInterpolation.load(Paths.get("CBIR_Project/solutions/"+setName+"/4x4x4RGB.csa"));
		ColorMetric.setDistanceInterface(cube);
	}
}
