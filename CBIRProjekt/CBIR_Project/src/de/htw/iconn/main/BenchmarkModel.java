/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.main;

import de.htw.iconn.settings.RBMSettingsModel;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.statistics.PrecisionRecallTester;
import de.htw.iconn.statistics.PrecisionRecallTestResult;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.settings.RBMSettingsMainController;
import de.htw.iconn.settings.RBMSettingsMainModel;
import de.htw.iconn.image.ImageManager;
import de.htw.iconn.image.ImageViewer;
import de.htw.iconn.persistence.Conserve;
import de.htw.iconn.views.PRTMAPController;
import java.util.LinkedList;

/**
 *
 * @author Moritz
 */
public class BenchmarkModel {

    private final BenchmarkController controller;
    private ImageViewer imageViewer;
    private final PRTMAPController prtmapController;
    private final LinkedList<RBMSettingsController> rbmSettingsList;
    
    @Conserve
    private ImageManager imageManager = null;
    @Conserve
    private boolean showImageViewer = false;
    @Conserve
    private int selectedMAPTest = 0;
    @Conserve
    private boolean isPRTMAPViewerVisible;
    @Conserve
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
    
    public int getInputSize(){
        return this.imageEdgeSize * this.imageEdgeSize;
    }
    
    public double[][] getInputData(){
        return DataConverter.generatePixelIntensityData(imageManager.getImages(false), this.imageEdgeSize);
    }

    PRTMAPController getPRTMAPController() {
        return prtmapController;
    }

}
