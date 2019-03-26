/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DBUtil;

import com.Model.QuestionHistory;
import com.Model.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class UserDao {

    /**
     * 根据用户名查找用户id
     *
     * @author Code_Life_LiWan
     */
    public int GetUserIdByUserName(String userName) {
        String sql = "select user_id from user_info where user_name=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 根据邮箱查找用户id
     *
     * @author Code_Life_LiWan
     */
    public int GetUserIdByEmail(String email) {
        if (null == email || "".equals(email)) {
            return 0;
        }
        String sql = "select user_id from user_info where email=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 根据电话查找用户id
     *
     * @author Code_Life_LiWan
     */
    public int GetUserIdByPhone(String phone) {
        String sql = "select user_id from user_info where phone=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 插入一个用户，返回用户id
     *
     * @author Code_Life_LiWan
     */
    public int AddUser(UserInfo u) {
        String sql = "insert into user_info(user_name,password,phone,email,address) values (?,?,?,?,?)";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u.getUserName());
            pstmt.setString(2, u.getPassword());
            pstmt.setString(3, u.getPhone());
            pstmt.setString(4, u.getEmail());
            pstmt.setString(5, u.getAddress());
            int count = pstmt.executeUpdate();
            if (count != 1) {
                ret = -1;
            } else {
                ret = GetUserIdByUserName(u.getUserName());
            }
        } catch (Exception e) {
            ret = -1;
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 根据用户名返回密码 正确为密码，错误为空
     *
     * @author Code_Life_LiWan
     */
    public String GetPasswordByUserName(String userName) {
        String sql = "select password from user_info where user_name=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        String ret = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getString(1);
            } else {
                ret = null;
            }
        } catch (Exception e) {
            ret = null;
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 查询是否在表user_questions里面已经存在某个记录
     *
     * @author Code_Life_LiWans
     */
    public boolean CheckUserQuestionExist(int userId, String question) {
        String sql = "select user_id from user_questions where user_id=? and question=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        boolean ret = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, question);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = true;
            } else {
                ret = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 往表user_questions里面插入某个记录
     *
     * @author Code_Life_LiWans
     */
    public boolean AddUserQuestion(int userId, String question) {
        String sql = "insert into user_questions(user_id,question,question_time) values (?,?,now())";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        boolean ret = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, question);
            int count = pstmt.executeUpdate();
            if (count == 1) {
                ret = true;
            } else {
                ret = false;
            }
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }

    /**
     * 根据User的id取得用户权限
     *
     * @author Code_Life_LiWan
     */
    public int GetUserRoor(int userId) {
        String sql = "select uroot from user_root where user_id=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getInt(1);
            }
        } catch (Exception e) {
            ret = 0;
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
            return ret;
        }
    }
    
        public ArrayList<QuestionHistory> GetQuestionHistory(int userid) {
        ArrayList<QuestionHistory> arrayList = new ArrayList<QuestionHistory>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        QuestionHistory qu = null;
        try {
            ps = con.prepareStatement("select * from user_questions where user_id=?");
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                qu = new QuestionHistory();
                qu.setUserid(rs.getInt("user_id"));
                qu.setQuestion(rs.getString("question"));
                qu.setQuestiontime(rs.getString("question_time"));
                arrayList.add(qu);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return arrayList;
    }

}
