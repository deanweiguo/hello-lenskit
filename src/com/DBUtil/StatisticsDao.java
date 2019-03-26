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
public class StatisticsDao {

    public void questionDing(String questionSting, String sbuquestionString) {
        int dingknum = 0;
        int flag = 0;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select Ding from click_statistic where Question=? and Subquestion=?");
            ps.setString(1, questionSting);
            ps.setString(2, sbuquestionString);
            ResultSet rs = ps.executeQuery();
            rs.last();
            flag = rs.getRow();
            if (flag == 0)//jie guo ji wei kong
            {
                //insert  插入
                dingknum = 1;
                String sql = "insert into click_statistic(Question,Subquestion,Ding) values('" + questionSting + "','" + sbuquestionString + "'," + dingknum + ")";
                ps = con.prepareStatement(sql);
                ps.execute();
            } else {
                ps = con.prepareStatement("select Ding from click_statistic where Question=? and Subquestion=?");
                ps.setString(1, questionSting);
                ps.setString(2, sbuquestionString);
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    dingknum = rs2.getInt("Ding") + 1;//quzhi
                    String sql2 = "update click_statistic set Ding=" + dingknum + " where Question='" + questionSting + "'  and Subquestion='" + sbuquestionString + "'";
                    ps = con.prepareStatement(sql2);
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }

    }

    public void questionCai(String questionSting, String sbuquestionString) {
        int cainum = 0;
        int flag = 0;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select Cai from click_statistic where Question=? and Subquestion=?");
            ps.setString(1, questionSting);
            ps.setString(2, sbuquestionString);
            ResultSet rs = ps.executeQuery();
            rs.last();
            flag = rs.getRow();
            if (flag == 0)//jie guo ji wei kong
            {
                //insert  插入
                cainum = 1;
                String sql = "insert into click_statistic(Question,Subquestion,Cai) values('" + questionSting + "','" + sbuquestionString + "'," + cainum + ")";
                ps = con.prepareStatement(sql);
                ps.execute();
            } else {
                ps = con.prepareStatement("select Cai from click_statistic where Question=? and Subquestion=?");
                ps.setString(1, questionSting);
                ps.setString(2, sbuquestionString);
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    cainum = rs2.getInt("Cai") + 1;//quzhi
                    String sql2 = "update click_statistic set Cai=" + cainum + " where Question='" + questionSting + "'  and Subquestion='" + sbuquestionString + "'";
                    ps = con.prepareStatement(sql2);
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    public void IncSuccess() {
        int totalnum;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;

        try {
            String sql = "select total from success_rate";
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                totalnum = rs.getInt("total") + 1;
                String sql2 = "update success_rate set total=" + totalnum;
                ps = con.prepareStatement(sql2);
                ps.execute();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    public void IncFail() {
        int failnum;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            String sql = "select failnumber from success_rate";
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                failnum = rs.getInt("failnumber") + 1;
                String sql2 = "update success_rate set failnumber=" + failnum;
                ps = con.prepareStatement(sql2);
                ps.execute();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    public void questionExpand(String questionSting, String sbuquestionString) {
        int clicknum = 0;
        int flag = 0;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select ClickNumber from click_statistic where Question=? and Subquestion=?");
            ps.setString(1, questionSting);
            ps.setString(2, sbuquestionString);
            ResultSet rs = ps.executeQuery();
            rs.last();
            flag = rs.getRow();
            if (flag == 0)//jie guo ji wei kong
            {
                //insert  插入
                clicknum = 1;
                String sql = "insert into click_statistic(Question,Subquestion,ClickNumber) values('" + questionSting + "','" + sbuquestionString + "'," + clicknum + ")";
                ps = con.prepareStatement(sql);
                ps.execute();
            } else {
                ps = con.prepareStatement("select ClickNumber from click_statistic where Question=? and Subquestion=?");
                ps.setString(1, questionSting);
                ps.setString(2, sbuquestionString);
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    clicknum = rs2.getInt("ClickNumber") + 1;//quzhi
                    String sql2 = "update click_statistic set ClickNumber=" + clicknum + " where Question='" + questionSting + "'  and Subquestion='" + sbuquestionString + "'";
                    ps = con.prepareStatement(sql2);
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }

    /**
     * 插入顾客输入的问题及点击到click_statistic表中
     *
     * @author zhaofangfei
     *
     */
    public void NewClick(String question, String subquestion) {

        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("insert into click_statistic(Question,SubQuestion) values(?,?)");
            ps.setString(1, question);
            ps.setString(2, subquestion);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
    }
}
