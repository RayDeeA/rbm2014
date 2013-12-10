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
public class RBMSettingsLoggerModel {
    private boolean continuousLoggerOn = false;
    private boolean finalLoggerOn = true;
    
    private int continuousIntervall = 1000;

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
     * @param finalLoggerOn the endLoggerOn to set
     */
    public void setFinalLoggerOn(boolean endLoggerOn) {
        this.finalLoggerOn = endLoggerOn;
    }

    /**
     * @return the continuousIntervall
     */
    public int getContinuousIntervall() {
        return continuousIntervall;
    }

    /**
     * @param continuousIntervall the continuousIntervall to set
     */
    public void setContinuousIntervall(int continuousIntervall) {
        this.continuousIntervall = continuousIntervall;
    }
}
