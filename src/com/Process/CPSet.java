package com.Process;

import com.DBUtil.FAQDao;
import com.DBUtil.RevertIndexDao;
import com.DBUtil.infoDao;
import com.Model.Question;
import java.util.ArrayList;

public class CPSet {

    public static int[][] SortArray(int[][] array) {
        int row = array.length;
        int temp = 0;
        int strtemp = 0;
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < row - 1; j++) {

                if (array[j][1] < array[j + 1][1]) {

                    temp = array[j][1];
                    strtemp = array[j][0];
                    array[j][1] = array[j + 1][1];
                    array[j][0] = array[j + 1][0];
                    array[j + 1][1] = temp;
                    array[j + 1][0] = strtemp;
                    continue;
                } else {
                    continue;
                }
            }

        }
        return array;
    }

    //返回含有最多关键字的问题列表
    public static int[][] BuildCPS(ArrayList<String> sentence) {
        FAQDao faqDao = new FAQDao();
        RevertIndexDao revertIndexDao=new RevertIndexDao();
        //返回用户问句同义词扩展后，里面包含的设备名称
        Equipment equipment = new Equipment();
        ArrayList<Question> arrayList = new ArrayList<Question>();
        arrayList = faqDao.getQuestionsContainsKeyWord(sentence);

        if (null == arrayList) {
            return null;
        }
        //ArrayList<Question> arrayList = dao.AllQuestion();
        int count = arrayList.size();
        int[][] WordList = new int[count][2];
        int n = 0;
        for (Question qu : arrayList) {
            int question_id = qu.getQuestion_id();
            WordList[n][0] = question_id;
            WordList[n][1] = 0;
            n++;
        }
        for (int i = 0; i < sentence.size(); i++) {
            String word = sentence.get(i);
            if (word != null && !word.equals("")) {
                String location = revertIndexDao.GetIndex(word);
                String locationlist[] = location.split(";");
                for (int j = 0; j < locationlist.length; j++) {
                    if (locationlist[j].trim() != null && !"".equals(locationlist[j].trim())) {
                        int id = Integer.parseInt(locationlist[j].trim());
                        for (int k = 0; k < count; k++) {
                            if (WordList[k][0] == id) {
                                WordList[k][1]++;
                            }
                        }
                    }
                }
            }
        }
        WordList = SortArray(WordList);
        return WordList;
    }
}
