package com.SystemFlow;

import java.util.ArrayList;

import com.DBUtil.*;
import com.Model.SegmentWord;

public class StopwordDelete {

    /**
     * 对分词结果进行去停用词
     *
     * @author zhuwangnan
	 *
     */
    public ArrayList<SegmentWord> ProcessStopword(ArrayList<SegmentWord> SegResult) {
        ArrayList<SegmentWord> resList = new ArrayList<SegmentWord>();
       StopWordsDao stopWordsDao=new StopWordsDao();
  
        for (int i = 0; i < SegResult.size(); i++) {
            if (!stopWordsDao.DeleteStopWord(SegResult.get(i).getWord())) {
                resList.add(SegResult.get(i));
            }
        }
        return resList;

    }

    public static void main(String[] args) {
        //new VSM().run();
    }
}
