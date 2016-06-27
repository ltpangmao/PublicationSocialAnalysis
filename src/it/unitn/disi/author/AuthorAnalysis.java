package it.unitn.disi.author;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class AuthorAnalysis {

	public static String name_separator = "_";
	public static int author_threshold = 1;

	public static void main(String[] args) throws Throwable {
		
		LinkedList<Author> authors = new LinkedList<Author>(); // author list
		LinkedList<Record> records = new LinkedList<Record>(); // paper list
		
		// input stream
		BufferedReader br = null;
		br = new BufferedReader(new FileReader("txt_author/authors.txt"));

		// first process all names
		obtainAllAuthors(authors, br, records);
		
		// secondly identify first name and last name for each one
		obtainAuthorInfo(authors);
		
		LinkedList<AuthorSet> author_sets = new LinkedList<AuthorSet>();
		// create a list of author_sets
		createAuthorSets(authors, author_sets);
		
		// map all authors into different author_sets
		mapAuthorToAuthorSet(authors, author_sets);

		// Calculate the full name of each author set by considering the longest name
		calculateFullName(author_sets);

		LinkedList<AuthorRelation> ars = new LinkedList<AuthorRelation>(); // list of relations
		//calculate co-author relations between big author sets (the author threshold can be customized)
		calculateAuthorSetRelation(records, ars);

		// create graphviz author diagram file
		String author_graph = generateGVFile(author_sets, ars);

		//output the diagram to external files
		outputFiles(author_graph);

		// generate pdf files from GV files
		//		Runtime rt = Runtime.getRuntime();
		//		Process pr = rt.exec("dot -Tpdf author_diagram.gv -o author_diagram.pdf");
		// print clusters and further improve

		printStatistics(records, author_sets);
	}

	private static void outputFiles(String author_graph) throws FileNotFoundException, UnsupportedEncodingException {
		String output = "author_diagram.gv";
		PrintWriter writer = new PrintWriter(output, "UTF-8");
		writer.println(author_graph);
		writer.close();
	}

	private static void printStatistics(LinkedList<Record> records, LinkedList<AuthorSet> author_sets) {
		int count = 0;
		for (AuthorSet as : author_sets) {
			if (as.alternative.size() >= author_threshold) {
				count++;
				//						for (Author a : as.alternative) {
				//							a.printName();
				//						}
				//						System.out.println(as.full_name);
			}
		}


		//draw pdf figure for the corresponding graph
//		Runtime rt;
//		Process pr;
//		String draw_graphviz = "txt_author/fdp -Tpdf author_diagram.gv -o author_diagram.pdf";
//		rt = Runtime.getRuntime();
//		pr = rt.exec(draw_graphviz);
		
		System.out.println(" the total number of papers: " + records.size() 
				+ "\n the total number of authors: " + author_sets.size() 
				+ "\n the total number of currently processed authors: " + count);
	}

	private static String generateGVFile(LinkedList<AuthorSet> author_sets, LinkedList<AuthorRelation> ars) {
		/****
		 * For less nodes (3), voronoi + fdp is better
		 * For more nodes (1), neato is much faster than fdp, but later has better result
		 * scalexy is better
		 ****/
		// create files
		String author_graph = "graph G {\n" + "overlap = scalexy;\n" + "splines=true;\n";
//						+ "ranksep = 0.5;";
//						+ "sep=\"100,100\";";
		//				+ "nodesep=1.0;\n";   //voronoi, scalexy, compress, false
		//add authors with customized properties
		String shown_label = "";
		for (AuthorSet as : author_sets) {
			if (as.alternative.size() >= author_threshold) {
				shown_label = as.identity.replaceAll(name_separator, ".");

				// proportional size
//				double node_height = 0.86 + as.alternative.size() * 0.1;
//				double node_width = 1.64 + as.alternative.size() * 0.2;
//				double font_size = 24 + as.alternative.size() * 2;

				//fixed size
//				double node_height = 15;
//				double node_width = 25;
				double node_height = 1.2;
				double node_width = 3;
				double font_size = 30;
				// the identity of an element in graphviz should not contain blank space
				author_graph += as.identity.replaceAll(" ", "_") + "[shape=ellipse,width=" + node_width + ",height="
						+ node_height + ",fixedsize = true, fontname=\"Helvetica\", fontsize=" + font_size
						+ ",label=\"" + shown_label + "\n" + as.alternative.size() + "\"" + "" + "];\n";
				//Helvetica-Bold
			}
		}
		for (AuthorRelation ar : ars) {
			//refactor according to the requirement of graphviz
			double pen_width = 1.5 + ar.number * 0.8;
			// the identity of an element in graphviz should not contain blank space
			author_graph += ar.au1.upper_set.identity.replaceAll(" ", "_") + " -- "
					+ ar.au2.upper_set.identity.replaceAll(" ", "_") + "[penwidth=" + pen_width + "];\n";
		}
		author_graph += "}";

		//exception, don't know why... 
		//		author_graph += author_graph.replaceAll("J_Castro.", "J_Castro");
		return author_graph;
	}

	private static void calculateAuthorSetRelation(LinkedList<Record> records, LinkedList<AuthorRelation> ars) {
		for (Record rec : records) {
			for (Author au1 : rec.contained_authors) {
				for (Author au2 : rec.contained_authors) {
					if (au1.upper_set.alternative.size() >= author_threshold
							&& au2.upper_set.alternative.size() >= author_threshold && (!au1.equals(au2))) {

						boolean processed = false;
						//find if this relation already exists
						for (AuthorRelation temp_ar : ars) {
							if ((temp_ar.au1.upper_set.equals(au1.upper_set) && temp_ar.au2.upper_set
									.equals(au2.upper_set))
									|| (temp_ar.au1.upper_set.equals(au2.upper_set) && temp_ar.au2.upper_set
											.equals(au1.upper_set))) {
								temp_ar.number++;
								processed = true;
							}
						}
						if (!processed) {
							AuthorRelation ar = new AuthorRelation(au1, au2);
							ars.add(ar);
						}
					}
				}
			}
		}
	}

	private static void calculateFullName(LinkedList<AuthorSet> author_sets) {
		for (AuthorSet as : author_sets) {
			if (as.alternative.size() > 1) {
				for (Author a : as.alternative) {
					if (a.first_name.length() > as.full_first_name.length()) {
						as.full_first_name = a.first_name;
						as.full_name = a.last_name + " " + a.first_name;
					}
				}
			}
		}
	}

	private static void mapAuthorToAuthorSet(LinkedList<Author> authors, LinkedList<AuthorSet> author_sets) {
		String id = ""; // unique id of author
		for (Author a : authors) {
			id = a.first_name.charAt(0) + name_separator + a.last_name;
			for (AuthorSet as : author_sets) {
				if (id.equals(as.identity)) {
					as.alternative.add(a);
					a.upper_set = as;
				}
			}
		}
	}

	private static void createAuthorSets(LinkedList<Author> authors, LinkedList<AuthorSet> author_sets) {
		/**
		 * cluster authors to create author sets. Each author set stands for a unique author, and its size indicate the number of publications 
		 * Each author will then be assigned a unique identifier that contains the "first letter of first name" & "last name"
		 */
		// facilitate analysis
		LinkedList<String> author_last_name_1first = new LinkedList<String>(); // list of all authors

		String id = ""; // unique id of author
		for (Author a : authors) {
			System.out.println(a.first_name + "***" + a.last_name);
			if (a.last_name != null) {
				id = a.first_name.charAt(0) + name_separator + a.last_name;
				if (!author_last_name_1first.contains(id)) {
					author_last_name_1first.add(id);

					AuthorSet new_author_set = new AuthorSet();
					new_author_set.identity = id;
					author_sets.add(new_author_set);
				}
			}
		}
	}

	private static void obtainAuthorInfo(LinkedList<Author> authors) {
		for (Author au : authors) {
			String content = au.name;

			content = content.trim();
			if (content.indexOf(",") > -1 && content.length() > 3) {
				// name separated by ","
				String name[] = content.split(",");
				au.last_name = name[0].trim();
				au.first_name = name[1].trim();
			} else if (content.indexOf(" ") == -1) {
				// name contains only last name
				au.last_name = content;
			} else {
				// name separated by " "
				au.last_name = content.substring(content.lastIndexOf(" ") + 1);
				au.first_name = content.substring(0, content.lastIndexOf(" "));
			}
		}
	}

	private static void obtainAllAuthors(LinkedList<Author> authors, BufferedReader br, LinkedList<Record> records) throws IOException {
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			// remove empty lines
			if (!line.equals("")) {
//				line = line.toLowerCase();
				Record new_record = new Record(line);
				records.add(new_record);
				// single author is not well treated here, we need pre-process single author
				// tackle lines separated by ";"
				if (line.indexOf(";") > -1) {
					String temp[] = line.split(";");
					for (String s : temp) {
						Author new_author = new Author(s);
						authors.add(new_author);

						new_record.contained_authors.add(new_author);
					}
				}
				/* temporally remove the this part, as current data set has its specific format
				// otherwise separate authors by ","
				else if (line.indexOf(",") > -1) {
					String temp[] = line.split(",");
					for (String s : temp) {
						Author new_author = new Author(s);
						authors.add(new_author);

						new_record.contained_authors.add(new_author);
					}
				}
				*/
				else{
					// add single author
					Author new_author = new Author(line);
					authors.add(new_author);
					new_record.contained_authors.add(new_author);
					//System.out.println("missing" + line);
				}
				i++;
			}
		}
		br.close();
	}

}
