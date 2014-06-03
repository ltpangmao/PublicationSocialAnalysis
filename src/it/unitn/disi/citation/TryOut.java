package it.unitn.disi.citation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.commons.lang3.tuple.MutablePair;

public class TryOut {
	public static void main(String[] args) throws IOException {
		File file = new File("/Users/litong30/Desktop/DBLP_Citation_2014_May/publications.txt");
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024);

		String line = "";
		int count =0;

		while ((line = reader.readLine()) != null) {
			//if (TryOut.findSimilarByWords(line,"Deriving tabular event-based specifications from goal-oriented requirements models")) {
			if (line.contains("#%738586")){
				count++;
			}
		}
		
		System.out.println(count);
		
		
		
		
//		LinkedList<MutablePair<Integer, Integer>> result = new LinkedList<MutablePair<Integer, Integer>>();
//		MutablePair<Integer, Integer> p1 = new MutablePair<Integer, Integer>(2, 1);
//		MutablePair<Integer, Integer> p2 = new MutablePair<Integer, Integer>(1, 2);
//		result.add(p1);
//		for (MutablePair<Integer, Integer> temp: result){
//			if ((temp.left.equals(p2.left)&& temp.right.equals(p2.right)) || (temp.left.equals(p2.right)&& temp.right.equals(p2.left))){
//				System.out.println("y");
//			}
//			else{
//				System.out.println("n");
//			}
//				
//		}
		
	}
	
	public static boolean findSimilarByWords(String title, String search){
		title = title.toLowerCase();
		search = search.toLowerCase();
		
		String [] temp_set = search.split(" ");
		int hit_count = 0;
		int allowed_difference = 3;
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

}
