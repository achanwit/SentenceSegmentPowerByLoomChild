package com.omniscien.lc.sentencesegment.util;

import static net.loomchild.segment.util.Util.getFileInputStream;
import static net.loomchild.segment.util.Util.getReader;
import static net.loomchild.segment.util.Util.getResourceStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;



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

public class Common {
	
	private static final String PREFIX_SRX_PROP = "srx.";
	private static final String DEFAULT_SRX = "/ner/config/LSSentenceSegment/srxFile/default.srx";
	private static final String SRX_PROPERTIES = "/ner/config/LSSentenceSegment/srx_properties/srx.properties";
	private static final String Option_Custom = "-c";
	private static final String Option_Language = "-l";
	private static final String Option_SRX = "-s";


	public static final int WORD_LENGTH = 2;
	private Random random;
	
	private enum Parser {
		jaxb, sax, stax;
	}
	
	CommandLineParser parser = new PosixParser();
	private  SrxDocument srxDocument = null;
	
	public Common() {
		
	}

	/*** Common method for get current date ***/
	public String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = df.format(c.getTime());
		
		return currentDate;
	}
	
	public Options createOptions() {
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
	
//	public SrxDocument createSrxDocument(CommandLine commandLine,
//			boolean profile) throws IOException {
	public SrxDocument createSrxDocument(String languageid) throws IOException, ParseException {
		SrxDocument document;
		Options options = createOptions();
		
		//SRX Management
				FileReader reader = new FileReader(SRX_PROPERTIES);
				Properties p = new Properties();
				p.load(reader);		
				//Set Properties of SRX file follow langID input
				languageid = languageid.toLowerCase().trim().substring(0,2);
				String srfValueProp = PREFIX_SRX_PROP+languageid;
				String srxFileName = p.getProperty(srfValueProp);
				if(srxFileName == null) {
					srxFileName = DEFAULT_SRX;
				}
		String[] arguments = {Option_Custom, Option_Language, languageid, Option_SRX, srxFileName};
		CommandLine commandLine = null;
		commandLine = parser.parse(options, arguments);	

		if (commandLine.hasOption("generate-srx")) {
			document = generateSrxDocument(commandLine, false);
		} else {
			document = readSrxDocument(commandLine, false);
		}

		return document;
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
