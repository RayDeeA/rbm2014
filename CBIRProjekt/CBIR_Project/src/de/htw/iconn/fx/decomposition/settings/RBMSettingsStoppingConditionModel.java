/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.AModel;

/**
 *
 * @author christoph
 */
public class RBMSettingsStoppingConditionModel extends AModel {
    private boolean epochsOn = true;
    private boolean errorOn = false;
    private int epochs = 1000;
    private double error = 0.1;

    public RBMSettingsStoppingConditionModel(RBMSettingsStoppingConditionController controller) {
        addObserver(controller);
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
