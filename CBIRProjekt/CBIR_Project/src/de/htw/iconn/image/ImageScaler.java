package de.htw.iconn.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageScaler {
	
	private BufferedImage orgImage;
	
	public ImageScaler() {
	
	}
	
	public ImageScaler(BufferedImage image) {
			this.orgImage = image;	
	}
	
	public BufferedImage scale(int longestEdge) {
		return scale(longestEdge, longestEdge);
	}
	
	public BufferedImage scale(int width, int height) {
		return getScaledImage(width, height);
	}
	
	private int[] getPixelsFromBufferedImage(BufferedImage bufferedImage) {
		return bufferedImage.getRGB( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());		
	}
	
	private BufferedImage getBufferedImageFromPixels(int[] pixels, int width, int height) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
		return bufferedImage;
	}
	
	public BufferedImage getScaledImageNeirestNeighbour(BufferedImage image, int newWidth, int newHeight) {
		int[] pixels = this.getPixelsFromBufferedImage(image);
		int[] newPixels = new int[newWidth * newHeight];
		
		int width = image.getWidth();
		int height = image.getHeight();
		
        double x_ratio = width / (double) newWidth;
        double y_ratio = height / (double) newHeight;
        
        for (int y = 0, pos = 0; y < newHeight; y++) {
            double py = Math.floor(y * y_ratio);
            for (int x = 0; x < newWidth; x++, pos++) {
                double px = Math.floor(x * x_ratio);
                int argb = (int) pixels[(int) (py * width + px)];
                newPixels[pos] = argb;
            }
        }
		
		return this.getBufferedImageFromPixels(newPixels, newWidth, newHeight);
	}
	
	private BufferedImage getScaledImage(int width, int height) {
		Image scaledImage = this.orgImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage scaledBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = scaledBufferedImage.getGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
		return scaledBufferedImage;
	}
	
}
