package de.htw.iconn.fx.decomposition.views;

import de.htw.iconn.fx.decomposition.IVisualizeObserver;
import de.htw.iconn.fx.decomposition.enhancement.RBMInfoPackage;
import java.util.ArrayList;


public class ErrorViewModel implements IVisualizeObserver {
	
	private final ErrorViewController controller;
        
        
	private final ArrayList<Double> errors;

	public ErrorViewModel(ErrorViewController controller) {
		this.controller = controller;
                this.errors = new ArrayList<>();
	}

    @Override
    public void update(RBMInfoPackage pack) {
        getErrors().add(pack.getError());
        controller.update();
    }

    /**
     * @return the errors
     */
    public ArrayList<Double> getErrors() {
        return errors;
    }
    
    public void reset() {
        errors.clear();
    }
    
   
        
        

}
