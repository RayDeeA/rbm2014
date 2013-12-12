package de.htw.iconn.rbm;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import de.htw.cbir.CBIREvaluationModel;

@Deprecated
public interface IRBMLogger {
	public void finalCsvLog(CBIREvaluationModel evaluationModel) throws IOException;
	public void stepCsvLog(CBIREvaluationModel evaluationModel) throws IOException;
	public void stepXmlLogEvolution(CBIREvaluationModel evaluationModel) throws IOException, ParserConfigurationException, TransformerException;
	public void stepXmlLogTraining(CBIREvaluationModel evaluationModel) throws IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException;
}
