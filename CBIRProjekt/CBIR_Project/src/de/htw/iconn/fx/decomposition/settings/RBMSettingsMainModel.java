package de.htw.iconn.fx.decomposition.settings;

/**
 *
 * @author Moritz
 */
public class RBMSettingsMainModel {
    //combobox select options
    private final String[] rbmImplementations = {"RBMJBlas"};
    private final String[] rbmFeatures = {"PixelRBM", "DCTRBM"};

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
    
    private int selectedRbmImplementation = 0;
    private int selectedRbmFeature = 0;
    private int selectedLogisticFunction = 0;
    private int inputSize = 28 * 28;
    private int outputSize = 10;

    public int getSelectedRbmImplementation() {
        return selectedRbmImplementation;
    }

    public void setSelectedRbmImplementation(int selectedRbmImplementation) {
        this.selectedRbmImplementation = selectedRbmImplementation;
    }

    public int getSelectedRbmFeature() {
        return selectedRbmFeature;
    }

    public void setSelectedRbmFeature(int selectedRbmFeature) {
        this.selectedRbmFeature = selectedRbmFeature;
        if(this.selectedRbmFeature == 0){
            this.inputSize = 28 * 28;
        }else if(this.selectedRbmFeature == 1){
            this.inputSize = 15;
        }
    }

    public int getSelectedLogisticFunction() {
        return selectedLogisticFunction;
    }

    public void setSelectedLogisticFunction(int selectedLogisticFunction) {
        this.selectedLogisticFunction = selectedLogisticFunction;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }
    
    public String[] getRbmImplementations() {
        return rbmImplementations;
    }

    public String[] getRbmFeatures() {
        return rbmFeatures;
    }

    public String[] getLogisticFunctions() {
        return logisticFunctions;
    }
    
}
