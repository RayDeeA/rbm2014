/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.settings;

/**
 *
 * @author christoph
 */
public class RBMSettingsStoppingConditionModel {
    private final RBMSettingsStoppingConditionController controller;
    
    private boolean epochsOn = true;
    private boolean errorOn = false;
    private int epochs = 100;
    private double error = 0.1;

    public RBMSettingsStoppingConditionModel(RBMSettingsStoppingConditionController controller) {
        this.controller = controller;
    }
    public boolean isEpochsOn() {
        return epochsOn;
    }

    public void setEpochsOn(boolean epochsOn) {
        this.epochsOn = epochsOn;
    }

    public boolean isErrorOn() {
        return errorOn;
    }

    public void setErrorOn(boolean errorOn) {
        this.errorOn = errorOn;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }
}
