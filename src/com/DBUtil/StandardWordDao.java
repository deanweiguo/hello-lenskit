/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class StandardWordDao {
    
    
      
    
    /**
     *
     * @param queryWord
     * @return 返回标准化后的词语
     */
    public String getStandardWord(String queryWord) {
        String resultString = "";
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select standardword from word_standardization where word=?");
            ps.setString(1, queryWord);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultString = rs.getString("standardword");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return resultString;
    }

    public boolean ExistStandardWord(String queryWord) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select standardword from word_standardization where word=?");
            ps.setString(1, queryWord);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
    
}
