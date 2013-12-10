/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

/**
 *
 * @author moritz
 */
public class RBMSettingsLearningRateModel {
    
    private double constantLearningRate = 0.1;

    /**
     * @return the constantLearningRate
     */
    public double getConstantLearningRate() {
        return constantLearningRate;
    }

    /**
     * @param constantLearningRate the constantLearningRate to set
     */
    public void setConstantLearningRate(double constantLearningRate) {
        this.constantLearningRate = constantLearningRate;
    }
}
