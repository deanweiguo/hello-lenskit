package com.SystemFlow;

import java.util.ArrayList;

import com.DBUtil.*;
import com.Model.SegmentWord;
//import com.Process.OwlProcess;
import com.method.hownetsim.XiaConceptParser;

public class SynonymExpand {

//    private OwlProcess op = OwlProcess.getInstance();

    /**
     * 对去停结果进行同义词扩展
     *
     * @author zhuwangnan
     * @param WordsList 词列表
     * @return 扩展后关键词
     *
     */
    public ArrayList<String> SynonymsExpand(ArrayList<String> WordsList) {
        ArrayList<String> expandList = new ArrayList<String>();
        EquipmentDao equipmentDao=new EquipmentDao();
        SymptomDao symptomDao=new SymptomDao();
        SynonymsDao synonymsDao=new SynonymsDao();

        for (int i = 0; i < WordsList.size(); i++) {
            //第一步：同义词扩展
            //查询是否存在该同义词
            if ("手机".equals(WordsList.get(i))) {
                expandList.add("手机");
                continue;
            }
            if (equipmentDao.ExistEquipment(WordsList.get(i)) || symptomDao.ExistSymptom(WordsList.get(i))) {
                expandList.add(WordsList.get(i));
                continue;
            }
            String WordIdentity = synonymsDao.GetIdentityOfSynonyms(WordsList.get(i));
            if (WordIdentity != "") {
                //存在，添加所有同义词语到ArrayList
                expandList.add(WordsList.get(i));
                ArrayList<String> synWordList = synonymsDao.GetSynonymsByIdentity(WordIdentity);
                for (int j = 0; j < synWordList.size(); j++) {
                    if (XiaConceptParser.getInstance().getSimilarity(WordsList.get(i), synWordList.get(j)) > 0.8) {
                        if (!expandList.contains(synWordList.get(j))) {
                            expandList.add(synWordList.get(j));
                        }
                    }
                }
            } else {
                if (!expandList.contains(WordsList.get(i))) {
                    expandList.add(WordsList.get(i));
                }
            }
        }
        //第二步，本体查询扩展
//        ArrayList<String> owlList = OwlExpand(WordsList);
//        if (!owlList.isEmpty()) {
//            for (int m = 0; m < owlList.size(); m++) {
//                if (!expandList.contains(owlList.get(m))) {
//                    expandList.add(owlList.get(m));
//                }
//            }
//        }
//        System.out.println(expandList);
        return expandList;

    }

//    public ArrayList<String> OwlExpand(ArrayList<String> WordsList) {
//        ArrayList<String> owlResult = new ArrayList<String>();
//        //OwlProcess op = new OwlProcess();
//        EquipmentDao equipmentDao=new EquipmentDao();
//        SymptomDao symptomDao=new SymptomDao();
//        ArrayList<String> StopResult = WordsList;
//        //将分词结果映射到设备List和现象List
//        ArrayList<String> SymArrayList = new ArrayList<String>();
//        ArrayList<String> EquipArrayList = new ArrayList<String>();
//        for (int s = 0; s < StopResult.size(); s++) {
//            String srString = StopResult.get(s);
//            if (symptomDao.ExistSymptom(srString)) {
//                //先进行等价实例查询扩展，然后添加
//                SymArrayList.add(srString);
//                ArrayList<String> sList = op.SameOfIndividual(srString, "SameOf");
//                for (int r = 0; r < sList.size(); r++) {
//                    String sameobject = sList.get(r);
//                    if (!SymArrayList.contains(sameobject)) {
//                        SymArrayList.add(sameobject);
//                    }
//                }
//            }
//            if (equipmentDao.ExistEquipment(srString)) {
//                EquipArrayList.add(srString);
//                ArrayList<String> sList2 = op.SameOfIndividual(srString, "SameOf");
//                for (int t = 0; t < sList2.size(); t++) {
//                    String samesubject = sList2.get(t);
//                    if (!EquipArrayList.contains(samesubject)) {
//                        EquipArrayList.add(samesubject);
//                    }
//                }
//            }
//        }
//
//        owlResult.addAll(SymArrayList);
//        owlResult.addAll(EquipArrayList);
//        // 扩展设备对应的所有现象
//        for (int i = 0; i < EquipArrayList.size(); i++) {
//            String queryword = EquipArrayList.get(i);
//            ArrayList<String> oList = op.ReasonerObject(queryword, "HasSym");
//            System.out.println(oList);
//            for (int k = 0; k < oList.size(); k++) {
//                if (!owlResult.contains(oList.get(k))) {
//                    owlResult.add(oList.get(k));
//                }
//            }
//        }
//        //扩展现象对应的所有设备
//        for (int i = 0; i < SymArrayList.size(); i++) {
//            String queryword = SymArrayList.get(i);
//            ArrayList<String> sList = op.ReasonerSubject("HasSym", queryword);
//            for (int j = 0; j < sList.size(); j++) {
//                if (!owlResult.contains(sList.get(j))) {
//                    owlResult.add(sList.get(j));
//                }
//            }
//        }
//        System.out.println(owlResult);
//        return owlResult;
//
//    }

