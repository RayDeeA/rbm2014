/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.htw.iconn.fx.decomposition.logistic.*;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Cristea
 */

public class RBMLogistics extends TestCase {
 
    public RBMLogistics(String testName) {
        super(testName);
    }  
    
    public RBMLogistics() {
    }
    
    double[][] testArray = new double[][]{
           {0.5, 0.5, 0.5}, 
           { 1, 1, 1},
           { 0, 0, 0}};
    
    double delta= 0.000001;
    
    DoubleMatrix testDoubleMatrix = new DoubleMatrix(testArray);
 
    
    public void testDefaultLogistic() {
     
        double[][] expectMatrix = new double[][]{
           {0.622459, 0.622459, 0.622459}, 
           { 0.731059, 0.731059, 0.731059},
           { 0.5, 0.5, 0.5}};

        System.out.println("Test Default Logistic");
        DefaultLogisticMatrixFunction x = new DefaultLogisticMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);
        
        double[][] resultArray = result.toArray2();
        
        System.out.println(result);

        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) { 
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }            
    }
    
    public void testTanhLogistic() {
   
        double[][] expectMatrix = new double[][]{
           {0.731059, 0.731059, 0.731059}, 
           { 0.880797, 0.880797, 0.880797},
           { 0.5, 0.5, 0.5}};

        System.out.println("Test TanH Logistic");
        TanHMatrixFunction x = new TanHMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);
        
        double[][] resultArray = result.toArray2();
        
        System.out.println(result);

        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) { 
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }            
    }
    
  
    
    
}
