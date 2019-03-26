package com.Model;

import java.util.ArrayList;

public class SegmentWord {
	public SegmentWord(String word, String wordclass) {
		super();
		this.word = word;
		this.wordclass = wordclass;
	}
	public SegmentWord(){
		
	}
	public SegmentWord(String segment, String word, String wordclass) {
		super();
		this.segment = segment;
		this.word = word;
		this.wordclass = wordclass;
	}
	public SegmentWord(String segment) {
		super();
		this.segment = segment;
		String[] tmp=segment.split("/");
		//System.out.println(segment);
		this.word=tmp[0];
		this.wordclass=tmp[1];
		
	}
	
	String segment;
	String word;
	String wordclass;
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getWordclass() {
		return wordclass;
	}
	public void setWordclass(String wordclass) {
		this.wordclass = wordclass;
	}
	public ArrayList<SegmentWord> getSpecificTypeword(ArrayList<SegmentWord> segment,String type){
		ArrayList<SegmentWord> result=new ArrayList<>();
		
		
		for (int i = 0; i < segment.size(); i++) {
			String tmp=segment.get(i).getWordclass();
			
			if(type.equals(tmp)){
				result.add(segment.get(i));
			}
		}
		
		return result;
	}
	
	public ArrayList<SegmentWord> getNoun(ArrayList<SegmentWord> segment){
		
        ArrayList<SegmentWord> result=new ArrayList<>();
		
		
		for (int i = 0; i < segment.size(); i++) {
			String tmp=segment.get(i).getWordclass();

			if(tmp.startsWith("n")){ 
				result.add(segment.get(i));
			}
		}
		
		return result;
		
	}
	

}
