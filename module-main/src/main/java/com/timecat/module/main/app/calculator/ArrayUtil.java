package com.timecat.module.main.app.calculator;

public abstract class ArrayUtil {
    public static double[] reverse(double[] data) {
        int left = 0;
        for (int right = data.length - 1; left < right; right--) {
            double temp = data[left];
            data[left] = data[right];
            data[right] = temp;
            left++;
        }
        return data;
    }
}
