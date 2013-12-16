/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.rbm.ARBMAdapter;
import de.htw.iconn.fx.decomposition.tools.ImageManager;
import de.htw.iconn.fx.decomposition.tools.Pic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

/**
 *
 * @author Radek
 */
public class RunHiddenModel {
    
    private final RunHiddenController controller;
    
    private ARBMAdapter rbmFeature;
    private Pic pic;
    
    private boolean useHiddenStates;
    private boolean useVisibleStates;
    
    public RunHiddenModel(RunHiddenController controller) {
    	useHiddenStates = false;
    	useVisibleStates = false;
        this.controller = controller;
    }

    public void setRbmFeature(ARBMAdapter rbmFeature) {
        this.rbmFeature = rbmFeature;
    }
    
    public Image openFile() {
        int width = 100;
        int height = 100;

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, Color.RED);
            }
        }

        return image;
    }
    
    public Image openFile2() {
        
        Image img = new Image("file:" + "/Volumes/Data/Dropbox/Development/Netbeans/ICONN/CBIRProjekt/CBIR_Project/images/MNIST_1000_Database/" + "0_01.png");
        
        return img;
    }
    
    public Image openFile3() {
        try {
            int width = 28;
            int height = 28;
            
            File img = new File("/Volumes/Data/Dropbox/Development/Netbeans/ICONN/CBIRProjekt/CBIR_Project/images/MNIST_1000_Database/" + "0_01.png");
            BufferedImage in = ImageIO.read(img);
            
            WritableImage wi = new WritableImage(width, height);
            SwingFXUtils.toFXImage(in, wi);
            
            return wi;
        } catch (IOException ex) {
            Logger.getLogger(RunHiddenModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Image openFile4() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("CBIR_Project/images"));
        Stage fileChooserStage = new Stage();
        
        File file = fileChooser.showOpenDialog(fileChooserStage);
        if(file != null) {
            ImageManager imageManager = new ImageManager();
            this.pic = imageManager.loadImage(file);
            
            BufferedImage bufferedImage = this.pic.getDisplayImage();
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            
            WritableImage image = new WritableImage(width, height);
            SwingFXUtils.toFXImage(bufferedImage, image);
            
            return image;
        } else return null;
    }

    public Image runHidden() {
        double[] hiddenData = null;
        double[] visibleData = null;
        
        int width = this.pic.getDisplayImage().getWidth();
        int height = this.pic.getDisplayImage().getHeight();

        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0, pos = 0; y < height; y++) {
            for (int x = 0; x < width; x++, pos++) {
                int value = (int) (Math.max(Math.min(visibleData[pos] * 255, 255), 0));
                writer.setColor(x, y, Color.rgb(value, value, value));
            }
        }
        
        return image;
    }
    
    public void setUseHiddenStates(boolean useHiddenStates) {
		this.useHiddenStates = useHiddenStates;
	}

	public void setUseVisibleStates(boolean useVisibleStates) {
		this.useVisibleStates = useVisibleStates;
	}
}
