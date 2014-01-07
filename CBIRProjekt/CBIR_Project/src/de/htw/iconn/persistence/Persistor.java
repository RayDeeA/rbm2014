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
import java.lang.reflect.Array;
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
                    field.setAccessible(true);
                    Class type = field.getType();
                    String name = field.getName();
                    String value = getFieldValue(field, model);
                    if(value != null){
                        hasConserveAnnotation = true;
                        createDataElement(type.toString(), name, value, modelElement, doc);
                    }else{
                        System.err.println("ERROR: Could not conserve field of type " + type.toString());
                    }                   
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
        Object value = null;
        try {
            value = field.get(model);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            System.err.println("ERROR: Could not get value from field");
            Logger.getLogger(Persistor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(value instanceof String){
            result = (String)value;
        }else if(value instanceof Boolean){
            result = bts((Boolean)value);
        }else if(value instanceof Number){
            result = nts((Number)value);
        }else if(value.getClass().isArray()){
            int len1 = Array.getLength(value);
            if(len1 > 0){
                Object[] array1 = new Object[len1];
                for (int i = 0; i < len1; ++i) {
                    array1[i] = Array.get(value, i);
                }
                if(array1[0].getClass().isArray()){
                    int len2 = Array.getLength(array1[0]);
                    if(len2 > 0){
                        Object[][] array2 = new Object[len1][len2];
                        for(int i = 0; i < len1; ++i){
                            for(int j = 0; j < len2; ++j){
                                array2[i][j] = Array.get(array1[i], j);
                            }
                        }                  
                        result = naats(array2);
                    }
                }else{
                    result = nats(array1);
                }
            }
        }
        
        return result;
    }   
    // boolean to string
    private String bts(Boolean b){
        if(b == null) return null;
        if(b.booleanValue()) return "true";
        return "false";
    }
    // number to string
    private String nts(Number n){
        if(n == null) return null;
        return n.toString();
    }
    // number array to string
    private String nats(Object[] n){
        if(n == null || n.length == 0 || !(n[0] instanceof Number)) return null;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n.length; ++i){
            sb.append(n[i]);
            if(i < n.length - 1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
    // two dimensional number array to string
    private String naats(Object[][] n){
        if(n == null || n.length == 0 || n[0].length == 0 
                || !(n[0][0] instanceof Number)) return null;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n.length; ++i){
            sb.append(nats(n[i]));
            if(i < n.length - 1){
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
