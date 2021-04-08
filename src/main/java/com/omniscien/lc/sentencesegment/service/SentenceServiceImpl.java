package com.omniscien.lc.sentencesegment.service;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;

import java.util.Scanner;


import org.apache.commons.cli.ParseException;

import org.springframework.stereotype.Service;

import com.omniscien.lc.sentencesegment.loomchild.LoomchildService;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;
import com.omniscien.lc.sentencesegment.util.Common;


import net.loomchild.segment.srx.SrxDocument;


@Service
public class SentenceServiceImpl implements SentenceService{
	
	


	

	private SentenceSegment sentenceSegment;
	private LoomchildService lm;
	



	@Override
	public String getSentenceSegmentResult(String jobid, String input, String languageid, String chunkno,
			String encryptline, String delimiter, SrxDocument srxDocument) throws FileNotFoundException, IOException, ParseException {
		
		//Create SRX Document
		if(srxDocument == null) {
			Common common = new Common();
			srxDocument = common.createSrxDocument(languageid);
		}

		sentenceSegment = new SentenceSegment();
		lm = new LoomchildService();
		
		sentenceSegment.setChunkno(chunkno);
		sentenceSegment.setEncryptline(encryptline);
		sentenceSegment.setInput(input);
		sentenceSegment.setJobid(jobid);
		sentenceSegment.setLanguageid(languageid);
		sentenceSegment.setDelimiter(delimiter);
		sentenceSegment.setSrxDocument(srxDocument);
		
				
		return lm.getSentenceSegmentResult(sentenceSegment);
	}

	@Override
	public String getSentenceSegmentResult(String input, String languageid) throws FileNotFoundException, IOException, ParseException {			
		//prepare jobid, chunkno, encryptline and delimitor
		String jobid = new String(), chunkno = new String(), encryptline = new String(), delimiter = new String();
		
		if(input == null || input.equals("")) {
			return "Parameter ①: input is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		return getSentenceSegmentResult(jobid, input, languageid, chunkno, encryptline, delimiter, null);
	}

	@Override
	public String getSentenceSegmentResult(String input, String languageid, String encryptline) throws FileNotFoundException, IOException, ParseException {
		String jobid = new String(), chunkno = new String(), delimiter = new String();
		
		
		if(input == null || input.equals("")) {
			return "Parameter ①: input is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ③: encryptline is required.";
		}

		return getSentenceSegmentResult(jobid, input, languageid, chunkno, encryptline, delimiter, null);
	}

	@Override
	public String getSentenceSegmentResult(String input, String languageid, String encryptline, String delimiter) throws FileNotFoundException, IOException, ParseException {
		String jobid = new String(), chunkno = new String();
		
		if(input == null || input.equals("")) {
			return "Parameter ①: input is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ③: encryptline is required.";
		}
		if(delimiter == null) {
			return "Parameter ④: delimiter is required.";
		}

		return getSentenceSegmentResult(jobid, input, languageid, chunkno, encryptline, delimiter, null);
	}

	@Override
	public String getSentenceSegmentOfInputFile(String inputFilePath, String languageid) throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: inputFilePath is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		//Prepare empty variable
		String jobid = new String();
		String chunkno = new String();
		String encryptline = new String();
		String delimiter = new String();
		
		//Prepare output result variable
		String output = new String();
		
		//Read file
		File file = new File(inputFilePath);
		Scanner reader = new Scanner(file);
		
		//Prepare output buffer
		StringBuffer outputBuf = new StringBuffer();
		
		//Create Srx Document
		Common common = new Common();
		SrxDocument srxDocument = common.createSrxDocument(languageid);
		
		while(reader.hasNextLine()) {
			String outputTmp = getSentenceSegmentResult(jobid, reader.nextLine(), languageid, chunkno, encryptline, delimiter, srxDocument);
			
			
			outputBuf = outputBuf.append(outputTmp).append("\r\n");
		}
		output = new String(outputBuf);
		
		return output;
	}

