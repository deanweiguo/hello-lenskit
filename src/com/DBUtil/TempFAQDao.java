/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class TempFAQDao {

    /**
     * 删除tempfaq表中的问句
     */
    public void DeleteTempQuestion(String queString) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("delete  from tempfaq where temp_question=?");
            ps.setString(1, queString);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 判断临时FAQ表中是否存在该问句
     *
     * @author liling
     */
    public boolean ExistTempQuestion(String question) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        boolean flag = false;
        try {
            ps = con.prepareStatement("select count(*) as num from tempfaq where temp_question=?");
            ps.setString(1, question);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return flag;
    }

    /**
     * 处理没有得到结果的用户问句存储到临时FAQ表中
     */
    public void InsertTempQuestion(String question) {

        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into tempfaq(temp_question) values(?)");
            ps.setString(1, question);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }
    
     /**
     * 返回tempFAQ表中的所有信息
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<String> GetAllTempQuestion() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from tempfaq where temp_answer is null");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arrayList.add(rs.getString("temp_question").trim());

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }
    
    
    

}
