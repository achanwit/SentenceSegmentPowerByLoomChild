package com.omniscien.lc.sentencesegment.loomchild;

import static net.loomchild.segment.util.Util.getFileInputStream;
import static net.loomchild.segment.util.Util.getReader;
import static net.loomchild.segment.util.Util.getResourceStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.omniscien.lc.sentencesegment.model.SentenceSegment;

import net.loomchild.segment.TextIterator;
import net.loomchild.segment.srx.LanguageRule;
import net.loomchild.segment.srx.Rule;
import net.loomchild.segment.srx.SrxDocument;
import net.loomchild.segment.srx.SrxParser;
import net.loomchild.segment.srx.SrxTextIterator;
import net.loomchild.segment.srx.SrxTransformer;
import net.loomchild.segment.srx.io.Srx1Transformer;
import net.loomchild.segment.srx.io.Srx2Parser;
import net.loomchild.segment.srx.io.Srx2SaxParser;
import net.loomchild.segment.srx.io.Srx2StaxParser;
import net.loomchild.segment.srx.io.SrxAnyParser;
import net.loomchild.segment.srx.io.SrxAnyTransformer;
import net.loomchild.segment.srx.legacy.AccurateSrxTextIterator;
import net.loomchild.segment.srx.legacy.FastTextIterator;
import net.loomchild.segment.srx.legacy.ScannerSrxTextIterator;

public class LoomchildService {
	
	public static final int WORD_LENGTH = 2;
	private enum Algorithm {
		accurate, fast, ultimate, scanner;
	}

	private enum Parser {
		jaxb, sax, stax;
	}
	
	private Random random;
	private String text;
	
	
	//Static value
	private static final String PREFIX_SRX_PROP = "srx.";
	private static final String DEFAULT_SRX = "/ner/config/LSSentenceSegment/srxFile/default.srx";
	private static final String SRX_PROPERTIES = "/ner/config/LSSentenceSegment/srx_properties/srx.properties";
	private static final String Option_Custom = "-c";
	private static final String Option_Language = "-l";
	private static final String Option_SRX = "-s";
	
	//Item use to call loomchild
	private String srxFileName = new String();
	private String languageID = new String();
	
	//Item in SentenceSegment will return
	private String jobidRequest = new String();
	private String inputRequest = new String();
	private String outputReturn = new String();
	private String languageidRequest = new String();
	private String chunknoRequest = new String();
	private String encryptlineRequest = new String(); 
	private String delimeter = new String();
	private List<String> inputList = new ArrayList<String>();
	private List<String> outputList = new ArrayList<String>();
	
	//Object will return
	public SentenceSegment sentenceSegmentLM = null;
	
	
	
	//Constructor
	public LoomchildService() {
		
	}
	
	//Get Sentence Segment Result
	public String getSentenceSegmentResult(SentenceSegment sentenceSegment) throws IOException {
		//log.info("::: Start Loomchild Sentence Segment Service Process :::");
		
		Options options = createOptions();
		
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();
		CommandLine commandLine = null;
		FileReader reader = new FileReader(SRX_PROPERTIES);
		Properties p = new Properties();
		p.load(reader);
		
		SrxDocument srxDocument = sentenceSegment.getSrxDocument();
		//Set input from Request
		inputRequest = sentenceSegment.getInput();
		
		//Set Language code/id
		languageID = sentenceSegment.getLanguageid().toLowerCase().trim().substring(0, 2);
		
		//Set encrypt line
		if(sentenceSegment.getEncryptline().equals("")){
			encryptlineRequest = "\r\n";
		}else {
			encryptlineRequest = sentenceSegment.getEncryptline();
		}
		
		
		//Set delimetor
		delimeter = sentenceSegment.getDelimiter();
		
		//Slipt input to List by delimiter
		String[] inputArr = null;
		if(!delimeter.equals("")) {
			inputArr = inputRequest.split("\\"+delimeter);
		}
		if(inputArr != null) {
			inputList = Arrays.asList(inputArr);
		}else {
			inputList.add(inputRequest);
		}		
		
		//Set Properties of SRX file follow langID input
		String srfValueProp = PREFIX_SRX_PROP+languageID;

		//Set ser file name
		srxFileName = p.getProperty(srfValueProp);
		if(srxFileName == null) {
			srxFileName = DEFAULT_SRX;
		}
	
		try {
			String[] arguments = {Option_Custom, Option_Language, languageID, Option_SRX, srxFileName};
			commandLine = parser.parse(options, arguments);		
			for(int i=0; i<inputList.size(); i++ ) {
				sentenceSegment(commandLine, inputList.get(i), srxDocument);
			}
			StringBuffer outputBuf = new StringBuffer();
			if(outputList.size() > 0) {
				
				for(int i=0; i< outputList.size(); i++) {
					if(i == outputList.size()-1) {
						outputBuf = outputBuf.append(outputList.get(i));
					}else {
						outputBuf = outputBuf.append(outputList.get(i)).append(encryptlineRequest);
					}
					
				}
			}
			
			outputReturn = new String(outputBuf);
			

		} catch (ParseException e) {
			printUsage(helpFormatter);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}		
		
		return outputReturn;
	}
	
