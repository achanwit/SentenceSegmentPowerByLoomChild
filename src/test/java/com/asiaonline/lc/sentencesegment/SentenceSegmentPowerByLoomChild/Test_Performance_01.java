package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;

import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_Performance_01 {

	public static void main(String[] args) throws ParseException, IOException {
		
		String inputFolder = "/home/chanwit/Documents/LSSentenceSegment/C.Coding_Implement/Input_String/performance";
		
		String outputFolder = "/home/chanwit/Documents/LSSentenceSegment/D.Test/Output/performance";
		String languageid = "en";
		
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 1+i;
			
			String inputFilePath = inputFolder+"/"+listOfFiles[i].getName();
			java.nio.file.Path path = Paths.get(inputFilePath);

			  long bytes = Files.size(path);
			  System.out.println("File size: "+bytes+" byte");
			String outputFilePath = outputFolder+"/"+listOfFiles[i].getName();
			
			long beginTime = System.currentTimeMillis();
			
			try {
				segment.getSentenceSegmentToOutputFile(inputFilePath, outputFilePath, languageid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long endTime = System.currentTimeMillis();
	        System.out.println("Used time: " + (endTime - beginTime) + " millisec");
			
			showUsedMemory();
		}
	}
	
	private static void showUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used Memory: " + (usedMemory / 1024) + " kb");
    }

}
