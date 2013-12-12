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
public class BenchmarkModel extends AModel {

    private final RBMStack rbmStack;

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
        this.rbmStack = new RBMStack();
        this.rbmSettingsList = new LinkedList<>();
        this.addObserver(controller);
        this.notifyObservers();
    }

    /**
     * @return the rbmStack
     */
    public RBMStack getRbmStack() {
        return rbmStack;
    }

    public boolean add(RBMSettingsController rbmSettings) {
        rbmStack.add(rbmSettings.getModel().getRBM());
        
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

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    private void setRBMImageSet() {
        if (this.getRbmSettingsList().size() > 0 && imageManager  != null) {
            // TODO: checkbox for shuffled input data
            this.getRbmSettingsList().getFirst().getModel().setData(imageManager.getImages(false));
        }
    }

}
