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
	
	public RBMLogger(IRBM rbm){
		this.rbm = rbm;
	}
	
	public void finalCsvLog(CBIREvaluationModel evaluationModel){
		//Paths
		String logString = "RBMLogs";
		String csvString = "logs.csv";
		
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
		String evaluationType = evaluationModel.getEvaluationType().toString();
		String includingBias = this.includingBias();
		String useSeed = this.useSeed(evaluationModel.getUseSeed());
		int seed = evaluationModel.getSeed();
		
		//start logging
		Date date = new Date();
		String dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(date);
		String headLine = "RBM;evaluationType;inputSize;outputSize;includingBias;learnRate;epochs;logisticFunction;seed;useSeed;error;mAP;imageSetSize;date";
		
		//System.out.println(mApWithComma);
		String logLine = rbmName + ";" + evaluationType + ";" + inputSize + ";" + outputSize + ";" + includingBias + ";" + learnRate + ";" + epochs + ";" + logisticFunctionName + "; " + seed + ";" + useSeed + ";" + error + ";" + mAP + ";" + imageSetSize + ";" + dateString;
		System.out.println(logLine);		
		Path logPath = FileSystems.getDefault().getPath(logString);	
		Path csvPath = FileSystems.getDefault().getPath(logString, csvString);
		File csvFile;
		boolean csvExistent = true;
		
		try {
			if(Files.notExists(logPath, LinkOption.NOFOLLOW_LINKS)){
				Files.createDirectory(logPath);
			}
			if(Files.notExists(csvPath, LinkOption.NOFOLLOW_LINKS)){
				csvFile = new File(logString + "/" + csvString);
				csvFile.createNewFile();
				csvExistent = false;
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(logString + "/" + csvString, true));
			if(!csvExistent){
				output.append(headLine);
				output.newLine();
			}
			output.append(logLine);
			output.newLine();
			output.flush();
			output.close();		
		} catch (IOException e) {
			System.out.println("Could not create RBMLogs folder");
			e.printStackTrace();
		}
	}

	private void serializeDataToXML(ArrayList<double[][]> data, Date actDate, String machine){

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("RBM_"+ machine);
			doc.appendChild(rootElement);

			// sample elements
			Element sample = doc.createElement("sample");
			rootElement.appendChild(sample);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			sample.setAttributeNode(attr);

			int count =0;

			final Iterator<double[][]> it = data.iterator();
			while (it.hasNext()) {

				double[][] array2d = it.next();

				//Sample Name's ("Matrix_"+count)
				Element element = doc.createElement("Matrix_"+count);
				for (int i = 0; i < array2d.length; i++) {
					element.appendChild(doc.createElement("Row_"+ i));
					for(int j = 0; j< array2d[0].length; j++){
						double currentValue = array2d[i][j];
						element.appendChild(doc.createTextNode( Double.toString(Math.round(currentValue*1e5)/1e5) + ( j != array2d[0].length - 1 ? "," : "")));
					}	
				}
				count++;
				sample.appendChild(element);

				data.iterator().next();
			}
			System.out.println("Finished");

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//Name of File
			StreamResult result = new StreamResult(new File("test.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

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
