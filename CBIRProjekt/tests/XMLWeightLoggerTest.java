/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.*;
import de.htw.iconn.fx.decomposition.rbm.*;
import de.htw.iconn.rbm.RBMOriginal;
import de.htw.iconn.fx.decomposition.*;
import de.htw.iconn.fx.decomposition.enhancement.IRBMEndTrainingEnhancement;
import de.htw.iconn.fx.decomposition.enhancement.IRBMTrainingEnhancement;
import de.htw.iconn.fx.decomposition.enhancement.RBMEnhancer;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;
import de.htw.iconn.fx.decomposition.enhancement.XMLWeightLogger;
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
    
    XMLWeightLogger xmlLogger = new XMLWeightLogger();
    XMLWeightLoggerOld xmlLoggerModified = new XMLWeightLoggerOld();
    XMLWeightsLoader xmlLoader = new XMLWeightsLoader();
    
    RBMInfoPackage infoOld;
 
    RBMSettingsController rbmController;
    
    
     IRBMTrainingEnhancement trainingEnhancement = new IRBMTrainingEnhancement() {
        @Override
        public int getUpdateInterval() {
           return 1;
        }
        
        @Override
        public void action(RBMInfoPackage info) { 
           
           System.out.println("* Update-Interval");
           //Compare Intervals
           assertEquals(updateInter,getUpdateInterval());
           //Compare Epochs
           System.out.println("* Epochs");
           assertEquals(epochs, info.getEpochs()); 
           //Compare Error
           System.out.println("* Error"); 
           //assertEquals(errorOrigin, info.getError());
           //Compare Weights
           System.out.println("* Weights"); 
           assertEquals(weights, info.getWeights()); 
          
        }

       
    };
     
    IRBMEndTrainingEnhancement endTrainingEnhancement = new IRBMEndTrainingEnhancement() {

        @Override
        public void action(RBMInfoPackage info) {       

             try {
                xmlLogger.singleWeights(info);
                xmlLoggerModified.singleWeights(info);
                
               File fileOld;
               File fileNew;
               
               fileOld = new File("/Users/Cristea/NetBeansProjects/iconn_v3/virrbm/CBIRProjekt/RBMLogs/XMLSteps/Old.xml");
               fileNew = new File("/Users/Cristea/NetBeansProjects/iconn_v3/virrbm/CBIRProjekt/RBMLogs/XMLSteps/New.xml");
                
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
    
    /*public void testFunctionTrainingEnhancement(){
       System.out.println("* TrainingEnhancement");
   
       IRBM jblasRBM = new RBMJBlas(numVisible, numHidden, learningRate, new DefaultLogisticMatrixFunction(), false, 0, null);
       RBMOriginal origRBM = new RBMOriginal(numVisible,numHidden,learningRate);
       
       //Train Original RBM
       origRBM.train(trainingData, epochs, true, true);
       errorOrigin = origRBM.error(trainingData, true, true);
       weights = origRBM.getWeights();
       
       //Train JBlas RBM
       RBMEnhancer enhanceJblas = new RBMEnhancer(jblasRBM);
       enhanceJblas.addEnhancement(trainingEnhancement);
       enhanceJblas.train(trainingData, epochs, true, true);
       
        //assertEquals(e1, e2, delta);
    }*/
    
    public void testFunctionEndTrainingEnhancement(){
       System.out.println("\n * EndTrainingEnhancement");
            
       IRBM jblasRBM = new RBMJBlas(numVisible, numHidden, learningRate, new DefaultLogisticMatrixFunction(), false, 0, null);
       RBMOriginal origRBM = new RBMOriginal(numVisible,numHidden,learningRate);
       
       //Train Original RBM
       origRBM.train(trainingData, epochs, true, true);
       errorOrigin = origRBM.error(trainingData, true, true);
       weights = origRBM.getWeights();
       
       //Train JBlas RBM
       RBMEnhancer enhanceJblas = new RBMEnhancer(jblasRBM);
       enhanceJblas.addEnhancement(endTrainingEnhancement);
       enhanceJblas.train(trainingData, epochs, true, true);
       
       //assertEquals(e1, e2, delta);
    }
    
}
