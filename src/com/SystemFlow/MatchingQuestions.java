/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SystemFlow;

import com.DBUtil.FAQDao;
import com.DBUtil.TempFAQDao;
import com.Model.Question;
import com.Model.Result;
import com.Process.CPSet;
import com.Process.OwlProcess;
import com.Process.Similarity;
import java.util.ArrayList;

/**
 *
 * @author jia
 */
public class MatchingQuestions {

    public ArrayList<Result> GetMatchingQuestions(ArrayList<String> ExpandResult, int CPNUM) {
        //构建候选问题集
        ArrayList<Question> questionlist = new ArrayList<Question>();
        questionlist = new FAQDao().GetCandidateQuestion(CPSet.BuildCPS(ExpandResult), CPNUM);
        System.out.println("BuildCPS成功！问题规模：" + questionlist.size());

        //与FAQ问句进行相似度计算，结果保存到FinalSimList
        ArrayList<Double> FinalSimList = new ArrayList<Double>();//相似度
        ArrayList<Result> resList = new ArrayList<Result>();
        //候选问题集规模为0，返回sorry
        if (null == questionlist || questionlist.isEmpty()) {

            return null;
        }
        for (Question quest : questionlist) {
            Result resTemp = new Result();
            String question_seg = quest.getQuestion_seg();
            ArrayList<String> questionWordList = new ArrayList<String>();
            questionWordList.clear();
            String[] wordlist = question_seg.trim().split(" ");
            for (String word : wordlist) {
                questionWordList.add(word);
            }
            double FinalSim = Similarity.getInstance().VSMSimilarity(ExpandResult, questionWordList);
            resTemp.setQuestionid(quest.getQuestion_id());
            resTemp.setQuestion(quest.getQuestion());
            resTemp.setAnswer(quest.getAnswer());
            resTemp.setSim(FinalSim);
            resList.add(resTemp);
            FinalSimList.add(FinalSim);
            System.out.println(resTemp.getQuestion());
        }
        return resList;
    }
}
