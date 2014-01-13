

package de.htw.iconn.persistence;

import de.htw.iconn.enhancement.IRBMTrainingEnhancement;
import de.htw.iconn.enhancement.RBMInfoPackage;

/**
 *
 * @author moritz
 */
public class XMLTrainingLogger implements IRBMTrainingEnhancement {

    private final int updateIntervall;
    private final XMLWeightsSaver logger;
    public XMLTrainingLogger(int updateIntervall) {
        this.updateIntervall = updateIntervall;
        this.logger = new XMLWeightsSaver();
    }

    public XMLTrainingLogger() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

      
    @Override
    public int getUpdateInterval() {
        return this.updateIntervall;
    }

    @Override
    public void action(RBMInfoPackage info) {
        System.out.println("XMLTrainingLogger does nothing");
    }
    
    
    
    
}
