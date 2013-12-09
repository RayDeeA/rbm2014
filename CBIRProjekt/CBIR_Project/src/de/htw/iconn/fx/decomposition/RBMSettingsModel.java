/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.IRBM;

/**
 *
 * @author Moritz
 */
public class RBMSettingsModel {
    private IRBM rbm;

    public RBMSettingsModel(IRBM rbm) {
        this.rbm = rbm;
    }
    
    
    public void setRBM(IRBM rbm) {
        this.rbm = rbm;
    }
    
    public IRBM getRBM() {
        return rbm;
    }
    
    
    
}
