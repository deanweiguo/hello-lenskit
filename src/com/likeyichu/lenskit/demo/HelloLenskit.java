/*
 * Copyright 2011 University of Minnesota
 */
package com.likeyichu.lenskit.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;  
import javax.xml.ws.Endpoint;

import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.data.text.Formats;
import org.grouplens.lenskit.data.text.TextEventDAO;

import org.lenskit.LenskitConfiguration;
import org.lenskit.LenskitRecommender;
import org.lenskit.LenskitRecommenderEngine;
import org.lenskit.api.ItemRecommender;
import org.lenskit.api.Result;
import org.lenskit.api.ResultList;
import org.lenskit.config.ConfigHelpers;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.ItemNameDAO;
import org.lenskit.data.dao.MapItemNameDAO;

import com.Model.ActivityTest;
import com.Model.Ariticle;
import com.Model.SegmentWord;
import com.SystemFlow.StopwordDelete;
import com.nlpir.wordseg.WordSeg;
import com.searchengine.webservice.GetAnswerWs;
//import com.searchengine.webservice.JSONArray;
//import com.searchengine.webservice.JSONObject;


import it.unimi.dsi.fastutil.longs.LongSet;

import com.DBUtil.ActivityTestDao;
import com.DBUtil.SimilarityDao;
import com.Model.SimilarityModel;
import com.DBUtil.DbUtil;
import com.DBUtil.SymptomDao;
import com.KnowledgeGraph.GetKnowledge;
import com.Process.LuceneIndex;
import com.Process.OwlProcess;
import com.Process.Similarity;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Util.HttpRequestUtil;
import com.Util.LuceneActivityIndex;
import com.Jing.model.Activity;
//import com.DBUtilJing.*;
import com.KnowledgeGraph.GetKnowledge;

/**
 * Demonstration app for LensKit. This application builds an item-item CF model
 * from a CSV file, then generates recommendations for a user.
 */
@WebService 
public class HelloLenskit {
	
	public static void main(String[] args) throws RuntimeException, IOException {
		//new HelloLenskit().run();
		//new HelloLenskit().Recommend();
		
		Endpoint.publish("http://127.0.0.1:8089/lenskit/recommend",new HelloLenskit()); 
//		System.out.println(new HelloLenskit().RecommendHomepage(5));
	}

	// about 100,000 pieces
	//private File inputFile = new File("resource/ratings.csv");
	// about 9,000 pieces
	//private File movieFile = new File("resource/movies.csv");
	
	//
	private File inputFile = new File("resource/test3.csv");
	private File movieFile = new File("resource/test4.csv");
	
	
	private List<Long> users = new ArrayList<>(Arrays.asList(3L, 4L));

	public void run() throws RuntimeException, IOException 
	{
		
		//EventDAO dao = TextEventDAO.create(inputFile, Formats.movieLensLatest());
		//ItemNameDAO names = MapItemNameDAO.fromCSVFile(movieFile, 1);

		// Next: load the LensKit algorithm configuration
		//LenskitConfiguration config = ConfigHelpers.load(new File("etc/item-item.groovy"));
		
		// Add our data component to the configuration
		//config.addComponent(dao);

		// There are more parameters, roles, and components that can be set. See
		// the
		// JavaDoc for each recommender algorithm for more information.

		// Now that we have a configuration, build a recommender engine from the
		// configuration
		// and data source. This will compute the similarity matrix and return a
		// recommender
		// engine that uses it.
		//LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

		// Finally, get the recommender and use it.
		//LenskitRecommender rec = engine.createRecommender();
		// we want to recommend items
		//ItemRecommender itemRec = rec.getItemRecommender();
		// for users
		//for (long user : users) {
			// get 10 recommendation for the user
			//ResultList recs = itemRec
					//.recommendWithDetails(user, 10, null, null);
			//System.out.format("Recommendations for user %d:\n", user);
			//for (Result item : recs)
				//System.out.format("\t%d (%s): %.2f\n", item.getId(),
						//names.getItemName(item.getId()), item.getScore());
		//}// for-user
	}
	
