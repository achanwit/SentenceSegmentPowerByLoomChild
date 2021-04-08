package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_GetSentenceSegmentOfInputFile_2_3 {

	public static void main(String[] args) throws IOException, ParseException {
		String inputFilePath = "/home/chanwit/Documents/LSSentenceSegment/C.Coding_Implement/Input_String/ZH-Test.txt";
		String languageid = "zh";
		String encryptLine = "\r\n :::MARKER::: \r\n";
		String delimiter = ",";
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		String result = segment.getSentenceSegmentOfInputFile(inputFilePath, languageid, encryptLine, delimiter);
		
		System.out.println("Output: \r\n"+result);

	}
}
