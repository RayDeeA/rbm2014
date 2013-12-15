package de.htw.iconn.fx.decomposition.rbm;

import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import java.util.LinkedList;


public class RBMStack implements IRBM {

	private final LinkedList<IRBM> rbms;
	
    public RBMStack() {
        this.rbms = new LinkedList<>();
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
	public void setWeights(double[][] weights) {
             throw new UnsupportedOperationException("Not supported by " + RBMStack.class.getName());
	}

	@Override
	public double[][] getWeights() {	
             throw new UnsupportedOperationException("Not supported by " + RBMStack.class.getName());
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