	private Options createOptions() {
		Options options = new Options();		
		options.addOption("s", "srx", true, "SRX file.");
		options.addOption("l", "language", true, "Language code.");
		options.addOption("m", "map", true, "Map rule name in SRX 1.0.");
		options.addOption("b", "begin", true, "Output segment prefix.");
		options.addOption("e", "end", true, "Output segment suffix.");
		options.addOption("a", "algorithm", true, "Algorithm. Can be accurate, fast or ultimate (default).");
		options.addOption("d", "parser", true, "Parser. Can be sax, stax or jaxb (default).");
		options.addOption("i", "input", true, "Use given input file instead of standard input.");
		options.addOption("o", "output", true, "Use given output file instead of standard output.");
		options.addOption("t", "transform", false, "Convert old SRX to current version.");
		options.addOption("p", "profile", false, "Print profile information.");
		options.addOption("r", "preload", false, "Preload document into memory before segmentation.");
		options.addOption("2", "twice", false, "Repeat the whole process twice.");
		options.addOption("z", "test", false, "Test the application by running a test suite.");
		options.addOption(null, "lookbehind", true, "Maximum length of a regular expression construct that occurs in lookbehind. Default: " + SrxTextIterator.DEFAULT_MAX_LOOKBEHIND_CONSTRUCT_LENGTH + ".");
		options.addOption(null, "buffer-length", true, "Length of a buffer when reading text as a stream. Default: " + SrxTextIterator.DEFAULT_BUFFER_LENGTH + ".");
		options.addOption(null, "margin", true, "If rule is matched but its position is in the margin (position > bufferLength - margin) then the matching is ignored. Default " + SrxTextIterator.DEFAULT_MARGIN + ".");
		options.addOption(null, "generate-text", true, "Generate random input with given length in KB.");
		options.addOption(null, "generate-srx", true, "Generate random segmentation rules with given rule count and rule length separated by a comma.");
		options.addOption("h", "help", false, "Print this help.");
		options.addOption("c", "custom", false, "Custom sentence segmenter");
    
		return options;
	}
	
	private void printUsage(HelpFormatter helpFormatter) {
		System.out.println("Unknown command. Use segment -h for help.");
	}
	
	private void sentenceSegment(CommandLine commandLine, String inputRequest, SrxDocument srxDocument) {
	   
		Reader inputString = new StringReader(inputRequest);
	    BufferedReader br = new BufferedReader(inputString); 	  
	  
	    try {
//	    	if(srxDocument == null) {
//	    		srxDocument = createSrxDocument(commandLine, false);
//	    	}
	      String sent = null;
	      while ((sent = br.readLine()) != null){
	        //String sent = br.readLine();
	        if (!"".equals(sent)) {
	          this.text = sent;
	          ArrayList<String> segments = doSentenceSegment(commandLine, srxDocument, inputString);
	          for(int i=0; i< segments.size(); i++) {
	        	  outputList.add(segments.get(i));
	          }
	          String segments_json = new Gson().toJson(segments);        
	        }
	      }
	     
	    }catch (IOException ex) {
	    	System.out.println("Error: "+ex);
	    	//log.error(LoomchildService.class.getName());
//	      Logger.getLogger(Segment.class.getName()).log(Level.SEVERE, null, ex);
	    }  
	  }
	
