package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_GetSentenceSegmentToOutputFile_3_3 {

	public static void main(String[] args) throws IOException, ParseException {
		String inputFilePath = "/home/chanwit/Documents/LSSentenceSegment/C.Coding_Implement/Input_String/ZH-Test.txt";
		String outputFilePath = "/home/chanwit/Documents/LSSentenceSegment/D.Test/Output/LageTest_02-Output_1.txt";
		String languageid = "en";
//		String encryptLine = "\r\n :::MARKER::: \r\n";
//		String delimiter = ",";
		
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		long beginTime = System.currentTimeMillis();
		
		String sts = segment.getSentenceSegmentToOutputFile(inputFilePath, outputFilePath, languageid);
		System.out.println("Status: "+sts);
		
		long endTime = System.currentTimeMillis();
        System.out.println("Used time: " + (endTime - beginTime) + " millisec");
		
		showUsedMemory();
		

	}
	
	private static void showUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used Memory: " + (usedMemory / 1024) + " kb");
    }
}
