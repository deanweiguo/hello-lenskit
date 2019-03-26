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
		List<Ariticle> list = LuceneIndex.QueryIndex("ë��");
//		Scanner input=new Scanner(System.in);
//		System.out.println("��������");
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
		
		//�ִ�
		WordSeg abc=new WordSeg();
		ArrayList<SegmentWord> SegResult=abc.wordSeg(question);
		System.out.print("\n�ִʽ����");
		if (null == SegResult || "".equals(SegResult)) {
            System.out.println("������");
        }
		 for (int i = 0; i < SegResult.size(); i++) {
	    	   System.out.print(SegResult.get(i).getWord()+"  ");
		}
		
		//ȥͣ�ô�
		 ArrayList<SegmentWord> StopResult = new StopwordDelete().ProcessStopword(SegResult);
		 System.out.print("\nȥͣ�ôʺ�����");
		 for (int i = 0; i < StopResult.size(); i++) {
	    	   System.out.print(StopResult.get(i).getWord()+"  ");
		}
		 
		 //�ʾ��׼�� ��һЩ��������滻 ͬ����滻
		 ArrayList<SegmentWord> standardResult = new WordStandardization().sentenceStandard(StopResult);
	     System.out.print("\n�û��ʾ��׼��������");
	     for (int i = 0; i < standardResult.size(); i++) {
	    	   System.out.print(standardResult.get(i).getWord()+"  ");
		}
	     
	     //ͬ�����չ 
	     ArrayList<SegmentWord> ExpandResult = standardResult;
	     ExpandResult = new SynonymExpand().SynonymsExpand2(standardResult);
	     System.out.print("\n�ʾ�ͬ�����չ��");
	     for (int i = 0; i < ExpandResult.size(); i++) {
	    	   System.out.print(ExpandResult.get(i).getWord()+"  ");
		}
	     
	     //������չ
	     
//         ArrayList<String> equipments = new ContextHandling().GetEquipment(ExpandResult);
 
         ArrayList<String> owllist=new ContextHandling().GetKeyword(ExpandResult);
         
	     return owllist;
//	     System.out.println("������չ�������а����Ĳ�����" + equipments);
		 
		
		
	}
	

}
