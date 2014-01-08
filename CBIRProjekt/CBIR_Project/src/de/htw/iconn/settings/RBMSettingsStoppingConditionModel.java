/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.settings;

import de.htw.iconn.persistence.Conserve;

/**
 *
 * @author christoph
 */
public class RBMSettingsStoppingConditionModel {
    private final RBMSettingsStoppingConditionController controller;
    
    @Conserve
    private boolean epochsOn = true;
    @Conserve
    private boolean errorOn = false;
    @Conserve
    private int epochs = 100;
    @Conserve
    private float error = 0.1f;

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

    public float getError() {
        return error;
    }

    public void setError(float error) {
        this.error = error;
    }
}
