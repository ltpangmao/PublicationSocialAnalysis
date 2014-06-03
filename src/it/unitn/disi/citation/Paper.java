package it.unitn.disi.citation;

import it.unitn.disi.author.Author;

import java.util.LinkedList;

public class Paper {
	
	// basic info
	public String title = "";
	public LinkedList<Author> authors = new LinkedList<Author>();
	public String year = "";
	public String venue = "";
	public String id = "";
	public LinkedList<String> references = new LinkedList<String>();
	
	
	//citation info
	public LinkedList<Paper> current_references = new LinkedList<Paper>();
	public LinkedList<Paper> current_citations = new LinkedList<Paper>();
	//syntactic sugar 
	public boolean found = false;
	public String found_title = "";
	public String found_paper = "";
	
	public String author_text = "";
	
	
	
	
	
	
	
	
	
	public void print(){
		String output = this.title +"\n"
				+ this.id + "\n"
				+ this.author_text + "\n"
				+ this.year + "\n";
				
		System.out.println(output);
	}









	public void processAuthors() {
		String[] author_list = author_text.split(",");
		for (String a: author_list){
			Author author = new Author (a);
			author.processName(1);
			this.authors.add(author);
			
			author.printName();
		}
		
	}
}
