package de.htw.iconn.rbm;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
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

import de.htw.cbir.DCTRBM;
import de.htw.iconn.rbm.functions.ILogistic;

public class RBMLogger implements IRBM, IRBMLogger{
	private Path logFolder = FileSystems.getDefault().getPath("RBMLogs");
	
	private IRBM rbm;
	private int numVisible, numHidden;
	private double learningRate;
	private String logisticName;
	private String rbmName;
	
	public RBMLogger(int numVisible, int numHidden, double learningRate, ILogistic sigmoid) {
		this.rbm = new RBMJBlas(numVisible, numHidden, learningRate, sigmoid);
		this.numVisible = numVisible;
		this.numHidden = numHidden;
		this.learningRate = learningRate;
		this.logisticName = sigmoid.getClass().getSimpleName();
		this.rbmName = this.rbm.getClass().getSimpleName();
	}
	
	public RBMLogger(int numVisible, int numHidden, double learningRate, double[][] weights, ILogistic sigmoid) {
		this.rbm = new RBMJBlas(numVisible, numHidden, learningRate, weights, sigmoid);
		this.numVisible = numVisible;
		this.numHidden = numHidden;
		this.learningRate = learningRate;
		this.logisticName = sigmoid.getClass().getSimpleName();
		this.rbmName = this.rbm.getClass().getSimpleName();
	}
	
	public RBMLogger(IRBM rbm){
		this.rbm = rbm;
		this.numVisible = rbm.getInputSize();
		this.numHidden = rbm.getOutputSize();
		this.learningRate = 0;
		this.logisticName = "NA";
		this.rbmName = this.rbm.getClass().getSimpleName();
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
	
	public void log(){
		System.out.println("Log");
		try {
			if(Files.notExists(logFolder, LinkOption.NOFOLLOW_LINKS))
				Files.createDirectory(logFolder);
			
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
}
