package com.Process;

import java.util.ArrayList;
import com.DBUtil.*;
import com.Model.*;

public class delstopword {

    /**
     * 删除同义词词林中的停用词
     *
     * @author zhuwangnan
	 *
     */
    public static void ProcessStopword() {
       SynonymsDao synonymsDao=new SynonymsDao();
       StopWordsDao stopWordsDao=new StopWordsDao();
        ArrayList<Synonyms> Synlist = synonymsDao.GetAllSynonyms();
        for (Synonyms syn : Synlist) {
            String synword = syn.getSyn_word();
            ArrayList<StopWords> stopwordlist = stopWordsDao.GetAllStopWords(synword);
            if (stopwordlist.size() != 0) {
                synonymsDao.DeleteSynonymsByWord(synword);
            }
        }
    }
}
