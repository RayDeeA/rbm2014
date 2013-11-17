package de.htw.cbir;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;

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

import de.htw.cbir.gui.CBIRUI;
import de.htw.cbir.gui.RBMVisualizationFrame;
import de.htw.cbir.histogram.IDWHistogramFactory;
import de.htw.cbir.model.ImagePair;
import de.htw.cbir.model.Pic;
import de.htw.cbir.model.PrecisionRecallTable;
import de.htw.cbir.model.Settings;
import de.htw.cbir.sorter.Sorter;
import de.htw.cbir.sorter.Sorter_ColorMean;
import de.htw.cbir.sorter.Sorter_ColorMean2;
import de.htw.cbir.sorter.Sorter_DCTRBM;
import de.htw.cbir.sorter.Sorter_FV15DCT;
import de.htw.cbir.sorter.Sorter_IDWHistogram;
import de.htw.cbir.sorter.Sorter_RGBInterpolation;
import de.htw.cma.GeneticColorDistance;
import de.htw.cma.GeneticDCTRBM;
import de.htw.cma.GeneticDCTRBMError;
import de.htw.cma.GeneticHistogram;
import de.htw.color.ColorConverter.ColorSpace;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.RBMJBlasRandomed;
import de.htw.iconn.rbm.RBMJBlasSeparatedWeights;
import de.htw.iconn.rbm.RBMLogger;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.GeneralisedLogisticFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearInterpolatedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;
import de.htw.iconn.sorter.Sorter_DCT_CJ;

public class CBIRController {

	private Settings settings;
	private ImageManager imageManager;

	private double error;
	private double rawError;

	private CBIRUI ui;
	private final RBMVisualizationFrame visualizationFrame;

	private Sorter sorter;
	private ForkJoinPool pool;
//	private double[][] getLoggingData;
	private ArrayList<double[][]> logData;

	
	private int inputSize = 15;
	private int outputSize = 4;
	
	private double learnRate = 0.1;
	private int epochs = 10000;
	private int updateFrequency = 100;

	public int getInputSize(){
		return inputSize;
	}
	public int getOutputSize(){
		return outputSize;
	}
	public CBIRController(Settings settings, ImageManager imageManager) {
		this.settings = settings;

		settings.setInputSizeValue(inputSize);
		settings.setOutputSizeValue(outputSize);
		
		this.imageManager = imageManager;
		this.pool = new ForkJoinPool();
		this.logData = new ArrayList<double[][]>();

		this.visualizationFrame = new RBMVisualizationFrame();
		this.visualizationFrame.setControllerRef(this);
		// GUI Elemente
		this.ui = new CBIRUI(this, this.visualizationFrame);
	}
	
	public void changeLogisticTest(ActionEvent e) {
		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();

		Pic[] allImages = imageManager.getImages();

		ILogistic logisticFunction = null;
		
		String cmd = e.getActionCommand();
		if (cmd.equalsIgnoreCase("Standard")) {
			logisticFunction = new GeneralisedLogisticFunction();
		} else if (cmd.equalsIgnoreCase("Gaussian")) {
			logisticFunction = new GaussMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Hard Clip")) {
			logisticFunction = new HardClipMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Clipped")) {
			logisticFunction = new LinearClippedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Interpolated")) {
			logisticFunction = new LinearInterpolatedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Linear Unclipped (Absolute Value)")) {
			logisticFunction = new LinearUnclippedMatrixFunction();
		} else if (cmd.equalsIgnoreCase("Rectifier")) {
			logisticFunction = new RectifierMatrixFunction();
		} else if (cmd.equalsIgnoreCase("TanH")) {
			logisticFunction = new TanHMatrixFunction();
		}
		
		IRBM rbm = new RBMJBlas(inputSize, outputSize, learnRate, logisticFunction);
		DCTRBM dctrbm = new DCTRBM(inputSize, outputSize, rbm);
		// nur damit die Datenanalysiert werden und
		// eine Normalisierung später stattfinden kann
		dctrbm.train(allImages, 0);
		
		this.sorter = new Sorter_DCTRBM(allImages, settings, dctrbm, pool);
		
		CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
		GeneticDCTRBMError gh = new GeneticDCTRBMError(dctrbm, imageManager, evalulation, pool);
		gh.run();
		
	}
	
	public String[] getLogisticsTestNames() {

		return new String[] { 
			"Standard", 
			"Gaussian", 
			"Hard Clip", 
			"Linear Clipped", 
			"Linear Interpolated", 
			"Linear Unclipped (Absolute Value)", 
			"Rectifier", 
			"TanH" 
		};
		
	}
	
