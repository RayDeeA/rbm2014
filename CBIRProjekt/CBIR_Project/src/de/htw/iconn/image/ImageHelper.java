package de.htw.iconn.image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/*
 * Will be responsible for all possible data manipulation Operations on Images
 */

// TODO
public class ImageHelper {
	
	public ImageHelper() {
		
	}
	
    public static BufferedImage loadImage(File imageFile) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println("Could not load: " + imageFile.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
        return image;
    }
	
}
