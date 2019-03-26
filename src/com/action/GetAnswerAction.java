package com.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.Model.Ariticle;
import com.Model.ResultBean;
import com.Model.SegmentWord;
import com.Process.LuceneIndex;
import com.SystemFlow.ContextHandling;
import com.SystemFlow.StopwordDelete;
import com.SystemFlow.SynonymExpand;
import com.SystemFlow.WordStandardization;
import com.nlpir.wordseg.WordSeg;
import com.opensymphony.xwork2.ActionSupport;
import com.searchengine.webservice.GetAnswerWs;


public class GetAnswerAction extends ActionSupport {
	private String search;
	private String result;
	private List<Ariticle> list;
	private String owlresult;
//	public static String search;
//	public String result;
//	public static List<Ariticle> list;
//	public static String owlresult;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<Ariticle> getList() {
		return list;
	}

	public void setList(List<Ariticle> list) {
		this.list = list;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("’‘’Ò∑Â");
		return SUCCESS;
	}

	public String getAnswer() {

		// ∑÷¥ 
			System.out.println(search);
			search=search.trim();
	    ArrayList<String> tmp=GetAnswerWs.getOWLresult(search);
        owlresult=tmp.toString();
        for (int i = 0; i < tmp.size(); i++) {
        	if(tmp.get(i).length()<8){
        		search+=tmp.get(i);
        	}
			
		}
	    System.out.println(search);
		list = LuceneIndex.QueryIndex(search);


		
		return SUCCESS;
	}

	public String getOwlresult() {
		return owlresult;
	}

	public void setOwlresult(String owlresult) {
		this.owlresult = owlresult;
	}

}
