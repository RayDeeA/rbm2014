/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.RBMStack;
import java.util.LinkedList;

/**
 *
 * @author Moritz
 */
public class BenchmarkModel {
    
    private final RBMStack rbmStack;
    
    private final LinkedList<RBMSettingsController> rbmSettingsList;

    public BenchmarkModel() {
        this.rbmStack = new RBMStack();
        this.rbmSettingsList = new LinkedList<>();
    }

    /**
     * @return the rbmStack
     */
    public RBMStack getRbmStack() {
        return rbmStack;
    }
    public boolean add(RBMSettingsController rbmSettings) {
        rbmStack.add(rbmSettings.getModel().getRBM());
        return this.rbmSettingsList.add(rbmSettings);
        
    }
    
    
    
    
    
}