	//第一个参数用户ID，第二个参数为活动ID
	public List Recommend(int id, int activityId) throws RuntimeException, IOException 
	{
		//EventDAO dao = TextEventDAO.create(inputFile, Formats.movieLensLatest());
		//ItemNameDAO names = MapItemNameDAO.fromCSVFile(movieFile, 1);
		
		Connection cxn = DbUtil.getCurrentConnection();
		JDBCRatingDAO dao =  JDBCRatingDAO.newBuilder().build(cxn);
		
		
		LenskitConfiguration config = ConfigHelpers.load(new File("etc/item-item.groovy"));
		
		config.addComponent(dao);
		LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

		LenskitRecommender rec = engine.createRecommender();
		ItemRecommender itemRec = rec.getItemRecommender();
		
		ResultList recs = itemRec.recommendWithDetails(id, 5, null, null);
		
		ArrayList list=new ArrayList<>();
		
		System.out.format("Recommendations for user %d:\n", id);
		for(Result item : recs){
			Long item_id=item.getId();
			list.add(item_id);
			//System.out.format("\t%d (%s): %.2f\n", item.getId(),names.getItemName(item.getId()), item.getScore());
		}
		
		ActivityTestDao acDao = new ActivityTestDao();
		WordSeg abc=new WordSeg();
		
		//获取当前活动标题
		String acTitle = acDao.GetTitleById(activityId);
		ArrayList<SegmentWord> segTitle = abc.wordSeg(acTitle);
		ArrayList<SegmentWord> StopTitle = new StopwordDelete().ProcessStopword(segTitle);
		ArrayList<String> sentenceTitle = new ArrayList();
		for(int k=0; k<StopTitle.size(); k++){
			sentenceTitle.add(StopTitle.get(k).getWord());
		}
		
		//推荐结果集活动标题
		ArrayList<ActivityTest> aclist = new ArrayList<ActivityTest>();
		aclist = acDao.GetTitleByIds(list);
	
		Similarity similarity = new Similarity(); 
		ArrayList<ActivityTest> SimList = new ArrayList();
		
		SimilarityDao SimDao = new SimilarityDao();
		
		System.out.println(list);
		for(int i=0; i<aclist.size(); i++){
//			System.out.println(aclist.get(i).getTitle());
			String str = aclist.get(i).getTitle();
			ArrayList<SegmentWord> SegResult = new ArrayList<SegmentWord>();
			SegResult = abc.wordSeg(str);
			ArrayList<SegmentWord> StopResult = new StopwordDelete().ProcessStopword(SegResult);
			
			ArrayList<String> sentenceList = new ArrayList();
			for(int k=0; k<StopResult.size(); k++){
				sentenceList.add(StopResult.get(k).getWord());
			}
			int idB=aclist.get(i).getId();
			Double simtmp=SimDao.GetSimilarityById(activityId, idB);
			Double sim=0.0;
			if(simtmp!=null){
				sim=simtmp;
			}
			else{
				sim=similarity.VSMSimilarity(sentenceTitle,sentenceList);
				SimilarityModel s = new SimilarityModel();
				s.setactivity_idA(activityId);
				s.setactivity_idB(idB);
				s.setsim(sim);
//				System.out.println("--------");
//				System.out.println(s.getsim());
				int flag = SimDao.AddSimilarity(s);
				System.out.println(flag);
			}
			
			ActivityTest tmp = new ActivityTest();
			tmp.setSim(sim);
			tmp.setActivityId(aclist.get(i).getId());
			SimList.add(tmp);
		}
		
		
		//采用冒泡排序算法，对相似度排序
		ActivityTest temp = new ActivityTest();
		for(int i=0; i<SimList.size()-1; i++){
			for(int j=0; j<SimList.size()-i-1; j++){
				if(SimList.get(j).getSim()>SimList.get(j+1).getSim()){
					temp = SimList.get(j);
					SimList.set(j, SimList.get(j+1));
					SimList.set(j+1, temp);
				}
			}
		}
		
		ArrayList result = new ArrayList();
		for(int i=SimList.size()-1; i>=0; i--){
			result.add(SimList.get(i).getId());
			System.out.println(SimList.get(i).getSim());
		}
		System.out.println(result);
		
		return result;
	}
	
	//首页推荐
	public List RecommendHomepage(int id) throws RuntimeException, IOException 
	{
		//配置数据访问；使用数据库
		Connection cxn = DbUtil.getCurrentConnection();
		JDBCRatingDAO dao =  JDBCRatingDAO.newBuilder().build(cxn);
		
		//载入lenskit算法
		LenskitConfiguration config = ConfigHelpers.load(new File("etc/item-item.groovy"));
		
		//把数据加入配置
		config.addComponent(dao);
		//从配置和数据源创建一个推荐引擎，这个引擎会计算相似矩阵并返回一个推荐
		LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config);

