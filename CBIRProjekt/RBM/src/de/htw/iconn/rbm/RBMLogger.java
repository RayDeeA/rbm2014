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
import de.htw.iconn.rbm.functions.ILogistic;

public class RBMLogger implements IRBM, IRBMLogger{
	
	private IRBM rbm;
	private CBIREvaluationModel evaluationModel;
	private String headLine;
	private String dateString;
	private String baseFolder;
	
	private String rbmName;
	private int inputSize;
	private int outputSize;
	private double learnRate;
	private String logisticFunctionName;
	private double mAP;
	private double error;		
	private int epochs;
	private int imageSetSize;
	private String evaluationType;
	private String includingBias;
	private String useSeed;
	private int seed;
	
	
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
		rbmName = this.getRbmName();
		inputSize = this.getInputSize();
		outputSize = this.getOutputSize();
		learnRate = this.getLearnRate();
		logisticFunctionName = this.getLogisticFunctionName();
		mAP = evaluationModel.getMAP();
		error = evaluationModel.getError();		
		epochs = evaluationModel.getEpochs();
		imageSetSize = evaluationModel.getImageSetSize();
		evaluationType = evaluationModel.getEvaluationType().toString();
		includingBias = this.includingBias();
		useSeed = this.useSeed(evaluationModel.getUseSeed());
		seed = evaluationModel.getSeed();
		String logLine = rbmName + ";" + evaluationType + ";" + inputSize + ";" + outputSize + ";" + includingBias + ";" + learnRate + ";" + epochs + ";" + logisticFunctionName + ";" + seed + ";" + useSeed + ";" + error + ";" + mAP + ";" + imageSetSize + ";" + dateString;
		
		return logLine;
	}
	
	public void finalCsvLog(CBIREvaluationModel evaluationModel){
		this.evaluationModel = evaluationModel;
		String logLine = evaluationDataToString();
		System.out.println(logLine);
		this.dateString = null;
		
		
		String csvLocation = "logs.csv";		
		Path csvLocationPath = FileSystems.getDefault().getPath(this.baseFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stepCsvLog(CBIREvaluationModel evaluationModel) {
		this.evaluationModel = evaluationModel;
		String logLine = evaluationDataToString();
		System.out.println(logLine);
		
		String csvFolder = this.baseFolder + "/CSVSteps";
		String csvLocation = this.dateString + ".csv";
		
		Path csvFolderPath = FileSystems.getDefault().getPath(csvFolder);
		Path csvLocationPath = FileSystems.getDefault().getPath(csvFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private void serializeDataToXML(ArrayList<double[][]> data, Date actDate, String machine){
//
//		try {
//
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			// root elements
//			Document doc = docBuilder.newDocument();
//			Element rootElement = doc.createElement("RBM_"+ machine);
//			doc.appendChild(rootElement);
//
//			// sample elements
//			Element sample = doc.createElement("sample");
//			rootElement.appendChild(sample);
//
//			// set attribute to staff element
//			Attr attr = doc.createAttribute("id");
//			attr.setValue("1");
//			sample.setAttributeNode(attr);
//
//			int count =0;
//
//			final Iterator<double[][]> it = data.iterator();
//			while (it.hasNext()) {
//
//				double[][] array2d = it.next();
//
//				//Sample Name's ("Matrix_"+count)
//				Element element = doc.createElement("Matrix_"+count);
//				for (int i = 0; i < array2d.length; i++) {
//					element.appendChild(doc.createElement("Row_"+ i));
//					for(int j = 0; j< array2d[0].length; j++){
//						double currentValue = array2d[i][j];
//						element.appendChild(doc.createTextNode( Double.toString(Math.round(currentValue*1e5)/1e5) + ( j != array2d[0].length - 1 ? "," : "")));
//					}	
//				}
//				count++;
//				sample.appendChild(element);
//
//				data.iterator().next();
//			}
//			System.out.println("Finished");
//
//			// write the content into xml file
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			//Name of File
//			StreamResult result = new StreamResult(new File("test.xml"));
//
//			// Output to console for testing
//			// StreamResult result = new StreamResult(System.out);
//
//			transformer.transform(source, result);
//
//			System.out.println("File saved!");
//
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace();
//		} catch (TransformerException tfe) {
//			tfe.printStackTrace();
//		}
//	}

	@Override
	public void train(double[][] trainingData, int max_epochs) {
		rbm.train(trainingData, max_epochs);
	}

	@Override
	public double error(double[][] trainingData) {
		return rbm.error(trainingData);
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
}
