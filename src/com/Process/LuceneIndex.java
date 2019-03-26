package com.Process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.Model.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
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
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class LuceneIndex {

	/**
	 * main方法
	 * @param args
	 * @throws Exception
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception, Exception {
//		Scanner input=new Scanner(System.in);
//		System.out.println("输入问题");
//		String question=input.next();
//		question=question.trim();
//	    QueryIndex(question);
		LuceneIndex.createIndexMethod();
	}
	
	public static List<Ariticle> QueryIndex(String userQuestion) {
		List<Ariticle> result = null;
		try {
			long time1 = Calendar.getInstance().getTimeInMillis();
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_2);
		
			// 创建查询
			QueryParser parser = new QueryParser(Version.LUCENE_4_10_2,
					"title", analyzer);
//			QueryParser parser = new QueryParser(Version.LUCENE_4_10_2, "content", analyzer);
			
			Query query = parser.parse(userQuestion);
			// Query query = parser.parse("科技");
			// LuceneIndex.search(query);
			result = LuceneIndex.searchList(query);
			long time2 = Calendar.getInstance().getTimeInMillis();
//			System.out.println(time2 - time1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * 外部创建索引方法
	 * @return
	 */
	public static boolean createIndexMethod(){
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_2);
		// //创建索引
		try {
			LuceneIndex.createIndex(analyzer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 创建索引
	 * 
	 * @param analyzer
	 * @throws Exception
	 */
	public static void createIndex(Analyzer analyzer) throws Exception {
		Directory dire = FSDirectory.open(new File(Constants.INDEX_STORE_PATH));
		
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2,
				analyzer);
		IndexWriter iw = new IndexWriter(dire, iwc);
		
		LuceneIndex.addDoc(iw);
		iw.close();
	}

	/**
	 * 动态添加Document
	 * 
	 * @param iw
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void addDoc(IndexWriter iw) throws Exception {
		File[] files = new File(Constants.INDEX_FILE_PATH).listFiles(); 
		for (File file : files) {
			Document doc = new Document();

			
			Ariticle tmp=Ariticle.getAriticle(file);
		
			//索引
			FieldType type=new FieldType(TextField.TYPE_STORED); 
			type.setStoreTermVectorOffsets(true);//记录相对增量
			type.setStoreTermVectorPositions(true);//记录位置信息
			type.setStoreTermVectors(true);//存储向量信息
			type.freeze();//阻止改动信息
			Field title=new Field("title1",tmp.getTitle(),type);
            Field content=new Field("content1",tmp.getContent(),type);
            
			doc.add(new TextField("title", tmp.getTitle(), Store.YES));	
			doc.add(new TextField("url", tmp.getUrl(), Store.YES));	
			doc.add(new TextField("source", tmp.getSource(), Store.YES));
			doc.add(new TextField("content", tmp.getContent(), Store.YES));
			doc.add(new TextField("path",file.getAbsolutePath(),Store.YES));
	        doc.add(title);
	        doc.add(content);
			
			iw.addDocument(doc);
			iw.commit();
		}
	}

	/**
	 * 获取文本内容
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String getContent(File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = null;
		}
		return sb.toString();
	}
	
	


	/**
	 * 搜索
	 * 
	 * @param query
	 * @throws Exception
	 */
	private static void search(Query query) throws Exception {
		Directory dire = FSDirectory.open(new File(Constants.INDEX_STORE_PATH));
		IndexReader ir = DirectoryReader.open(dire);
		IndexSearcher is = new IndexSearcher(ir);
		TopDocs td = is.search(query, 1000);
		System.out.println("共为您查找到" + td.totalHits + "条结果");
		ScoreDoc[] sds = td.scoreDocs;
		for (ScoreDoc sd : sds) {
			System.out.println();
			Document d = is.doc(sd.doc);
			System.out.println(d.get("path") + ":[" + d.get("path") + "]");
			System.out.println(d.get("name"));
			System.out.println(sd.score);
		}
	}
	/**
	 * 搜索
	 * 
	 * @param query
	 * @throws Exception
	 */
	private static List<Ariticle> searchList(Query query) throws Exception {
		List<Ariticle> results = new ArrayList<Ariticle>();
		Directory dire = FSDirectory.open(new File(Constants.INDEX_STORE_PATH));
		IndexReader ir = DirectoryReader.open(dire);
		IndexSearcher is = new IndexSearcher(ir);
		TopDocs td = is.search(query, 1000);
		
		QueryScorer score=new QueryScorer(query, "content");//传入评分  
	    SimpleHTMLFormatter fors=new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");//定制高亮标签 
	    Highlighter  highlighter=new Highlighter(fors,score);
	    
		System.out.println("共为您查找到" + td.totalHits + "条结果");
		ScoreDoc[] sds = td.scoreDocs;
		for (ScoreDoc sd : sds) {
			Document d = is.doc(sd.doc);
			String fileName = d.get("title");
			String url=d.get("url");
			String content=d.get("content");
			String source=d.get("source");
//			System.out.println(content);
			
			String tmp1=d.get("title1");
			String tmp2=d.get("content1");
//			
//			System.out.println(tmp1+" "+tmp2);
			
			
//			//高亮的标签和内容
//            TokenStream token=TokenSources.getAnyTokenStream(is.getIndexReader(), sd.doc, tmp1, new StandardAnalyzer());//获取tokenstream  
//	        Fragmenter  fragment=new SimpleSpanFragmenter(score);  
//	        highlighter.setTextFragmenter(fragment);  
//	        String redtitle=highlighter.getBestFragment(token, fileName);//获取高亮的片段，可以对其数量进行限制 
	        
//	        TokenStream token1=TokenSources.getAnyTokenStream(is.getIndexReader(), sd.doc, tmp2, new StandardAnalyzer());//获取tokenstream   
//	        String redcontent=highlighter.getBestFragment(token1, content);//获取高亮的片段，可以对其数量进行限制
	        String redcontent = highlighter.getBestFragment(new StandardAnalyzer(), "content", d.get("content")) ;  
//	        String redtitle = highlighter.getBestFragment(new StandardAnalyzer(), "title", d.get("title")) ;  
//            System.out.println(str);     
	        if(sd.score>=0.3){
				results.add(new Ariticle(fileName,url,source,redcontent, sd.score));
			}
	     	
		}
		return results;
	}
	
}
