/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.settings;

import de.htw.iconn.persistence.Conserve;

/**
 *
 * @author moritz
 */
public class RBMSettingsLoggerModel{
    private final RBMSettingsLoggerController controller;
    @Conserve
    private boolean continuousLoggerOn = false;
    @Conserve
    private boolean finalLoggerOn = true;
    @Conserve
    private int continuousInterval = 1000;

    RBMSettingsLoggerModel(RBMSettingsLoggerController controller) {
        this.controller = controller;
    }
    /**
     * @return the continuousLoggerOn
     */
    public boolean isContinuousLoggerOn() {
        return continuousLoggerOn;
    }

    /**
     * @param continuousLoggerOn the continuousLoggerOn to set
     */
    public void setContinuousLoggerOn(boolean continuousLoggerOn) {
        this.continuousLoggerOn = continuousLoggerOn;
    }

    /**
     * @return the finalLoggerOn
     */
    public boolean isFinalLoggerOn() {
        return finalLoggerOn;
    }

    /**
     * @param endLoggerOn
     */
    public void setFinalLoggerOn(boolean endLoggerOn) {
        this.finalLoggerOn = endLoggerOn;
    }

    /**
     * @return the continuousInterval
     */
    public int getContinuousInterval() {
        return continuousInterval;
    }

    /**
     * @param continuousInterval the continuousInterval to set
     */
    public void setContinuousInterval(int continuousInterval) {
        this.continuousInterval = continuousInterval;
    }
}
