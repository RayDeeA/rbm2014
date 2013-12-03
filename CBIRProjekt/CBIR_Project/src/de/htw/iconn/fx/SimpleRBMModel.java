/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htw.iconn.fx;

import de.htw.cbir.ImageManager;

/**
 *
 * @author christoph
 */
public class SimpleRBMModel {
    
    private final String[] rbmImplementations = {"RBMJBlas", "RBMNico"};
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
    
    private boolean useRandomOrder;
    private boolean showImageViewer;
    private boolean useLogger;
    private boolean showVisualization;
    private int updateFrequency;
    //private Set<String> imageGroups;
    private String rbmImplementation;
    private String rbmFeature;
    private String logisticFunction;
    private int inputSize;
    private int outputSize;
    private int stoppingCondition;
    private int epochs;
    private double error;
    private double learningRate;
    private boolean useMomentum;
    private boolean useSeed;
    private int seed;
    private boolean useBias;
    private boolean binarizeProbabilities;
    private boolean rbmTrained;
    
    
    public SimpleRBMModel(boolean useRandomOrder, boolean showImageViewer, 
            boolean useLogger, boolean showVisualization, int updateFrequency,
            String rbmImplementation, String rbmFeature, String logisticFunction,
            int inputSize, int outputSize, int stoppingCondition, int epochs, 
            double error, double learningRate, boolean useMomentum, boolean useSeed,
            int seed, boolean useBias, boolean binarizeProbabilities, boolean rbmTrained){
        this.useRandomOrder = useRandomOrder;
        this.showImageViewer = showImageViewer;
        this.useLogger = useLogger;
        this.showVisualization = showVisualization;
        this.updateFrequency = updateFrequency;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.stoppingCondition = stoppingCondition;
        this.epochs = epochs;
        this.error = error;
        this.learningRate = learningRate;
        this.useMomentum = useMomentum;
        this.useSeed = useSeed;
        this.seed = seed;
        this.useBias = useBias;
        this.binarizeProbabilities = binarizeProbabilities;
        this.rbmTrained = rbmTrained;
    }
    
    public SimpleRBMModel() {       
        this(false, true, true, true, 100, "RBMJBlas", "PixelRBM", "Standard", 15, 
                10, 0, 10000, 0.1, 0.1, false, false, 0, true, false, false);
    }

    public void setUseRandomOrder(boolean useRandomOrder) {
        this.useRandomOrder = useRandomOrder;
    }

    public boolean isUseRandomOrder() {
        return useRandomOrder;
    }

    public boolean isShowImageViewer() {
        return showImageViewer;
    }

    public boolean isUseLogger() {
        return useLogger;
    }

    public boolean isShowVisualization() {
        return showVisualization;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public String getRbmImplementation() {
        return rbmImplementation;
    }

    public String getRbmFeature() {
        return rbmFeature;
    }

    public String getLogisticFunction() {
        return logisticFunction;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getStoppingCondition() {
        return stoppingCondition;
    }

    public int getEpochs() {
        return epochs;
    }

    public double getError() {
        return error;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public boolean isUseMomentum() {
        return useMomentum;
    }

    public boolean isUseSeed() {
        return useSeed;
    }

    public int getSeed() {
        return seed;
    }

    public boolean isUseBias() {
        return useBias;
    }

    public boolean isBinarizeProbabilities() {
        return binarizeProbabilities;
    }

    public void setShowImageViewer(boolean showImageViewer) {
        this.showImageViewer = showImageViewer;
    }

    public void setUseLogger(boolean useLogger) {
        this.useLogger = useLogger;
    }

    public void setShowVisualization(boolean showVisualization) {
        this.showVisualization = showVisualization;
    }

    public void setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public void setRbmImplementation(String rbmImplementation) {
        this.rbmImplementation = rbmImplementation;
    }

    public void setRbmFeature(String rbmFeature) {
        this.rbmFeature = rbmFeature;
    }

    public void setLogisticFunction(String logisticFunction) {
        this.logisticFunction = logisticFunction;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public void setStoppingCondition(int stoppingCondition) {
        this.stoppingCondition = stoppingCondition;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public void setError(double error) {
        this.error = error;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setUseMomentum(boolean useMomentum) {
        this.useMomentum = useMomentum;
    }

    public void setUseSeed(boolean useSeed) {
        this.useSeed = useSeed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setUseBias(boolean useBias) {
        this.useBias = useBias;
    }

    public void setBinarizeProbabilities(boolean binarizeProbabilities) {
        this.binarizeProbabilities = binarizeProbabilities;
    }  

    public boolean isRbmTrained() {
        return rbmTrained;
    }

    public void setRbmTrained(boolean rbmTrained) {
        this.rbmTrained = rbmTrained;
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
