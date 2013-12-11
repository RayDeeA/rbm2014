/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import de.htw.cbir.ImageManager;
import de.htw.iconn.fx.ImageViewer;
import de.htw.iconn.rbm.RBMStack;
import java.util.LinkedList;

/**
 *
 * @author Moritz
 */
public class BenchmarkModel {

    private final RBMStack rbmStack;

    private final LinkedList<RBMSettingsController> rbmSettingsList;
    private ImageManager imageManager;
    private ImageViewer imageViewer;
    private boolean showImageViewer = false;

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

    public LinkedList<RBMSettingsController> getRbmSettingsList() {
        return rbmSettingsList;
    }

    public void setImageManager(ImageManager imageManager) {

        this.imageManager = imageManager;
    }

    public boolean isShowImageViewer() {
        return showImageViewer;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public void createImageViewer() {
        this.imageViewer = new ImageViewer(imageManager);
    }

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

}
