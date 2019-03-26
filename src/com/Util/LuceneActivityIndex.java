package com.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.DBUtilJing.ActivityDao;
import com.Jing.model.Activity;
import com.Model.Ariticle;
import com.Model.Constants;
import com.Process.LuceneIndex;

public class LuceneActivityIndex {

	public static void main(String[] args) throws Exception, Exception {

		//LuceneActivityIndex.QueryIndex("���߻���");
		LuceneActivityIndex.BuildIndex(new ActivityDao().GetAllActivity());
	}
	public static void BuildIndex(ArrayList<Activity> activities) throws Exception {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_2);
		
		Directory dire = FSDirectory.open(new File("E:\\Programs\\jinggangshan\\luceneSpace\\indexforactivity"));;
		
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2,
				analyzer);
		
		
		IndexWriter iw = new IndexWriter(dire, iwc);
		
		
		for (int i = 0; i < activities.size(); i++) {
			Activity act=activities.get(i);
			Document doc = new Document();
			doc.add(new TextField("id",  Integer.toString(act.getId()), Store.YES));	
			doc.add(new TextField("title", act.getTitle(), Store.YES));	
			doc.add(new TextField("discription", act.getDescription(), Store.YES));
			System.out.println(i);
			iw.addDocument(doc);
			iw.commit();
		}

	}
	
	public static ArrayList<Activity> QueryIndex(String title) throws Exception {
		ArrayList<Activity> result=new ArrayList<>();
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_2);
		
		// ������ѯ
		QueryParser parser = new QueryParser(Version.LUCENE_4_10_2,
				"discription", analyzer);
		Query query = parser.parse(title);
	
		Directory dire = FSDirectory.open(new File("E:\\Programs\\jinggangshan\\luceneSpace\\indexforactivity"));
		IndexReader ir = DirectoryReader.open(dire);
		IndexSearcher is = new IndexSearcher(ir);
		TopDocs td = is.search(query, 1000);
		
		ScoreDoc[] sds = td.scoreDocs;
		for (ScoreDoc sd : sds) {
			Document d = is.doc(sd.doc);
			String name = d.get("title");
			String dis=d.get("discription");
			String id=d.get("id");
            
	        if(sd.score>=0.2){
	        	System.out.println(name+" "+id);
	        	int idd=Integer.parseInt(id);
				result.add(new Activity(idd,name,dis));
			}
	     	
		}
		return result;
		
		
		
		
	}
}
