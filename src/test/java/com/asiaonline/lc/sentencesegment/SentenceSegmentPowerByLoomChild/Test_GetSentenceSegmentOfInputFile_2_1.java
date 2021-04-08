package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;

public class Test_GetSentenceSegmentOfInputFile_2_1 {

	public static void main(String[] args) throws IOException, ParseException {
		String inputFilePath = "/home/chanwit/Documents/LSSentenceSegment/D.Test/InputDion/Test-CZ.txt";
		String languageid = "cz";
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		String result = segment.getSentenceSegmentOfInputFile(inputFilePath, languageid);
		
		System.out.println("Output: \r\n"+result);

	}
}
