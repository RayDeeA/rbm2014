package de.htw.iconn.rbm;

import java.util.LinkedList;

import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;


public class RBMStack implements IRBM {

	private final LinkedList<IRBM> rbms;
	
        public RBMStack() {
            this.rbms = new LinkedList<IRBM>();
        }
        
	public RBMStack(LinkedList<IRBM> rbms) {
		
		int lastRBMSize = rbms.getFirst().getInputSize();
		for (IRBM rbm : rbms) {
			if(rbm.getInputSize() == lastRBMSize) {
				lastRBMSize = rbm.getOutputSize();
			}
			else throw new IllegalArgumentException("InputSize: " + rbm.getInputSize()+ " differs from last outputsize: " + lastRBMSize);
		}
		
		this.rbms = rbms;
                
		
	}
	
	@Override
	public void train(double[][] trainingData, int max_epochs, boolean useHiddenStates, boolean useVisibleStates) {
		
		double[][] data = trainingData;
		
		for (IRBM rbm : rbms) {
			rbm.train(data, max_epochs, useHiddenStates, useVisibleStates);
			data = rbm.run_visible(data, false);
		}
	}

	@Override
	public double error(double[][] trainingData, boolean useHiddenStates, boolean useVisibleStates) {
		double sumError = 0;
		
		double[][] data = trainingData;
		for (IRBM rbm : rbms) {
			sumError += rbm.error(data, false, false);
			data = rbm.run_visible(data, false);
		}
		
		return sumError;
	}

	@Override
	public double[][] run_visible(double[][] userData, boolean useHiddenStates) {
		
		double[][] data = userData;
		for (IRBM rbm : rbms)  {

			data = rbm.run_visible(data, useHiddenStates);
		}
		return data;
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData, boolean useVisibleStates) {
		
		double[][] data = hiddenData;
		for (IRBM rbm : rbms)  {
			data = rbm.run_hidden(data, useVisibleStates);
		}
		return data;
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {

	}

	@Override
	public double[][][] getWeights() {
		LinkedList<double[][]> weights = new LinkedList<>();
		
		for (IRBM rbm : rbms) {
			if(rbm instanceof RBMStack) {
				double [][][] cascadeWeights = ((RBMStack)rbm).getWeights();
				for (double[][] ds : cascadeWeights) {
					weights.add(ds);
				}
			}
			else {
				weights.add(rbm.getWeights()[0]);
			}
		}
		
		return (double[][][])weights.toArray();
	}

	@Override
	public double[][][] getWeightsWithBias() {
		LinkedList<double[][]> weights = new LinkedList<>();
		
		for (IRBM rbm : rbms) {
			if(rbm instanceof RBMStack) {
				double [][][] cascadeWeights = ((RBMStack)rbm).getWeightsWithBias();
				for (double[][] ds : cascadeWeights) {
					weights.add(ds);
				}
			}
			else {
				weights.add(rbm.getWeightsWithBias()[0]);
			}
		}
		
		return (double[][][])weights.toArray();
	}

	@Override
	public int getInputSize() {
		return rbms.getFirst().getInputSize();
	}

	@Override
	public int getOutputSize() {
		return rbms.getLast().getOutputSize();
	}

	@Override
	public double getLearnRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ILogistic getLogisticFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBias() {
		// TODO Auto-generated method stub
		return false;
	}
        
        public boolean add(IRBM rbm) {
           return this.rbms.add(rbm);
        }

}