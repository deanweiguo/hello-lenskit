package com.searchengine.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.DBUtil.FAQDao;
import com.DBUtil.TempFAQDao;
import com.Model.Ariticle;
import com.Model.SegmentWord;
import com.Process.LuceneIndex;
import com.SystemFlow.ContextHandling;
import com.SystemFlow.StopwordDelete;
import com.SystemFlow.SynonymExpand;
import com.SystemFlow.WordStandardization;
import com.nlpir.wordseg.WordSeg;

public class GetAnswerWs {
	
	public static void main(String[] args){
		List<Ariticle> list = LuceneIndex.QueryIndex("毛泽东");
//		Scanner input=new Scanner(System.in);
//		System.out.println("输入问题");
//		String question=input.next();
//		question=question.trim();
//		getOWLresult(question);
	}
	public void savefaq(String question) {
        FAQDao faqDao = new FAQDao();
        if (!faqDao.ExistQuestion(question)) {
            TempFAQDao tempFAQ = new TempFAQDao();
            tempFAQ.InsertTempQuestion(question);
        }
    }

	
	public static ArrayList<String> getOWLresult(String  question){
		

		
		ArrayList<String> result=new ArrayList<String>();
		
		//分词
		WordSeg abc=new WordSeg();
		ArrayList<SegmentWord> SegResult=abc.wordSeg(question);
		System.out.print("\n分词结果：");
		if (null == SegResult || "".equals(SegResult)) {
            System.out.println("出错了");
        }
		 for (int i = 0; i < SegResult.size(); i++) {
	    	   System.out.print(SegResult.get(i).getWord()+"  ");
		}
		
		//去停用词
		 ArrayList<SegmentWord> StopResult = new StopwordDelete().ProcessStopword(SegResult);
		 System.out.print("\n去停用词后结果：");
		 for (int i = 0; i < StopResult.size(); i++) {
	    	   System.out.print(StopResult.get(i).getWord()+"  ");
		}
		 
		 //问句标准化 把一些词语进行替换 同义词替换
		 ArrayList<SegmentWord> standardResult = new WordStandardization().sentenceStandard(StopResult);
	     System.out.print("\n用户问句标准化后结果：");
	     for (int i = 0; i < standardResult.size(); i++) {
	    	   System.out.print(standardResult.get(i).getWord()+"  ");
		}
	     
	     //同义词扩展 
	     ArrayList<SegmentWord> ExpandResult = standardResult;
	     ExpandResult = new SynonymExpand().SynonymsExpand2(standardResult);
	     System.out.print("\n问句同义词扩展后：");
	     for (int i = 0; i < ExpandResult.size(); i++) {
	    	   System.out.print(ExpandResult.get(i).getWord()+"  ");
		}
	     
	     //推理拓展
	     
//         ArrayList<String> equipments = new ContextHandling().GetEquipment(ExpandResult);
 
         ArrayList<String> owllist=new ContextHandling().GetKeyword(ExpandResult);
         
	     return owllist;
//	     System.out.println("本体扩展结束其中包含的部件：" + equipments);
		 
		
		
	}
	

}
