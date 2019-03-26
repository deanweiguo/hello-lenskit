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
public class FAQDao {

    /**
     * 返回question表中的question
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<Question> GetAllQuestion() {
        ArrayList<Question> arrayList = new ArrayList<Question>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Question qu;
        try {
            ps = con.prepareStatement("select * from faq ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new Question();
                qu.setQuestion_id(rs.getInt("question_id"));
                qu.setQuestion(rs.getString("question"));
                qu.setAnswer(rs.getString("answer"));
                qu.setQuestion_seg(rs.getString("question_seg"));
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
     * 返回指定大小的候选问题集问题集，用于计算相似度
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<Question> GetCandidateQuestion(int[][] array, int CPNUM) {
        if (null == array) {
            return null;
        }
        ArrayList<Question> arrayList = new ArrayList<Question>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Question qu = null;
        int allArray = array.length;
        try {
            for (int i = 0; i < CPNUM; i++) {
                if (i >= allArray) {
                    break;
                }
                ps = con.prepareStatement("select * from faq where question_id=?");
                ps.setInt(1, array[i][0]);
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

    /**
     * 查询FAQ是否存在该问题
     *
     * @author zhuwangnan
     *
     */
    public boolean ExistQuestion(String question) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from faq where question=?");
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
     * 插入新FAQ到faq表中
     *
     * @author zhuwangnan
     *
     */
    public void InsertQuestion(String question, String answer) {

        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into faq(question,answer) values(?,?)");
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
     * 返回FAQ表中未生成VSM特征向量的question
     *
     * @author zhuwangnan
     *
     */
    public ArrayList<Question> GetUnVSMQuestion() {
        ArrayList<Question> arrayList = new ArrayList<Question>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        Question qu;
        try {
            ps = con.prepareStatement("select * from faq where question_seg is null");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new Question();
                qu.setQuestion_id(rs.getInt("question_id"));
                qu.setQuestion(rs.getString("question"));
                qu.setAnswer(rs.getString("answer"));
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
     * 设置FAQ的VSM向量
     *
     * @author zhuwangnan
     *
     */
    public void SetQuestionVSM(String question, ArrayList<String> vsmList) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        String vsm = "";
        for (String temp : vsmList) {
            vsm += temp + " ";
        }
        try {
            ps = con.prepareStatement("update faq set question_seg=? where question=?");
            ps.setString(1, vsm);
            ps.setString(2, question);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 返回数据库中问题的数目
     *
     * @author liling
     */
    public int GetQuestetionNumber() {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        int count = 0;
        try {
            ps = con.prepareStatement("select count(*) as num from faq");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("num");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return count;
    }

    /**
     * 根据问题得到相应的Question对象
     *
     * @author Code_Life_LiWan
     */
    public Question getQuestionByContent(String question) {
        Question q = null;
        String sql = "select question_id,answer,question_seg from faq where question=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, question);
            ResultSet rel = pstmt.executeQuery();
            while (rel.next()) {
                q = new Question();
                q.setQuestion_id(rel.getInt(1));
                q.setAnswer(rel.getString(2));
                q.setQuestion_seg(rel.getString(3));
                q.setQuestion(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return q;
    }

    /**
     * 查找含有特定关键词的faq
     *
     * @author Code_Life_LiWan
     */
    public ArrayList<Question> getQuestionsContainsKeyWord(ArrayList<String> sentence) {
        ArrayList<Question> all = new ArrayList<Question>();
        if (sentence == null || sentence.isEmpty()) {
            return null;
        }
        String sql = "select distinct question_id,question,answer,question_seg from faq where question_seg like '%" + sentence.get(0) + "%'";
        int size = sentence.size();
        for (int i = 1; i < size; i++) {
            sql = sql + " or question_seg like '%" + sentence.get(i) + "%'";
        }
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rel = pstmt.executeQuery();
            while (rel.next()) {
                Question q = new Question();
                q.setQuestion_id(rel.getInt(1));
                q.setQuestion(rel.getString(2));
                q.setAnswer(rel.getString(3));
                q.setQuestion_seg(rel.getString(4));
                all.add(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return all;
    }

    /**
     * 返回FAQ表中的该问题的ID
     *
     * @author zhuwangnan
     *
     */
    public String GetQuestionID(String question) {
        String questionid = "";
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from faq where question=?");
            ps.setString(1, question);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questionid = rs.getString("question_id");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return questionid;
    }

    /**
     * 将处理后的问题插入到FAQ表中，包括该问题问句生成的VSM向量。
     */
    public void InsertFAQandVSM(String prequestion, String question, String answer, String vsm) {
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into faq(question,answer,question_seg) values(?,?,?)");
            ps.setString(1, question);
            ps.setString(2, answer);
            ps.setString(3, vsm);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 根据现象和设备，查询faq中与之对应的所有相关问题id，组成string返回
     */
    public ArrayList getQidString(String equname, String symname) {
        ArrayList<String> qidList = new ArrayList<String>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select question_id from faq where question like '%" + equname + "%' and question like '%" + symname + "%' order by question_id asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String qidString = rs.getString("question_id").trim().toString();
                if (!qidList.contains(qidString)) {
                    qidList.add(qidString);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return qidList;
    }
}
