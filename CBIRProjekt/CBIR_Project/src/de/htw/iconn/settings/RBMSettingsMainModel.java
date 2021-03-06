package de.htw.iconn.settings;


import de.htw.iconn.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.logistic.GaussMatrixFunction;
import de.htw.iconn.logistic.HardClipMatrixFunction;
import de.htw.iconn.logistic.ILogistic;
import de.htw.iconn.logistic.LinearClippedMatrixFunction;
import de.htw.iconn.logistic.LinearInterpolatedMatrixFunction;
import de.htw.iconn.logistic.LinearUnclippedMatrixFunction;
import de.htw.iconn.logistic.RectifierMatrixFunction;
import de.htw.iconn.logistic.SquareRootLogistic;
import de.htw.iconn.logistic.TanHMatrixFunction;
import de.htw.iconn.persistence.Conserve;

/**
 *
 * @author Moritz
 */
public class RBMSettingsMainModel{
	
    private final RBMSettingsMainController controller;
    
    //combobox select options
    private final String[] rbmImplementations = {"RBMJBlas"};
    private final String[] rbmFeatures = {"Pixel Intensities"};

    public RBMSettingsMainModel(RBMSettingsMainController controller) {
        this.controller = controller;
    }

    private final String[] logisticFunctions = {
        "Standard",
        "Gaussian",
        "Hard Clip",
        "Linear Clipped",
        "Linear Interpolated",
        "Linear Unclipped (Absolute Value)",
        "Rectifier",
        "TanH",
        "SqareRoot"
    };
    
    private final ILogistic[] logisticFunctionImplementation = {
        new DefaultLogisticMatrixFunction(),
        new GaussMatrixFunction(),
        new HardClipMatrixFunction(),
        new LinearClippedMatrixFunction(),
        new LinearInterpolatedMatrixFunction(),
        new LinearUnclippedMatrixFunction(),
        new RectifierMatrixFunction(),
        new TanHMatrixFunction(),
        new SquareRootLogistic()
    };
    
    @Conserve
    private int selectedRbmImplementation = 0;
    @Conserve
    private int selectedRbmFeature = 0;
    @Conserve
    private int selectedLogisticFunction = 0;
    @Conserve
    private int inputSize = 0;
    @Conserve
    private int outputSize = 150;
    @Conserve
	private boolean isRgb;

    public int getSelectedRbmImplementation() {
        return selectedRbmImplementation;
    }

    public void setSelectedRbmImplementation(int selectedRbmImplementation) {
        this.selectedRbmImplementation = selectedRbmImplementation;
    }

    public int getSelectedRbmFeature() {
        return this.selectedRbmFeature;      
    }

    public void setSelectedRbmFeature(int selectedRbmFeature) {
        this.selectedRbmFeature = selectedRbmFeature;
    }

    public int getSelectedLogisticFunction() {
        return selectedLogisticFunction;
    }
    
    public ILogistic getSelectedLogisticFunctionImplementation() {
        return this.logisticFunctionImplementation[selectedLogisticFunction];
    }

    public void setSelectedLogisticFunction(int selectedLogisticFunction) {
        this.selectedLogisticFunction = selectedLogisticFunction;
    }

    public int getInputSize() {
        return (this.isRgb) ? 3 * inputSize : inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
        this.controller.update();
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }
    
    public void setRgb(boolean isRgb) {
    	this.isRgb = isRgb;
    	this.controller.update();
    }
    
    public boolean isRgb() {
		return isRgb;
	}

	public String[] getRbmImplementations() {
        return rbmImplementations;
    }

    public String[] getFeatures() {
        return rbmFeatures;
    }

    public String[] getLogisticFunctions() {
        return logisticFunctions;
    }
    
}
