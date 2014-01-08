package de.htw.iconn.legacy;

import java.util.LinkedList;

@Deprecated
public class CBIREvaluationModel {
	private float mAP;
	private float error;
	private int epochs;
	private evaluationType type;
	private int imageSetSize;
	private float[][] weights;
	private LinkedList<float[][]> collectedWeights;
	private float[][] weights2d;
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
		this.mAP = 0.0f;
		this.error = 0.0f;
		this.type = evaluationType.UNDEFINED;
		this.weights = null;
		this.logger = null;
		this.useSeed = false;
		this.seed = 0;
		this.collectedWeights = new LinkedList<float[][]>();
	}
	
	public CBIREvaluationModel(){
		this(0);
	}	
	public void resetCollectedWeights(){
		collectedWeights = new LinkedList<float[][]>();
	}	
	public void addToCollectedWeights(float[][] weights){
		collectedWeights.add(weights);
	}
	public LinkedList<float[][]> getCollectedWeights(){
		return this.collectedWeights;
	}
	public float getMAP() {
		return mAP;
	}
	public void setMAP(float mAP) {
		this.mAP = mAP;
	}
	public float getError() {
		return error;
	}
	public void setError(float error) {
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
	public float[][] getWeights() {
		return weights;
	}
	public void setWeights(float[][] weights) {
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
	public float[][] getWeights2d() {
		return weights2d;
	}
	public void setWeights2d(float[][] weights2d) {
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

    private static class IRBMLogger {

        public IRBMLogger() {
        }
    }
}
