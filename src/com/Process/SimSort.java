package com.Process;

import java.util.Comparator;

import com.Model.Result;

public class SimSort implements Comparator<Result> {

    /**
     * 对相似度进行排序
     *
     * @author jiamingjing
	 *
     */
    @Override
    public int compare(Result o1, Result o2) {
        // TODO Auto-generated method stub
        if (o1.getSim() >= o2.getSim()) {
            return -1;
        }
        return 1;
    }
}
