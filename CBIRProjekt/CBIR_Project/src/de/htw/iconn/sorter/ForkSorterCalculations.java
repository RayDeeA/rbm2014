package de.htw.iconn.sorter;

import de.htw.iconn.sorter.ASorter;
import de.htw.iconn.sorter.SorterRBMFeatures;
import java.util.concurrent.RecursiveAction;

import de.htw.iconn.image.Pic;

public class ForkSorterCalculations extends RecursiveAction {

	private static final long serialVersionUID = 428974300938969664L;
	protected static int sThreshold = 10;
	
	private ASorter sorter;
	private Pic[] images;
	private int start;
	private int length;
	
	public ForkSorterCalculations(ASorter sorter, Pic[] images, int start, int length) {
		this.sorter = sorter;
		this.images = images;
		this.start = start;
		this.length = length;
	}
	
    protected void computeDirectly() {
        for (int index = start; index < start + length; index++) {
        	Pic image = images[index];
			image.setFeatureImage(sorter.getFeatureImage(image));
        }
    }
    
	@Override
	protected void compute() {
		
	    if (length < sThreshold) {
	        computeDirectly();
	        return;
	    }
	    
	    int split = length / 2;
	    
	    invokeAll(new ForkSorterCalculations(sorter, images, start, split),
	              new ForkSorterCalculations(sorter, images, start + split, length - split));
	}

}
