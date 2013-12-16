/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.rbm.RBMStack;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.ImageViewer;
import de.htw.iconn.fx.decomposition.views.PRTMAPController;
import java.io.IOException;
import java.util.LinkedList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Moritz
 */
public class BenchmarkModel {

    private final BenchmarkController controller;
    private final RBMStack rbmStack;

    private final LinkedList<RBMSettingsController> rbmSettingsList;
    private ImageManager imageManager = null;
    private ImageViewer imageViewer;
    private final PRTMAPController prtmapController;
    private boolean showImageViewer = false;
    private int selectedMAPTest = 0;
    private boolean isPRTMAPViewerVisible;

    public int getSelectedMAPTest() {
        return selectedMAPTest;
    }

    public void setSelectedMAPTest(int selectedMAPTest) {
        this.selectedMAPTest = selectedMAPTest;
    }

    public BenchmarkModel(BenchmarkController controller, PRTMAPController prtmapController) {
        this.rbmStack = new RBMStack();
        this.rbmSettingsList = new LinkedList<>();
        this.controller = controller;
        this.prtmapController = prtmapController;

    }

    /**
     * @return the rbmStack
     */
    public RBMStack getRbmStack() {
        return rbmStack;
    }

    public boolean add(RBMSettingsController rbmSettings) {
        rbmStack.add(rbmSettings.getModel().getRBM());

        if (rbmSettingsList.size() == 0) {
            if (this.rbmSettingsList.add(rbmSettings)) {
                setRBMImageSet();
                return true;
            }
        }

        return this.rbmSettingsList.add(rbmSettings);

    }

    public LinkedList<RBMSettingsController> getRbmSettingsList() {
        return rbmSettingsList;
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
        this.setRBMImageSet();
        this.imageViewer = new ImageViewer(imageManager);
    }

    public boolean isShowImageViewer() {
        return showImageViewer;
    }

    public void setShowImageViewer(boolean showImageViewer) {
        this.showImageViewer = showImageViewer;
    }

    public void setVisibilityPRTMAPViewer(boolean b) {
        this.isPRTMAPViewerVisible = b;
    }

    public boolean isPRTMAPViewerVisible() {
        return isPRTMAPViewerVisible;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public void createDaydreamViewer() {

    }

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    private void setRBMImageSet() {
        if (this.getRbmSettingsList().size() > 0 && imageManager != null) {
            // TODO: checkbox for shuffled input data
            this.getRbmSettingsList().getFirst().getModel().setData(imageManager.getImages(false));
        }
    }

    PRTMAPController getPRTMAPController() {
        return prtmapController;
    }

}
