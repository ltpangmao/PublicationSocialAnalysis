package it.unitn.disi.author;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;

public class AuthorAnalysis {

	public static String name_separator = "_";
	public static int author_threshold = 3;

	public static void main(String[] args) throws Throwable {

		LinkedList<Author> authors = new LinkedList<Author>();

		BufferedReader br = null;
		String line;
		br = new BufferedReader(new FileReader("txt_author/authors.txt"));

		LinkedList<Record> records = new LinkedList<Record>();
		// first process all names
		int i = 0;
		while ((line = br.readLine()) != null) {
			// remove empty lines
			if (!line.equals("")) {
				Record new_record = new Record(line);
				records.add(new_record);
				// tackle lines separated by ";"
				if (line.indexOf(";") > -1) {
					String temp[] = line.split(";");
					for (String s : temp) {
						Author new_author = new Author(s);
						authors.add(new_author);

						new_record.contained_authors.add(new_author);
					}
				} else if (line.indexOf(",") > -1) {
					String temp[] = line.split(",");
					for (String s : temp) {
						Author new_author = new Author(s);
						authors.add(new_author);

						new_record.contained_authors.add(new_author);
					}
				}
				i++;
			}
		}
		br.close();

		// secondly identify first name and last name for each one
		for (Author au : authors) {
			String content = au.name;

			content = content.trim();
			if (content.indexOf(",") > -1 && content.length() > 5) {
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

		LinkedList<AuthorSet> author_sets = new LinkedList<AuthorSet>();
		//facilitate analysis
		LinkedList<String> author_last_names = new LinkedList<String>();
		boolean added = false;
		/** finally cluster authors
		 *  Each cluster stands for a unique author, and its size indicate the number of publications 
		 *  Each author is assigned a unique identifier, which contains the "first letter of first name" & "last name"
		 */
		String id = "";
		for (Author a : authors) {
			if (a.last_name != null) {
				id = a.first_name.charAt(0) + name_separator + a.last_name; 
				if (!author_last_names.contains(id)) {
					author_last_names.add(id);

					AuthorSet new_author_set = new AuthorSet();
					new_author_set.identity = id;
					author_sets.add(new_author_set);
				}
			}
		}

		for (Author a : authors) {
			id = a.first_name.charAt(0) + name_separator + a.last_name;
			for (AuthorSet as : author_sets) {
				if (id.equals(as.identity)) {
					as.alternative.add(a);
					a.upper_set = as;
				}
			}
		}

		// Calculate the full name of each author set by considering the longest name
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

		LinkedList<AuthorRelation> ars = new LinkedList<AuthorRelation>();
		//calculate co-author relations between big author set (we set the throughold as 2 )
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

		// create files
		String author_graph = "graph G {\n" + "overlap = scalexy;\n" + "splines=true;\n";
		//				+ "ranksep = 0.5;"
		//				+ "sep=\"+1,1\";"
		//				+ "nodesep=1.0;\n";//voronoi, scalexy, compress, false
		//add authors with customized properties
		String shown_label = "";
		for (AuthorSet as : author_sets) {
			if (as.alternative.size() >= author_threshold) {
				shown_label = as.identity.replaceAll("_", ".");

				double node_height = 0.86 + as.alternative.size() * 0.1;
				double node_width = 1.64 + as.alternative.size() * 0.2;
				double font_size = 30;// + as.alternative.size() * 2;
				author_graph += as.identity.replaceAll("-", "_") + "[shape=ellipse,width=" + node_width + ",height="
						+ node_height + ",fixedsize = true, fontname=\"Helvetica-Bold\", fontsize=" + font_size
						+ ",label=\"" + shown_label + "\n" + as.alternative.size() + "\"" + "" + "];\n";
			}
		}
		for (AuthorRelation ar : ars) {
			//refactor according to the requirement of graphviz
			double pen_width = 1.5 + ar.number * 0.8;
			author_graph += ar.au1.upper_set.identity.replaceAll("-", "_") + " -- "
					+ ar.au2.upper_set.identity.replaceAll("-", "_") + "[penwidth=" + pen_width + "];\n";
		}
		author_graph += "}";

		//exception, don't know why... 
		//		author_graph += author_graph.replaceAll("J_Castro.", "J_Castro");

		//write to files
		String output = "author_diagram.gv";
		PrintWriter writer = new PrintWriter(output, "UTF-8");
		writer.println(author_graph);
		writer.close();

		//		Runtime rt = Runtime.getRuntime();
		//		Process pr = rt.exec("dot -Tpdf author_diagram.gv -o author_diagram.pdf");

		// print clusters and further improve

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
		
		System.out.println(records.size() + " " + author_sets.size() + " " + count);
	}

}
