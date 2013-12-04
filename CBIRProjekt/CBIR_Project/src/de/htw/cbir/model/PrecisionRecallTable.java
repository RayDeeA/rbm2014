package de.htw.cbir.model;

import java.util.Locale;

import de.htw.cbir.gui.Graph;

public class PrecisionRecallTable {

    private static Graph graph = null;

    public static void initializeGraph() {
        graph = new Graph();
    }

    private boolean isStarted;
    private boolean visualize;
    private String sortMethod;
    private String description;

    private double meanAveragePrecision;
    private double[][] recalls;
    private double[][] precisions;

    public PrecisionRecallTable(boolean visualize, String sortMethod, String description) {
        this.isStarted = false;
        this.visualize = visualize;
        this.sortMethod = sortMethod;
        this.description = description;

        this.meanAveragePrecision = 0;
        this.recalls = new double[0][];
        this.precisions = new double[0][];
    }

    /**
     * Analysiert und liefert die Average Precision zurück
     *
     * @param sortedArray
     * @return
     */
    public double analyse(ImagePair[] sortedArray, int num) {
        return (visualize) ? complexAnalysis(sortedArray, num) : calcMeanAveragePrecision(sortedArray);
    }

    private double complexAnalysis(ImagePair[] sortedArray, int num) {

        // existiert schon der passende Array
        if (isStarted == false) {
            System.out.println("Need to start the Complex Analysis first.");
            return 0;
        }

        // wurde die Anzahl an Testruns noch nicht überschritten
        if (num > precisions.length) {
            System.out.println("Reached the maximum amount of analysis for this table.");
            return 0;
        }

        int numPics = sortedArray.length;
        double[] precision = new double[numPics];
        double[] recall = new double[numPics];

        double precisionSum = 0;
        int currRelevantRetrieved = 0;

        for (int pos = 0; pos < sortedArray.length; pos++) {
            ImagePair imagePair = sortedArray[pos];
            if (imagePair.isSameCategory()) {
                currRelevantRetrieved++;
                precisionSum += (double) currRelevantRetrieved / (pos + 1);
            }

            precision[pos] = (float) currRelevantRetrieved / (pos + 1);
            recall[pos] = (float) currRelevantRetrieved / imagePair.getQueryImage().getTypeOccurrence();
        }

        // speichere in globalen Array
        precisions[num] = precision;
        recalls[num] = recall;
        meanAveragePrecision += precisionSum / currRelevantRetrieved;

        return precisionSum / currRelevantRetrieved;
    }

    public static double calcMeanAveragePrecision(ImagePair[] sortedArray) {
        double precisionSum = 0;
        int currRelevantRetrieved = 0;

        for (int pos = 0; pos < sortedArray.length; pos++) {
            ImagePair imagePair = sortedArray[pos];

            if (imagePair.isSameCategory()) {
                currRelevantRetrieved++;
                precisionSum += (double) currRelevantRetrieved / (pos + 1);
            }
        }

        return precisionSum / currRelevantRetrieved;
    }

    public void finish() {
        if (graph != null && visualize) {

            int numTestRun = precisions.length;
            int numPics = precisions[0].length;

            float[][] avgPOverR = new float[4][numPics];
            for (int pic = 0; pic < numPics; pic++) {

                // zähle alle Testergebnisse zusammen
                double avgPrecision = 0, avgRecall = 0;
                for (int runs = 0; runs < numTestRun; runs++) {
                    avgPrecision += precisions[runs][pic];
                    avgRecall += recalls[runs][pic];
                }

                avgPOverR[2][pic] = (float) avgPrecision / numTestRun;
                avgPOverR[3][pic] = (float) avgRecall / numTestRun;
            }

            String title = String.format(Locale.ENGLISH, "%s %s mAP=%6.3f", sortMethod, description, meanAveragePrecision / numTestRun);
            graph.addGraph(avgPOverR, title);
        }
    }

    public void start(int numTestRuns) {
        this.recalls = new double[numTestRuns][];
        this.precisions = new double[numTestRuns][];
        this.isStarted = true;
        this.meanAveragePrecision = 0;
    }
}
