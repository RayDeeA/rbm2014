/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.persistence;

import de.htw.iconn.image.ImageManager;
import de.htw.iconn.main.AController;
import de.htw.iconn.main.BenchmarkModel;
import de.htw.iconn.main.ControlCenterController;
import de.htw.iconn.settings.RBMSettingsController;
import de.htw.iconn.settings.RBMSettingsModel;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author christoph
 */
public class Creator {
    public void load(ControlCenterController controller, File file) throws ParserConfigurationException, SAXException, IOException{     
        System.out.println("Load Configuration");
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        
        NodeList benchmarkList = doc.getElementsByTagName("BenchmarkModel");
        Node benchmarkNode = benchmarkList.item(0);
        NodeList rbmList = doc.getElementsByTagName("rbm");
        
        
        loadBenchmark(controller, benchmarkNode);
        loadRBMs(controller, rbmList);
    }
    
    private void loadBenchmark(ControlCenterController controller, Node benchmark){
        BenchmarkModel benchmarkModel = controller.getBenchmarkController().getModel();
    }
    
    private void loadRBMs(ControlCenterController controller, NodeList rbmNodes){
        BenchmarkModel benchmarkModel = controller.getBenchmarkController().getModel();
        for(int i = 0; i < rbmNodes.getLength(); ++i){
            RBMSettingsController settingsController = controller.createRBM();
            loadRBM(settingsController, (Element)rbmNodes.item(i));
        }        
    }
    
    private void loadRBM(RBMSettingsController settingsController, Element rbmElement){
        RBMSettingsModel settingsModel = settingsController.getModel();
        AController[] controllers = settingsModel.getControllers();
        for(int i = 0; i < controllers.length; ++i){
            try {
                Method method = controllers[i].getClass().getMethod("getModel");
                Object model = method.invoke(controllers[i], new Object[]{});
                String className = model.getClass().getSimpleName();
                NodeList modelNodes = rbmElement.getElementsByTagName(className);
                create(model, (Element)modelNodes.item(0));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                System.err.println("ERROR: Could not get Model with Reflection");
                Logger.getLogger(Persistor.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
    }
    
    private void create(Object model, Element modelElement){
        String[][] data = parseData(modelElement.getChildNodes());
        Field[] fields = model.getClass().getDeclaredFields();
        
        //Remember matches for validation
        boolean[] dataFound = new boolean[data.length];
        boolean[] fieldFound = new boolean[fields.length];
        
        for(int i = 0; i < fields.length; ++i){
            Field field = fields[i];
            Annotation[] annotations = field.getDeclaredAnnotations();
            
            for(int j = 0; j < annotations.length; ++j){
                if(annotations[j].annotationType().equals(Conserve.class)){
                    String type = field.getType().getSimpleName();
                    String name = field.getName();
                    
                    //Find data set belonging to field
                    for(int k = 0; k < data.length; ++k){
                        //correct data set is equivalent in name and type
                        if(data[k][0].equals(name) && data[k][1].equals(type)){
                            dataFound[k] = true;
                            
                            writeDataToField(field, model, data[k][2]);
                            
                            break;
                        }else{
                            fieldFound[i] = false;
                        }
                    }
                    break;
                }else{
                    fieldFound[i] = true;
                }              
            }
        }
    }
    
    private void writeDataToField(Field field, Object model, String value){
        field.setAccessible(true);
        Class type = field.getType();
        if(type.isArray()){
            parseArray(field, value, model);
        }else {
            Object o = parseString(field, value);
            if(o != null){
                try {
                    field.set(model, o);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                System.err.println("ERROR: could not parse field of type " + type.getSimpleName());
            }
        }
    }
    
    private String cropArrayTypeName(Field field){
        String typeName = field.getType().getSimpleName();
        int end = 0;
        char[] chars = typeName.toCharArray();
        for(; end < chars.length; ++end){
            if(chars[end] == '[') break;
        }
        return new String(Arrays.copyOf(chars, end));
    }
    
    private void parseArray(Field field, String value, Object model){
        String typeName = field.getType().getSimpleName();
        if(typeName.equals("float[][]")){
            try {
                field.set(model, parseFloatArray(field, value));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Creator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.err.println("ERROR: can't parse array of type " + typeName);
        }
    }
    
    public int getArrayDimensionFromType(Field field){
        String typeName = field.getType().getSimpleName();
        int result = 0;
        char[] chars = typeName.toCharArray();
        for(int i = 0; i < chars.length; ++i){
            if(chars[i] == '[') ++result;
        }
        return result;
    }
    
    private float[][] parseFloatArray(Field field, String value){
        String[] banana = value.split(";");
        int firstDimensionSize = banana.length;
        int secondDimensionSize = banana[0].split(",").length;
        float[][] result = new float[firstDimensionSize][secondDimensionSize];
        for(int i = 0; i < firstDimensionSize; ++i){
            String[] bananasplit = banana[i].split(",");
            for(int j = 0; j < secondDimensionSize; ++j){
                result[i][j] = new Float(bananasplit[j]);
            }
        }      
        return result;
    }
    
    private Object parseString(Field field, String value){
        String typeName = cropArrayTypeName(field);
        
        // Number classes
        if(typeName.equals("int")) return new Integer(value);
        if(typeName.equals("float")) return new Float(value);
        if(typeName.equals("double")) return new Double(value);       
        if(typeName.equals("long")) return new Long(value);
        if(typeName.equals("byte")) return new Byte(value);
        if(typeName.equals("short")) return new Short(value);
        // other basic types
        if(typeName.equals("boolean")) return Boolean.valueOf(value);
        if(typeName.equals("char") && value.length() == 1) return new Character(value.charAt(0));
        if(typeName.equalsIgnoreCase("String")) return value;
        // parsing custom classes
        if(typeName.equals("ImageManager")){
            String path = "CBIR_Project/images/" + value;
            File images = new File(path);
            return new ImageManager(images);
        }
        // no type found
        return null;
    }
    
    private String[][] parseData(NodeList dataNodes){
        int len = dataNodes.getLength();
        String[][] data = new String[len][3];
        for(int i = 0; i < len; ++i){
            Element dataElement = (Element)dataNodes.item(i);
            data[i][0] = dataElement.getAttribute("name");
            data[i][1] = dataElement.getAttribute("type");
            data[i][2] = dataElement.getTextContent();            
        }
        return data;
    }
}