		LenskitRecommender rec = engine.createRecommender();
		//得到物品推荐
		ItemRecommender itemRec = rec.getItemRecommender();
		
		//为用户得到4个推荐
		ResultList recs = itemRec.recommendWithDetails(id, 4, null, null);
		
		ArrayList list=new ArrayList<>();
		
		System.out.format("Recommendations for user %d:\n", id);
		for(Result item : recs){
			Long item_id=item.getId();
			list.add(item_id);
			//System.out.format("\t%d (%s): %.2f\n", item.getId(),names.getItemName(item.getId()), item.getScore());
		}
		System.out.println(list);
		return list;
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
	  
		JSONObject jObject = new JSONObject();
		jObject.put("Ariticles", jArray);
		
		jObject.put("OWLresult", owlresult);
		
		jObject.put("question", oldquestion);
		
		jObject.put("pagenumber",pagenumber );
		
		jObject.put("total", list.size());

		return jObject.toString();

	}
	
	public String getRelatedKnowledge(String title){
		title=title.replace(" ", "");
		String result=new GetKnowledge().getKeyWord(title);
		return result;
	}

	public String getKnowledgeDetail(String title) throws Exception{
		
		String entitylist=HttpRequestUtil.sendGet("http://knowledgeworks.cn:30001/", "p="+title);
//		System.out.println(entitylist);
		//去掉两边多余字符
		String[] entitys=entitylist.replace("[\"","").replace("\"]","").split("\", \"");
		JSONArray jArray = new JSONArray();
		
		for (int i = 0; i <entitys.length; i++) {
			int tag=0;
//			System.out.println(entitys[i]);
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
		
//		System.out.println(jObject.toString());
		
		return jObject.toString();
	}
	
	//测试用
	public String search(String title,int pagenumber,int pagesize){

		List<Ariticle> list = LuceneIndex.QueryIndex(title);
		
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
	  
		JSONObject jObject = new JSONObject();
		jObject.put("Ariticles", jArray);

		jObject.put("question", title);
		
		jObject.put("pagenumber",pagenumber );
		
		jObject.put("total", list.size());

		return jObject.toString();
	}

	
	public String search2(String title,int pagenumber,int pagesize){
		
		title=title.replace(" ", "");
		String result=new GetKnowledge().getKeyWord(title);
		
		System.out.println(result);
		
		List<Ariticle> list = LuceneIndex.QueryIndex(title);
		
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
	  
		JSONObject jObject = new JSONObject();
		jObject.put("Ariticles", jArray);

		jObject.put("question", title);
		
		jObject.put("pagenumber",pagenumber );
		
		jObject.put("total", list.size());

		return jObject.toString();
	}
}

//44 45 46 15 14 65 68 6 47 48


/*Recommendations for user 1:
	40870 (C.R.A.Z.Y. (2005)): 5.63
	670 (World of Apu, The (Apur Sansar) (1959)): 5.59
	666 (All Things Fair (Lust och f盲gring stor) (1995)): 5.58
	854 (Ballad of Narayama, The (Narayama Bushiko) (1958)): 5.57
	4384 (Lumumba (2000)): 5.57
	138 (Neon Bible, The (1995)): 5.56
	128 (Jupiter's Wife (1994)): 5.53
	1412 (Some Mother's Son (1996)): 5.53
	2607 (Get Real (1998)): 5.51
	26082 (Harakiri (Seppuku) (1962)): 5.50
Recommendations for user 2:
	3222 (Carmen (1984)): 6.70
	1669 (Tango Lesson, The (1997)): 6.10
	7820 (Virgin Spring, The (Jungfruk盲llan) (1960)): 6.04
	5968 (Miami Blues (1990)): 5.98
	90524 (Abduction (2011)): 5.96
	3127 (Holy Smoke (1999)): 5.94
	3881 (Phish: Bittersweet Motel (2000)): 5.94
	5221 (Harrison's Flowers (2000)): 5.83
	25763 (Pandora's Box (B眉chse der Pandora, Die) (1929)): 5.73
	2388 (Steam: The Turkish Bath (Hamam) (1997)): 5.73
*/
