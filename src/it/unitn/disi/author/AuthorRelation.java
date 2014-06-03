package it.unitn.disi.author;

public class AuthorRelation {
	public Author au1=null;
	public Author au2=null;
	
	public int number=0;
	
	public AuthorRelation(Author au1, Author au2) {
		super();
		this.au1 = au1;
		this.au2 = au2;
		this.number=1;
	}
	
	
}
