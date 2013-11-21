package de.htw.iconn.rbm.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class IdxReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream inImage = null;
		FileInputStream inLabel = null;
		
		String inputImagePath = "CBIR_Project/imagesRaw/MNIST/train-images-idx3-ubyte";
		String inputLabelPath = "CBIR_Project/imagesRaw/MNIST/train-labels-idx1-ubyte";
		
		String outputPath = "CBIR_Project/images/MNIST_Database/";
		
		int[] hashMap = new int[10]; 

        try {
        	inImage = new FileInputStream(inputImagePath);
        	inLabel = new FileInputStream(inputLabelPath);
            
            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfRows  = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            
            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            
            BufferedImage image = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_BYTE_GRAY);
            int numberOfPixels = numberOfRows * numberOfColumns;
            int[] imgPixels = new int[numberOfPixels];
            
            for(int i = 0; i < numberOfImages; i++) {
            	
            	for(int p = 0; p < numberOfPixels; p++) {
            		imgPixels[p] = (byte) (255 - inImage.read()); 
            	}
            	
                image.setRGB(0, 0, numberOfColumns, numberOfRows, imgPixels, 0, numberOfColumns);
                
                int label = inLabel.read();
                
                hashMap[label]++;
            	File outputfile = new File(outputPath + label + "_0" + hashMap[label] + ".png");
            	
            	ImageIO.write(image, "png", outputfile);
            }
            
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (inImage != null) {
                try {
                	inImage.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            if (inLabel != null) {
                try {
                	inLabel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
	}

}
