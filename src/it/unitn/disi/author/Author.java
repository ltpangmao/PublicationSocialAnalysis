package it.unitn.disi.author;

public class Author {

	public String name = null; // the raw text extracted from file, so called "full name"
	public String first_name=null;
	public String last_name=null;
	public AuthorSet upper_set = null; // the cluster for this author
	
	
	public Author() {
		super();
	}


	public Author(String name) {
		super();
		this.name = name;
	}


	public Author(String first_name, String last_name) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	public void printName(){
		System.out.println(last_name+" "+first_name);
	}


	public void processName(int method) {
		if(method ==1){
			int separate = this.name.lastIndexOf(" ");
			this.first_name = this.name.substring(0, separate);
			this.last_name = this.name.substring(separate);
		}
	}
	
	
}
