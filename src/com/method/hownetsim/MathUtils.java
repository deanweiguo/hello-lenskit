package com.method.hownetsim;

public class MathUtils {

    public static int min(int... values) {
        int min = Integer.MAX_VALUE;
        for (int v : values) {
            min = (v < min) ? v : min;
        }
        return min;
    }
}