    public ArrayList<SegmentWord> SynonymsExpand2(ArrayList<SegmentWord> WordsList) {
        ArrayList<SegmentWord> expandList = new ArrayList<SegmentWord>();
        EquipmentDao equipmentDao=new EquipmentDao();
        SymptomDao symptomDao=new SymptomDao();
        StandardWordDao standardWordDao=new StandardWordDao();
        SynonymsDao synonymsDao=new SynonymsDao();

        for (int i = 0; i < WordsList.size(); i++) {
            //第一步：同义词扩展
            //查询是否存在该同义词
//            if ("手机".equals(WordsList.get(i))) {
//                expandList.add("手机");
//                continue;
//            }
//            if (equipmentDao.ExistEquipment(WordsList.get(i)) || symptomDao.ExistSymptom(WordsList.get(i))||standardWordDao.ExistStandardWord(WordsList.get(i))) {
//                expandList.add(WordsList.get(i));
//                continue;
//            }
            String WordIdentity = synonymsDao.GetIdentityOfSynonyms(WordsList.get(i).getWord());
            if (!"".equals(WordIdentity)&&WordIdentity.substring(WordIdentity.length()-1).equals("=")) {
                //存在，添加所有同义词语到ArrayList
//            	SegmentWord tmp=new SegmentWord(WordsList.get(i));
                expandList.add(WordsList.get(i));
                ArrayList<String> synWordList = synonymsDao.GetSynonymsByIdentity(WordIdentity);
                int count = 0;
                for (int j = 0; j < synWordList.size(); j++) {
                    if (XiaConceptParser.getInstance().getSimilarity(WordsList.get(i).getWord(), synWordList.get(j)) > 0.8) {
                        if (!expandList.contains(synWordList.get(j))) {
                        	SegmentWord tmp=new SegmentWord(synWordList.get(j),WordsList.get(i).getWordclass());
                            expandList.add(tmp);
                        }
                    }
                    if (count == 8) {
                        break;
                    }
                    count++;
                }
            } else {
                if (!expandList.contains(WordsList.get(i))) {
                    expandList.add(WordsList.get(i));
                }
            }
        }
        //第二步，本体查询扩展
//        ArrayList<String> owlList = OwlExpand2(WordsList);
//        if (!owlList.isEmpty()) {
//            for (int m = 0; m < owlList.size(); m++) {
//                if (!expandList.contains(owlList.get(m))) {
//                    expandList.add(owlList.get(m));
//                }
//            }
//        }
        return expandList;

    }
    
