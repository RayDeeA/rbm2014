package iconn.htw.sorter;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

import de.htw.cbir.model.Pic;
import de.htw.cbir.model.Settings;
import de.htw.cbir.sorter.*;
import iconn.htw.helper.MathHelper;

public class Sorter_DCT_CJ extends Sorter{
	
	private int counter;
	//val equals 2 results in 15 dimensional feature vector (3 + val * 2 * 3)
	private int numberOfDctValuesPerDimension = 2;
	//reduce image to size (val * val)
	private int parts = 8;

	public Sorter_DCT_CJ(Pic[] images, Settings settings, ForkJoinPool pool) {
		super(images, settings, pool);
		counter = 0;
	}

	@Override
	protected BufferedImage getFeatureImage(Pic image) {
		int w = 1;
		int h = 1;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();

		int[] pixels = new int[h * w];

		double[] featureVector = image.getFeatureVector();
		int r = (int) featureVector[0];
		int g = (int) featureVector[1];
		int b = (int) featureVector[2];

		pixels[0] = (0xFF << 24) | (r << 16) | (g << 8) | b;

		BufferedImage bThumb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		bThumb.setRGB(0, 0, w, h, pixels, 0, w);

		big.drawImage(bThumb, 0, 0, w, h, null);
		big.dispose();
		return bi;
	}

	@Override
	protected double[] getFeatureVector(Pic image) {
		System.out.println(++counter);
		BufferedImage bi = image.getDisplayImage();
		
		int w = bi.getWidth();
		int h = bi.getHeight();
		
		int[] rgb = new int[w * h];
		bi.getRGB(0, 0, w, h, rgb, 0, w);
		
		int[] reduction = rgb;
		
		int widthPerPart = w;
		int heightPerPart = h;
		
		int resultingHeight = h;
		int resultingWidth = w;
		
		//reduce the image size by calculating the mean colors for a number of blocks
		if(parts > 1){
			resultingHeight = resultingWidth = parts;
			
			widthPerPart = w / parts;
			heightPerPart = h / parts;
			
			int[][] rgbValues = new int[parts * parts][widthPerPart * heightPerPart];
			
			for(int i = 0; i < parts; i++){
				for(int j = 0; j < parts; j++){
					bi.getRGB(j * widthPerPart, i * heightPerPart, widthPerPart, heightPerPart, rgbValues[i*parts + j], 0, widthPerPart);
				}
			}
	
			reduction = new int[parts * parts];
			
			for(int z = 0; z < parts * parts; z++){
				int r = 0;
				int g = 0;
				int b = 0;
				int sum = 0;
	
				for (int y = 0; y < heightPerPart; y++) {
					for (int x = 0; x < widthPerPart; x++) {
						int pos = y * widthPerPart + x;
	
						r += (rgbValues[z][pos] >> 16) & 255;
						g += (rgbValues[z][pos] >> 8) & 255;
						b += (rgbValues[z][pos]) & 255;
	
						sum++;
					}
				}
	
				// compute the mean color
				reduction[z] = ((r / sum) << 16) | ((g / sum) << 8) | b / sum;
			}
		}
		
		double[][][] ycbcr = MathHelper.RGBToYCbCr(MathHelper.rgbArrayToMultiDimension(reduction, resultingWidth, resultingHeight));
		
		double[][] dctY = MathHelper.twoDimDCT(ycbcr[0]);
		double[][] dctCb = MathHelper.twoDimDCT(ycbcr[1]);
		double[][] dctCr = MathHelper.twoDimDCT(ycbcr[2]);
		
		if(numberOfDctValuesPerDimension >= w) numberOfDctValuesPerDimension = w-1;
		if(numberOfDctValuesPerDimension >= h) numberOfDctValuesPerDimension = h-1;	
		
		//create feature vector		
		double[] featureVector = new double[(2 * numberOfDctValuesPerDimension + 1) * 3];
		
		featureVector[0] = dctY[0][0];
		featureVector[1] = dctCb[0][0];
		featureVector[2] = dctCr[0][0];	
		for(int i = 0; i < numberOfDctValuesPerDimension; i++){
			featureVector[i*6+3+0] = dctY[0][i+1];
			featureVector[i*6+3+1] = dctCb[0][i+1];
			featureVector[i*6+3+2] = dctCr[0][i+1];
			featureVector[i*6+3+3] = dctY[i+1][0];
			featureVector[i*6+3+4] = dctCb[i+1][0];
			featureVector[i*6+3+5] = dctCr[i+1][0];
		}
		return featureVector;
	}

	@Override
	public double getDistance(double[] fv1, double[] fv2) {
		return getL1Dist(fv1, fv2);
	}

	@Override
	public String getName() {
		return "DCT_CJ";
	}

	@Override
	public void settingsChanged(ActionEvent e) {
		
	}

}