	public void changeSorter(ActionEvent e) {
		IRBM rbm = null;
		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();
		
		if (cmd.equalsIgnoreCase("ColorMean"))
			sorter = new Sorter_ColorMean(allImages, settings, pool);
		else if (cmd.equalsIgnoreCase("ColorMean2"))
			sorter = new Sorter_ColorMean2(allImages, settings, pool);
		else if (cmd.equalsIgnoreCase("IDW Histogram")) {
			// double power = ui.askDouble("Power");
			// double[][] histogramDataPoints =
			// IDWHistogram.getEmpiricalDataPoints();
			// IDWHistogramFactory factory = new
			// IDWHistogramFactory(histogramDataPoints, ColorSpace.Genetic,
			// power, 0.34);

			IDWHistogramFactory factory = IDWHistogramFactory.load(new File("solutions//WebImages_71x6//10Histogram.gh").toPath());
			factory.printINPPMFormat();
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);
		} else if (cmd.equalsIgnoreCase("FV15DCT")) {
			sorter = new Sorter_FV15DCT(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("DCTRBM")) {
			DCTRBM dctRBM = new DCTRBM(15, 4);
			dctRBM.train(allImages, 0);

			// made global for update RBMVisualizationFrame.update()
			error = dctRBM.getError(allImages);
			rawError = dctRBM.getRawError(allImages);

			System.out.println("error " + error);
			System.out.println("raw error " + rawError);

			for(int i = 0; i < 30; i++) {	
				dctRBM.train(allImages, 100);
				visualizationFrame.update(dctRBM.getWeights(), error);
				logData.add(i, dctRBM.getWeights());
			}

//			for (int i = 0; i < 30; i++) {
//				dctRBM.train(allImages, 100);
//				visualizationFrame.update(dctRBM.getWeights(), error);
//			}

			System.out.println("error " + dctRBM.getError(allImages));
			System.out.println("raw error " + dctRBM.getRawError(allImages));
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		} else if (cmd.equalsIgnoreCase("DCT_CJ")) {
			sorter = new Sorter_DCT_CJ(allImages, settings, pool);
		} else if (cmd.equalsIgnoreCase("RBMJBlas_Sigmoid")) {
			rbm = new RBMLogger(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());
		} else if (cmd.equalsIgnoreCase("RBMJBlasRandomed_Sigmoid")) {
			rbm = new RBMLogger(new RBMJBlasRandomed(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction()));
		} else if (cmd.equalsIgnoreCase("DCTRBM_RM")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());			
		} else if (cmd.equalsIgnoreCase("DCTRBM_CJ")) {
			rbm = new RBMJBlasSeparatedWeights(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_DefaultLogisticMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new DefaultLogisticMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_RectifierMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new RectifierMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_TanHMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new TanHMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_GaussMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new GaussMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearClippedMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new LinearClippedMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearUnclippedMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new LinearUnclippedMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_LinearInterpolatedMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new LinearInterpolatedMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_HardClipMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new HardClipMatrixFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_BasicSigmoidMatrixFunction")) {
			rbm = new RBMJBlas(inputSize, outputSize, learnRate, new GeneralisedLogisticFunction());
		} else if (cmd.equalsIgnoreCase("DCTRBM_MU")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_RC")) {

		} else if (cmd.equalsIgnoreCase("DCTRBM_SR")) {

		}
		if(rbm != null) {
			DCTRBM dctRBM = new DCTRBM(inputSize, outputSize, rbm);
			updateVisualization(epochs, updateFrequency, dctRBM);
			sorter = new Sorter_DCTRBM(allImages, settings, dctRBM, pool);
		}
		sorter.getFeatureVectors();
	}

	public String[] getSorterNames() {

		return new String[] { 
			"None", "ColorMean", "ColorMean2",
			"IDW Histogram", "FV15DCT", "DCTRBM", "DCT_CJ",
			"RBMJBlas_Sigmoid", "DCTRBM_RM", "DCTRBM_CJ",
			"DCTRBM_DefaultLogisticMatrixFunction",
			"DCTRBM_RectifierMatrixFunction", "DCTRBM_TanHMatrixFunction",
			"DCTRBM_GaussMatrixFunction",
			"DCTRBM_LinearClippedMatrixFunction",
			"DCTRBM_LinearUnclippedMatrixFunction",
			"DCTRBM_LinearInterpolatedMatrixFunction",
			"DCTRBM_HardClipMatrixFunction",
			"DCTRBM_BasicSigmoidMatrixFunction", "DCTRBM_MU", "DCTRBM_RC",
			"DCTRBM_SR", "RBMJBlasRandomed_Sigmoid" 
		};
	}

	public ImageManager getImageManager() {
		return imageManager;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSorter(Sorter sorter) {
		this.sorter = sorter;
	}

	public void sortByImage(Pic queryImage) {

		// wurde bereits ein Sortieralgorithmus ausgewählt
		if (sorter == null) {
			System.out.println("No sorting algorithm selected");
			return;
		}

		Pic[] allImages = imageManager.getImages();
		CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);
		long milliSec = System.currentTimeMillis();

		// Sortere das alle Bilder nach dem Querybild
		ImagePair[] sortedArray = eval.sortBySimilarity(queryImage);
		double ap = PrecisionRecallTable.calcMeanAveragePrecision(sortedArray);

		// logge die Ergebnisse
		System.out.println("sortmethod:" + sorter.getName());
		System.out.println("meanAveragePrecision:" + ap + " - Time: " + (System.currentTimeMillis() - milliSec) + "ms");

		// wende die Reihnfolge an und zeige sie dem Benutzer
		for (int i = 0; i < sortedArray.length; i++) {
			sortedArray[i].getSearchImage().setRank(i);
		}
		ui.repaint();
	}

