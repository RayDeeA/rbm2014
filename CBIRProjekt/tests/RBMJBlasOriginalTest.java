/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import de.htw.ait.rbm.RBMNico;
import de.htw.ait.rbm.RBMNico;
import de.htw.iconn.fx.decomposition.*;
import de.htw.iconn.fx.decomposition.rbm.*;
import de.htw.iconn.fx.decomposition.logistic.*;
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

public class RBMJBlasOriginalTest extends TestCase{

    double errorOrigin = 0;
    IRBM irbm;
    final double delta = 0.001;
    final double learningRate = 0.1;
    final int numVisible = 3;
    final int numHidden = 2;
    final int epochs = 100;
    final int updateInter = 1;
    
    double[][] trainingData = new double[][]
        {{ 0, 0, 0},{1, 0, 1},{ 1, 1, 1}};
    
    double[][] tempOriginweights;
    double[][] tempOriginCorrectweights;
    
    int trainingEpochs = 0;
    
    double[][] tempJBlasWeights;
    
    double[][] wJblas;
    double[][] wOrigin;
    
    double[][] compareJblas;
    double[][] compareOrig;
    
    
    RBMSettingsController rbmController;
    
    
     /*IRBMTrainingEnhancement trainingEnhancement = new IRBMTrainingEnhancement() {
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
           assertEquals(tempOriginweights, info.getWeights());  
        } 
    };*/
     
    IRBMEndTrainingEnhancement endTrainingEnhancement = new IRBMEndTrainingEnhancement() {

        @Override
        public void action(RBMInfoPackage info) {       

            
            
            //Compare Epochs
            //System.out.println("* Epochs");
            //assertEquals(epochs, info.getEpochs()); 
            //Compare Error
            System.out.println("* Error"); 
            assertEquals(errorOrigin, info.getError(),delta);
            //Compare Weights
            //System.out.println("* Weights"); 
            
            compareJblas = info.getWeights();
            compareOrig = wOrigin;
            
            System.out.println("* Weights");
            for(int i = 0; i < compareJblas.length; i++){
             for(int j = 0; j < compareJblas[0].length; j++){
                assertEquals(compareJblas[i][j], compareOrig[i][j], delta);
                
             }
            }
            
            
        }
    };
    
    /*public void testFunctionTrainingEnhancement(){
       System.out.println("* TrainingEnhancement");
   
       IRBM jblasRBM = new RBMJBlas(numVisible, numHidden, learningRate, new DefaultLogisticMatrixFunction(), false, 0, null);
       RBMOriginal origRBM = new RBMOriginal(numVisible,numHidden,learningRate);
       
       //Train Original RBM
       origRBM.train(trainingData, epochs, false, false);
       errorOrigin = origRBM.error(trainingData, false, false);
       tempOriginweights = origRBM.getWeights();
       
       //Train JBlas RBM
       RBMEnhancer enhanceJblas = new RBMEnhancer(jblasRBM);
       enhanceJblas.addEnhancement(trainingEnhancement);
       enhanceJblas.train(trainingData, epochs, false, false);
       
        //assertEquals(e1, e2, delta);
    }*/
    
    public void testFunctionEndTrainingEnhancement(){
       System.out.println("\n * EndTrainingEnhancement");
            
       
       RBMOriginal origRBM = new RBMOriginal(numVisible,numHidden,learningRate);
       tempOriginweights = origRBM.getWeights();
       
       RBMOriginalCorrect rbmCorrect = new RBMOriginalCorrect(numVisible, numHidden, learningRate);
       tempOriginCorrectweights = rbmCorrect.getWeights();
       
       /*RBMNico nicoRBM = new RBMNico(numVisible,numHidden,learningRate);
        nicoRBM.getWeights();
        */
       
       IRBM jblasRBM = new RBMJBlas(numVisible, numHidden, learningRate, new DefaultLogisticMatrixFunction(), true, 0, tempOriginCorrectweights);
       tempJBlasWeights = jblasRBM.getWeights();
      
       
       
       //Train Original RBM
       rbmCorrect.train(trainingData, epochs, false, false);
       errorOrigin = rbmCorrect.error(trainingData, true, true);  
       wOrigin = rbmCorrect.getWeights();
       
        
       /*nicoRBM.train(trainingData, epochs, false, false);
       errorOrigin = nicoRBM.error(trainingData, false, false);
       wOrigin = nicoRBM.getWeights();
       */
       //Train JBlas RBM
       RBMEnhancer enhanceJblas = new RBMEnhancer(jblasRBM);
       enhanceJblas.addEnhancement(endTrainingEnhancement);
       enhanceJblas.train(trainingData, epochs, false, false);
       
       
       
//assertEquals(e1, e2, delta);
    }
    
}
