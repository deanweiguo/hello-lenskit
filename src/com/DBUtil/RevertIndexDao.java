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
public class RevertIndexDao {
    
       /**
     * 清空所有的索引
     *
     * @author zhuwangnan
     *
     */
    public void TruncateRevertIndex() {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            //ps = con.prepareStatement("update revertindex set location=null");
            ps = con.prepareStatement("truncate table revertindex");
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }

    }

    /**
     * 查询索引表中是否存在该关键词
     *
     * @author liling
     *
     */
    public boolean ExistIndexWord(String word) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        boolean flag =false;

        try {
            String sql = "select * from revertindex where word='" + word + "'";
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flag=true;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return flag;
    }

    /**
     * 往索引表添加索引词
     *
     * @author liling
     *
     */
    public void AddIndexWord(String word) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            String sql = "insert into revertindex(word) values('" + word + "')";
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 获得索引表中的索引序列
     *
     */
    public String GetIndex(String word) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        String location = "";

        try {
            String sql = "select location from revertindex where word='" + word + "'";
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                location = rs.getString("location");
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return location;
    }

    /**
     * 追加新索引后，更新索引表索引字段
     *
     * @author liling
     *
     */
    public void UpdateIndex(String word, String location) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;

        try {
            String sql = "update revertindex set location='" + location + "' where word='" + word + "'";
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    
}
