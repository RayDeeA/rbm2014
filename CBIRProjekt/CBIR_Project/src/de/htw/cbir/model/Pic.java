package de.htw.cbir.model;

import java.awt.image.BufferedImage;

import de.htw.cbir.histogram.IDWHistogram;

public class Pic extends Object{
	

	
	private String name;
	private String type;
	private int id;
	
	
	private boolean isSelected;
	private int rank;                 	// Position bei sortierter 1D-Reihenfolge
	private double distance;
	private int typeOccurrence;
	
	private double[] featureVector;	
	
	// Originalgröße des Bildes
	private int origWidth; 
	private int origHeight;
	
	// Zeichenpositionen 
	private int xStart = 0;
	private int xLen = 0;
	private int yStart = 0;
	private int yLen = 0;
	
	// zur Visualisierung
	private BufferedImage bImage;
	private BufferedImage featureImage;
	private IDWHistogram histogram;

	
	
	public IDWHistogram getHistogram() {
		return histogram;
	}

	public void setHistogram(IDWHistogram histogram) {
		this.histogram = histogram;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getTypeOccurrence() {
		return typeOccurrence;
	}

	public void setTypeOccurrence(int typeOccurrence) {
		this.typeOccurrence = typeOccurrence;
	}

	public double[] getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(double[] featureVector) {
		this.featureVector = featureVector;
	}

	public int getOrigWidth() {
		return origWidth;
	}

	public void setOrigWidth(int origWidth) {
		this.origWidth = origWidth;
	}

	public int getOrigHeight() {
		return origHeight;
	}

	public void setOrigHeight(int origHeight) {
		this.origHeight = origHeight;
	}

	public int getxStart() {
		return xStart;
	}

	public void setxStart(int xStart) {
		this.xStart = xStart;
	}

	public int getxLen() {
		return xLen;
	}

	public void setxLen(int xLen) {
		this.xLen = xLen;
	}

	public int getyStart() {
		return yStart;
	}

	public void setyStart(int yStart) {
		this.yStart = yStart;
	}

	public int getyLen() {
		return yLen;
	}

	public void setyLen(int yLen) {
		this.yLen = yLen;
	}

	public BufferedImage getDisplayImage() {
		return bImage;
	}

	public void setDisplayImage(BufferedImage bImage) {
		this.bImage = bImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public BufferedImage getFeatureImage() {
		return featureImage;
	}

	public void setFeatureImage(BufferedImage featureImage) {
		this.featureImage = featureImage;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
}
