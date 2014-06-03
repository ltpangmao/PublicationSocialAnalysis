package it.unitn.disi.author;
import java.util.LinkedList;


public class Record {
	
	public String content="";
	public LinkedList<Author> contained_authors = new LinkedList<Author>();
	
	
	
	public Record(String line) {
		content = line;
	}
}
