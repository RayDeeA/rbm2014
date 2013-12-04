/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.iconn.fx;

import de.htw.cbir.DCTRBM;
import de.htw.cbir.ImageManager;
import de.htw.cbir.PixelRBM;
import de.htw.cbir.RBMWrapper;
import de.htw.iconn.rbm.IRBM;
import de.htw.iconn.rbm.RBMJBlas;
import de.htw.iconn.rbm.functions.DefaultLogisticMatrixFunction;
import de.htw.iconn.rbm.functions.GaussMatrixFunction;
import de.htw.iconn.rbm.functions.HardClipMatrixFunction;
import de.htw.iconn.rbm.functions.ILogistic;
import de.htw.iconn.rbm.functions.LinearClippedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearInterpolatedMatrixFunction;
import de.htw.iconn.rbm.functions.LinearUnclippedMatrixFunction;
import de.htw.iconn.rbm.functions.RectifierMatrixFunction;
import de.htw.iconn.rbm.functions.SquareRootLogistic;
import de.htw.iconn.rbm.functions.TanHMatrixFunction;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author christoph
 */
public class SimpleRBMModel {
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

    private boolean useRandomOrder;
    private boolean showImageViewer;
    private boolean useLogger;
    private boolean showVisualization;
    private int updateFrequency;
    //private Set<String> imageGroups;
    private int rbmImplementation;
    private int rbmFeature;
    private int logisticFunction;
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
    
    private ImageManager imageManager;
    private Sorter sorter;
    private RBMWrapper wrapper;
    private IRBM rbm;

    public SimpleRBMModel(boolean useRandomOrder, boolean showImageViewer,
            boolean useLogger, boolean showVisualization, int updateFrequency,
            String rbmImplementation, String rbmFeature, String logisticFunction,
            int inputSize, int outputSize, int stoppingCondition, int epochs,
            double error, double learningRate, boolean useMomentum, boolean useSeed,
            int seed, boolean useBias, boolean binarizeProbabilities, boolean rbmTrained) {
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
    
    public boolean generateRBM(){
        this.rbm = null;
        this.wrapper = null;
        
        if(validate()){
            ILogistic logistic = null;
            if(this.logisticFunction == 0){
                logistic = new DefaultLogisticMatrixFunction();
            }else if(this.logisticFunction == 1){
                logistic = new GaussMatrixFunction();
            }else if(this.logisticFunction == 2){
                logistic = new HardClipMatrixFunction();
            }else if(this.logisticFunction == 3){
                logistic = new LinearClippedMatrixFunction();
            }else if(this.logisticFunction == 4){
                logistic = new LinearInterpolatedMatrixFunction();
            }else if(this.logisticFunction == 5){
                logistic = new LinearUnclippedMatrixFunction();
            }else if(this.logisticFunction == 6){
                logistic = new RectifierMatrixFunction();
            }else if(this.logisticFunction == 7){
                logistic = new TanHMatrixFunction();
            }else if(this.logisticFunction == 8){
                logistic = new SquareRootLogistic();
            }
            
            if(logistic != null){
                if(this.rbmImplementation == 0){
                    this.rbm = new RBMJBlas(this.inputSize, this.outputSize, this.learningRate, logistic, this.useSeed, this.seed);
                }
            }
            
            if(this.rbm != null){
                if(this.rbmFeature == 0){
                    this.wrapper = new PixelRBM(this.inputSize, this.outputSize, rbm);
                }else if(this.rbmFeature == 1){
                    this.wrapper = new DCTRBM(this.inputSize, this.outputSize, rbm);
                }
            }
            
            if(this.wrapper != null){
                return true;
            }
        }
        return false;
    }
    
    public boolean generateSorter(){
        if(this.wrapper != null && this.rbmTrained){
            ForkJoinPool pool = new ForkJoinPool();
            this.sorter = new SorterRBMWrapper(this.imageManager.getImages(), pool, wrapper);
            return true;
        }
        return false;
    }
    
    public void trainRBM(){      
        if(this.generateRBM()){
            System.out.println("start training");
            this.wrapper.train(this.imageManager.getImages(), this.epochs);
        }
        this.rbmTrained = true;
        this.generateSorter();
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

    public int getRbmImplementation() {
        return rbmImplementation;
    }

    public int getRbmFeature() {
        return rbmFeature;
    }

    public int getLogisticFunction() {
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

    public void setRbmImplementation(int rbmImplementation) {
        this.rbmImplementation = rbmImplementation;
    }

    public void setRbmFeature(int rbmFeature) {
        this.rbmFeature = rbmFeature;
    }

    public void setLogisticFunction(int logisticFunction) {
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
    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
    }

    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    public void setWrapper(RBMWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void setRbm(IRBM rbm) {
        this.rbm = rbm;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public Sorter getSorter() {
        return sorter;
    }

    public RBMWrapper getWrapper() {
        return wrapper;
    }

    public IRBM getRbm() {
        return rbm;
    }       
    
    public boolean validate() {

        if (this.rbmImplementation != -1 && this.rbmFeature != -1
                && this.logisticFunction != -1) {

            if (rbmFeature == 0) {
                this.inputSize = 28 * 28;
            }
            if (rbmFeature == 1) {
                this.inputSize = 15;
            }

            return true;

        } else {
            return false;
        }

    }

  
}