	  private ArrayList doSentenceSegment(CommandLine commandLine, SrxDocument document, Reader reader) throws IOException{
		    TextIterator textIterator = createTextIterator(commandLine, document, reader, false);
		    ArrayList<String> segments = new ArrayList<String>();
		    while (textIterator.hasNext()) {
		      String segment = textIterator.next();
		      segment = segment.trim();
		      if (!segment.equals("")){
		        segments.add(segment);
		      }
		    }
		    return segments;
		  }
	
	private SrxDocument createSrxDocument(CommandLine commandLine,
			boolean profile) throws IOException {
		SrxDocument document;

		if (commandLine.hasOption("generate-srx")) {
			document = generateSrxDocument(commandLine, profile);
		} else {
			document = readSrxDocument(commandLine, profile);
		}

		return document;
	}
	
	private SrxDocument generateSrxDocument(CommandLine commandLine, boolean profile) {
		if (profile) {
			System.out.print("Generating rules... ");
		}

		long start = System.currentTimeMillis();

		String generateSrxOption = commandLine.getOptionValue("generate-srx");
		String[] parts = generateSrxOption.split(",");
		if (parts.length != 2) {
			throw new RuntimeException("Cannot parse rule count and length.");
		}
		int ruleCount = Integer.parseInt(parts[0]);
		if (ruleCount < 0) {
			throw new RuntimeException("Rule count must be positive: " + ruleCount + ".");
		}
		int ruleLength = Integer.parseInt(parts[1]);
		if (ruleLength < 1) {
			throw new RuntimeException("Rule length must be greater or equal to one: " + ruleCount + ".");
		}

		SrxDocument srxDocument = new SrxDocument();
		LanguageRule languageRule = generateLanguageRule(ruleCount, ruleLength);
		srxDocument.addLanguageMap(".*", languageRule);

		if (profile) {
			System.out.println(System.currentTimeMillis() - start + " ms.");
		}

		return srxDocument;
	}
	
