/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.main;

import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.statistics.PrecisionRecallTester;
import de.htw.iconn.statistics.PrecisionRecallTestResult;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.image.ImageManager;
import de.htw.iconn.image.ImageViewer;
import de.htw.iconn.persistence.Conserve;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.views.FeatureViewer;
import de.htw.iconn.views.PRTMAPController;

import java.util.LinkedList;


/**
 *
 * @author Moritz
 */
public class BenchmarkModel {

    private final BenchmarkController controller;
    private ImageViewer imageViewer;
    private FeatureViewer featureViewer;
    private final PRTMAPController prtmapController;
    private final LinkedList<RBMSettingsController> rbmSettingsList;
    private final RBMTrainer rbmTrainer;
    
    @Conserve
    private ImageManager imageManager = null;
    @Conserve
    private boolean binarizeImages;
    @Conserve
    private boolean showImageViewer = false;
    @Conserve
    private boolean showFeatureViewer = false;
    @Conserve
    private int selectedMAPTest = 0;
    @Conserve
    private boolean isPRTMAPViewerVisible;
    @Conserve
    private int imageEdgeSize = 28;
    @Conserve
    private boolean sorted = true;

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
        this.rbmTrainer = new RBMTrainer();
    }

    public void add(RBMSettingsController rbmSettings) {
        this.rbmSettingsList.add(rbmSettings);
        this.globalUpdate();
    }

    public LinkedList<RBMSettingsController> getRbmSettingsList() {
        return rbmSettingsList;
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
        this.imageViewer = new ImageViewer(imageManager, this.sorted);
        this.globalUpdate();
    }

    public boolean isShowImageViewer() {
        return this.showImageViewer;
    }
    
	public boolean isShowFeatureViewer() {
		return this.showFeatureViewer;
	}
    
	public void setBinarizeImages(boolean binarizeImages) {
		this.binarizeImages = binarizeImages;
	}

    public void setShowImageViewer(boolean showImageViewer) {
        this.showImageViewer = showImageViewer;
    }
    
	public void setShowFeatureViewer(boolean showFeatureViewer) {
		this.showFeatureViewer = showFeatureViewer;
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

        float[][] features = this.rbmTrainer.getHiddenAllRBMs(controller.getModel(), null, showImageViewer);
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
        return this.imageViewer;
    }
    
    public void initFeatureViewer(BenchmarkController benchmarkController) {
    	this.featureViewer = new FeatureViewer(benchmarkController);
    }
    
	public FeatureViewer getFeatureViewer() {
		return this.featureViewer;
	}
    
    public int getInputSize(){
        return this.imageEdgeSize * this.imageEdgeSize;
    }
    
    public float[][] getInputData() {
    	return DataConverter.generatePixelIntensityData(imageManager.getImages(this.sorted), this.imageEdgeSize, this.binarizeImages);
    }

    PRTMAPController getPRTMAPController() {
        return prtmapController;
    }
    
    public void globalUpdate(){
        this.rbmTrainer.updateRBMs(this);
    }
    
    public void updateAllViews(){
        this.controller.update();
        for(RBMSettingsController settingsController : rbmSettingsList){
            for(AController c : settingsController.getModel().getControllers()){
                c.update();
            }
        }
    }
    
    public void trainRBMs(){
        this.rbmTrainer.trainAllRBMs(this);
    }
    
    public void trainDeep(){
        this.rbmTrainer.trainAllRBMsDeepBelieve(this);
    }


	public void setShuffleImages(boolean shuffled) {
		this.sorted  = !shuffled;
	}

}
