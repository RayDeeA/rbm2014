/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx.decomposition;

import de.htw.iconn.fx.decomposition.settings.RBMSettingsMainController;
import de.htw.iconn.fx.decomposition.settings.RBMSettingsMainModel;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.ImageViewer;
import de.htw.iconn.fx.decomposition.views.PRTMAPController;
import java.util.LinkedList;

/**
 *
 * @author Moritz
 */
public class BenchmarkModel {

    private final BenchmarkController controller;

    private final LinkedList<RBMSettingsController> rbmSettingsList;
    private ImageManager imageManager = null;
    private ImageViewer imageViewer;
    private final PRTMAPController prtmapController;
    private boolean showImageViewer = false;
    private int selectedMAPTest = 0;
    private boolean isPRTMAPViewerVisible;
    private int imageEdgeSize = 28;

    public int getImageEdgeSize() {
        return imageEdgeSize;
    }

    public void setImageEdgeSize(int imageEdgeSize) {
        this.imageEdgeSize = imageEdgeSize;
    }

    public int getSelectedMAPTest() {
        return selectedMAPTest;
    }

    public void setSelectedMAPTest(int selectedMAPTest) {
        this.selectedMAPTest = selectedMAPTest;
    }

    public BenchmarkModel(BenchmarkController controller, PRTMAPController prtmapController) {
        this.rbmSettingsList = new LinkedList<>();
        this.controller = controller;
        this.prtmapController = prtmapController;
    }

    public boolean add(RBMSettingsController rbmSettings) {

        if (rbmSettingsList.size() == 0) {
            if (this.rbmSettingsList.add(rbmSettings)) {
                this.setRBMImageSet();
                return true;
            }
        } else {
            RBMSettingsController lastController = this.rbmSettingsList.getLast();
            int lastOutputSize = lastController.getModel().getController(RBMSettingsMainController.class).getModel().getOutputSize();
            RBMSettingsMainModel mainModel = rbmSettings.getModel().getController(RBMSettingsMainController.class).getModel();
            mainModel.setInputSize(lastOutputSize);
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

    public void startMAPTest(String imageCategory) {

        double[][] features = controller.getRbmTrainer().getHiddenAllRBMs(controller, null, showImageViewer);
        PrecisionRecallTester prTester = new PrecisionRecallTester(features, imageManager);

        PrecisionRecallTestResult result;

        if (imageCategory.equalsIgnoreCase("All")) {
            result = prTester.testAll();
        } else {
            result = prTester.test(imageCategory);
        }

        this.prtmapController.addGraph(result);
    }

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    private void setRBMImageSet() {
        if (this.getRbmSettingsList().size() > 0 && imageManager != null) {
            // TODO: checkbox for shuffled input data
            RBMSettingsModel firstModel = this.getRbmSettingsList().getFirst().getModel();
            RBMSettingsMainModel firstMainModel = firstModel.getController(RBMSettingsMainController.class).getModel();
            firstMainModel.setInputSize(this.imageEdgeSize * this.imageEdgeSize);
            firstModel.setData(DataConverter.generatePixelIntensityData(imageManager.getImages(false), this.imageEdgeSize));
        }
    }

    PRTMAPController getPRTMAPController() {
        return prtmapController;
    }

}
