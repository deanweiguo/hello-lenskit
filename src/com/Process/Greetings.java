/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Process;

import com.DBUtil.ChatDao;
import com.DBUtil.infoDao;
import com.Model.Chat;
import com.Model.ChatSim;
import com.method.hownetsim.CharBasedSimilarity;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author wanli
 */
public class Greetings {

    public String ChatProcess(String str, String seg) {
        ChatDao chatDao = new ChatDao();
        String[] keyWords = seg.split(" ");
        ArrayList<Chat> chatlist = chatDao.GetChatsContainKeyWords(keyWords);
        if (chatlist == null) {
            return null;
        }
        ArrayList<ChatSim> resList = new ArrayList<ChatSim>();
        for (Chat c : chatlist) {
            ChatSim resTemp = new ChatSim();
            String cquestion = c.getChatquestion();
            double sim = new CharBasedSimilarity().getSimilarity(str, cquestion);
            resTemp.setChatquestion(c.getChatquestion());
            resTemp.setChatanswer(c.getChatanswer());
            resTemp.setSim(sim);
            resList.add(resTemp);
        }
        ChatSimSort comsim = new ChatSimSort();
        Collections.sort(resList, comsim);
        int count = 1;
        for (ChatSim result2 : resList) {
            count++;
            if (result2.getSim() > 0.75) {
                //寒暄模块内容输出，可以自定义输出形式
                return result2.getChatanswer();
            } else {
                return null;
            }
        }
        return null;
    }
}
