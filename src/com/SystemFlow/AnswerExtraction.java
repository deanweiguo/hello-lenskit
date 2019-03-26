/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SystemFlow;

import com.Model.Result;
import com.Process.SimSort;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jia
 */
public class AnswerExtraction {

    public ArrayList<String> GetTopKAnswer(ArrayList<Result> resList, int k, double threshold) {
        ArrayList<String> result = new ArrayList<String>();
        //对结果进行排序
        Collections.sort(resList, new SimSort());
        System.out.println("排序成功！");

        //返回TopK答案
        int count = 0;
        for (Result res : resList) {
            if (res.getSim() > threshold) {
                System.out.println(res.getQuestion());
                System.out.println(res.getSim());
                result.add(res.getQuestion());
                result.add(res.getAnswer());
            }
            count++;
            if (count >= k) {
                break;
            }
        }
        return result;
    }
}
