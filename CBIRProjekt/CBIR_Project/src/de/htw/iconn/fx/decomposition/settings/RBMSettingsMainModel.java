package de.htw.iconn.fx.decomposition.settings;


import de.htw.iconn.fx.decomposition.AModel;
import de.htw.iconn.fx.decomposition.logistic.DefaultLogisticMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.GaussMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.HardClipMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.ILogistic;
import de.htw.iconn.fx.decomposition.logistic.LinearClippedMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.LinearInterpolatedMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.LinearUnclippedMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.RectifierMatrixFunction;
import de.htw.iconn.fx.decomposition.logistic.SquareRootLogistic;
import de.htw.iconn.fx.decomposition.logistic.TanHMatrixFunction;

/**
 *
 * @author Moritz
 */
public class RBMSettingsMainModel extends AModel{
    //combobox select options
    private final String[] rbmImplementations = {"RBMJBlas"};
    private final String[] rbmFeatures = {"PixelRBM", "DCTRBM"};

    public RBMSettingsMainModel(RBMSettingsMainController controller) {
        this.addObserver(controller);
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
        return this.selectedRbmFeature;      
    }

    public void setSelectedRbmFeature(int selectedRbmFeature) {
        this.selectedRbmFeature = selectedRbmFeature;
        if(this.selectedRbmFeature == 0) {
            this.inputSize = 28 * 28;
        }else if(this.selectedRbmFeature == 1) {
            this.inputSize = 15;
        }
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
