/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.settings;

import de.htw.iconn.fx.decomposition.XMLWeightsLoader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

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
    private final XMLWeightsLoader loader;

    public RBMSettingsWeightsModel() {
        this.loader = new XMLWeightsLoader();
    }
            
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
    
    public void loadWeights() {
        try {
            weights = this.loader.loadWeightsFromXML();
            this.initializedWeights = false;
            
            System.out.println(getWeights()[0][0]);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(RBMSettingsWeightsModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
