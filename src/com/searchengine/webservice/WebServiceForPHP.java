package com.searchengine.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Jing.model.Activity;
import com.KnowledgeGraph.GetKnowledge;
import com.Model.Ariticle;
import com.Process.LuceneIndex;



import com.Process.OwlProcess;
import com.Util.HttpRequestUtil;
import com.Util.LuceneActivityIndex;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;


@WebService
public class WebServiceForPHP {
	
	public static void main(String[] args) throws Exception {
		//new WebServiceForPHP().getKnowledgeDetail("毛泽东");
		
		Endpoint.publish("http://127.0.0.1:1233/searchengine/getAnswer", new WebServiceForPHP());  
	}

	public  String getAnswerService(String question,int pagenumber,int pagesize) {
     
		ArrayList<String> tmp = GetAnswerWs.getOWLresult(question);
        String oldquestion=question;
		String owlresult = "";

		for (int i = 0; i < tmp.size(); i++) {
			if (tmp.get(i).length() < 8) {
				question += tmp.get(i);
		
			}
			owlresult+=tmp.get(i)+"  ";
		}

		List<Ariticle> list = LuceneIndex.QueryIndex(question);
		JSONArray jArray = new JSONArray();
		
		
		int start=(pagenumber-1)*pagesize;
		
		int end=list.size()>start+pagesize-1?start+pagesize-1:list.size()-1;
		
		for (int i = start; i <= end; i++) {
			Map<String,String> ariticle=new HashMap<String,String>();
			ariticle.put("title",list.get(i).getTitle());
			ariticle.put("source",list.get(i).getSource() );
			ariticle.put("url", list.get(i).getUrl());
			ariticle.put("content", list.get(i).getContent());
			ariticle.put("score",Float.toString( list.get(i).getScore()));
			jArray.put(ariticle);
		}
		
//		for (int i = 0; i < list.size(); i++) {
//			Map<String,String> ariticle=new HashMap<String,String>();
//			ariticle.put("title",list.get(i).getTitle());
//			ariticle.put("source",list.get(i).getSource() );
//			ariticle.put("url", list.get(i).getUrl());
//			ariticle.put("content", list.get(i).getContent());
//			ariticle.put("score",Float.toString( list.get(i).getScore()));
//			jArray.put(ariticle);
//		}
	  
		JSONObject jObject = new JSONObject();
		jObject.put("Ariticles", jArray);
		
		jObject.put("OWLresult", owlresult);
		
		jObject.put("question", oldquestion);
		
		jObject.put("pagenumber",pagenumber );
		
		jObject.put("total", list.size());
		
//		JSONArray result=new JSONArray();
//		result.put(jArray);
//		
//		result.put(owlresult);
		
//		jArray.put(owlresult);
	
		return jObject.toString();

	}
	
	public String getRelatedKnowledge(String title){
		title=title.replace(" ", "");
		String result=new GetKnowledge().getKeyWord(title);
		return result;
//		return result;
	}

	public String getKnowledgeDetail(String title) throws Exception{
		
		String entitylist=HttpRequestUtil.sendGet("http://knowledgeworks.cn:30001/", "p="+title);
		//System.out.println(entitylist);
		//去掉两边多余字符
		String[] entitys=entitylist.replace("[\"","").replace("\"]","").split("\", \"");
		JSONArray jArray = new JSONArray();
		
		for (int i = 0; i <entitys.length; i++) {
			int tag=0;
			String detail=HttpRequestUtil.sendGet("http://knowledgeworks.cn:20313/cndbpedia/api/entityAVP","entity="+entitys[i]);
			String tmp1=detail.replace("{\"av pair\": [[\"","");
			String tmp2=tmp1.replace("\"]]}","");
			String[] tmp3=tmp2.split("\"\\], \\[\"");
			Map<String,String> knowledge=new HashMap<String,String>();
			knowledge.put("标题",entitys[i]);
			for (int j = 0; j < tmp3.length; j++) {
				String[] temp=tmp3[j].split("\", \"");
				if(temp.length!=2){
				break;
				}else{
				
					if(temp[0].equals("书名")||temp[0].equals("定价"))
						tag=1;
					knowledge.put(temp[0],temp[1]);
				}	
			
			}
			if(tag==0)
				jArray.put(knowledge);
					
		}
	    title=OwlProcess.GetRelatedWord(title);
        ArrayList<Activity> activities=LuceneActivityIndex.QueryIndex(title);
        
        JSONArray search= new JSONArray();
        
        for (int i = 0; i < activities.size(); i++) {
        	search.put(activities.get(i).getId());
			
		}
        JSONObject jObject = new JSONObject();
		jObject.put("Knowledgedetail", jArray);
		
		jObject.put("searchresult",search );
		
		System.out.println(jObject.toString());
		
		
	  
		
		return jObject.toString();
	}
}
