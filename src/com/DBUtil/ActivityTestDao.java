package com.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.Model.ActivityTest;

public class ActivityTestDao {
	
	/**
     * 根据单个活动ID获取活动标题
     */
	public String GetTitleById(int id) {
        
        ArrayList<ActivityTest> arrayList = new ArrayList<ActivityTest>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        String title = null;
        try {
            
            ps = con.prepareStatement("select * from activity where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                title = rs.getString(2);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return title;
    }
	
	/**
     * 根据一组活动ID获取活动标题
     */
	public ArrayList<ActivityTest> GetTitleByIds(ArrayList<Long> QList) {
        if (QList == null || QList.isEmpty()) {
            return null;
        }
        ArrayList<ActivityTest> arrayList = new ArrayList<ActivityTest>();
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        ActivityTest qu = null;
        try {
            for (int i = 0; i < QList.size(); i++) {
                //int activityID = Integer.parseInt(QList.get(i));
            	long activityID = QList.get(i);
            	ps = con.prepareStatement("select * from activity where id=?");
                ps.setLong(1, activityID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    qu = new ActivityTest();
                    qu.setActivityId(rs.getInt("id"));
                    qu.setTitle(rs.getString("title"));
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
