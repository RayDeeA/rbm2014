package de.htw.iconn.settings;

import de.htw.iconn.main.AController;
import de.htw.iconn.rbm.ARBMAdapter;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Moritz
 */
public class RBMSettingsModel {
    
    private final RBMSettingsController controller;

    private final AController[] controllers;
    private final TreeItem<String>[] items;

    private double[][] data;

    private ARBMAdapter rbmFeature;

    public RBMSettingsModel(TreeItem<String>[] items, AController[] controllers, RBMSettingsController controller){
        this.controllers = controllers;
        this.items = items;    
        this.controller = controller;
    }

    public TreeItem<String>[] getTreeItems() {
        return this.items;
    }

    public AController[] getControllers() {
        return this.controllers;
    }

    public <T extends AController> T getController(Class<T> type) {
        for (AController aController : controllers) {
            if (aController.getClass().equals(type)) {
                return type.cast(aController);
            }
        }
        return null;
    }

    /**
     * @return the data
     */
    public double[][] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(double[][] data) {
        this.data = data;
    }

    /**
     * @return the rbmFeature
     */
    public ARBMAdapter getARBMFeature() {
        return this.rbmFeature;
    }

}
