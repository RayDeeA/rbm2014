/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.persistence;

import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.settings.RBMSettingsLearningRateController;
import de.htw.iconn.settings.RBMSettingsLearningRateModel;
import de.htw.iconn.settings.RBMSettingsLoggerController;
import de.htw.iconn.settings.RBMSettingsLoggerModel;
import de.htw.iconn.settings.RBMSettingsMainController;
import de.htw.iconn.settings.RBMSettingsMainModel;
import de.htw.iconn.settings.RBMSettingsStoppingConditionController;
import de.htw.iconn.settings.RBMSettingsStoppingConditionModel;
import de.htw.iconn.settings.RBMSettingsVisualizationsController;
import de.htw.iconn.settings.RBMSettingsVisualizationsModel;
import de.htw.iconn.settings.RBMSettingsWeightsController;
import de.htw.iconn.settings.RBMSettingsWeightsModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 *
 * @author christoph
 */
public class ConfigurationSaver {   
    private final String baseFolder = "RBMSaver";
    
    public void saveConfigurationToFile(BenchmarkModel benchmarkModel) throws 
            IOException, ParserConfigurationException, 
            TransformerConfigurationException, TransformerException{
        
        LinkedList<RBMSettingsController> rbmSettingsList = benchmarkModel.getRbmSettingsList();
        
        String dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());		
        String xmlFolder = this.baseFolder;
        String xmlLocation = dateString + ".xml";
        
        Path xmlFolderPath = FileSystems.getDefault().getPath(xmlFolder);
        Path xmlLocationPath = FileSystems.getDefault().getPath(xmlFolder, xmlLocation);

        if(Files.notExists(xmlFolderPath, LinkOption.NOFOLLOW_LINKS)){
                Files.createDirectory(xmlFolderPath);
        }
        File xmlFile = new File(xmlFolder + "/" + xmlLocation);
        StreamResult result = new StreamResult(xmlFile);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // root
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
        
        // benchmark
        Element benchmarkElement = doc.createElement("benchmark");
        rootElement.appendChild(benchmarkElement);
        retrieveBenchmarkData(benchmarkModel, benchmarkElement, doc);  
        
        int rbmID = 0;
        
        for(RBMSettingsController rbmSettings : rbmSettingsList){
         
            // get different models from controllers

            RBMSettingsMainModel mainModel = rbmSettings.getModel().getController(RBMSettingsMainController.class).getModel();
            RBMSettingsLearningRateModel learningRateModel = rbmSettings.getModel().getController(RBMSettingsLearningRateController.class).getModel();
            RBMSettingsStoppingConditionModel stoppingConditionModel = rbmSettings.getModel().getController(RBMSettingsStoppingConditionController.class).getModel();
            RBMSettingsVisualizationsModel visualizationsModel = rbmSettings.getModel().getController(RBMSettingsVisualizationsController.class).getModel();
            RBMSettingsLoggerModel loggerModel = rbmSettings.getModel().getController(RBMSettingsLoggerController.class).getModel();
            RBMSettingsWeightsModel weightsModel = rbmSettings.getModel().getController(RBMSettingsWeightsController.class).getModel();         
            
            // create document nodes
            Element rbmElement = doc.createElement("rbm");
            rootElement.appendChild(rbmElement);

            Attr id = doc.createAttribute("id");
            id.setValue(its(rbmID++));
            rbmElement.setAttributeNode(id);
            
            // settings root
            Element settingsElement = doc.createElement("settings");
            rbmElement.appendChild(settingsElement);
            
            // settings children
            Element mainSettingsElement = doc.createElement("main");
            settingsElement.appendChild(mainSettingsElement);         
            retrieveMainData(mainModel, mainSettingsElement, doc);
            
            Element learningRateSettingsElement = doc.createElement("learningRate");
            settingsElement.appendChild(learningRateSettingsElement);
            retrieveLearningRateData(learningRateModel, learningRateSettingsElement, doc);
            
            Element stoppingConditionSettingsElement = doc.createElement("stoppingCondition");
            settingsElement.appendChild(stoppingConditionSettingsElement);
            retrieveStoppingConditionData(stoppingConditionModel, stoppingConditionSettingsElement, doc);
            
            Element visualizationsSettingsElement = doc.createElement("visualizations");
            settingsElement.appendChild(mainSettingsElement);
            retrieveVisualizationsData(visualizationsModel, visualizationsSettingsElement, doc);
            
            Element loggerSettingsElement = doc.createElement("logger");
            settingsElement.appendChild(loggerSettingsElement);
            retrieveLoggerData(loggerModel, loggerSettingsElement, doc);
            
            Element weightsSettingsElement = doc.createElement("weights");
            settingsElement.appendChild(weightsSettingsElement);
            retrieveWeightsData(weightsModel, weightsSettingsElement, doc);
            
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        transformer.transform(source, result);
    }
    
