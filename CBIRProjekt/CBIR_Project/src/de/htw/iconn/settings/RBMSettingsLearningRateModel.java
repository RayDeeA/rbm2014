/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.settings;

/**
 *
 * @author moritz
 */
public class RBMSettingsLearningRateModel{
    
    private final RBMSettingsLearningRateController controller;
    
    private double constantLearningRate = 0.1;

    public RBMSettingsLearningRateModel(RBMSettingsLearningRateController controller) {
        this.controller = controller;
    }

    
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
