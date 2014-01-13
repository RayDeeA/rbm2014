package de.htw.iconn.legacy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.persistence.XMLWeightsLoader;
import de.htw.iconn.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.enhancement.IRBMEndTrainingEnhancement;
import de.htw.iconn.enhancement.RBMEnhancer;
import de.htw.iconn.enhancement.RBMInfoPackage;
import de.htw.iconn.persistence.XMLWeightsSaver;
import de.htw.iconn.rbm.StoppingCondition;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import org.junit.Assert;
import org.xml.sax.SAXException;

/**
 *
 * @author Cristea
 */

public class XMLWeightLoggerTest extends TestCase{

  /* public RBMEnhanceTest(String testName) {
        super(testName);
    }  
    public RBMEnhanceTest(){}
    */
    float errorOrigin = 0;
    IRBM irbm;
    final float delta = 0.01f;
    final float learningRate = 0.1f;
    final int numVisible = 3;
    final int numHidden = 2;
    final int epochs = 1;
    final int updateInter = 1;
    
    float[][] trainingData = new float[][]
        {{ 0, 0, 0},{1, 0, 1},{ 1, 1, 1}};
    float[][] weights;
    float[][] oldData;
    float[][] newData;
    int trainingEpochs = 0;
    
    XMLWeightsSaver xmlLogger = new XMLWeightsSaver();
    
    XMLWeightsLoader xmlLoader = new XMLWeightsLoader();
    
    RBMInfoPackage infoOld;
 
    IRBMEndTrainingEnhancement endTrainingEnhancement = new IRBMEndTrainingEnhancement() {

        @Override
        public void action(RBMInfoPackage info) {       

             try {
                xmlLogger.singleWeights(info);
                
               File fileOld;
               File fileNew;
               
               fileOld = new File("RBMLogs/XMLSteps/Old.xml");
               fileNew = new File("RBMLogs/XMLSteps/New.xml");
                
               oldData = xmlLoader.loadWeightsFromXML(fileOld);
               newData = xmlLoader.loadWeightsFromXML(fileNew);
               
               Assert.assertArrayEquals(newData, oldData);
                
            } catch (    ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                Logger.getLogger(XMLWeightLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
   
    public void testFunctionEndTrainingEnhancement(){
       System.out.println("\n * XMLWeightsTest");
            
       IRBM jblasRBM = new RBMJBlas(numVisible, numHidden, learningRate, new DefaultLogisticMatrixFunction(), false, 0, null);
       
       //Train JBlas RBM
       RBMEnhancer enhanceJblas = new RBMEnhancer(jblasRBM);
       enhanceJblas.addEnhancement(endTrainingEnhancement);
       enhanceJblas.train(trainingData, new StoppingCondition(epochs), true, true);
       
    }
    
}
