package it.unitn.disi.citation;

import it.unitn.disi.author.AuthorRelation;
import it.unitn.disi.author.AuthorSet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CitationAnalysis {
	public int redundancy_difference = 2;
	public int redundancy_length_low = 2;
	public int redundancy_length_high = 3;
	

	public static void main(String[] args) throws IOException {
		LinkedList<Paper> paper_set = new LinkedList<Paper>();
		
		CitationAnalysis ca = new CitationAnalysis();
		
		/****************
		 * Pre-processing on the citation documents
		 ****************/
		//import the set of paper that are to be analyzed
//		paper_set = ca.importPaperTitle("txt_citation/test.txt");
//		paper_set = ca.importPaperTitle("txt_citation/verified_titles.txt");
		
		// check out whether there are redundant papers in the given paper list.
//		ca.checkRedundancy(paper_set);
		
		//analyze the existence of the given paper set against the DBLP data set
//		ca.analyzeGivenPaperSet(paper_set);
		
		//generate a citation documentation from the DBLP dataset 
//		ca.prepareSelectedCitationDocument(paper_set);
		
		
		/****************
		 * Analyze citation documents
		 ****************/
		// import the citation document
		paper_set = ca.importCitationDocument("txt_citation/selected_papers_info.txt");
		
		// calculate the citation relations
		ca.identifyCitations(paper_set);
		
		// visualize the citation information
		ca.drawCitationGraph(paper_set);
	}

	


	


	



	/**
	 * import papers from a particular list 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public LinkedList<Paper> importPaperTitle(String file) throws IOException{
		LinkedList<Paper> papers = new LinkedList<Paper>();
		
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"), 5*1024*1024);  
				
		String line = "";
		while((line = reader.readLine()) != null){
			if(!line.equals("")){
				Paper temp = new Paper();
				temp.title= line;
				papers.add(temp);
			}
		}
//		for (Paper p : papers) {
//			System.out.println(p.title);
//		}
//		System.out.println(papers.size());
		
		return papers;
	}
	
	/**
	 * check redundancy
	 * @param paper_set2
	 */
	public void checkRedundancy(LinkedList<Paper> set) {
		LinkedList<MutablePair<Integer, Integer>> result = new LinkedList<MutablePair<Integer, Integer>>();

		int count_1 = 0, count_2 = 0;
		for (count_1 = 0; count_1 < set.size(); count_1++) {
			Paper paper_1 = set.get(count_1);
			String title = paper_1.title;
			for (count_2 = 0; count_2 < set.size(); count_2++) {
				Paper paper_2 = set.get(count_2);
				String search = paper_2.title;
				if (count_1 != count_2 && findSimilarByWords(title, search, redundancy_difference, redundancy_length_low, redundancy_length_high)) {
					MutablePair<Integer, Integer> pair = new MutablePair<Integer, Integer>();
					pair.left = count_1;
					pair.right = count_2;

					if (result.size() == 0) {
						result.add(pair);
					} else {
						boolean add_pair = true;
						for (MutablePair<Integer, Integer> temp : result) {
							if ((temp.left.equals(pair.left) && temp.right.equals(pair.right))
									|| (temp.left.equals(pair.right) && temp.right.equals(pair.left))) {
								add_pair = false;
								break;
							} 
						}
						if(add_pair){
							result.add(pair);
						}
					}
				}
			}
		}
		
		//print results
		if (result.size() == 0) {
			System.out.println("No redundancy found!");
		} else {
			for (MutablePair<Integer, Integer> record : result) {
				System.out.println(record.left + " " + set.get(record.left).title);
				System.out.println(record.right + " " + set.get(record.right).title);
				System.out.println();
			}
		}
	}

	
	
	/**
	 * check how many papers of the target paper set exist in the base paper set 
	 * @param paper_set2
	 */
	public void checkOverlap(LinkedList<Paper> target, LinkedList<Paper> base) {
		LinkedList<MutablePair<Integer, Integer>> result = new LinkedList<MutablePair<Integer, Integer>>();

		int count_1 = 0, count_2 = 0;
		for (count_1 = 0; count_1 < target.size(); count_1++) {
			Paper paper_1 = target.get(count_1);
			String title = paper_1.title;
			for (count_2 = 0; count_2 < base.size(); count_2++) {
				Paper paper_2 = base.get(count_2);
				String search = paper_2.title;
				if (count_1 != count_2 && findSimilarByWords(title, search, redundancy_difference, redundancy_length_low, redundancy_length_high)) {
					MutablePair<Integer, Integer> pair = new MutablePair<Integer, Integer>();
					pair.left = count_1;
					pair.right = count_2;

					if (result.size() == 0) {
						result.add(pair);
					} else {
						boolean add_pair = true;
						for (MutablePair<Integer, Integer> temp : result) {
							if ((temp.left.equals(pair.left) && temp.right.equals(pair.right))
									|| (temp.left.equals(pair.right) && temp.right.equals(pair.left))) {
								add_pair = false;
								break;
							} 
						}
						if(add_pair){
							result.add(pair);
						}
					}
				}
			}
		}
		
		//print results
		if (result.size() == 0) {
			System.out.println("No redundancy found!");
		} else {
			for (MutablePair<Integer, Integer> record : result) {
				System.out.println(record.left + " " + target.get(record.left).title);
				System.out.println(record.right + " " + base.get(record.right).title);
				System.out.println();
			}
			System.out.println(result.size());
			for (MutablePair<Integer, Integer> record : result) {
				System.out.println(target.get(record.left).title);
			}
		}
	}

	
	
	
	
	
	
	
	/**
	 * show matched and unmatched papers
	 * generate an additional documents which contains the citation information regarding to our selected papers.
	 * @param set
	 * @throws IOException
	 */
	public void prepareSelectedCitationDocument(LinkedList<Paper> set) throws IOException{
		// read the DBLP data set
		File file = new File("/Users/litong30/research/Trento/Other files/DBLP_Citation_2014_May/publications.txt");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"), 5*1024*1024);  

		String line = "";
		String output_matched="";
		String output_unmatched="";
		
		Boolean extract_info = false;
		LinkedList<Paper> matched_set = new LinkedList<Paper>();
		LinkedList<Paper> unmatched_set = new LinkedList<Paper>();
		while ((line = reader.readLine()) != null) {
			if(set.size() == 0 && extract_info == false){
				break;
			}
			if (line.startsWith("#*")) { //analyze the title line
				extract_info = false;
				for (Paper paper : set) {
					if(line.toLowerCase().contains(paper.title.toLowerCase())){
						// if paper is successfully matched, then start extracting information
						extract_info = true;
						paper.found = true;
						
						matched_set.add(paper);
						set.remove(paper); // to accelerate 
						break;
					}
				}
			}
			if(extract_info == true){
				matched_set.getLast().found_paper += line + "\n";
			}
		}

		String output_paper_info = "";
		int count=0;
		for(Paper paper: matched_set){
			// output title of found papers
			output_matched+=paper.title+"\n";
			// output full information of found papers
			output_paper_info += paper.found_paper;
			// record the matched number
			count++;
		}
		// output info of papers that are not found
		for(Paper paper: set){
			output_unmatched += paper.title+"\n";
		}
		
		System.out.println(output_matched+"\n\n\n\n\n\n"+output_unmatched);
		System.out.println(count);
		
		// output titles
		String output_file = "txt_citation/match_result.txt";
		PrintWriter writer = new PrintWriter(output_file, "UTF-8");
		writer.println(output_matched+"\n\n\n\n\n\n"+output_unmatched);
		writer.close();
		
		// output info
		output_file = "txt_citation/selected_papers_info.txt";
		PrintWriter writer2 = new PrintWriter(output_file, "UTF-8");
		writer2.println(output_paper_info);
		writer2.close();
	}

	/**
	 * Analyze the selected papers to determine to which extent they are included in the DBLP dataset
	 * @param set
	 * @throws IOException
	 */
	public void analyzeGivenPaperSet(LinkedList<Paper> set) throws IOException{
		// read the DBLP data set
		File file = new File("/Users/litong30/research/Trento/Other files/DBLP_Citation_2014_May/publications.txt");   
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));    
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"), 5*1024*1024);  
		
		String line = "";
		String output="";
		
		while ((line = reader.readLine()) != null) {
			if(set.size()==0){
				break;
			}
			if (line.startsWith("#*")) { //only analyze the title line
				for (Paper paper : set) {
					if(line.toLowerCase().contains(paper.title.trim().toLowerCase())){
//					if(ca.findSimilarByWords(line, paper.title, 3)){
//					if(ca.findSimilarIgnoreMarks(line, paper.title)){
						System.out.println(paper.title);
						
						output+=paper.title+"\n";
						paper.found = true;
						//when they are a large number of papers
						set.remove(paper);
						
						paper.found_title=line.substring(2);
						output+=paper.found_title+"\n";
						System.out.println(paper.found_title);
						break;
					}
				}
			}
		}
		// produce additional space to facilitate reading
		output+="\n\n\n\n\n\n\n\n\n\n\n";
		int count=0;
		for(Paper paper: set){
			// output the unmatched papers
			if(paper.found == false){
				output+=paper.title+"\n";
				count++;
			}
		}
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		System.out.println(output);
		System.out.println(count);	
	}

	
	/**
	 * Similarity calculation method 1
	 * search individual words
	 * @param title
	 * @param search
	 * @return
	 */
	private boolean findSimilarByWords(String title, String search, int difference, int low, int high){
		title = title.toLowerCase();
		search = search.toLowerCase();
		
		
		String [] temp_set = search.split(" ");
		String [] temp_set2 = title.split(" ");
		//first verify the length of paper to be valid
		if(temp_set.length<low || temp_set.length>high || temp_set2.length<low || temp_set2.length>high){
			return false;
		}
		
		int hit_count = 0;
		int allowed_difference = difference;
		for (String temp: temp_set){
			if(title.indexOf(temp)>=0){
				hit_count++;
			}
		}
		if(hit_count>=temp_set.length-allowed_difference){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Similarity calculation method 2
	 * pre-process titles
	 * @param title
	 * @param search
	 * @return
	 */
	private boolean findSimilarIgnoreMarks(String title, String search){
		title = title.toLowerCase();
		title = title.replaceAll("-", "");
		title = title.replaceAll(":", "");
		title = title.replaceAll(" ", "");
		
		search = search.toLowerCase();
		search = search.replaceAll("-", "");
		search = search.replaceAll(":", "");
		search = search.replaceAll(" ", "");
		
		if(title.indexOf(search)>=0){
			return true;
		}
		else {
			return false;
		}
	}


	/**
	 * Import the selected paper information that need to process.
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	public LinkedList<Paper> importCitationDocument(String citation_file) throws IOException {
		File file = new File(citation_file);
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		
		LinkedList<Paper> papers = new LinkedList<Paper>();
		
		String line ="";
		Paper paper = null;
		
		while ((line=reader.readLine())!=null){
			if(line.startsWith("#*")){
				// create a new paper object and start a new around extraction
				paper = new Paper();
				paper.title = line.substring(2); 
			} else if (line.startsWith("#@")) {
				paper.author_text = line.substring(2);
				paper.processAuthors();
			} else if (line.startsWith("#t")){
				paper.year = line.substring(2);
			} else if (line.startsWith("#c")){
				paper.venue = line.substring(2);
			} else if (line.startsWith("#index")){
				paper.id = line.substring(6);
			} else if (line.startsWith("#%")){
				paper.references.add(line.substring(2));
			} else if (line.startsWith("#!")){
				if (paper != null) {
					papers.add(paper);
				}
			}
		}
//		for(Paper temp: papers){
//			temp.print();
//		}
		return papers;
	}
	
	/**
	 * Traverse the our data set to identify the citation relationships 
	 * @param paper_set
	 */
	public void identifyCitations(LinkedList<Paper> paper_set) {
		for(Paper paper : paper_set){
			// only process the papers that have references
			if(paper.references.size()!=0){
				// for each of the references, check their existences in current paper set.
				for(String reference_id: paper.references){
					for(Paper search: paper_set){
						if (reference_id.equals(search.id)) {
							if (!paper.current_references.contains(search)) {
								paper.current_references.add(search);
							}
							if (!search.current_citations.contains(paper)) {
								search.current_citations.add(paper);
							}
						}
					}
				}
			}
		}
		
		int count_relation = 0;
		LinkedList<Paper> node_counter = new LinkedList<Paper>();
		for(Paper paper : paper_set){
			//count links
			if(paper.current_citations.size()!=0){
				for (Paper citation: paper.current_citations){
//					String citation_info = "PID." + paper.id + "  " +paper.title +"\n"
//							+ "is cited by\n"
//							+ "PID." + citation.id + "  " + citation.title +"\n";
//					System.out.println(citation_info);
					count_relation++;
				}
			}
			// count nodes
			if(paper.current_citations.size()!=0||paper.current_references.size()!=0){
				if(!node_counter.contains(paper)){
					node_counter.add(paper);
				}
			}
		}
		
		System.out.println("Number of citation relations " + count_relation);
		System.out.println("Number of related nodes " + node_counter.size());
	}


	public void drawCitationGraph(LinkedList<Paper> paper_set) throws IOException {
		// create files
		// use fdp
		String author_graph = "digraph G {\n" + "overlap = voronoi;\n" + "splines=true;\n"
//						+ "nodesep=2.0;\n"
						+ "sep = 0.3;\n";//voronoi, scalexy, compress, false

		//add authors with customized properties
		String shown_label = "";
		for (Paper paper : paper_set ) {
			if (paper.current_citations.size() > 0 || paper.current_references.size() > 0) {
				shown_label = paper.authors.getFirst().last_name+"\n"+paper.year;

				double node_height = 2.5;//0.86 + as.alternative.size() * 0.1;
				double node_width = 4;//1.64 + as.alternative.size() * 0.2;
				double font_size = 50;// + as.alternative.size() * 2;
				author_graph += paper.id + "[shape=ellipse,width=" + node_width + ",height=" + node_height
						+ ",fixedsize = true, fontname=\"Helvetica\", fontsize=" + font_size + ",label=\""
						+ shown_label + "\n" + "\"" + "];\n";
			}
		}
		for (Paper paper : paper_set) {
			if (paper.current_citations.size() > 0) {
				//refactor according to the requirement of graphviz
				double pen_width = 1.5;

				for (Paper citation : paper.current_citations) {
					author_graph += citation.id + " -> " + paper.id + "[penwidth=" + pen_width + ", arrowsize = 3];\n";
				}
			}
		}
		author_graph += "}";

		//exception, don't know why... 
		//		author_graph += author_graph.replaceAll("J_Castro.", "J_Castro");

		//write to files
		String output = "citation_diagram.gv";
		PrintWriter writer = new PrintWriter(output, "UTF-8");
		writer.println(author_graph);
		writer.close();

		//draw pdf figure for the corresponding graph
		// TODO: still don't know why this call doesn't work. (sometimes work but incorrectly)
//		Runtime rt;
//		Process pr;
//		String draw_graphviz = "txt_citation/dot -Tpdf citation_diagram.gv -o citation_diagram.pdf";
//		rt = Runtime.getRuntime();
//		pr = rt.exec(draw_graphviz);

	}

}
