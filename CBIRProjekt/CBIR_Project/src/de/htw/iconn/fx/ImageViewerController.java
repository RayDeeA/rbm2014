/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import de.htw.cbir.model.Pic;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author christoph
 */
public class ImageViewerController implements Initializable, IFXController {
    @FXML
    private AnchorPane view;
     
    private BufferedImage bimage;
    private Pic[] images;
    // letzter Zoomfaktor (zur Berechnung der Verschiebung des Bildes bei Zoomaenderung)
    private double zoomFactorLast = 1;
    private double zoomFactor = 1;
    
    private boolean drawFeatures = false;

    // diese Variablen steuern die Verschiebung der Ansicht (ueber Mouse-Drag)
    private double xm = 0;
    private double ym = 0;
    private int xMouseMove;
    private int yMouseMove;
    private int xMouseStartPos;
    private int yMouseStartPos;
    protected int xMousePos;
    protected int yMousePos;
    private double borderFactor = 0.9;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @Override
    public Node getView() {
        return view;
    }   
    
    public void draw(Pic[] images) throws IOException {
        this.images = images;
        if(images != null) {
            if(images.length != 0) {              
                if(images[0].getDisplayImage() != null) {
                    
                    Image image = SwingFXUtils.toFXImage(ImageIO.read(new File("sonnenblumeblau_03.png")), null);

                    ImageView imgView = new ImageView(image);
                    imgView.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
                }
            }
        }//do like the awt version of the imageViewer, but ONLY for images, not for controls
    }
}
