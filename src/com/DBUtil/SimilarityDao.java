package com.DBUtil;

import com.Model.SimilarityModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SimilarityDao {
	
    /**
     * 插入一个句子相似度，成功后返回1
     *
     * @author Code_Life_LiWan
     */
    public int AddSimilarity(SimilarityModel s) {
        String sql = "insert into similarity(activity_idA,activity_idB,sim) values (?,?,?)";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        int ret = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, s.getactivity_idA());
            pstmt.setInt(2, s.getactivity_idB());
            pstmt.setDouble(3, s.getsim());
           
            int count = pstmt.executeUpdate();
            if (count != 1) {
                ret = -1;
            } else {
                //ret = GetUserIdByUserName(u.getUserName());
            	ret = 1;
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
     * 根据两个活动ID，返回它们的相似度值
     *
     * @author Code_Life_LiWan
     */
    public Double GetSimilarityById(int idA, int idB) {
        String sql = "select sim from similarity where activity_idA=? and activity_idB=?";
        Connection conn = DbUtil.getCurrentConnection();
        PreparedStatement pstmt = null;
        Double ret = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idA);
            pstmt.setInt(2, idB);
            ResultSet rel = pstmt.executeQuery();
            if (rel.next()) {
                ret = rel.getDouble(1);
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
}