    //2017-8-17
    public SegmentWord[][] SynonymsExpand3(ArrayList<SegmentWord> WordsList) {
        
        SegmentWord result[][]=new SegmentWord[WordsList.size()][];

        StandardWordDao standardWordDao=new StandardWordDao();
        SynonymsDao synonymsDao=new SynonymsDao();
        ArrayList<String> tmparray=new ArrayList<>();
        for (int i = 0; i < WordsList.size(); i++) {
        	ArrayList<SegmentWord> expandList = new ArrayList<SegmentWord>();

            String WordIdentity = synonymsDao.GetIdentityOfSynonyms(WordsList.get(i).getWord());
            if (!"".equals(WordIdentity)&&WordIdentity.substring(WordIdentity.length()-1).equals("=")) {
                //存在，添加所有同义词语到ArrayList
//            	SegmentWord tmp=new SegmentWord(WordsList.get(i));
                expandList.add(WordsList.get(i));
                tmparray.add(WordsList.get(i).getWord());
                ArrayList<String> synWordList = synonymsDao.GetSynonymsByIdentity(WordIdentity);
                int count = 0;
                for (int j = 0; j < synWordList.size(); j++) {
                    if (XiaConceptParser.getInstance().getSimilarity(WordsList.get(i).getWord(), synWordList.get(j)) > 0.8) {
                        if (!tmparray.contains(synWordList.get(j))) {
                        	SegmentWord tmp=new SegmentWord(synWordList.get(j),WordsList.get(i).getWordclass());
                            expandList.add(tmp);
                            tmparray.add(tmp.getWord());
//                            result[i][count]=tmp;
                        }
                    }
                    if (count == 5) {
                        break;
                    }
                    count++;
                }
            } else {
                if (!tmparray.contains(WordsList.get(i).getWord())) {
                    expandList.add(WordsList.get(i));
                    tmparray.add(WordsList.get(i).getWord());
//                    result[i][0]=WordsList.get(i);
                }
            }
            result[i]=new SegmentWord[expandList.size()];
            
            for (int j = 0; j < expandList.size(); j++) {
            	result[i][j]=expandList.get(j);
			}
        }
        return result;
    }

    
//    public ArrayList<String> OwlExpand2(ArrayList<String> WordsList) {
//        ArrayList<String> owlResult = new ArrayList<String>();
//        //OwlProcess op = new OwlProcess();
//        EquipmentDao equipmentDao=new EquipmentDao();
//        SymptomDao symptomDao=new SymptomDao();
//        StandardWordDao standardWordDao=new StandardWordDao();
//        ArrayList<String> StopResult = WordsList;
//        //将分词结果映射到设备List和现象List
//        ArrayList<String> SymArrayList = new ArrayList<String>();
//        ArrayList<String> EquipArrayList = new ArrayList<String>();
//        for (int s = 0; s < StopResult.size(); s++) {
//            String srString = StopResult.get(s);
//            if (symptomDao.ExistSymptom(srString)&&!standardWordDao.ExistStandardWord(srString)) {
//                //先进行等价实例查询扩展，然后添加
//                SymArrayList.add(srString);
//                ArrayList<String> sList = op.SameOfIndividual(srString, "SameOf");
//                for (int r = 0; r < sList.size(); r++) {
//                    String sameobject = sList.get(r);
//                    if (!SymArrayList.contains(sameobject)) {
//                        SymArrayList.add(sameobject);
//                    }
//                }
//            }
//            if (equipmentDao.ExistEquipment(srString)&&!standardWordDao.ExistStandardWord(srString)) {
//                EquipArrayList.add(srString);
//                ArrayList<String> sList2 = op.SameOfIndividual(srString, "SameOf");
//                System.out.println("同义词扩展设备：" + sList2);
//                for (int t = 0; t < sList2.size(); t++) {
//                    String samesubject = sList2.get(t);
//                    if (!EquipArrayList.contains(samesubject)) {
//                        EquipArrayList.add(samesubject);
//
//                    }
//                }
////                //本体设备向下扩展
////                ArrayList<String> r3 = op.QueryIndividualOfClass(srString);//查询类的实例
////                for (String temp : r3) {
////                    if (!EquipArrayList.contains(temp)) {
////                        EquipArrayList.add(temp);
////                    }
////                }
//            }
//        }
//
//        owlResult.addAll(SymArrayList);
//        owlResult.addAll(EquipArrayList);
//        return owlResult;
//
//    }

    public static void main(String[] args) {
        //new VSM().run();
    }
}
