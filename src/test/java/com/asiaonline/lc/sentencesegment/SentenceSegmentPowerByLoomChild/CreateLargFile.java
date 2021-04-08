package com.asiaonline.lc.sentencesegment.SentenceSegmentPowerByLoomChild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.function.PassthruPathFunction;

public class CreateLargFile {
	private static List<String> listInput = new ArrayList<String>();
	public static void main(String[] args) throws IOException {
		try {
		      
		      FileWriter myWriter = new FileWriter("/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input/InputTest_Large.txt",true);
		      java.nio.file.Path path = Paths.get("/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input/InputTest_Large.txt");
		      
		 
		    	  File myObj = new File("/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input1Gig/InputTest.txt");
		    	  Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  listInput.add(myReader.nextLine());  
		    	  listInput.add("\n");
		      }
		      myReader.close();
		      
		 for(int i = 0; i < listInput.size(); i++) {
			 if(i == listInput.size()-1) {
				 i = 0;
			 }
			 myWriter.write(listInput.get(i));
			
			  long bytes = Files.size(path);
			  System.out.println("Byte run: "+bytes);
	    	  if(bytes > 1073741824 ) {
	    		         
	    		  break;
	    	  }
		 }
		 myWriter.close();
		      
		      
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }

	}

}
