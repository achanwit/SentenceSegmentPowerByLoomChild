package com.omniscien.lc.sentencesegment.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;

import com.omniscien.lc.sentencesegment.model.SentenceSegment;

import net.loomchild.segment.srx.SrxDocument;

public interface SentenceService {
	
	/*** Get Sentence Segment Result General, use for base all of method by param
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException ***/
	String getSentenceSegmentResult(String jobid, String input, String languageid, String chunkno, String encryptline, String delimiter, SrxDocument srxDocument) throws FileNotFoundException, IOException, ParseException;
	
	
	/*** Get Sentence Segment Result by param
	 * 
	 * @param input
	 * @param languageid
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentResult(String input, String languageid) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Result by param
	 * 
	 * @param input
	 * @param languageid
	 * @param encryptline
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentResult(String input, String languageid, String encryptline) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Result by param
	 * 
	 * @param input
	 * @param languageid
	 * @param encryptline
	 * @param delimiter
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentResult(String input, String languageid, String encryptline, String delimiter) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Of Input File by param
	 * 
	 * @param inputFilePath
	 * @param languageid
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentOfInputFile(String inputFilePath, String languageid) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Of Input File by param
	 * 
	 * @param inputFilePath
	 * @param languageid
	 * @param encryptline
	 * @return
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentOfInputFile(String inputFilePath, String languageid, String encryptline) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Of Input File by param
	 * 
	 * @param inputFilePath
	 * @param languageid
	 * @param encryptline
	 * @param delimiter
	 * @return
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentOfInputFile(String inputFilePath, String languageid, String encryptline, String delimiter) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Of Input file and create output as text file by
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param languageid
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid) throws FileNotFoundException, IOException, ParseException;
	
	/*** Get Sentence Segment Of Input file and create output as text file by
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param languageid
	 * @param encryptline
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid, String encryptline) throws IOException, ParseException;
	
	/*** Get Sentence Segment Of Input file and create output as text file by
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param languageid
	 * @param encryptline
	 * @param delimiter
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	String getSentenceSegmentToOutputFile(String inputFilePath, String outputFilePath, String languageid, String encryptline, String delimiter) throws FileNotFoundException, IOException, ParseException;



	
	

}
