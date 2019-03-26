/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Process;

import com.DBUtil.FAQDao;
import com.DBUtil.FaultFAQDao;
import com.DBUtil.SymptomDao;
import com.DBUtil.infoDao;
import java.util.ArrayList;

/**
 * 1、查询到数据库中的所有现象 2、根据现象推理与其相关的设备 3、查询faq表中 like '%设备%' and like '%现象%'的所有问句
 * 4、将设备+现象存入faultfaq表，并添加上所有的question_id
 *
 * @author zhuwangnan
 */
public class OwlFaqProcess {

    public void owlfaq() {
        SymptomDao dao = new SymptomDao();
        //1、首先得到所有现象
        ArrayList<String> symList = dao.GetAllSymptom();
        if (!symList.isEmpty()) {
            //对现象进行遍历
            for (int sid = 0; sid < symList.size(); sid++) {
                String symstring = symList.get(sid);
                //对单个现象推理相关设备，并补充questionid
                faultprocess(symstring);
            }
        }
    }

    public void faultprocess(String symname) {
        FAQDao faqDao = new FAQDao();
        OwlProcess op = OwlProcess.getInstance();
        String prostr = "SymOf";
        //对现象进行推理，得到对应的所有相关设备
        ArrayList<String> equList = op.ReasonerObject(symname, prostr);//查询主体和谓词对应的客体
        if (!equList.isEmpty()) {
            for (int x = 0; x < equList.size(); x++) {
                String equname = equList.get(x);
                //根据设备和现象查询数据库中所有的问句的id
                ArrayList<String> qidList = faqDao.getQidString(equname, symname);
                //查询推理与之同义的设备
                String proString = "SameOf";
                ArrayList<String> sameequList = op.ReasonerObject(equname, proString);//查询主体和谓词对应的客体
                if (!sameequList.isEmpty()) {
                    //循环获取同义设备对应的所有问题id
                    for (int sameequid = 0; sameequid < sameequList.size(); sameequid++) {
                        String sameequname = sameequList.get(sameequid);
                        ArrayList<String> sameequqidList = faqDao.getQidString(sameequname, symname);
                        //循环遍历，存储id到qidList中
                        for (int sel = 0; sel < sameequqidList.size(); sel++) {
                            String selid = sameequqidList.get(sel);
                            if (!qidList.contains(selid)) {
                                qidList.add(selid);
                            }
                        }
                    }
                }
                //循环遍历qidlist，将其组成String，并根据需要insert或update
                String QidString = "";
                if (!qidList.isEmpty()) {
                    for (int id = 0; id < qidList.size(); id++) {
                        String questionid = qidList.get(id);
                        if (id == 0) {
                            QidString = QidString + questionid;
                        } else {
                            QidString = QidString + "," + questionid;
                        }
                    }
                }
                //如果存在故障执行update，否则进行insert
                String faultNameString = equname.trim() + symname.trim();
                FaultFAQDao faultFAQDao = new FaultFAQDao();
                if (faultFAQDao.ExistFault(faultNameString)) {
                    faultFAQDao.UpdateFaultFaq(faultNameString, QidString);
                } else {
                    faultFAQDao.InsertFaultFaq(faultNameString, QidString);
                }
            }
        }
    }

    public static void main(String args[]) {
        OwlFaqProcess owp = new OwlFaqProcess();
        owp.owlfaq();
    }
}