    private void retrieveRBMData(IRBM rbm, Element parent, Document doc){
        
        Element weightsElement = doc.createElement("weights");
        parent.appendChild(weightsElement);

        float[][] weights2d = rbm.getWeights();
        StringBuffer rowSB;
        for(int i = 0; i < weights2d.length; ++i){
            rowSB = new StringBuffer();
            for(int j = 0; j < weights2d[0].length; ++j){
                    rowSB.append(weights2d[i][j]);
                    if(j < weights2d[0].length - 1){
                            rowSB.append(",");
                    }
            }
            Element rowElement = doc.createElement("row");
            weightsElement.appendChild(rowElement);

            Attr num = doc.createAttribute("num");
            num.setValue(its(i));
            rowElement.setAttributeNode(num);

            rowElement.appendChild(doc.createTextNode(rowSB.toString()));
        }
    }
    
    private void retrieveBenchmarkData(BenchmarkModel model, Element parent, Document doc){
        createDataElement("boolean", "isShowImageViewer", bts(model.isShowImageViewer()), parent, doc);
        if(model.getImageManager() != null)
            createDataElement("String", "selectedImageSet", model.getImageManager().getImageSetName(), parent, doc);
    }
    
    private void retrieveMainData(RBMSettingsMainModel model, Element parent, Document doc){
        createDataElement("int", "selectedRBMImplementation", its(model.getSelectedRbmImplementation()), parent, doc);
        createDataElement("int", "selectedRBMFeature", its(model.getSelectedRbmFeature()), parent, doc);
        createDataElement("int", "selectedLogisticFunction", its(model.getSelectedLogisticFunction()), parent, doc);
        createDataElement("int", "inputSize", its(model.getInputSize()), parent, doc);
        createDataElement("int", "outputSize", its(model.getOutputSize()), parent, doc);
    }
    
    private void retrieveLearningRateData(RBMSettingsLearningRateModel model, Element parent, Document doc){
        createDataElement("double", "constantLearningRate", dts(model.getConstantLearningRate()), parent, doc);
    }
    
    private void retrieveStoppingConditionData(RBMSettingsStoppingConditionModel model, Element parent, Document doc){
        createDataElement("boolean", "epochsOn", bts(model.isEpochsOn()), parent, doc);
        createDataElement("boolean", "errorOn", bts(model.isErrorOn()), parent, doc);
        createDataElement("int", "epochs", its(model.getEpochs()), parent, doc);
        createDataElement("double", "error", dts(model.getError()), parent, doc);
    }
    
    private void retrieveVisualizationsData(RBMSettingsVisualizationsModel model, Element parent, Document doc){
        createDataElement("boolean", "showWeights", bts(model.isShowWeights()), parent, doc);
        createDataElement("boolean", "showErrorGraph", bts(model.isShowErrorGraph()), parent, doc);
    }
    
    private void retrieveLoggerData(RBMSettingsLoggerModel model, Element parent, Document doc){
        createDataElement("boolean", "continuousLoggerOn", bts(model.isContinuousLoggerOn()), parent, doc);
        createDataElement("boolean", "finalLoggerOn", bts(model.isFinalLoggerOn()), parent, doc);
        createDataElement("int", "continuousInterval", its(model.getContinuousInterval()), parent, doc);
    }
    
    private void retrieveWeightsData(RBMSettingsWeightsModel model, Element parent, Document doc){
        createDataElement("boolean", "useSeed", bts(model.isUseSeed()), parent, doc);
        createDataElement("int", "seed", its(model.getSeed()), parent, doc);
    }
    
    private void createDataElement(String type, String key, String value, Element parent, Document doc){
        Element dataElement = doc.createElement("data");
        parent.appendChild(dataElement);
        
        Attr typeAttribute = doc.createAttribute("type");
        typeAttribute.setValue(type);
        dataElement.setAttributeNode(typeAttribute);
        
        Attr keyAttribute = doc.createAttribute("key");
        keyAttribute.setValue(key);
        dataElement.setAttributeNode(keyAttribute);
        
        Attr valueAttribute = doc.createAttribute("value");
        valueAttribute.setValue(value);
        dataElement.setAttributeNode(valueAttribute);
    }
    
    // boolean to string
    private String bts(boolean b){
        if(b) return "true";
        return "false";
    }
    
    // int to string
    private String its(int i){
        return new Integer(i).toString();
    }
    
    // double to string
    private String dts(double d){
        return new Double(d).toString();
    }
    
    public BenchmarkModel loadConfigurationFromFile(){
        BenchmarkModel benchmarkModel = null;
        return benchmarkModel;
    }
}
