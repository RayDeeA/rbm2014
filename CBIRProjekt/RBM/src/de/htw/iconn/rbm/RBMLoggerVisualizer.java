package de.htw.iconn.rbm;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.cbir.gui.RBMVisualizationFrame;
import de.htw.iconn.rbm.functions.ILogistic;

public class RBMLoggerVisualizer implements IRBMLogger, IRBM{
	
	private RBMLogger logger;
	private RBMVisualizationFrame frame;
	private CBIREvaluationModel evaluationModel;
	
	public RBMLoggerVisualizer(RBMLogger logger, RBMVisualizationFrame frame, CBIREvaluationModel evaluationModel){
		this.logger = logger;
		this.frame = frame;
		this.evaluationModel = evaluationModel;
	}
	@Override
	public void train(double[][] trainingData, int max_epochs) {
		int steps = max_epochs / evaluationModel.getUpdateInterval();
		for(int i = 0; i < steps; ++i){		
			logger.train(trainingData, evaluationModel.getUpdateInterval());
			frame.updateLeftPanel(logger.getWeights()[0], logger.error(trainingData), 47.11);
			evaluationModel.addToCollectedWeights(logger.getWeightsWithBias());
			if((i*evaluationModel.getUpdateInterval()) % evaluationModel.getXmlOutputFrequency() == 0){
				try {
					logger.stepXmlLogTraining(evaluationModel);
				} catch (IOException | ParserConfigurationException
						| SAXException | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			logger.stepXmlLogTraining(evaluationModel);
		} catch (IOException | ParserConfigurationException
				| SAXException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finalCsvLog(CBIREvaluationModel evaluationModel)
			throws IOException {
		logger.finalCsvLog(evaluationModel);
		
	}

	@Override
	public void stepCsvLog(CBIREvaluationModel evaluationModel)
			throws IOException {
		logger.stepCsvLog(evaluationModel);
		
	}

	@Override
	public void stepXmlLogEvolution(CBIREvaluationModel evaluationModel)
			throws IOException, ParserConfigurationException,
			TransformerException {
		logger.stepXmlLogEvolution(evaluationModel);
		
	}

	@Override
	public void stepXmlLogTraining(CBIREvaluationModel evaluationModel)
			throws IOException, ParserConfigurationException, SAXException,
			TransformerConfigurationException, TransformerException {
		logger.stepXmlLogTraining(evaluationModel);
		
	}

	@Override
	public double error(double[][] trainingData) {
		return logger.error(trainingData);
	}

	@Override
	public double[][] run_visible(double[][] userData) {
		return logger.run_visible(userData);
	}

	@Override
	public double[][] run_hidden(double[][] hiddenData) {
		return logger.run_hidden(hiddenData);
	}

	@Override
	public void setWeightsWithBias(double[][] weights) {
		logger.setWeightsWithBias(weights);	
	}

	@Override
	public double[][][] getWeights() {
		return logger.getWeights();
	}

	@Override
	public double[][][] getWeightsWithBias() {
		return logger.getWeightsWithBias();
	}

	@Override
	public int getInputSize() {
		return logger.getInputSize();
	}

	@Override
	public int getOutputSize() {
		return logger.getOutputSize();
	}

	@Override
	public double getLearnRate() {
		return logger.getLearnRate();
	}

	@Override
	public ILogistic getLogisticFunction() {
		return logger.getLogisticFunction();
	}

	@Override
	public boolean hasBias() {
		return logger.hasBias();
	}

}
