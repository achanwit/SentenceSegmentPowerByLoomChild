package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.service.SentenceServiceImpl;



public class Test_GetSentenceSegmentResult_1_1 {

	public static void main(String[] args) throws IOException, ParseException {
		String input = "Ok! Ok! Buka saja ia!";
		String languageid = "MS";
		
		SentenceServiceImpl segment = new SentenceServiceImpl();
		
		String result = segment.getSentenceSegmentResult(input, languageid);
		
		System.out.println("Input: \r\n"+input);
		System.out.println("\r\n");
		System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
		System.out.println("Output: \r\n"+result);

	}
}
