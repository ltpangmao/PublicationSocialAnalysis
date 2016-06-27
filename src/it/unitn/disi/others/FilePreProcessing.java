package it.unitn.disi.others;

import it.unitn.disi.author.Author;
import it.unitn.disi.author.Record;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePreProcessing {

	public static void main(String[] args) throws IOException {

		// input stream
		BufferedReader br = null;
		String line;
		br = new BufferedReader(new FileReader("txt_author/title_author.csv"));
		
//		int i = 0;
		int mid = 0; //record the middle position of the processed string
		String current_paper = "";
		String paper_title = ""; // currently processed paper
		String author_name = ""; // currently processed paper author
		String authors_per_paper = ""; // all authors of one paper
		String output_author_text = ""; // the final text that should be output
		while ((line = br.readLine()) != null) {
			// remove empty lines
			if (!line.equals("")) {
				// processing example:  Designing data marts for data warehouses,"Bonifati, A."
				mid = line.indexOf(",\"");
				if(mid>-1){ // ensure the processed string aligns with the assumed format
					// obtain current paper title
					paper_title = line.substring(0, mid);
					// obtain current paper author
					author_name = line.substring(mid+2, line.length()-1);
					// replace strange characters
					char[] temp_author_name = author_name.toCharArray();
					author_name = "";
					for(char temp_cha: temp_author_name){
						 int i =(int)temp_cha;   
						 if((i>=65&&i<=90)||(i>=97&&i<=122)||(temp_cha=='.')||(temp_cha==' ')||(temp_cha==','))   
						 {   
							 // use original character
							 author_name += temp_cha;   
						 }
						 else{
							 // replace it
							 author_name += "_";
						 }
					}
					// check whether add the author or create a new entry
					if(paper_title.equals(current_paper)){
						// incrementally add authors
						authors_per_paper += "; " + author_name;
					}
					else{
						// add the current paper author information to the output file
						output_author_text += authors_per_paper + "\n";
						// create a new one
						current_paper = paper_title;
						authors_per_paper = author_name;
					}
				}
			}
		}
		br.close();
		
		// output all the processed author information
		String output = "txt_author/processed_authors.txt";
		PrintWriter writer = new PrintWriter(output, "UTF-8");
		writer.println(output_author_text);
		writer.close();
	}

}
