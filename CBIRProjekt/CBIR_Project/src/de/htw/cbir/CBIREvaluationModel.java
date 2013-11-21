package de.htw.cbir;

import de.htw.iconn.rbm.IRBMLogger;

public class CBIREvaluationModel {
	private double mAP;
	private double error;
	private int epochs;
	private evaluationType type;
	private int imageSetSize;
	private double[][] resultWeights;
	private double[][] startWeights;
	private int seed;
	private boolean useSeed;
	private IRBMLogger logger;
	private int logStepFrequency;
	
	public static enum evaluationType{
		EVOLUTION, TRAINING, UNDEFINED
	}
		
	public CBIREvaluationModel(int imageSetSize){
		this.reset();
		this.setLogStepFrequency(100);
		this.imageSetSize = imageSetSize;
	}
	
	public void reset(){
		this.epochs = 0;
		this.mAP = 0.0;
		this.error = 0.0;
		this.type = evaluationType.UNDEFINED;
		this.resultWeights = null;
		this.startWeights = null;
		this.logger = null;
		this.useSeed = false;
		this.seed = 0;
	}
	
	public CBIREvaluationModel(){
		this(0);
	}
	public double[][] getStartWeights() {
		return startWeights;
	}
	public void setStartWeights(double[][] startWeights) {
		this.startWeights = startWeights;
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
	public double[][] getResultWeights() {
		return resultWeights;
	}
	public void setResultWeights(double[][] resultWeights) {
		this.resultWeights = resultWeights;
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
	public int getLogStepFrequency() {
		return logStepFrequency;
	}
	public void setLogStepFrequency(int logStepFrequency) {
		this.logStepFrequency = logStepFrequency;
	}
}
