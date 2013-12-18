/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.rbm.*;
import de.htw.iconn.fx.decomposition.*;
import de.htw.iconn.fx.decomposition.enhancement.IRBMEndTrainingEnhancement;
import de.htw.iconn.fx.decomposition.enhancement.IRBMTrainingEnhancement;
import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;
import de.htw.iconn.fx.decomposition.enhancement.XMLWeightLoggerNew;
import de.htw.iconn.fx.decomposition.enhancement.XMLWeightLoggerOld;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static junit.framework.Assert.assertEquals;
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
    double errorOrigin = 0;
    IRBM irbm;
    final double delta = 0.01;
    final double learningRate = 0.1;
    final int numVisible = 3;
    final int numHidden = 2;
    final int epochs = 1;
    final int updateInter = 1;
    
    double[][] trainingData = new double[][]
        {{ 0, 0, 0},{1, 0, 1},{ 1, 1, 1}};
    double[][] weights;
    double[][] oldData;
    double[][] newData;
    int trainingEpochs = 0;
    
    XMLWeightLoggerOld xmlLoggerOld = new XMLWeightLoggerOld();
    XMLWeightLoggerNew xmlLoggerNew = new XMLWeightLoggerNew();
    
    XMLWeightsLoader xmlLoader = new XMLWeightsLoader();
    
    RBMInfoPackage infoOld;
 
    IRBMEndTrainingEnhancement endTrainingEnhancement = new IRBMEndTrainingEnhancement() {

        @Override
        public void action(RBMInfoPackage info) {       

             try {
                xmlLoggerOld.singleWeights(info);
                xmlLoggerNew.singleWeights(info);
                
               File fileOld;
               File fileNew;
               
               fileOld = new File("RBMLogs/XMLSteps/Old.xml");
               fileNew = new File("RBMLogs/XMLSteps/New.xml");
                
               oldData = xmlLoader.loadWeightsFromXML(fileOld);
               newData = xmlLoader.loadWeightsFromXML(fileNew);
               
               Assert.assertArrayEquals(newData, oldData);
                
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(XMLWeightLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XMLWeightLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(XMLWeightLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
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
       enhanceJblas.train(trainingData, epochs, true, true);
       
    }
    
}
