package com.Process;

import com.DBUtil.FAQDao;
import com.DBUtil.RevertIndexDao;
import com.Model.Question;
import java.util.ArrayList;

/**
 * 对FAQ库中的问题构建倒排索引 方法如下： 0、先清空索引表 1、查询得到所有问题的seglist 2、逐条切分，将词添加到索引表，将问句编号添加到索引
 * 3、重复执行2，直至所有的问句seg添加完成
 *
 */
public class Revertindex {

    public static void revertaction() {
        FAQDao faqDao = new FAQDao();
        RevertIndexDao revertIndexDao=new RevertIndexDao();
        revertIndexDao.TruncateRevertIndex();
        ArrayList<Question> arrayList = faqDao.GetAllQuestion();
        for (Question qu : arrayList) {
            String question_seg = qu.getQuestion_seg();
            if (null == question_seg) {
                continue;
            }
            String wordlist[] = question_seg.split(" ");

            int question_id = qu.getQuestion_id();

            for (int i = 0; i < wordlist.length; i++) {
                String word = wordlist[i].trim();
                if (revertIndexDao.ExistIndexWord(word)) {
                    String oldlocation = revertIndexDao.GetIndex(word);
                    if (null == oldlocation || oldlocation.equals("")) {
                        String location = String.valueOf(question_id);
                        revertIndexDao.UpdateIndex(word, location);
                    } else {
                        String location = oldlocation + ";" + String.valueOf(question_id);
                        revertIndexDao.UpdateIndex(word, location);
                    }
                } else {
                    revertIndexDao.AddIndexWord(word);
                    String location = String.valueOf(question_id);
                    revertIndexDao.UpdateIndex(word, location);
                }
            }
        }
    }

    /**
     * 插入问题时候用，更新单条索引
     */
    public static boolean insertRevertaction(String question) {
         RevertIndexDao revertIndexDao=new RevertIndexDao();
        Question qu = new FAQDao().getQuestionByContent(question);
        String question_seg = qu.getQuestion_seg();
        if (null == question_seg) {
            return false;
        }
        String wordlist[] = question_seg.split(" ");

        int question_id = qu.getQuestion_id();

        for (int i = 0; i < wordlist.length; i++) {
            String word = wordlist[i].trim();
            if (revertIndexDao.ExistIndexWord(word)) {
                String oldlocation = revertIndexDao.GetIndex(word);
                if (null == oldlocation || oldlocation.equals("")) {
                    String location = String.valueOf(question_id);
                    revertIndexDao.UpdateIndex(word, location);
                } else {
                    String location = oldlocation + ";" + String.valueOf(question_id);
                    revertIndexDao.UpdateIndex(word, location);
                }
            } else {
                revertIndexDao.AddIndexWord(word);
                String location = String.valueOf(question_id);
                revertIndexDao.UpdateIndex(word, location);
            }
        }
        return true;
    }

    
    public static void main(String args[]) {
        revertaction();
        System.out.println("Done!");
    } 
}
