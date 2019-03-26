package com.Process;

import com.Model.ChatSim;
import java.util.Comparator;

public class ChatSimSort implements Comparator<ChatSim>{

	/**对相似度进行排序
	 * @author jiamingjing
	 * */
	@Override
	public int compare(ChatSim o1, ChatSim o2) {
		// TODO Auto-generated method stub
		if(o1.getSim()>=o2.getSim())
			return 0;
		return 1;
	}

	
}
