package com.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.SystemFlow.FileOperation;

public class Ariticle {
	 public Ariticle(int id,String title, String url, String source, String content,
			float score) {
		super();
		this.id=id;
		this.title = title;
		this.url = url;
		this.source = source;
		this.content = content;
		this.score = score;
	}
	 public Ariticle(String title, String url, String source, String content,
				float score) {
			super();
	
			this.title = title;
			this.url = url;
			this.source = source;
			this.content = content;
			this.score = score;
		}
	 public Ariticle(){
		 
	 }
	 int id;
	private String title;
	 private String url;
	 private String source;
	 private String content;
	 private String textlist;
	 private String path;
	 private float score;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTextlist() {
		return textlist;
	}
	public void setTextlist(String textlist) {
		this.textlist = textlist;
	}
	public static Ariticle getAriticle(File file){
		Ariticle result = new Ariticle();
		ArrayList<String> tmp=FileOperation.readFileByLine(file);
		for (int i = 0; i < tmp.size(); i++) {
			System.out.println(i+":::"+tmp.get(i));
		}
		
		result.setTitle(tmp.get(1));
		result.setUrl(tmp.get(3));
		result.setSource(tmp.get(5));
		String content = "";
		for (int i = 7; i <tmp.size(); i++) {
			content+=tmp.get(i);
			
		} 
		result.setContent(content);
	
		return result;
		
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public static void main(String[] args) throws Exception, Exception {
		File tmp=new File("D:/luceneSpace/file/23.txt");
		Ariticle.getAriticle(tmp);
	}
	
	

}
