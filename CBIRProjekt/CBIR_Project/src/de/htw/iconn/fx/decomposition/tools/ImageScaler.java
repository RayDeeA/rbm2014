package de.htw.iconn.fx.decomposition.tools;

import java.awt.image.BufferedImage;

public class ImageScaler {
	
	private BufferedImage	orgImage;
	private int[] 			orgImagePixels;	
	private int 			orgWidth, orgHeight;
	
	public ImageScaler(BufferedImage image) {
			this.orgImage 				= image;			
			this.orgImagePixels 		= getPixelsFromBufferedImage(orgImage);
			this.orgWidth 				= orgImage.getWidth();
			this.orgHeight 				= orgImage.getHeight();			
	}

	public BufferedImage scale(int longestEdge) {
		return getScaledImage(longestEdge);
	}
	
	private int[] getPixelsFromBufferedImage(BufferedImage bufferedImage) {
		return bufferedImage.getRGB( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());		
	}
	
	private BufferedImage getBufferedImageFromPixels(int[] pixels, int width, int height) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
		return bufferedImage;
	}
	
	private BufferedImage getScaledImage(int longestEdge) {	
		// TODO crop
		/*
		if((orgWidth == longestEdge || orgHeight == longestEdge) && (orgWidth <= longestEdge && orgHeight <= longestEdge)) { 
			return getBufferedImageFromPixels(orgImagePixels, orgWidth, orgHeight);
		}
		
		// TODO crop
		
		int widthNew = 0, heightNew = 0;	
		if(this.orgWidth > this.orgHeight) {
			widthNew 	= longestEdge;
			heightNew 	= (int)((this.orgHeight * longestEdge / (double)orgWidth) + 0.5);
		} else if(this.orgWidth < this.orgHeight) {
			heightNew 	= longestEdge;			
			widthNew 	= (int)((orgWidth * longestEdge / (double)orgHeight) + 0.5);
		} else {
			heightNew	= longestEdge;
			widthNew 	= longestEdge;
		}
		*/
		
		int heightNew	= longestEdge;
		int widthNew 	= longestEdge;
			
		int[] pixelsNew = new int[widthNew * heightNew];
		int newPos = 0;
	 		
	 	final int XSTEP = 1;
	 	final int YSTEP = orgWidth;
	 
	 	float xDelta = (float) orgWidth / widthNew;
	 	float yDelta = (float) orgHeight / heightNew;

		for (int yNew = 0; yNew < heightNew; yNew++) {
			float yCoord = yNew * yDelta;
			int yPixelCoord = (int) yCoord;
			int yPixelPos = yPixelCoord * orgWidth;
			
			float v = yCoord - yPixelCoord;
			for (int xNew = 0; xNew < widthNew; xNew++, newPos++) {
				float xCoord = xNew * xDelta;
				int xPixelCoord = (int) xCoord;
				int xPixelPos = xPixelCoord;
				
				float h = xCoord - xPixelCoord;
				int pixelCoords = yPixelPos + xPixelPos;
				if (yCoord + 1 < orgHeight && xCoord + 1 < orgWidth) {

					int A = orgImagePixels[pixelCoords];
					int B = orgImagePixels[pixelCoords + XSTEP];
					int C = orgImagePixels[pixelCoords + YSTEP];
					int D = orgImagePixels[pixelCoords + XSTEP + YSTEP];

					int rA = (A >> 16) & 0xff, gA = (A >> 8) & 0xff, bA = A & 0xff;
					int rB = (B >> 16) & 0xff, gB = (B >> 8) & 0xff, bB = B & 0xff;
					int rC = (C >> 16) & 0xff, gC = (C >> 8) & 0xff, bC = C & 0xff;
					int rD = (D >> 16) & 0xff, gD = (D >> 8) & 0xff, bD = D & 0xff;

					int r = (int) (rA * (1 - h) * (1 - v) + rB * h * (1 - v) + rC * (1 - h) * v + rD * h * v);
					int g = (int) (gA * (1 - h) * (1 - v) + gB * h * (1 - v) + gC * (1 - h) * v + gD * h * v);
					int b = (int) (bA * (1 - h) * (1 - v) + bB * h * (1 - v) + bC * (1 - h) * v + bD * h * v);

					pixelsNew[newPos] = (0xFF << 24) | (r << 16) | (g << 8) | b;
				} else {
					pixelsNew[newPos] = orgImagePixels[pixelCoords];
				}
			}		
		}
		return getBufferedImageFromPixels(pixelsNew, widthNew, heightNew);
	}
}
