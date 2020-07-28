package com.jecelyin.editor.v2.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class Main {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        int count = 10000;
        boolean b = false;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            b = pattern.matcher("234.0234.2342"+i).matches();
        }
        long t2 = System.currentTimeMillis();
        System.out.println(b+" time=" + (t2 - t1));
        Matcher matcher = pattern.matcher("");
        for (int i = 0; i < count; i++) {
            b = matcher.reset("234.0234.2324"+i).matches();
        }
        long t3 = System.currentTimeMillis();
        System.out.println(b+" time=" + (t3 - t2));
    }
}
