package de.htw.cbir;

public class CBIREvaluationModel {
	private double mAP;
	private double error;
	private int epochs;
	private String evaluationType;
	private int imageSetSize;
		
	public CBIREvaluationModel(){
		this.epochs = 0;
		this.mAP = 0.0;
		this.error = 0.0;
		this.evaluationType = "NA";
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
	public String getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}
	public int getImageSetSize() {
		return imageSetSize;
	}
	public void setImageSetSize(int imageSetSize) {
		this.imageSetSize = imageSetSize;
	}
	public void reset(){
		this.epochs = 0;
		this.mAP = 0.0;
		this.error = 0.0;
		this.evaluationType = "NA";
	}
}
