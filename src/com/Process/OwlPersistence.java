/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Process;

import com.DBUtil.SymptomDao;
import com.DBUtil.infoDao;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class OwlPersistence {

    public static ArrayList<String> GetEquipmentFromOwl() {
        ArrayList<String> resultArrayList = new ArrayList<String>();
        OwlProcess op = OwlProcess.getInstance();
        System.out.println(op.QuerySubClass("功能"));
        ArrayList<String> allclassArrayList = op.QuerySubClass("功能");
        for (String classString : allclassArrayList) {
            System.out.println(op.QueryIndividualOfClass(classString));
            resultArrayList.addAll(op.QueryIndividualOfClass(classString));
       }
        return resultArrayList;
    }

    public static void main(String[] args) {
//        ArrayList<String> resultArrayList = GetEquipmentFromOwl();
//        EquipmentDao dao = new EquipmentDao();
//        for (String string : resultArrayList) {
//            if (!dao.ExistEquipment(string)) {
//                System.out.println(string);
//                dao.InsertEquipment(string);
//            }
//        }
        ArrayList<String> resultArrayList = GetEquipmentFromOwl();
        SymptomDao dao = new SymptomDao();
        for (String string : resultArrayList) {
            if (!dao.ExistSymptom(string)) {
                System.out.println(string);
                dao.InsertSymptom( string);
            }
        }
    }
}
