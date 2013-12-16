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
    private boolean showImageViewer = false;
    private int selectedMAPTest = 0;

    public int getSelectedMAPTest() {
        return selectedMAPTest;
    }

    public void setSelectedMAPTest(int selectedMAPTest) {
        this.selectedMAPTest = selectedMAPTest;
    }

    public BenchmarkModel(BenchmarkController controller) {
        this.rbmSettingsList = new LinkedList<>();
        this.controller = controller;
    }

    public boolean add(RBMSettingsController rbmSettings) {
        
        if(rbmSettingsList.size() == 0) {
            if( this.rbmSettingsList.add(rbmSettings)) {
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
    
    public void setShowImageViewer(boolean showImageViewer){
        this.showImageViewer = showImageViewer;
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
        if (this.getRbmSettingsList().size() > 0 && imageManager  != null) {
            // TODO: checkbox for shuffled input data
        	RBMSettingsModel firstModel = this.getRbmSettingsList().getFirst().getModel();
        	RBMSettingsMainModel firstMainModel = firstModel.getController(RBMSettingsMainController.class).getModel();
        	int inputSize = firstMainModel.getInputSize();
            firstModel.setData(DataConverter.generatePixelIntensityData(imageManager.getImages(false), inputSize));
        }
    }

}
