package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_GetSentenceSegmentToOutputFile_3_1 {

	public static void main(String[] args) throws IOException, ParseException {
		String inputFilePath = "/home/chanwit/Documents/LSSentenceSegment/C.Coding_Implement/Input_String/ZH-Test.txt";
		String outputFilePath = "/home/chanwit/Documents/LSSentenceSegment/D.Test/Output/ZH-Output.txt";
		String languageid = "zh";

		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		segment.getSentenceSegmentToOutputFile(inputFilePath, outputFilePath, languageid);
		

	}
}
