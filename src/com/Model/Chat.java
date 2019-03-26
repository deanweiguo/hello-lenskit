/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Model;

/**
 *
 * @author Administrator
 */
public class Chat {

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
}
