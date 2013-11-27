package de.htw.iconn.rbm.enhancements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.htw.cbir.CBIREvaluationModel;
import de.htw.iconn.rbm.IRBM;

public class CSVSettingsLogger {

	
	private String headLine;
	private String dateString;
	private String baseFolder;

	
	
	public CSVSettingsLogger(){

		this.dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());		
		this.headLine = "RBM;evaluationType;inputSize;outputSize;includingBias;learnRate;epochs;logisticFunction;seed;useSeed;error;mAP;imageSetSize;date";
		
		this.baseFolder = "RBMLogs";
		Path baseFolderPath = FileSystems.getDefault().getPath(this.baseFolder);	
		try {
			if(Files.notExists(baseFolderPath, LinkOption.NOFOLLOW_LINKS)){
				Files.createDirectory(baseFolderPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String evaluationDataToString(CBIREvaluationModel evaluationModel, IRBM rbm){
		if(dateString == null){
			dateString = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		}
		String logLine = 
		this.getRbmName(rbm) + ";" + 
		evaluationModel.getEvaluationType().toString() + ";" +
		rbm.getInputSize() + ";" +  
		rbm.getOutputSize() + ";" + 
		this.includingBias(rbm) + ";" + 
		rbm.getLearnRate() + ";" +
		evaluationModel.getEpochs() + ";" +
		this.getLogisticFunctionName(rbm) +";" +
		evaluationModel.getSeed() + ";" + 
		this.useSeed(evaluationModel.getUseSeed()) + ";" +
		evaluationModel.getError() + ";" +
		evaluationModel.getMAP() + ";" +
		evaluationModel.getImageSetSize() + ";" +
		dateString;
		
		return logLine;
	}
	
	public void finalCsvLog(CBIREvaluationModel evaluationModel, IRBM rbm) throws IOException{

		String logLine = evaluationDataToString(evaluationModel, rbm);
		System.out.println(logLine);
		this.dateString = null;
		
		
		String csvLocation = "logs.csv";		
		Path csvLocationPath = FileSystems.getDefault().getPath(this.baseFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		if(Files.notExists(csvLocationPath, LinkOption.NOFOLLOW_LINKS)){
			csvFile = new File(this.baseFolder + "/" + csvLocation);
			csvFile.createNewFile();
			csvExistent = false;
		}
		
		BufferedWriter output = new BufferedWriter(new FileWriter(this.baseFolder + "/" + csvLocation, true));
		if(!csvExistent){
			output.append(headLine);
			output.newLine();
		}
		output.append(logLine);
		output.newLine();
		output.flush();
		output.close();
		
		evaluationModel.reset();
	}
	
	public void stepCsvLog(CBIREvaluationModel evaluationModel, IRBM rbm) throws IOException {
		String logLine = evaluationDataToString(evaluationModel, rbm);
		System.out.println(logLine);
		
		String csvFolder = this.baseFolder + "/CSVSteps";
		String csvLocation = this.dateString + ".csv";
		
		Path csvFolderPath = FileSystems.getDefault().getPath(csvFolder);
		Path csvLocationPath = FileSystems.getDefault().getPath(csvFolder, csvLocation);
		
		File csvFile;
		boolean csvExistent = true;
		
		if(Files.notExists(csvFolderPath, LinkOption.NOFOLLOW_LINKS)){
			Files.createDirectory(csvFolderPath);
		}
		if(Files.notExists(csvLocationPath, LinkOption.NOFOLLOW_LINKS)){
			csvFile = new File(csvFolder + "/" + csvLocation);
			csvFile.createNewFile();
			csvExistent = false;
		}
		
		BufferedWriter output = new BufferedWriter(new FileWriter(csvFolder + "/" + csvLocation, true));
		if(!csvExistent){
			output.append(headLine);
			output.newLine();
		}
		output.append(logLine);
		output.newLine();
		output.flush();
		output.close();		
	}

	
	public String getLogisticFunctionName(IRBM rbm){
		if(rbm.getLogisticFunction() != null){
			return rbm.getLogisticFunction().getClass().getSimpleName();
		}else{
			return "NA";
		}
	}
	
	private String getRbmName(IRBM rbm){
		return rbm.getClass().getSimpleName();
	}

	
	private String includingBias(IRBM rbm){
		if(rbm.hasBias()){
			return "yes";
		}else{
			return "no";
		}
	}
	
	private String useSeed(boolean hasSeed){
		if(hasSeed){
			return "yes";
		}else{
			return "no";
		}
	}
}
