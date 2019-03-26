/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Process;
import com.DBUtil.EquipmentDao;
import com.DBUtil.FaultFAQDao;
import com.DBUtil.SymptomDao;
import com.DBUtil.infoDao;
import com.Model.SegmentWord;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.nlpir.wordseg.CLibrary;
import com.sun.jna.Native;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class OwlProcess {
	// http://www.qtech.edu.cn/ontologies/myontology.owl#
	private static String NS = "http://www.qtech.edu.cn/ontologies/myontology.owl#";// owl本体命名空间
	private static String PNS = "http://www.cidoc-crm.org/cidoc-crm/";// owl本体命名空间
	private static String owlPath = "file:F:/www/jinggangshan/webroot/Ontology/myOntology.owl";// 本体路径
	
	private String rulePath = "file:F:/www/jinggangshan/webroot/Ontology/CultureOntology.rules";// 规则文件路径

	private OntModel om;
	private List rules;
	private GenericRuleReasoner reasoner;
	private InfModel inf;
	private static OwlProcess instance = null;

	private OwlProcess() {
		om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		om.read(owlPath);
		rules = Rule.rulesFromURL(rulePath);
		reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);
		reasoner.setDerivationLogging(true);
		reasoner.setTransitiveClosureCaching(true);
		inf = ModelFactory.createInfModel(reasoner, om);

	}

	public synchronized static OwlProcess getInstance() {
		if (instance == null) {
			instance = new OwlProcess();
		}
		return instance;
	}

	// 1、 通过子类遍历其所属的父类
	public ArrayList<String> QuerySuperClass(String classString) {
		ArrayList<String> resultList = new ArrayList<String>();
		// OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// om.read(owlPath);
		String URI = NS + classString;
		OntClass oc = om.getOntClass(URI);
		if (oc != null) {
			ExtendedIterator<OntClass> it = oc.listSuperClasses();
			if (it.hasNext()) {
				OntClass sp = it.next();// 获取父类；sp.getURI();获取父类的URI
				String str = sp.getLocalName();
				resultList.add(str);
			}
		} else {
			System.out.println("Warn:类不存在");
		}
		// om.close();
		return resultList;
	}

	// 2、 通过父类遍历输出所有子类
	public ArrayList<String> QuerySubClass(String classString) {
		ArrayList<String> resultList = new ArrayList<String>();
		// OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// om.read(owlPath);
		String URI = NS + classString;
		OntClass oc = om.getOntClass(URI);
		if (oc != null) {
			ExtendedIterator<OntClass> it = oc.listSubClasses();// 遍历本体类，找到当前类的所有子类
			while (it.hasNext()) {
				OntClass sb = it.next();
				String child = sb.getLocalName();// 获取子类的名称
				resultList.add(child);
			}
		} else {
			System.out.println("Warn:类不存在");
		}
		// om.close();
		return resultList;
	}

	// 3、 根据本体类遍历输出所有实例(个体)
	public ArrayList<String> QueryIndividualOfClass(String classString) {
		ArrayList<String> resultList = new ArrayList<String>();
		// OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		// om.read(owlPath);
		String URI = NS + classString;
		OntClass oc = om.getOntClass(URI);
		if (oc != null) {
			// 循环输出该类别下的所有实例(个体)
			for (Iterator i = om.listIndividuals(oc); i.hasNext();) {
				Individual ind = (Individual) i.next();
				resultList.add(ind.getLocalName());
			}// Individual ends
		} else {
			System.out.println("Warn:类不存在");
		}
		// om.close();
		return resultList;
	}

	// 4、 根据实例(个体)输出所属类，以及所属类的相关属性
	public ArrayList<String> QueryClassOfIndividual(String classString) {
		ArrayList<String> resultList = new ArrayList<String>();
	
		om.setStrictMode(false);
		String URI = NS + classString;
		Individual instance = (Individual) om.getIndividual(URI);
	
		if (instance != null) {

			OntClass classont = instance.getOntClass();
			resultList.add(classont.getLocalName());
		
		} else {
			System.out.println("Warn:实例(个体)空");
		}
		return resultList;
	}

	public ArrayList<String> QueryClassProperty(String classString) {
		ArrayList<String> resultArrayList = new ArrayList<String>();
		// OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		om.setStrictMode(false);
		// om.read(owlPath);
		String URI = NS + classString;
		Individual instance = (Individual) om.getIndividual(URI);
		if (instance == null) {
			return resultArrayList;
		}
		OntClass classont = instance.getOntClass();
		if (classont == null) {
			return resultArrayList;
		}
		for (Iterator ipp = classont.listDeclaredProperties(); ipp.hasNext();) {
			OntProperty p = (OntProperty) ipp.next();

			resultArrayList.add(p.getLocalName());
		}
		return resultArrayList;
	}

	// 5、 根据主体S(资源)、客体O(值)推理得到对应的谓词(属性P)
	public ArrayList<String> QueryPropertyOfIndividual(String subjectString,
			String objectString) {
		ArrayList<String> resultList = new ArrayList<String>();

		System.out.println(rulePath);
		System.out.println(rules);

		Resource S = om.getResource(NS + subjectString);
		Resource O = om.getResource(NS + objectString);
		Resource configuration = om.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		StmtIterator stmtIter = inf.listStatements(S, null, O);
		if (!stmtIter.hasNext() || stmtIter == null) {
			System.out.println(S.getLocalName() + " 和 " + O.getLocalName()
					+ "  没有直接关系！");
		}
		while (stmtIter.hasNext()) {
			Statement sm = stmtIter.nextStatement();
			resultList.add(sm.getPredicate().getLocalName());
		}
		// inf.close();
		// om.close();
		return resultList;
	}

	// 6、 根据谓词(属性P)得到具备该属性关系的主体S(资源)、客体O(值)
	public ArrayList<String> SameIndividual(String propertyString) {
		ArrayList<String> resultList = new ArrayList<String>();

		Property P = om.getProperty(NS + propertyString);
		Resource configuration = om.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		for (ResIterator i = inf.listSubjectsWithProperty(P); i.hasNext();) {
			resultList.add(i.nextResource().getLocalName());
		}

		return resultList;
	}

	// 7、 根据主体S(资源)、谓词(属性P)推理得到对应的客体O(值)
	public ArrayList<String> ReasonerObject(String subjectString,
			String propertyString) {
     //  System.out.println("aaa:"+subjectString+"bbb:"+propertyString);
		ArrayList<String> resultList = new ArrayList<String>();

		Resource S = om.getResource(NS + subjectString);
		//System.out.print(S.getLocalName());
		Property P = om.getProperty(propertyString);
		//System.out.print(P.getLocalName()+":"+propertyString);
		if(propertyString.length()<20){
			P = om.getProperty(PNS + propertyString);
		}
		Resource configuration = om.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		for (NodeIterator j = inf.listObjectsOfProperty(S, P); j.hasNext();) {
			Resource node = (Resource) j.next();
			resultList.add(node.getLocalName());
		}
		// inf.close();
		// om.close();
		return resultList;
	}

	// 8、 根据客体O(值)、谓词(属性P)推理得到对应的主体S(资源)
	public ArrayList<String> ReasonerSubject(String propertyString,
			String objectString) {
		ArrayList<String> resultList = new ArrayList<String>();
        
		Resource O = om.getResource(NS + objectString);
		Property P = om.getProperty(propertyString);
		if(propertyString.length()<25){
			P = om.getProperty(PNS + propertyString);
		}
	
		Resource configuration = om.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		// InfModel inf = ModelFactory.createInfModel(reasoner, om);
		for (ResIterator j = inf.listSubjectsWithProperty(P, O); j.hasNext();) {
			Resource node = (Resource) j.next();
			resultList.add(node.getLocalName());
		}
		// inf.close();
		// om.close();
		return resultList;
	}

	// 输出实例的等价实例（个体）
	public ArrayList<String> SameOfIndividual(String str91, String str92) {
		ArrayList<String> resultList = new ArrayList<String>();
		Resource R = om.getResource(NS + str91);
		Property P = om.getProperty(NS + str92);
		Resource configuration = om.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		// InfModel inf = ModelFactory.createInfModel(reasoner, om);
		for (ResIterator j = inf.listResourcesWithProperty(P, R); j.hasNext();) {
			Resource node = (Resource) j.next();
			resultList.add(node.getLocalName());
		}
		// inf.close();
		// om.close();
		return resultList;
	}

	// 返回用户问句和本体关联的所有主谓个体对

	
	// 返回根节点（功能，现象等）
	public ArrayList<String> GetSuperClass(ArrayList<String> list) {
		ArrayList<String> resultArrayList = new ArrayList<String>();
		// OwlProcess op = new OwlProcess();
		for (String word : list) {
			ArrayList<String> classof = QueryClassOfIndividual(word);
			if (classof != null) {
				System.out.println(word + "所属类：" + classof);
				for (String string : classof) {
					ArrayList<String> temp = QuerySuperClass(string);
					if (temp != null && !temp.isEmpty()) {
						resultArrayList.addAll(temp);

					} else {
						resultArrayList.add(string);
						System.out.println("上层节点：" + string);
					}
					System.out.println("根节点：" + QuerySuperClass(string));
				}
			}
		}
		return resultArrayList;
	}

	/**
	 * 
	 * @param list
	 * @return 返回列表中的实例
	 */
	public ArrayList<String> GetIndividuals(ArrayList<String> list) {
		ArrayList<String> result = new ArrayList<String>();
		for (String word : list) {
			// OntModel om =
			// ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
			// om.read(owlPath);
			String URI = NS + word;
			Individual instance = (Individual) om.getIndividual(URI);
			// System.out.println("实例(个体)名为：" +
			// instance.getLocalName());//输出当前实例(个体)名
			if (instance != null) {
				result.add(instance.getLocalName());
			}
		}
		return result;
	}

	public ArrayList<String> owlExpandEquip(ArrayList<String> expandResult) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<String> owlGetAnswer(ArrayList<String> subject,
			ArrayList<String> properties) {
		ArrayList<String> result = new ArrayList<>();
		OwlProcess op = new OwlProcess();
		for (int i = 0; i < subject.size(); i++) {
			System.out.print("owl推理结果：");
			for (int j = 0; j < properties.size(); j++) {
				ArrayList<String> tmp1 = op.ReasonerObject(subject.get(i),
						properties.get(j));
				ArrayList<String> tmp2 = op.ReasonerSubject(properties.get(j),
						subject.get(i));

				for (int k = 0; k < tmp1.size(); k++) {
					System.out.print(tmp1.get(k) + " ");
					result.add(tmp1.get(k));
				}
				for (int k = 0; k < tmp2.size(); k++) {
					System.out.print(tmp2.get(k) + " ");
					result.add(tmp2.get(k));
				}

			}

		}

		return result;

	}
	public String relatedKnowledge(ArrayList<SegmentWord> keyword,String type){

		OwlProcess op = new OwlProcess();
		
		//唯一的变量，进行简单属性映射
		Map<String, String> typeMap=new HashMap<String,String>();
		typeMap.put("红色诗词", "created_redpoem");
		typeMap.put("红色著作", "created_Red_writings");
		typeMap.put("", "created");
		typeMap.put("红色文物", "created_Redrelic");
		
		JSONObject jObject = new JSONObject();
		JSONArray worksarray = new JSONArray();
		JSONArray eventsarray = new JSONArray();
		JSONArray peoplearray = new JSONArray();
		
		//用于去重 
		ArrayList<String> tmp=new ArrayList<>();
		
		for (SegmentWord word : keyword) {
		
			String URI = NS + word.getWord();
			Individual instance = (Individual) om.getIndividual(URI);
			
			if (instance != null) {
				OntClass classont = instance.getOntClass();
				String classname = classont.getLocalName();
				String instancename = instance.getLocalName();

				if (classname.equals("E21_Person")) {
					if(!tmp.contains(instancename)){
						peoplearray.put(instancename);
						tmp.add(instancename);
					}
					
					String workstype = typeMap.get(type);

					ArrayList<String> works = op.ReasonerObject(instancename,
							NS.concat(workstype));
                    if(works.size()==0){
                    	works = op.ReasonerObject(instancename,
    							NS.concat("created"));
                    }
					for (int i = 0; i < works.size(); i++) {
						String work=works.get(i);
						System.out.println(work);
						if(!tmp.contains(work)){
							worksarray.put(work);
							tmp.add(work);
							
						}
					}

					ArrayList<String> events = op.ReasonerSubject(
							"P11_had_participant", instancename);

					for (int i = 0; i < events.size(); i++) {
						String event=events.get(i);
						System.out.println(event);
						if(!tmp.contains(event)){
						   eventsarray.put(event);
						   tmp.add(event);
						}

					}

				} else if (classname.equals("E5_Event")) {
					if(!tmp.contains(instancename)){
					    eventsarray.put(instancename);
					    tmp.add(instancename);
					}
					ArrayList<String> people = op.ReasonerObject(instancename,
							"P11_had_participant");
					for (int i = 0; i < people.size(); i++) {
						String peo=people.get(i);
						System.out.println(peo);
						if(!tmp.contains(peo)){
						    peoplearray.put(peo);
						    tmp.add(peo);
						}

					}
					ArrayList<String> works = op.ReasonerSubject(
							"P67_refers_to", instancename);
                       
					for (int i = 0; i < works.size(); i++) {
						String work=works.get(i);
						System.out.println(work);
						if(!tmp.contains(work)){
							worksarray.put(work);
							tmp.add(work);
							
						}

					}
					
				}
			}
			 
		}
		
		
		jObject.put("相关人物", peoplearray);
		jObject.put("相关事件", eventsarray);
		
		jObject.put("相关作品", worksarray);
		
		System.out.println(jObject.toString());
		
		return jObject.toString();
		
		
		
		
		
	}
	
	public static String GetRelatedWord(String subject) {
		String result ="";
		OwlProcess op = new OwlProcess();
		
		String queryString1 = "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>"
				+ "PREFIX my: <http://www.qtech.edu.cn/ontologies/myontology.owl#>"
				+ "SELECT ?p  WHERE { my:"+subject+" ?s ?p . } limit 2";
		
		String queryString2 = "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>"
				+ "PREFIX my: <http://www.qtech.edu.cn/ontologies/myontology.owl#>"
				+ "SELECT ?p  WHERE {  ?p ?s my:"+subject+". } limit 2";

		Query query1 = QueryFactory.create(queryString1);
		Query query2 = QueryFactory.create(queryString2);
		
		QueryExecution qe1 = QueryExecutionFactory.create(query1, op.om);
		QueryExecution qe2 = QueryExecutionFactory.create(query2, op.om);
		
		ResultSet results1 = qe1.execSelect();

		while (results1.hasNext()) {
			QuerySolution qs = results1.next();
			//System.out.println(qs.get("p"));	
			String url=qs.get("p").toString();
			if(url.contains("#")){
				result+=url.split("#")[1];
			}
		}

		ResultSet results2 = qe2.execSelect();

		while (results2.hasNext()) {
			QuerySolution qs = results2.next();
			//System.out.println(qs.get("p"));
			String url=qs.get("p").toString();
			if(url.contains("#")){
				result+=url.split("#")[1];
			}
		}
		// ResultSetFormatter.out(System.out, results, query);
		qe1.close();
		qe2.close();
		System.out.println(result);
       
		return result;
	}
	
	public static void main(String[] args){
		GetRelatedWord("八七会议");
		
	}


}
