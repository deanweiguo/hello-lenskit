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
public class EquipmentDao {

    public boolean ExistEquipment(String queryword) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from equipment where word=?");
            ps.setString(1, queryword);
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

    public void InsertEquipment(String value) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            String sql = "insert into  equipment(word) values(?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, value);
            ps.execute();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }
}
