/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.IRBM;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Moritz
 */
public class RBMSettingsModel {
    

    private final AController[] controllers;
    private final TreeItem[] items;

    private IRBM rbm;

    public RBMSettingsModel(IRBM rbm, TreeItem[] items, AController[] controllers) {
        this.rbm = rbm;
        this.controllers = controllers;
        this.items = items;
    }
    
    
    public void setRBM(IRBM rbm) {
        this.rbm = rbm;
    }
    
    public IRBM getRBM() {
        return rbm;
    }
    
    public TreeItem[] getTreeItems() {
        return this.items;
    }
    
    public AController[] getControllers() {
        return this.controllers;
    }
    
    public <T extends AController> T getController(Class<T> type) {
        for (AController aController : controllers) {
            if(aController.getClass().equals(type)) {
                return type.cast(aController);
            }
        }
        return null;
    } 
    
}
