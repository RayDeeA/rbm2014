package de.htw.iconn.image;

import java.awt.image.BufferedImage;

public class DataConverter {

    public static float[][] generatePixelIntensityData(Pic[] pics, int edgeLength) {

        float[][] data = new float[pics.length][edgeLength * edgeLength];

        for (int i = 0; i < pics.length; i++) {
            ImageScaler imageScaler = new ImageScaler(pics[i].getDisplayImage());
            BufferedImage scaledImage = imageScaler.scale(edgeLength);
            int[] pixels = scaledImage.getRGB(0, 0, edgeLength, edgeLength, null, 0, edgeLength);

            for (int p = 0; p < pixels.length; p++) {
                int argb = pixels[p];

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb) & 0xFF;

                float intensity = (float)(0.299 * r + 0.587 * g + 0.114 * b) / 255.0f;

                data[i][p] = intensity;
            }

        }

        return data;
    }

    public static float[][] binarizeGrayImageWithIsoData(float[][] data) {
        float[][] binarized = new float[data.length][data[0].length];
        
        for(int i = 0; i < data.length; ++i){
            binarized[i] = binarizeGrayImageWithIsoData(data[i]);
        }
        
        return binarized;
    }

    public static float[] binarizeGrayImageWithIsoData(float[] data) {
        final int safetyStop = 50;
        
        float[] binarized = new float[data.length];

        int tOld = 0;
        int tNew = -2;
        int sum = 0;

        //initial threshold is mean gray value
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }

        tOld = sum / data.length;

        int smallerSum = 0;
        int biggerSum = 0;
        int smallerNumber = 0;
        int biggerNumber = 0;

        for (int j = 0; Math.abs(tOld - tNew) > 1 && j < safetyStop; j++) {
            tOld = tNew;
            for (int i = 0; i < data.length; i++) {

                if (data[i] < tOld) {
                    smallerSum += data[i];
                    smallerNumber++;
                } else {
                    biggerSum += data[i];
                    biggerNumber++;
                }
            }
            //check if one side has no color values
            if (smallerNumber == 0) {
                tNew = biggerSum / biggerNumber;
            } else if (biggerNumber == 0) {
                tNew = smallerSum / smallerNumber;
            } else {
                tNew = (smallerSum / smallerNumber + biggerSum / biggerNumber) / 2;
            }
            smallerSum = biggerSum = smallerNumber = biggerNumber = 0;
        }

        int threshold = tNew;

        for(int i = 0; i < data.length; i++){
            binarized[i] = data[i] < threshold ? 0.0f : 1.0f;
        }    
        
        return binarized;
    }
}
