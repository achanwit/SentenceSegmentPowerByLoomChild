package com.omniscien.lc.sentencesegment.model;

import net.loomchild.segment.srx.SrxDocument;

public class SentenceSegment {


	private String jobid;
	private String input;
	private String output;
	private String languageid;
	private String chunkno;
	private String delimiter;
	private SrxDocument srxDocument;
	
	
	public SrxDocument getSrxDocument() {
		return srxDocument;
	}
	public void setSrxDocument(SrxDocument srxDocument) {
		this.srxDocument = srxDocument;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	private String encryptline;
	
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getLanguageid() {
		return languageid;
	}
	public void setLanguageid(String languageid) {
		this.languageid = languageid;
	}
	public String getChunkno() {
		return chunkno;
	}
	public void setChunkno(String chunkno) {
		this.chunkno = chunkno;
	}
	public String getEncryptline() {
		return encryptline;
	}
	public void setEncryptline(String encryptline) {
		this.encryptline = encryptline;
	}
	
	

}
