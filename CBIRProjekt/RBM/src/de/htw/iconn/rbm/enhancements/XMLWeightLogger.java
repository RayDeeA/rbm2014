package de.htw.iconn.rbm.enhancements;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

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
import org.xml.sax.SAXException;

import de.htw.cbir.CBIREvaluationModel;

public final class XMLWeightLogger implements IRBMTrainingEnhancement {

	private final int updateInterval;
	private String dateString;
	private String baseFolder;
	
	public XMLWeightLogger(int updateInterval) {
		this.updateInterval = updateInterval;
		this.dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());		
		
		this.baseFolder = "RBMLogs";
	}

	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

	
	private void stepXmlLogTraining(CBIREvaluationModel evaluationModel) throws ParserConfigurationException, IOException, SAXException, TransformerException {

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
	
	private void stepXmlLogEvolution(CBIREvaluationModel evaluationModel) throws IOException, ParserConfigurationException, TransformerException {
		
		double[][] weights2d = evaluationModel.getWeights2d();
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
	public void action(CBIREvaluationModel evaluationModel) {
		
		if(CBIREvaluationModel.evaluationType.EVOLUTION == evaluationModel.getEvaluationType()) {
			try {
				stepXmlLogEvolution(evaluationModel);
			} catch (IOException | ParserConfigurationException
					| TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(CBIREvaluationModel.evaluationType.TRAINING == evaluationModel.getEvaluationType()) {
			try {
				stepXmlLogTraining(evaluationModel);
			} catch (ParserConfigurationException | IOException | SAXException
					| TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
