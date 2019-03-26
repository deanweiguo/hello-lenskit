package com.Process;

import java.util.Random;

public class RandomIdentity {

    /**
     * 生成随机码，用于保存同义词，保证同义词的唯一性
     *
     * @author zhuwangnan
	 *
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
}
