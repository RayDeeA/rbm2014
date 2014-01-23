package de.htw.iconn.views;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import de.htw.iconn.enhancement.IVisualizeObserver;
import de.htw.iconn.enhancement.RBMInfoPackage;
import de.htw.iconn.image.DataConverter;
import de.htw.iconn.image.ImageViewer;
import de.htw.iconn.image.Pic;
import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkController;
import de.htw.iconn.rbm.RBMTrainer;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsMainController;
import de.htw.iconn.views.imageviewer.ImageViewerController;

public class FeatureViewer extends AController  {
  
  private BenchmarkController benchmarkController;
  ImageViewerController       imageViewerController;
  
  public FeatureViewer(BenchmarkController benchmarkController) {
    try {
      imageViewerController = (ImageViewerController) loadController("../views/ImageViewer.fxml");
    } catch (IOException ex) {
      Logger.getLogger(BenchmarkController.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.benchmarkController = benchmarkController;
  }
  
  @Override
  public void update() {
    List<RBMSettingsController> rbmSettingsControllers = this.benchmarkController.getModel().getRbmSettingsList();
    
    int outputSize = ((RBMSettingsMainController) (rbmSettingsControllers.get(rbmSettingsControllers.size() - 1).getModel()
        .getController(RBMSettingsMainController.class))).getModel().getOutputSize();
    
    RBMTrainer rbmTrainer = new RBMTrainer();
    
    Pic[] pics = new Pic[outputSize];
    
    for (int i = 0; i < outputSize; i++) {
      float[] hiddenData = new float[outputSize];
      hiddenData[i] = 1.0f;
      
      float[] visibleData = rbmTrainer.getVisibleAllRBMs1D(benchmarkController.getModel(), hiddenData, false);
      
      BufferedImage image = DataConverter.pixelDataToImage(visibleData, 0, this.benchmarkController.getModel().isRgb());
      
      Pic pic = new Pic();
      pic.setDisplayImage(image);
      pic.setOrigWidth(image.getWidth());
      pic.setOrigHeight(image.getHeight());
      pic.setRank(i);
      pics[i] = pic;
    }
    
    imageViewerController.getModel().setImages(pics);
    // return pics;
  }
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Node getView() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public void show() {
    if (this.benchmarkController.getModel().getRbmSettingsList().size() == 0) {
      System.out.println("Please create a RBM first. (Edit > add RBM)");
      return;
    }
    update();
    imageViewerController.show();
  }
  
  public void close() {
    imageViewerController.close();
  }
  
}
