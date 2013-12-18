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

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 *
 * @author Cristea
 */

public class RBMEnhanceTest extends TestCase{

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
    int trainingEpochs = 0;
    
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

            //Compare Epochs
            System.out.println("* Epochs");
            assertEquals(epochs, info.getEpochs()); 
            //Compare Error
            System.out.println("* Error"); 
            //assertEquals(errorOrigin, info.getError());
            //Compare Weights
            System.out.println("* Weights"); 
            Assert.assertArrayEquals(weights, info.getWeights()); 
        }
    };
    
    public void testFunctionTrainingEnhancement(){
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
    }
    
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
