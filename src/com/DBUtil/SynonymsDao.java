/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

import com.Model.Synonyms;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class SynonymsDao {

    /**
     * 返回所有同义词
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<Synonyms> GetAllSynonyms() {
        ArrayList<Synonyms> arrayList = new ArrayList<Synonyms>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Synonyms qu;
        try {
            ps = con.prepareStatement("select * from synonyms");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new Synonyms();
                qu.setSyn_id(rs.getInt("syn_id"));
                qu.setIdentity(rs.getString("identity"));
                qu.setSyn_word(rs.getString("syn_word"));
                arrayList.add(qu);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }

    /**
     * 删除同义词词林中的词语
     *
     * @author zhuwangnan
     *
     */
    public void DeleteSynonymsByWord(String synword) {

        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("delete  from synonyms where syn_word=?");
            ps.setString(1, synword);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }

    }

    
    
    /**
     * 根据同义词Identity，返回所有的同义词
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<String> GetSynonymsByIdentity(String Identity) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select syn_word from synonyms where identity =?");
            ps.setString(1, Identity);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arrayList.add(rs.getString("syn_word"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }
    
        /**
     * 查询同义词词林,返回同义词列表
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<String> SynonymsSearch(String word) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select identity from synonyms where syn_word=?");
            ps.setString(1, word);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               arrayList.addAll(GetSynonymsByIdentity(rs.getString("identity")));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }


    /**
     * 查询同义词返回其identity
     *
     * @author zhuwangnan
     *
     */
    public String GetIdentityOfSynonyms(String word) {
        String value = "";
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select identity from synonyms where syn_word=?");
            ps.setString(1, word);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("identity").trim();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return value;
    }

    /**
     * 插入同义词到同义词表中
     *
     * @author zhuwangnan
     *
     */
    public void InsertSynonyms(String word, String identity) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into synonyms(identity,syn_word) values(?,?)");
            ps.setString(1, identity);
            ps.setString(2, word);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 查询是否存在该ID
     *
     * @author jia
     *
     */
    public boolean ExistIdentity(String identity) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select identity from synonyms where identity=?");
            ps.setString(1, identity);
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
     * 查询同义词表中是否存在该词语
     *
     * @author jia
     *
     */
    public boolean ExistSynonyms(String Syn_Word) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select identity from synonyms where syn_word=?");
            ps.setString(1, Syn_Word);
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
