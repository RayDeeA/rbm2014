package de.htw.iconn.rbm;

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
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.iconn.rbm.functions.ILogistic;

public class RBMLogger implements IRBM, IRBMLogger{
	
	private IRBM rbm;
	private CBIREvaluationModel evaluationModel;
	private String headLine;
	private String dateString;
	private String baseFolder;

	
	
	public RBMLogger(IRBM rbm){
		this.rbm = rbm;
		this.dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());		
		this.headLine = "RBM;evaluationType;inputSize;outputSize;includingBias;learnRate;epochs;logisticFunction;seed;useSeed;error;mAP;imageSetSize;date";
		
		this.baseFolder = "RBMLogs";
		Path baseFolderPath = FileSystems.getDefault().getPath(this.baseFolder);	
		try {
			if(Files.notExists(baseFolderPath, LinkOption.NOFOLLOW_LINKS)){
				Files.createDirectory(baseFolderPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String evaluationDataToString(){
		if(dateString == null){
			dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		}
		String logLine = 
		this.getRbmName() + ";" + 
		evaluationModel.getEvaluationType().toString() + ";" +
		this.getInputSize() + ";" +  
		this.getOutputSize() + ";" + 
		this.includingBias() + ";" + 
		this.getLearnRate() + ";" +
		evaluationModel.getEpochs() + ";" +
		this.getLogisticFunctionName() +";" +
		evaluationModel.getSeed() + ";" + 
		this.useSeed(evaluationModel.getUseSeed()) + ";" +
		evaluationModel.getError() + ";" +
		evaluationModel.getMAP() + ";" +
		evaluationModel.getImageSetSize() + ";" +
		dateString;
		
		return logLine;
	}
	
	public void finalCsvLog(CBIREvaluationModel evaluationModel) throws IOException{
		this.evaluationModel = evaluationModel;
		String logLine = evaluationDataToString();
		System.out.println(logLine);
		this.dateString = null;
		
		
		String csvLocation = "logs.csv";		
		Path csvLocationPath = FileSystems.getDefault().getPath(this.baseFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		if(Files.notExists(csvLocationPath, LinkOption.NOFOLLOW_LINKS)){
			csvFile = new File(this.baseFolder + "/" + csvLocation);
			csvFile.createNewFile();
			csvExistent = false;
		}
		
		BufferedWriter output = new BufferedWriter(new FileWriter(this.baseFolder + "/" + csvLocation, true));
		if(!csvExistent){
			output.append(headLine);
			output.newLine();
		}
		output.append(logLine);
		output.newLine();
		output.flush();
		output.close();
		
		evaluationModel.reset();
	}
	
	@Override
	public void stepCsvLog(CBIREvaluationModel evaluationModel) throws IOException {
		this.evaluationModel = evaluationModel;
		String logLine = evaluationDataToString();
		System.out.println(logLine);
		
		String csvFolder = this.baseFolder + "/CSVSteps";
		String csvLocation = this.dateString + ".csv";
		
		Path csvFolderPath = FileSystems.getDefault().getPath(csvFolder);
		Path csvLocationPath = FileSystems.getDefault().getPath(csvFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		if(Files.notExists(csvFolderPath, LinkOption.NOFOLLOW_LINKS)){
			Files.createDirectory(csvFolderPath);
		}
		if(Files.notExists(csvLocationPath, LinkOption.NOFOLLOW_LINKS)){
			csvFile = new File(csvFolder + "/" + csvLocation);
			csvFile.createNewFile();
			csvExistent = false;
		}
		
		BufferedWriter output = new BufferedWriter(new FileWriter(csvFolder + "/" + csvLocation, true));
		if(!csvExistent){
			output.append(headLine);
			output.newLine();
		}
		output.append(logLine);
		output.newLine();
		output.flush();
		output.close();		
	}
	
	@Override
	public void stepXmlLogTraining(CBIREvaluationModel evaluationModel) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		this.evaluationModel = evaluationModel;
		LinkedList<double[][][]> collectedWeights = evaluationModel.getCollectedWeights();
		evaluationModel.resetCollectedWeights();
		StringBuffer rowSB;
		
		String xmlFolder = this.baseFolder + "/XMLSteps";
		String xmlLocation = this.dateString + ".xml";
		
		Path xmlFolderPath = FileSystems.getDefault().getPath(xmlFolder);
		Path xmlLocationPath = FileSystems.getDefault().getPath(xmlFolder, xmlLocation);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc;
		Element rootElement;
			
		if(Files.notExists(xmlFolderPath, LinkOption.NOFOLLOW_LINKS)){
			Files.createDirectory(xmlFolderPath);
		}
		
		File xmlFile = new File(xmlFolder + "/" + xmlLocation);
		StreamResult result = new StreamResult(xmlFile);
		
		int stepCount = 0;
		
		if(Files.notExists(xmlLocationPath, LinkOption.NOFOLLOW_LINKS)){		
			doc = docBuilder.newDocument();
			rootElement = doc.createElement("logdata");
			doc.appendChild(rootElement);			
		}else{
			doc = docBuilder.parse(new File(xmlFolder + "/" + xmlLocation));
			doc.getDocumentElement().normalize();
			rootElement = doc.getDocumentElement();
			stepCount = rootElement.getElementsByTagName("step").getLength();
		}
		
		Iterator<double[][][]> it = collectedWeights.iterator();
		while(it.hasNext()){
			double[][][] weights = it.next();
			Element step = doc.createElement("step");
			rootElement.appendChild(step);
	 
			Attr count = doc.createAttribute("count");
			count.setValue(new Integer(stepCount++).toString());
			step.setAttributeNode(count);
			
			for(int i = 0; i < weights.length; ++i){
				Element rbm = doc.createElement("rbm");
				step.appendChild(rbm);
				
				Attr id = doc.createAttribute("id");
				id.setValue(new Integer(i).toString());
				rbm.setAttributeNode(id);
				for(int j = 0; j < weights[i].length; ++j){
					rowSB = new StringBuffer();
					for(int k = 0; k < weights[i][j].length; ++k){
						rowSB.append(weights[i][j][k]);
						if(k < weights[i][j].length - 1){
							rowSB.append(",");
						}
					}
					Element row = doc.createElement("row");
					rbm.appendChild(row);
					
					Attr num = doc.createAttribute("num");
					num.setValue(new Integer(j).toString());
					row.setAttributeNode(num);
					
					row.appendChild(doc.createTextNode(rowSB.toString()));
				}
			}
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
 
		transformer.transform(source, result);	
	}
	
	@Override
	public void stepXmlLogEvolution(CBIREvaluationModel evaluationModel) throws IOException, ParserConfigurationException, TransformerException {
		this.evaluationModel = evaluationModel;
		double[][] weights2d = this.evaluationModel.getWeights2d();
		StringBuffer rowSB;
		
		String xmlFolder = this.baseFolder + "/XMLSteps";
		String xmlLocation = this.dateString + ".xml";
		
		Path xmlFolderPath = FileSystems.getDefault().getPath(xmlFolder);
		Path xmlLocationPath = FileSystems.getDefault().getPath(xmlFolder, xmlLocation);
		
		if(Files.notExists(xmlFolderPath, LinkOption.NOFOLLOW_LINKS)){
			Files.createDirectory(xmlFolderPath);
		}
		File xmlFile = new File(xmlFolder + "/" + xmlLocation);
		StreamResult result = new StreamResult(xmlFile);
		
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		//logdata
		Element rootElement = doc.createElement("logdata");
		doc.appendChild(rootElement);
		
		//step
		Element step = doc.createElement("step");
		rootElement.appendChild(step);
 
		Attr count = doc.createAttribute("count");
		count.setValue("0");
		step.setAttributeNode(count);
		
		//rbm
		Element rbm = doc.createElement("rbm");
		step.appendChild(rbm);
 
		Attr id = doc.createAttribute("id");
		id.setValue("0");
		rbm.setAttributeNode(id);
		
		for(int i = 0; i < weights2d.length; ++i){
			rowSB = new StringBuffer();
			for(int j = 0; j < weights2d[0].length; ++j){
				rowSB.append(weights2d[i][j]);
				if(j < weights2d[0].length - 1){
					rowSB.append(",");
				}
			}
			Element row = doc.createElement("row");
			rbm.appendChild(row);
			
			Attr num = doc.createAttribute("num");
			num.setValue(new Integer(i).toString());
			row.setAttributeNode(num);
			
			row.appendChild(doc.createTextNode(rowSB.toString()));
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
 
		transformer.transform(source, result);
		
	}

	@Override
	public void train(double[][] trainingData, int max_epochs, boolean useHiddenStates, boolean useVisibleStates) {
		rbm.train(trainingData, max_epochs, useHiddenStates, useVisibleStates);
	}

	@Override
	public double error(double[][] trainingData, boolean useHiddenStates, boolean useVisibleStates) {
		return rbm.error(trainingData, useHiddenStates, useVisibleStates);
	}

	@Override
	public double[][] run_visible(double[][] userData, boolean useHiddenStates) {
		return rbm.run_visible(userData, useHiddenStates);
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates) {
		return rbm.run_hidden(hiddenData, useVisibleStates);
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
	
	private String includingBias(){
		if(this.hasBias()){
			return "yes";
		}else{
			return "no";
		}
	}
	
	private String useSeed(boolean hasSeed){
		if(hasSeed){
			return "yes";
		}else{
			return "no";
		}
	}

	@Override
	public double[][] daydream(int numberOfSamples) {
		return rbm.daydream(numberOfSamples);
	}
}
