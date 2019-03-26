/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SystemFlow;

import com.DBUtil.EquipmentDao;
import com.DBUtil.WordDao;
import com.Model.SegmentWord;
import com.Model.Word;
import com.Process.Equipment;
import com.Process.OwlProcess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jia
 */
public class ContextHandling {

    //
//    public ArrayList<String> GetEquipment(ArrayList<String> ExpandResult) {
//        EquipmentDao equipmentDao = new EquipmentDao();
//        Equipment equipment = new Equipment();
//        ArrayList<String> equipments = equipment.findEquipment(ExpandResult);
//        if (equipments.isEmpty()) {
//            OwlProcess op = OwlProcess.getInstance();
//            ArrayList<String> tempequipmentList = op.owlExpandEquip(ExpandResult);
//            if (tempequipmentList.size() == 1) {
//                equipments.add(tempequipmentList.get(0));
//            } else {
//                for (String temp : tempequipmentList) {
//                    if (equipmentDao.ExistEquipment(temp)) {
//                        equipments.add(temp);
//                    }
//                }
//            }
//        }
//        return equipments;
//        
//    }
	public ArrayList<String> GetKeyword(ArrayList<SegmentWord> ExpandResult) {
    	
    	ArrayList<String> words=new ArrayList<>();
    	for (int i = 0; i < ExpandResult.size(); i++) {
			words.add(ExpandResult.get(i).getWord());
		}
    	WordDao dao=new WordDao();
    	ArrayList<Word> keyword=new ArrayList<Word>();
    	ArrayList<String> subject=new ArrayList<String>();
    	ArrayList<String> property=new ArrayList<String>();
  
    	for (String word : words) {
    		 keyword=dao.getWord(word);
             if(keyword!=null){
            	 for(Word tmp:keyword){
            		 if(tmp.getStype()==1){
            			 if(!property.contains(tmp.getBelongto())){
            				  property.add(tmp.getBelongto());
            			 }	 
            		 }else{
            			 if(!subject.contains(tmp.getWord())){
            			      subject.add(tmp.getWord()); 
            			 }
            		 } 
            		 System.out.println("\n"+tmp.getWord());
            	 }
             }
        }
    	System.out.println("实体："+subject+"  属性："+property);
    	//System.out.println(property);

    	OwlProcess op = OwlProcess.getInstance();
        ArrayList<String> result =op.owlGetAnswer(subject,property);
        
//        ArrayList<String> result =op.owlGetRecomendation(subject,property);
        return result;
     
    }
    
}

