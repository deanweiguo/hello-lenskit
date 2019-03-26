/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

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
public class FaultFAQDao {

    public ArrayList<String> GetFaultFaq(String faultName) {
        ArrayList<String> idList = new ArrayList<String>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select questionid from faultfaq where faultname=?");
            ps.setString(1, faultName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("questionid").toString().equals("")) {
                    String wordList[] = rs.getString("questionid").split(",");
                    for (int i = 0; i < wordList.length; i++) {
                        if (!idList.contains(wordList[i])) {
                            idList.add(wordList[i].toString());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return idList;
    }

    public ArrayList<Question> FaultAnswer(ArrayList<String> QList) {
        if (QList == null || QList.isEmpty()) {
            return null;
        }
        ArrayList<Question> arrayList = new ArrayList<Question>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Question qu = null;
        try {
            for (int i = 0; i < QList.size(); i++) {
                int questionID = Integer.parseInt(QList.get(i));
                ps = con.prepareStatement("select * from faq where question_id=?");
                ps.setInt(1, questionID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    qu = new Question();
                    qu.setQuestion_id(rs.getInt("question_id"));
                    qu.setQuestion(rs.getString("question"));
                    qu.setAnswer(rs.getString("answer"));
                    qu.setQuestion_seg(rs.getString("question_seg"));
                    arrayList.add(qu);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }

    public boolean ExistFault(String faultNameString) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from faultfaq where faultname=?");
            ps.setString(1, faultNameString);
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

    public void UpdateFaultFaq(String faultNameString, String QidString) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;

        try {
            String sql = "update faultfaq set questionid='" + QidString + "' where faultname='" + faultNameString + "'";
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    public void InsertFaultFaq(String faultNameString, String QidString) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;

        try {
            String sql = "insert into faultfaq(faultname,questionid) values('" + faultNameString + "','" + QidString + "')";
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    public boolean IsFaultFaq(String subjectString, String objectString, Integer question_id) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {

            ps = con.prepareStatement("select * from faq where question like '%" + subjectString + "%' and question like '%" + objectString + "%' and question_id=" + question_id);
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

    public String GetQuestionIDByFaultName(String faultNameString) {
        String qidString = "";
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from faultfaq where faultname=?");
            ps.setString(1, faultNameString);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qidString = rs.getString("questionid");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return qidString;
    }
}
