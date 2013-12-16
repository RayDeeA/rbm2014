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

    double[][] testArray = new double[][]{{0, 0, 0}, {0.5, 0.5, 0.5}, {1, 1, 1}};

    double[][] testArrayHardClipped = new double[][]{{2, 2, 2}, {-1.0, -1.0, -1.0}, {1, 1, 1}};

    double delta = 0.000001;

    DoubleMatrix testDoubleMatrix = new DoubleMatrix(testArray);
    DoubleMatrix testDoubleMatrixHard = new DoubleMatrix(testArrayHardClipped);

    public void testDefaultLogistic() {

        double[][] expectMatrix = new double[][]{
            {0.5, 0.5, 0.5},
            {0.622459, 0.622459, 0.622459},
            {0.731059, 0.731059, 0.731059}};

        System.out.println("* testDefaultLogistic()");
        DefaultLogisticMatrixFunction x = new DefaultLogisticMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);

        double[][] resultArray = result.toArray2();

        //System.out.println(result);
        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) {
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }
    }

    public void testTanhLogistic() {

        double[][] expectMatrix = new double[][]{
            {0.5, 0.5, 0.5},
            {0.731059, 0.731059, 0.731059},
            {0.880797, 0.880797, 0.880797}};

        System.out.println("* testTanhLogistic");
        TanHMatrixFunction x = new TanHMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);

        double[][] resultArray = result.toArray2();

        //System.out.println(result);
        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) {
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }
    }

    public void testGaussLogistic() {

        double[][] expectMatrix = new double[][]{
            {1.0, 1.0, 1.0},
            {0.778801, 0.778801, 0.778801},
            {0.367879, 0.367879, 0.367879}};

        System.out.println("* testGaussLogistic");
        GaussMatrixFunction x = new GaussMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);

        double[][] resultArray = result.toArray2();

        //System.out.println(result);
        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) {
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }
    }

    public void testHardClippedLogistic() {

        double[][] expectMatrix = new double[][]{
            {1, 1, 1},
            {0, 0, 0},
            {1, 1, 1}};

        System.out.println("* testHardClippedLogistic");
        HardClipMatrixFunction x = new HardClipMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrixHard);

        double[][] resultArray = result.toArray2();

        //System.out.println(result);
        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) {
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }
    }

    public void testRectifierLogistic() {

        double[][] expectMatrix = new double[][]{
            {0.693147, 0.693147, 0.693147},
            {0.974077, 0.974077, 0.974077},
            {1.313262, 1.313262, 1.313262}};

        System.out.println("* testRectifierLogistic");
        RectifierMatrixFunction x = new RectifierMatrixFunction();
        DoubleMatrix result = x.function(testDoubleMatrix);

        double[][] resultArray = result.toArray2();

        //System.out.println(result);
        for (int i = 0; i < expectMatrix.length; i++) {
            for (int j = 0; j < expectMatrix.length; j++) {
                assertEquals(expectMatrix[i][j], resultArray[i][j], delta);
            }
        }
    }

}