	@Override
	public String getSentenceSegmentOfInputFile(String inputFilePath, String languageid, String encryptline)
			throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: input is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ③: encryptline is required.";
		}
		
		String jobid = new String();
		String chunkno = new String();
		String delimiter = new String();
		
		// Prepare output result variable
		String output = new String();

		// Read file
		File file = new File(inputFilePath);
		Scanner reader = new Scanner(file);

		StringBuffer outputBuf = new StringBuffer();
		Common common = new Common();
		SrxDocument srxDocument = common.createSrxDocument(languageid);

		while (reader.hasNextLine()) {
			String outputTmp = getSentenceSegmentResult(jobid, reader.nextLine(), languageid, chunkno, encryptline, delimiter, srxDocument);
			outputBuf = outputBuf.append(outputTmp).append(encryptline);
		}
		output = new String(outputBuf);

		return output;
	}

	@Override
	public String getSentenceSegmentOfInputFile(String inputFilePath, String languageid, String encryptline,
			String delimiter) throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: inputFilePath is required.";
		}
		if(languageid == null || languageid.equals("")) {
			return "Parameter ②: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ③: encryptline is required.";
		}
		if(delimiter == null) {
			return "Parameter ④: delimiter is required.";
		}

		
		String jobid = new String();
		String chunkno = new String();
		
		//Prepare output result variable
		String output = new String();
		
		//Read file
		File file = new File(inputFilePath);
		Scanner reader = new Scanner(file);
		
		StringBuffer outputBuf = new StringBuffer();
		Common common = new Common();
		SrxDocument srxDocument = common.createSrxDocument(languageid);
		
		while(reader.hasNextLine()) {
			String outputTmp = getSentenceSegmentResult(jobid, reader.nextLine(), languageid, chunkno, encryptline, delimiter, srxDocument);
			outputBuf = outputBuf.append(outputTmp).append(encryptline);
		}
		output = new String(outputBuf);
		
		return output;
	}

	@Override
	public String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid) throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: inputFilePath is required.";
		}
		if(outputFilePath == null || outputFilePath.equals("")) {
			return "Parameter ②: outputFilePath is required.";
		}
		
		if(languageid == null || languageid.equals("")) {
			return "Parameter ③: languageid is required.";
		}

		String encryptline = "\r\n";
		String delimiter = new String();
		
		return getSentenceSegmentToOutputFile(inputFilePath, outputFilePath, languageid, encryptline, delimiter);
		
	}

	@Override
	public String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid,
			String encryptline) throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: inputFilePath is required.";
		}
		if(outputFilePath == null || outputFilePath.equals("")) {
			return "Parameter ②: outputFilePath is required.";
		}
		
		if(languageid == null || languageid.equals("")) {
			return "Parameter ③: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ④: encryptline is required.";
		}
		
		String delimiter = new String();
		return getSentenceSegmentToOutputFile(inputFilePath, outputFilePath, languageid, encryptline, delimiter);
		
		
	}

	@Override
	public String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid,
			String encryptline, String delimiter) throws IOException, ParseException {
		
		if(inputFilePath == null || inputFilePath.equals("")) {
			return "Parameter ①: inputFilePath is required.";
		}
		if(outputFilePath == null || outputFilePath.equals("")) {
			return "Parameter ②: outputFilePath is required.";
		}
		
		if(languageid == null || languageid.equals("")) {
			return "Parameter ③: languageid is required.";
		}
		
		if(encryptline == null) {
			return "Parameter ④: encryptline is required.";
		}
		
		if(delimiter == null) {
			return "Parameter ⑤: delimiter is required.";
		}
		
		System.out.println("Start process "+inputFilePath);
		
		Common common = new Common();
		
		long beginTime = System.currentTimeMillis();
		
		//Get Total line
		BufferedReader readerForTotalLine = new BufferedReader(new FileReader(inputFilePath));
		int totalLines = 0;
		while (readerForTotalLine.readLine() != null) totalLines++;
		readerForTotalLine.close();
		
		long endTime = System.currentTimeMillis();
		
        System.out.println("Used time: " + (endTime - beginTime) + " millisec");		
		System.out.println("Total line input: "+totalLines);
		
		//Prepare progress every 10%
		double tenPD = totalLines*0.1;
		double twentyPD = totalLines*0.2;
		double thirtyPD = totalLines*0.3;
		double fortytyPD = totalLines*0.4;
		double fiftyPD = totalLines*0.5;
		double sixtyPD = totalLines*0.6;
		double seventyPD = totalLines*0.7;
		double eightyPD = totalLines*0.8;
		double ninetyPD = totalLines*0.9;
		double compltePD = totalLines;
		
		//Set target progress as integer every 10%
		int tenPT = (int)Math.round(tenPD);
		int twentyPT = (int)Math.round(twentyPD);
		int thirtyPT = (int)Math.round(thirtyPD);
		int fortytyPT = (int)Math.round(fortytyPD);
		int fiftyPT = (int)Math.round(fiftyPD);
		int sixtyPT = (int)Math.round(sixtyPD);
		int seventyPT = (int)Math.round(seventyPD);
		int eightyPT = (int)Math.round(eightyPD);
		int ninetyPT = (int)Math.round(ninetyPD);
		int compltePT = (int)Math.round(compltePD);
		
		//Read file		
		FileInputStream fstream = new FileInputStream(inputFilePath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		//Output file
		File outputFile = new File(outputFilePath);		
		FileWriter writer = new FileWriter(outputFile, true);		
		BufferedWriter bw = new BufferedWriter(writer);	
		
		//Create SrxDocument
		SrxDocument srxDocument = common.createSrxDocument(languageid);
		
		//Prepare empty variable
		String jobid = new String();		
		String chunkno = new String();
		
		int progress = 0;

		String strLine;
		while((strLine = br.readLine()) != null) {
			//count progress
			progress++;
			
			//Get output
			String outputTmp = getSentenceSegmentResult(jobid, strLine, languageid, chunkno, encryptline, delimiter, srxDocument);

			//write file
			bw.write(outputTmp);
			bw.write(encryptline);
			
			//Display log every 10% completed
			if(progress == 1) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 0% Completed.");
			}			
			if(progress == tenPT) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 10% Completed. at line: "+progress);
			}
			if(progress == twentyPT) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 20% Completed. at line: "+progress);
			}
			if(progress == thirtyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 30% Completed. at line: "+progress);
			}
			if(progress == fortytyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 40% Completed. at line: "+progress);
			}
			if(progress == fiftyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 50% Completed. at line: "+progress);
			}
			if(progress == sixtyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 60% Completed. at line: "+progress);
			}
			if(progress == seventyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 70% Completed. at line: "+progress);
			}
			if(progress == eightyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 80% Completed. at line: "+progress);
			}
			if(progress == ninetyPT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 90% Completed. at line: "+progress);
			}
			if(progress == compltePT ) {
				String currentDate = common.getCurrentDate();
				System.out.println(currentDate+"::The progress status: 100% Process Completed.");
			}
		}
		bw.close();
		in.close();
		
		return "::: Sentence Segment Process Finished :::";
	}
	
}