	public void triggerTests(ActionEvent e) {

		// wurde bereits ein Sortieralgorithmus ausgewählt
		if (sorter == null) {
			System.out.println("No sorting algorithm selected");
			return;
		}

		// evaluiere (durch MAP Wert) den Sortieralgorithmus
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();
		CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);

		// welche Teste sollen durchgeführt werden
		if (cmd.equals("Alle")) {
			eval.testAll(true, cmd);
		} else {
			Pic[] queryImages = imageManager.getImageInGroup(cmd).toArray(new Pic[0]);
			eval.test(queryImages, true, cmd);
		}
	}

	/**
	 * Wird getriggert wenn einer der automatischen Tests aufgerufen wird.
	 * 
	 * @param e
	 */
	public void triggerAutomaticTests(ActionEvent e) {

		inputSize = settings.getInputSizeValue();
		outputSize = settings.getOutpotSizeValue();
		
		String cmd = e.getActionCommand();
		Pic[] allImages = imageManager.getImages();

		if (cmd.equalsIgnoreCase("Finde besten Lum-Wert")) {
			Settings sett = new Settings();
			String cmdStr = "ColorMean2";
			for (int i = 0; i < 50; i += 2) {

				// berechne die Featurevektoren
				sett.setLumValue((double) i / 10);
				sorter = new Sorter_ColorMean2(allImages, sett, pool);
				sorter.getFeatureVectors();

				// berechne den MAP
				CBIREvaluation eval = new CBIREvaluation(sorter, allImages, pool);
				eval.testAll(true, cmdStr);
			}
		} else if (cmd.equalsIgnoreCase("Finde ColorSpace Distance")) {
			sorter = new Sorter_RGBInterpolation(allImages, settings, pool);
			sorter.getFeatureVectors();

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticColorDistance csd = new GeneticColorDistance(4, imageManager.getImageSetName(), evalulation);
			csd.run();
		} else if (cmd.equalsIgnoreCase("Finde Genetic Histogram")) {
			IDWHistogramFactory factory = new IDWHistogramFactory( ColorSpace.AdvYCbCr, 5, 0.34);
			sorter = new Sorter_IDWHistogram(allImages, settings, factory, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticHistogram gh = new GeneticHistogram(3, imageManager.getImageSetName(), evalulation, factory);
			gh.run();
		} else if (cmd.equalsIgnoreCase("Finde RBM Weights")) {


			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticDCTRBM gh = new GeneticDCTRBM(rbm, imageManager.getImageSetName(), evalulation);
			gh.run(visualizationFrame);
		} else if (cmd.equalsIgnoreCase("reduziere den RBM Fehler")) {

			DCTRBM rbm = new DCTRBM(inputSize, outputSize);
			// nur damit die Datenanalysiert werden und
			// eine Normalisierung später stattfinden kann
			rbm.train(allImages, 0);
			sorter = new Sorter_DCTRBM(allImages, settings, rbm, pool);

			CBIREvaluation evalulation = new CBIREvaluation(sorter, allImages, pool);
			GeneticDCTRBMError gh = new GeneticDCTRBMError(rbm, imageManager, evalulation, pool);
			gh.run();
		}
	}

	private void updateVisualization(int epochs, int updateFrequency,
			DCTRBM dctrbm) {
		int update = epochs / updateFrequency;
		for (int i = 0; i < update; i++) {
			dctrbm.train(imageManager.getImages(), updateFrequency);
			visualizationFrame.update(dctrbm.getWeights(), error);
		}
	}

//	private double[][] serializeXMLToData(File xmlFile) {
//
//		try {
//
//			File file = new File("test.xml");
//
//			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
//					.newDocumentBuilder();
//
//			Document doc = dBuilder.parse(file);
//
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//			if (doc.hasChildNodes()) {
//				printNote(doc.getChildNodes());			 
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return getLoggingData;
//	}

//	private static void printNote(NodeList nodeList) {
//
//		for (int count = 0; count < nodeList.getLength(); count++) {
//
//			Node tempNode = nodeList.item(count);
//
//			// make sure it's element node.
//			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
//
//				// get node name and value
//				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
//				System.out.println("Node Value =" + tempNode.getTextContent());
//				//System.out.println("Value: "+ tempNode.getNextSibling().getNodeValue().toString());
//
//				if (tempNode.hasAttributes()) {
//
//					// get attributes names and values
//					NamedNodeMap nodeMap = tempNode.getAttributes();
//
//					for (int i = 0; i < nodeMap.getLength(); i++) {
//
//						Node node = nodeMap.item(i);
//						System.out.println("attr name : " + node.getNodeName());
//						System.out.println("attr value : " + node.getNodeValue());
//					}
//				}
//				if (tempNode.hasChildNodes()) {
//					// loop again if has child nodes
//					printNote(tempNode.getChildNodes());
//				}
//				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
//			}
//		}
//	}

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
	
	public void saveButtonPressed() {
		// triggered by save button in VisFrame
		serializeDataToXML(logData, null, null);	}
}