	private SrxDocument readSrxDocument(CommandLine commandLine, boolean profile)
			throws IOException {

		if (profile) {
			System.out.print("Reading rules... ");
		}

		long start = System.currentTimeMillis();

		Reader srxReader;

		String fileName = commandLine.getOptionValue('s');
		if (fileName != null) {
			srxReader = getReader(getFileInputStream(fileName.trim()));
		} else {
			srxReader = getReader(getResourceStream(DEFAULT_SRX));
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		String mapRule = commandLine.getOptionValue('m');
		if (mapRule != null) {
			parameterMap.put(Srx1Transformer.MAP_RULE_NAME, mapRule);
		}

		// If there are transformation parameters then separate transformation
		// is needed.
		if (parameterMap.size() > 0) {
			SrxTransformer transformer = new SrxAnyTransformer();
			srxReader = transformer.transform(srxReader, parameterMap);
		}

		SrxParser srxParser = createParser(commandLine, profile);

		SrxDocument document = srxParser.parse(srxReader);
		srxReader.close();

		if (profile) {
			System.out.println(System.currentTimeMillis() - start + " ms.");
		}

		return document;
	}
	
	private SrxParser createParser(CommandLine commandLine, boolean profile) {
		Parser parser = Parser.jaxb;
		String parserString = commandLine.getOptionValue('d');
		if (parserString != null) {
			parser = Parser.valueOf(parserString);
		}

		SrxParser srxParser;
		switch (parser) {
		case jaxb:
			srxParser = new Srx2Parser();
			break;
		case sax:
			srxParser = new Srx2SaxParser();
			break;
		case stax:
			srxParser = new Srx2StaxParser();
			break;
		default:
			throw new IllegalArgumentException("Unknown parser: " + parser + ".");
		}

		srxParser = new SrxAnyParser(srxParser);

		return srxParser;
	}
	
	private TextIterator createTextIterator(CommandLine commandLine,
			SrxDocument document, Reader reader, boolean profile) {

		TextIterator textIterator;

		String languageCode = commandLine.getOptionValue('l');
		if (languageCode == null) {
			languageCode = "";
		}

		String algorithmString = commandLine.getOptionValue('a');
		Algorithm algorithm = Algorithm.ultimate;
		if (algorithmString != null) {
			algorithm = Algorithm.valueOf(algorithmString);
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		if (commandLine.hasOption("lookbehind")) {
			if (algorithm != Algorithm.ultimate && algorithm != Algorithm.fast) {
				throw new IllegalArgumentException("--lookbehind parameter can be only used with ultimate or fast algorithm.");
			}
			parameterMap.put(
					SrxTextIterator.MAX_LOOKBEHIND_CONSTRUCT_LENGTH_PARAMETER,
					Integer.parseInt(commandLine.getOptionValue("lookbehind")));
		}
		if (commandLine.hasOption("buffer-length")) {
			if (commandLine.hasOption('r')) {
				throw new IllegalArgumentException("--buffer-length can be only used when reading text from a stream (--preload option not allowed).");
			}
			parameterMap.put(
					SrxTextIterator.BUFFER_LENGTH_PARAMETER, 
					Integer.parseInt(commandLine.getOptionValue("buffer-length")));
		}
		if (commandLine.hasOption("margin")) {
			if (algorithm != Algorithm.ultimate) {
				throw new IllegalArgumentException("--margin parameter can be only used with ultimate algorithm.");
			}
			parameterMap.put(
					SrxTextIterator.MARGIN_PARAMETER,
					Integer.parseInt(commandLine.getOptionValue("margin")));
		}

		if (profile) {
			System.out.print("    Creating text iterator... ");
		}

		long start = System.currentTimeMillis();

		if (algorithm == Algorithm.accurate) {
			if (text != null) {
				textIterator = new AccurateSrxTextIterator(document, languageCode, text);
			} else {
				throw new IllegalArgumentException("For accurate algorithm preload option (-r) is mandatory.");
			}
		} else if (algorithm == Algorithm.ultimate) {
			if (text != null) {
				textIterator = new SrxTextIterator(document, languageCode, text, parameterMap);
			} else {
				
				textIterator = new SrxTextIterator(document, languageCode, reader, parameterMap);
				//List<String> myList = IteratorUtils.toList(textIterator);  
				
//				int i =0;
//				for(String sentent : myList) {
//					System.out.println(i+"sentent: "+sentent);
//					i++;
//				}

//					int i =0;
//					while(textIterator.hasNext()){
//						String valueCheckIterator = textIterator.next();
//						System.out.println(i+"valueCheckIterator: "+valueCheckIterator);
//						i++;
//						
//					}
				
			}
		} else if (algorithm == Algorithm.fast) {
			if (text != null) {
				textIterator = new FastTextIterator(document, languageCode, text, parameterMap);
			} else {
				textIterator = new FastTextIterator(document, languageCode, reader, parameterMap);
			}
		} else if (algorithm == Algorithm.scanner) {
			if (text != null) {
				textIterator = new ScannerSrxTextIterator(document, languageCode, text, parameterMap);
			} else {
				textIterator = new ScannerSrxTextIterator(document, languageCode, reader, parameterMap);
			}
		} else {
			throw new IllegalArgumentException("Unknown algorithm: " + algorithm + ".");
		}

		if (profile) {
			System.out.println(System.currentTimeMillis() - start + " ms.");
		}

		return textIterator;
	}
	
	private LanguageRule generateLanguageRule(int ruleCount, int ruleLenght) {
		LanguageRule languageRule = new LanguageRule("");
		// Add rules
		for (int i = 0; i < ruleCount; ++i) {
			Rule rule = generateRule(ruleLenght);
			languageRule.addRule(rule);
		}
		// Add end of sentence rule
		languageRule.addRule(new Rule(true, "\\.", " "));
		return languageRule;
	}
	
	private Rule generateRule(int length) {
		StringBuilder regex = new StringBuilder();
		regex.append('(');
		for (int i = 0; i < length; ++i) {
			String word = generateWord(WORD_LENGTH);
			regex.append(word);
			if (i != length - 1) {
				regex.append('|');
			}
		}
		regex.append(')');
		Rule rule = new Rule(false, regex + "\\.", " ");
		return rule;
	}
	
	private String generateWord(int length) {
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			char character = generateCharacter();
			word.append(character);
		}
		return word.toString();
	}
	
	private char generateCharacter() {
		int character = random.nextInt('Z' - 'A' + 1) + 'A';
		return (char)character;
	}

}
