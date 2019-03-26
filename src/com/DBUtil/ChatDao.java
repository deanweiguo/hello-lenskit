/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

import com.Model.Chat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ChatDao {

    /**
     * tempfaq表中问句经分词去停后为空，添加到chat表中
     */
    public void InsertChat(String question, String answer) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into chat(chatquestion,chatanswer) values(?,?)");
            ps.setString(1, question);
            ps.setString(2, answer);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 返回Chat寒暄表中的是否存在该问题
     *
     * @author zhuwangnan
     *
     */
    public boolean ExistChat(String question) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from chat where chatquestion=?");
            ps.setString(1, question);
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

    /**
     * 查找并返回所有寒暄语句
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<Chat> GetAllChat() {
        ArrayList<Chat> arrayList = new ArrayList<Chat>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Chat qu;
        try {
            ps = con.prepareStatement("select * from chat order by chatid asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new Chat();
                qu.setChatid(rs.getInt("chatid"));
                qu.setChatquestion(rs.getString("chatquestion"));
                qu.setChatanswer(rs.getString("chatanswer"));
                arrayList.add(qu);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }

    /**
     * 查找并返回含有特定关键词的寒暄语句
     *
     * @author Code_Life_LiWan
     */
    public ArrayList<Chat> GetChatsContainKeyWords(String[] keywords) {
        if (null == keywords) {
            return null;
        }
        ArrayList<Chat> all = new ArrayList<Chat>();
        int size = keywords.length;
        if (size == 0) {
            return null;
        }
        String sql = "select distinct * from chat where chatquestion like '%" + keywords[0] + "%'";
        for (int i = 1; i < size; i++) {
            sql += " or chatquestion like '%" + keywords[i] + "%'";
        }
        sql += " order by chatid asc";
        Connection con = DbUtil.getCurrentConnection();
        PreparedStatement ps = null;
        Chat qu = null;
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new Chat();
                qu.setChatid(rs.getInt("chatid"));
                qu.setChatquestion(rs.getString("chatquestion"));
                qu.setChatanswer(rs.getString("chatanswer"));
                all.add(qu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return all;
    }
}
