/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtilJing;


import com.Jing.model.Activity;
import com.Model.Chat;
import com.Model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ActivityDao {

    /**
     * tempfaq�����ʾ侭�ִ�ȥͣ��Ϊ�գ���ӵ�chat����
     */
    public void InsertActivity(String title, String content) {
        Connection con = null;
        con = DbUtilJing.getSqlConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into activity(title,content,poster,category,district,address,number,needorder,user_id,type,isdraft,phone,lat,lng) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, "upload/activity/Imgea205b3cae634525a742656f6e117651_600_450.jpg");
            ps.setInt(4, 1);   
            ps.setInt(5, 1);
            ps.setString(6, "�ൺ����ѧ");
            ps.setInt(7, 10);
            ps.setInt(8, 0);
            ps.setInt(9, 29);
            ps.setInt(10, 1);
            ps.setInt(11, 0);
            ps.setString(12, "15154298345");
            ps.setString(13, "36.10611");
            ps.setString(14, "120.371329");
                        
            
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtilJing.closeCurrentConnection();
        }
    }
    
    public ArrayList<Activity> GetAllActivity(){
    	
    	 ArrayList<Activity> arrayList = new ArrayList<Activity>();
         Connection con = null;
         con = DbUtilJing.getCurrentConnection();
         PreparedStatement ps;
         Activity tmp;
         try {
             ps = con.prepareStatement("select * from Activity ");
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                 tmp = new Activity();
                 tmp.setId(rs.getInt("id"));
               
                 tmp.setTitle(rs.getString("title"));
                 tmp.setDescription(rs.getString("content"));;
                 arrayList.add(tmp);
             }
         } catch (SQLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } finally {
             DbUtilJing.closeCurrentConnection();
         }
         return arrayList;
    }
    
    

   
}
