/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Model;

/**
 *
 * @author Administrator
 */
public class QuestionHistory {
      private int userid;
    private String question;
    private String question_time;
    
     public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }
    public String getQuestion() {
        return question;
    }
     public void setQuestion(String question) {
        this.question = question;
    }
     public String getQuestiontime()
     {
         return question_time;
     }
     public void setQuestiontime(String question_time)
     {
         this.question_time=question_time;
     }
}
