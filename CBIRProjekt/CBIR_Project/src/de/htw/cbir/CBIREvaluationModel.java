package de.htw.cbir;

import java.util.LinkedList;

import de.htw.iconn.rbm.IRBMLogger;

@Deprecated
public class CBIREvaluationModel {
	private double mAP;
	private double error;
	private int epochs;
	private evaluationType type;
	private int imageSetSize;
	private double[][] weights;
	private LinkedList<double[][]> collectedWeights;
	private double[][] weights2d;
	private int seed;
	private boolean useSeed;
	private IRBMLogger logger;
	private int csvOutputFrequency;
	private int updateInterval;
	private int xmlOutputFrequency;
	
	public static enum evaluationType{
		EVOLUTION, TRAINING, UNDEFINED
	}
		
	public CBIREvaluationModel(int imageSetSize){
		this.reset();
		this.csvOutputFrequency = 100;
		this.updateInterval = 100;
		this.xmlOutputFrequency = 1000;
		this.imageSetSize = imageSetSize;
	}
	
	public void reset(){
		this.epochs = 0;
		this.mAP = 0.0;
		this.error = 0.0;
		this.type = evaluationType.UNDEFINED;
		this.weights = null;
		this.logger = null;
		this.useSeed = false;
		this.seed = 0;
		this.collectedWeights = new LinkedList<double[][]>();
	}
	
	public CBIREvaluationModel(){
		this(0);
	}	
	public void resetCollectedWeights(){
		collectedWeights = new LinkedList<double[][]>();
	}	
	public void addToCollectedWeights(double[][] weights){
		collectedWeights.add(weights);
	}
	public LinkedList<double[][]> getCollectedWeights(){
		return this.collectedWeights;
	}
	public double getMAP() {
		return mAP;
	}
	public void setMAP(double mAP) {
		this.mAP = mAP;
	}
	public double getError() {
		return error;
	}
	public void setError(double error) {
		this.error = error;
	}
	public int getEpochs() {
		return epochs;
	}
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}
	public evaluationType getEvaluationType() {
		return type;
	}
	public void setEvaluationType(evaluationType evaluationType) {
		this.type = evaluationType;
	}
	public int getImageSetSize() {
		return imageSetSize;
	}
	public void setImageSetSize(int imageSetSize) {
		this.imageSetSize = imageSetSize;
	}
	public double[][] getWeights() {
		return weights;
	}
	public void setWeights(double[][] weights) {
		this.weights = weights;
	}	
	public boolean getUseSeed() {
		return useSeed;
	}
	public void setUseSeed(boolean hasSeed) {
		this.useSeed = hasSeed;
	}	
	public int getSeed() {
		return seed;
	}
	public void setSeed(int seed) {
		this.seed = seed;
	}
	public IRBMLogger getLogger() {
		return logger;
	}
	public void setLogger(IRBMLogger logger) {
		this.logger = logger;
	}
	public int getCsvOutputFrequency() {
		return csvOutputFrequency;
	}
	public void setCsvOutputFrequency(int csvOutputFrequency) {
		this.csvOutputFrequency = csvOutputFrequency;
	}
	public double[][] getWeights2d() {
		return weights2d;
	}
	public void setWeights2d(double[][] weights2d) {
		this.weights2d = weights2d;
	}
	public int getUpdateInterval() {
		return updateInterval;
	}
	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}
	public int getXmlOutputFrequency() {
		return xmlOutputFrequency;
	}
	public void setXmlOutputFrequency(int xmlOutputFrequency) {
		this.xmlOutputFrequency = xmlOutputFrequency;
	}
}
