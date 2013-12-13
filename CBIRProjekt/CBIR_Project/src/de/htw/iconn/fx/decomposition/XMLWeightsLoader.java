/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author christoph
 */
public class XMLWeightsLoader {
    
    public double[][] loadWeightsFromXML(File file) throws ParserConfigurationException, SAXException, IOException{
        
        if(file == null){
            return null;
        }
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.parse(file);
        Element rootElement = doc.getDocumentElement();
        
        NodeList rows = rootElement.getElementsByTagName("row");
        int inputSize = rows.getLength();
        int outputSize = rows.item(0).getTextContent().split(",").length;

        double[][] weights = new double[inputSize][outputSize];
        
        for(int i = 0; i < weights.length; ++i){
            String[] weightRow = rows.item(i).getTextContent().split(",");
            for(int j = 0; j < weights[0].length; ++j){
                weights[i][j] = Double.parseDouble(weightRow[j]);
            }
        }
        
        return weights;
    }
}
