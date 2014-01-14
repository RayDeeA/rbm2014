/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.rbm;

import java.util.Random;

/**
 *
 * @author christoph
 */
public final class FloatMatrix {

    private float[][] m;

    public enum Bias {

        NONE, COLUMN_ONLY, BOTH, ROW_ONLY
    }
    
    public FloatMatrix(int rows, int columns, boolean useSeed, int seed){
        Random random;
        if(useSeed) random = new Random(seed);
        else random = new Random();
        this.m = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m[i][j] = (float) random.nextGaussian() * 0.01f;
            }
        }
    }

    public FloatMatrix(float[][] m) {
        this(m, Bias.NONE);
    }

    public FloatMatrix(float[][] m, Bias bias) {
        if (bias == Bias.COLUMN_ONLY) {
            this.m = new float[m.length][m[0].length + 1];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    this.m[i][j + 1] = m[i][j];
                }
            }
            
            setFirstColumnOne();
            
        } else if(bias == Bias.ROW_ONLY) {
            this.m = new float[m.length + 1][m[0].length];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    this.m[i + 1][j] = m[i][j];
                }
            }
            
            setFirstRowOne();
            
        } else if(bias == Bias.BOTH) {
        this.m = new float[m.length + 1][m[0].length + 1];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    this.m[i + 1][j + 1] = m[i][j];
                }
            }
            
            setFirstColumnOne();
            setFirstRowOne();
            
        } else {
            this.m = m;
        }
    }

    public FloatMatrix(float[][] m, boolean biasColumn, boolean biasRow) {
        this.m = new float[m.length][m[0].length + 1];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                this.m[i][j + 1] = m[i][j];
            }
        }
        setFirstColumnOne();
    }

    public void mmuli(FloatMatrix other, FloatMatrix result) {
        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < other.m[0].length; ++j) {
                result.m[i][j] = 0;
                for (int k = 0; k < m[0].length; ++k) {
                    result.m[i][j] += m[i][k] * other.m[k][j];
                }
            }
        }
    }

    public void transposei(FloatMatrix result) {
        for (int i = 0; i < m.length; ++i) {
            for (int j = 0; j < m[0].length; j++) {
                result.m[i][j] = m[j][i];
            }
        }
    }

    public void setFirstColumnOne() {
        for (int i = 0; i < m.length; i++) {
            m[i][0] = 1.0f;
        }
    }
    public void setFirstRowOne() {
        for (int i = 0; i < m[0].length; i++) {
            m[0][i] = 1.0f;
        }
    }

    public float[][] getData() {
        return m;
    }

}
