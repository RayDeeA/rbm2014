/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.persistence;

import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkController;
import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsModel;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Persistor {
    
    private final String baseFolder = "Persistor";
    
    public void save(BenchmarkController benchmarkController) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
        BenchmarkModel benchmarkModel = benchmarkController.getModel();
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
        
        conserve(benchmarkModel, rootElement, doc);
        
        int id = 0;
        for(RBMSettingsController settingsController : rbmSettingsList){
            Element rbmElement = doc.createElement("rbm");
            rootElement.appendChild(rbmElement);

            Attr idAttr = doc.createAttribute("id");
            idAttr.setValue(Integer.toString(id++));
            rbmElement.setAttributeNode(idAttr);
            
            saveSettings(settingsController, rbmElement, doc);           
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        transformer.transform(source, result);
    }
    
    private void saveSettings(RBMSettingsController settingsController, Element parent, Document doc){
        RBMSettingsModel settingsModel = settingsController.getModel();
        
        AController[] controllers = settingsModel.getControllers();
        for(int i = 0; i < controllers.length; ++i){
            try {
                Method method = controllers[i].getClass().getMethod("getModel");
                Object model = method.invoke(controllers[i], new Object[]{});
                conserve(model, parent, doc);               
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                System.err.println("ERROR: Could not get Model with Reflection");
                Logger.getLogger(Persistor.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }        
    }
    
    private void conserve(Object model, Element parent, Document doc){
        boolean hasConserveAnnotation = false;
        String className = model.getClass().getSimpleName();
        Element modelElement = doc.createElement(className);
        
        for(Field field : model.getClass().getDeclaredFields()){
            Annotation[] annotations = field.getDeclaredAnnotations();
            
            for(int i = 0; i < annotations.length; ++i){
                if(annotations[i].annotationType().equals(Conserve.class)){
                    hasConserveAnnotation = true;
                    field.setAccessible(true);
                    Class type = field.getType();
                    String name = field.getName();
                    String value = getFieldValue(field, model);
                    createDataElement(type.toString(), name, value, modelElement, doc);
                }
            }         
        }
        if(hasConserveAnnotation){
           parent.appendChild(modelElement);
        }
    }
    
    private void createDataElement(String type, String name, String value, Element parent, Document doc){
        Element dataElement = doc.createElement("data");
        parent.appendChild(dataElement);
        
        Attr typeAttr = doc.createAttribute("type");
        typeAttr.setValue(type);
        dataElement.setAttributeNode(typeAttr);
        
        Attr nameAttr = doc.createAttribute("name");
        nameAttr.setValue(name);
        dataElement.setAttributeNode(nameAttr);
        
        Attr valueAttr = doc.createAttribute("value");
        valueAttr.setValue(value);
        dataElement.setAttributeNode(valueAttr);
    }
    
    private String getFieldValue(Field field, Object model){
        String result = null;
        try {
            if(field.get(model) instanceof Integer){
                result = its((int)field.get(model));
            }else if(field.get(model) instanceof Double){
                result = dts((double)field.get(model));
            }else if(field.get(model) instanceof Float){
                result = fts((float)field.get(model));
            }else if(field.get(model) instanceof Boolean){
                result = bts((boolean)field.get(model));
            }
            //TODO: toString methods for arrays
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Persistor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
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
    
    // float to string
    private String fts(float f){
        return new Float(f).toString();
    }
}
