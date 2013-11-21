package de.htw.cbir.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.cbir.DCTRBM;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.functions.ILogistic;


public class VisWrapper implements IRBM{
	
	private IRBM rbm;
	private RBMVisualizationFrame visframe;
	private double[][] traindata;
	private int maxepochs;
	private double error;
	
	private final int gap = 20;

	public VisWrapper(RBMVisualizationFrame visframe, IRBM rbm) {
		this.rbm = rbm;
		this.visframe = visframe;
		
	}
	
	public void interrupt() {
		
	}
	
	public void updateVis(int epochs, int updateFrequency, DCTRBM dctrbm) {
		
		
		double error = dctrbm.getError(null);
		int update = epochs / updateFrequency;
		for (int i = 0; i < update; i++) {
			visframe.update(dctrbm.getWeights(), this.error);
		}
		
		
	}
	
	public void getdata(CBIREvaluationModel evaluationModel){

		//values to log
		String rbmName = this.getRbmName();
		int inputSize = this.getInputSize();
		int outputSize = this.getOutputSize();
		double learnRate = this.getLearnRate();
		String logisticFunctionName = this.getLogisticFunctionName();
		double mAP = evaluationModel.getMAP();
		double error = evaluationModel.getError();		
		int epochs = evaluationModel.getEpochs();
		int imageSetSize = evaluationModel.getImageSetSize();

	}



	@Override
	public void train(double[][] trainingData, int max_epochs) {
		this.traindata = trainingData;
		this.maxepochs = max_epochs;
		int brakes = max_epochs / gap;
		int tempnumber = gap;
		while (tempnumber < maxepochs) {
			
			System.out.println("in while loop");
			
			// run number of gaps
			rbm.train(trainingData, gap);
			
			// get data
			this.traindata = reducematrix(rbm.getWeights());
			
			// send to vis
			visframe.update(traindata, error);
			
			// count up
			tempnumber = tempnumber + gap;
		}
		
//		dctrbm.train(imageManager.getImages(), updateFrequency);
//		visualizationFrame.update(dctrbm.getWeights(), error);
	}
	
	private double[][] reducematrix(double[][][] weights) {
		
		return weights[0];
		
	}
	
	private int getNumberOfCascades(double[][][] weights) {
		return weights.length;
	}

	@Override
	public double error(double[][] trainingData) {
		this.error = rbm.error(trainingData);
		return error;
	}

	@Override
	public double[][] run_visible(double[][] userData) {
		return rbm.run_visible(userData);
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		return rbm.run_visible(hiddenData);
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {
		rbm.setWeightsWithBias(weights);
	}

	@Override
	public double[][][] getWeights() {
		return rbm.getWeights();
	}

	@Override
	public double[][][] getWeightsWithBias() {
		return rbm.getWeightsWithBias();
	}

	@Override
	public int getInputSize() {
		return rbm.getInputSize();
	}

	@Override
	public int getOutputSize() {
		return rbm.getOutputSize();
	}

	@Override
	public double getLearnRate() {
		return rbm.getLearnRate();
	}

	@Override
	public ILogistic getLogisticFunction() {
		return rbm.getLogisticFunction();
	}
	
	public String getLogisticFunctionName(){
		if(rbm.getLogisticFunction() != null){
			return rbm.getLogisticFunction().getClass().getSimpleName();
		}else{
			return "NA";
		}
	}
	
	public String getRbmName(){
		return rbm.getClass().getSimpleName();
	}

	@Override
	public boolean hasBias() {
		return rbm.hasBias();
	}
	
	public String includingBias(){
		if(this.hasBias()){
			return "yes";
		}else{
			return "no";
		}
	}
}
