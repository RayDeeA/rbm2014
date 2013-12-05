package de.htw.iconn.fx;

import java.util.LinkedList;

public class EvaluationModel {
	private double mAP;
	private double error;
	private LinkedList<double[][][]> collectedWeights;
        private int loggerInterval;
        private int visualizationInterval;
		
	public EvaluationModel(int loggerInterval, int visualizationInterval){
            this.loggerInterval = loggerInterval;
            this.visualizationInterval = visualizationInterval;
            this.reset();
	}
	
	public void reset(){
		this.mAP = 0.0;
		this.error = 0.0;
		this.collectedWeights = new LinkedList<double[][][]>();
	}	
	public void resetCollectedWeights(){
		collectedWeights = new LinkedList<double[][][]>();
	}	
	public void addToCollectedWeights(double[][][] weights){
		collectedWeights.add(weights);
	}
	public LinkedList<double[][][]> getCollectedWeights(){
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
}
