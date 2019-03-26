/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Model;

/**
 *
 * @author Administrator
 */
public class ChatSim {
    private int chatid;
    private String chatquestion;
    private String chatanswer;
    private double sim;

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public int getChatid() {
        return chatid;
    }
     public void setChatquestion(String chatquestion) {
        this.chatquestion = chatquestion;
    }

    public String getChatquestion() {
        return chatquestion;
    }
     public void setChatanswer(String chatanswer) {
        this.chatanswer = chatanswer;
    }

    public String getChatanswer() {
        return chatanswer;
    }
     public void setSim(double sim) {
        this.sim = sim;
    }

    public double getSim() {
        return sim;
    }
}
