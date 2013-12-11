/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.rbm.enhancements;

import java.util.LinkedList;

/**
 *
 * @author moritz
 */
public class RBMInfoPackage {
    
    private double error;
    private double[][] weights;
    private int epochs;
    private final LinkedList<double[][]> collectedWeights;

    public RBMInfoPackage(double error, double[][] weights, int epochs) {
        this.error = error;
        this.weights = weights;
        this.epochs = epochs;
        this.collectedWeights = new LinkedList<>();
    }

    public double getError() {
        return error;
    }

    public double[][] getWeights() {
        return weights;
    }

    public int getEpochs() {
        return epochs;
    }

    LinkedList<double[][]> getCollectedWeights() {
        return collectedWeights;
    }

    /**
     * @param error the error to set
     */
    public void setError(double error) {
        this.error = error;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(double[][] weights) {
        this.collectedWeights.add(weights);
        this.weights = weights;
    }

    /**
     * @param epochs the epochs to set
     */
    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }
    
    
    
}
