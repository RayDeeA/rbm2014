/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import de.htw.cbir.model.Pic;
import java.awt.image.BufferedImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author moritz
 */
public class ImageViewer {

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

    private int width = 800;
    private int height = 600;

    private final Scene scene;
    private final Group root;
    private final Stage stage;
    private final Pic[] images;

    public ImageViewer(Pic[] images) {
        this.images = images;
        this.root = new Group();
        this.stage = new Stage();
        this.scene = new Scene(root, width, height);
        this.scene.setFill(Color.LIGHTSLATEGREY);
        this.stage.setScene(this.scene);
        this.stage.setResizable(true);
        ChangeListener<Number> onResize =  new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newHeight) {            
                xMouseMove = 0;
                yMouseMove = 0;
                ImageViewer.this.draw();
            }
        };
        this.scene.widthProperty().addListener(onResize);
        this.scene.heightProperty().addListener(onResize);
        
        this.scene.setOnScroll(new EventHandler() {
            @Override
            public void handle(Event t) {
                double delta = ((ScrollEvent)t).getDeltaY();
                if (delta < 0) {
			zoomFactor = zoomFactor*1.1;
			if (zoomFactor > 50) 
				zoomFactor = 50;
		}
		else {
			zoomFactor = zoomFactor/1.1;	
			if (zoomFactor < 1) zoomFactor = 1;
		}
                ImageViewer.this.draw();
            }
    });

    }

    public void draw() {

        if (images != null) {
            root.getChildren().clear();
            calculateDrawingPositions(0, 0, 0, 0, zoomFactor);
            for (Pic image : images) {
                BufferedImage bi = image.getDisplayImage();

                Image img = SwingFXUtils.toFXImage(bi, null);
                ImageView imgView = new ImageView(img);

                imgView.setX(image.getxStart());
                imgView.setY(image.getyStart());
                imgView.setFitWidth(image.getxLen());
                imgView.setFitHeight(image.getyLen());

                imgView.setVisible(true);
                root.getChildren().add(imgView);
            }
        }
        stage.show();
    }

    private void calculateDrawingPositions(int xMousePos, int yMousePos, int xMouseMove, int yMouseMove, double zoomFactor) {

        int nThumbs = images.length;

        int hCanvas = (int) scene.getHeight();
        int wCanvas = (int) scene.getWidth();
        int h2 = hCanvas / 2;
        int w2 = wCanvas / 2;

        // Groesse eines thumbnail-Bereichs
        int thumbSize = (int) Math.sqrt((double) wCanvas * hCanvas / nThumbs);
        while (thumbSize > 0 && (wCanvas / thumbSize) * (hCanvas / thumbSize) < nThumbs) {
            --thumbSize;
        }

        int mapPlacesX = wCanvas / thumbSize;
        int mapPlacesY = hCanvas / thumbSize;

        double thumbSizeX = (double) wCanvas / mapPlacesX;
        double thumbSizeY = hCanvas / (double) mapPlacesY;

        // avoid empty lines at the bottom
        while (mapPlacesX * (mapPlacesY - 1) >= nThumbs) {
            mapPlacesY--;
        }
        thumbSizeY = (double) hCanvas / mapPlacesY;

        double scaledThumbSizeX = thumbSizeX * zoomFactor;
        double scaledThumbSizeY = thumbSizeY * zoomFactor;

        double sizeX = scaledThumbSizeX * borderFactor;
        double sizeY = scaledThumbSizeY * borderFactor;
        double size = Math.min(sizeX, sizeY);

        double xDelta = (w2 - xMousePos) * (zoomFactor / zoomFactorLast - 1);
        double yDelta = (h2 - yMousePos) * (zoomFactor / zoomFactorLast - 1);
        zoomFactorLast = zoomFactor;

        double xmLast = xm;
        double ymLast = ym;

        xm -= (xMouseMove + xDelta) / scaledThumbSizeX;
        ym -= (yMouseMove + yDelta) / scaledThumbSizeY;

        int xMinPos = (int) (w2 - xm * scaledThumbSizeX);
        int xMaxPos = (int) (xMinPos + mapPlacesX * scaledThumbSizeX);
        int yMinPos = (int) (h2 - ym * scaledThumbSizeY);
        int yMaxPos = (int) (yMinPos + mapPlacesY * scaledThumbSizeY);

        // disallow to move out of the map by dragging
        if (xMinPos > 0 || xMaxPos < wCanvas - 1) {
            xm = xmLast;
            xMinPos = (int) (w2 - xm * scaledThumbSizeX);
            xMaxPos = (int) (xMinPos + mapPlacesX * scaledThumbSizeX);
        }
		// when zooming out (centered at the mouseposition) it might be
        // necessary to shift the map back to the canvas
        if (xMaxPos < wCanvas - 1) {
            int xMoveCorrection = wCanvas - 1 - xMaxPos;
            xMinPos += xMoveCorrection;
            xm -= xMoveCorrection / scaledThumbSizeX;
        } else if (xMinPos > 0) {
            xm += xMinPos / scaledThumbSizeX;
            xMinPos = 0;
        }

        // same for y
        if (yMinPos > 0 || yMaxPos < hCanvas - 1) {
            ym = ymLast;
            yMinPos = (int) (h2 - ym * scaledThumbSizeY);
            yMaxPos = (int) (yMinPos + mapPlacesY * scaledThumbSizeY);
        }
        if (yMaxPos < hCanvas - 1) {
            int yMoveCorrection = hCanvas - 1 - yMaxPos;
            yMinPos += yMoveCorrection;
            ym -= yMoveCorrection / scaledThumbSizeY;
        } else if (yMinPos > 0) {
            ym += yMinPos / scaledThumbSizeY;
            yMinPos = 0;
        }

        // Zeichenposition errechnen
        for (Pic image : images) {

            int w = (drawFeatures) ? 64 : image.getOrigWidth();
            int h = (drawFeatures) ? 64 : image.getOrigHeight();

            // skalierung, keep aspect ratio
            double s = Math.max(w, h);
            double scale = size / s;

            int xLen = (int) (scale * w);
            int yLen = (int) (scale * h);

            int pos = image.getRank();

            int xStart = (int) (xMinPos + (pos % mapPlacesX) * scaledThumbSizeX);
            int yStart = (int) (yMinPos + (pos / mapPlacesX) * scaledThumbSizeY);

            int xs = xStart + (int) ((scaledThumbSizeX - xLen + 1) / 2); // xStart mit Rand
            int ys = yStart + (int) ((scaledThumbSizeY - yLen + 1) / 2);

            image.setxStart(xs);
            image.setxLen(xLen);
            image.setyStart(ys);
            image.setyLen(yLen);
        }
    }

}
