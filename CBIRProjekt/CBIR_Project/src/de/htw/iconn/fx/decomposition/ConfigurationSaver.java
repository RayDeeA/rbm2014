/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx.decomposition;

import de.htw.iconn.rbm.IRBM;
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
    
    public void saveConfigurationToFile(BenchmarkModel model) throws 
            IOException, ParserConfigurationException, 
            TransformerConfigurationException, TransformerException{
        
        IRBM rbmStack = model.getRbmStack();
        
        // TODO: get rbm list from benchmarkmodel
        LinkedList<IRBM> rbms = new LinkedList<>();
        
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

        // Output to console for testing
        //StreamResult result = new StreamResult(System.out);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        //logdata
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);
        
        int rbmID = 0;
        
        for(IRBM rbm : rbms){
            //rbm
            Element rbmElement = doc.createElement("rbm");
            doc.appendChild(rbmElement);

            Attr id = doc.createAttribute("id");
            id.setValue(new Integer(rbmID).toString());
            rbmElement.setAttributeNode(id);

            StringBuffer rowSB;
            double[][] weights2d = rbm.getWeightsWithBias()[0];
            
            Element weightsElement = doc.createElement("weights");
            rbmElement.appendChild(weightsElement);

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
                num.setValue(new Integer(i).toString());
                rowElement.setAttributeNode(num);

                rowElement.appendChild(doc.createTextNode(rowSB.toString()));
            } 
            ++rbmID;
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        transformer.transform(source, result);
    }
    
    public IRBM loadConfigurationFromFile(){
        IRBM rbmStack = null;
        return rbmStack;
    }
}
