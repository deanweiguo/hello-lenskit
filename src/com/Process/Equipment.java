/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Process;

import com.DBUtil.EquipmentDao;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Equipment {
    
    //用户问句同义词扩展后其中包含的设备列表
     public ArrayList<String> findEquipment(ArrayList<String> list){
        ArrayList<String> equipmentList=new ArrayList<String>();
        EquipmentDao dao=new EquipmentDao();
        for (String word : list) {
            if(dao.ExistEquipment(word)){
                equipmentList.add(word);
            }
        }
        return equipmentList;
    }
    
}
