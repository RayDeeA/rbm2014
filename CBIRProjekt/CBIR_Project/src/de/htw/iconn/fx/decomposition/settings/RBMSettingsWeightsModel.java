/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

/**
 *
 * @author Moritz
 */
public class RBMSettingsWeightsModel {
    
    private boolean initializedWeights = true;
    private boolean useBias = true;
    private boolean useSeed = false;
    private int seed = 0;
    private double[][] weights;

    public boolean isUseBias() {
        return useBias;
    }

    public void setUseBias(boolean useBias) {
        this.useBias = useBias;
    }

    public boolean isUseSeed() {
        return useSeed;
    }

    public void setUseSeed(boolean useSeed) {
        this.useSeed = useSeed;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     * @return the initializedWeights
     */
    public boolean isInitializedWeights() {
        return initializedWeights;
    }

    /**
     * @param initializedWeights the initializedWeights to set
     */
    public void setInitializedWeights(boolean initializedWeights) {
        this.initializedWeights = initializedWeights;
    }

    public double[][] getWeights() {
        return this.weights;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }
   
}
