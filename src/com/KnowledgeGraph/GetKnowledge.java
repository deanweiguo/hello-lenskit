package com.KnowledgeGraph;

import java.util.ArrayList;
import java.util.Scanner;

import com.Model.SegmentWord;
import com.Process.OwlProcess;
import com.SystemFlow.ContextHandling;
import com.SystemFlow.StopwordDelete;
import com.SystemFlow.SynonymExpand;
import com.SystemFlow.WordStandardization;
import com.Util.HttpRequestUtil;
import com.nlpir.wordseg.WordSeg;

public class GetKnowledge {
	
	public static void main(String[] args){
		Scanner input=new Scanner(System.in);
		System.out.println("活动标题：");
		String question=input.next();
		question=question.trim();
		new GetKnowledge().getKeyWord(question);
	}
	public String getKeyWord(String title){
		ArrayList<SegmentWord> segmentword=new ArrayList<>();
		segmentword=WordSeg.wordSeg(title);
		
		 ArrayList<SegmentWord> StopResult = new StopwordDelete().ProcessStopword(segmentword);
		 System.out.print("\n去停用词结果：");
		 for (int i = 0; i < StopResult.size(); i++) {
	    	   System.out.print(StopResult.get(i).getWord()+"  ");
		}
		 
		 ArrayList<SegmentWord> keyword=new SegmentWord().getNoun(StopResult);
		 System.out.print("\n获取名词：");
		 for (int i = 0; i < keyword.size(); i++) {
	    	   System.out.print(keyword.get(i).getWord()+"  ");
		 }
		 
	     SegmentWord[][] ExpandResult =  new SynonymExpand().SynonymsExpand3(keyword);
	     System.out.print("\n同义词扩展：");
	     for (int i = 0; i < ExpandResult.length; i++) {
	    	 System.out.print("[");
			for (int j = 0; j < ExpandResult[i].length; j++) {
				  System.out.print(ExpandResult[i][j].getWord()+"  ");
			}
			System.out.print("]");
		}

		 ArrayList<SegmentWord> standardResult = new WordStandardization().sentenceStandard2(ExpandResult);
	     System.out.print("\n标准化：");
	     for (int i = 0; i < standardResult.size(); i++) {
	    	   System.out.print(standardResult.get(i).getWord()+"  ");
		 } 
	   
//	     for (int i = 0; i < standardResult.size(); i++) {
//	    	 
//			 System.out.println(HttpRequestUtil.sendGet("http://zhishi.me/api/entity/"+standardResult.get(i).getWord(), "property=infobox"));
//			 System.out.println(HttpRequestUtil.sendGet("http://knowledgeworks.cn:20313/cndbpedia/api/entityAVP", "entity="+standardResult.get(i).getWord()));
//	     }
	     String type=""; 
	     ArrayList<SegmentWord> forowl=new ArrayList<>();
	     for (int i = 0; i < standardResult.size(); i++) {
	    	 String tempString=standardResult.get(i).getWord();
	    	 if(tempString.equals("红色诗词")||tempString.equals("红色著作")||tempString.equals("红色文物")||tempString.equals("红色歌舞")){
	    		
	    		 type=tempString;
	    	
	    	 }else{
	    		 forowl.add(standardResult.get(i));
	    	 }
		 }
	   
	     String result= OwlProcess.getInstance().relatedKnowledge(forowl,type);
	    
	     return result;
	          	
		
	}
	
}
