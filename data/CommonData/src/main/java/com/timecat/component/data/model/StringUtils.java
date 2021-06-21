package com.timecat.component.data.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/22
 * @description null
 * @usage null
 */
public class StringUtils {
    private static StandardToStringStyle toStringStyle = null;

    public static String getRandomId() {
        return new BigInteger(260, new Random()).toString(32).substring(0, 32);
    }

    public static ToStringStyle defaultToStringStyle() {
        if (toStringStyle == null) {
            toStringStyle = new StandardToStringStyle();
            toStringStyle.setFieldSeparator(", ");
            toStringStyle.setUseClassName(false);
            toStringStyle.setUseIdentityHashCode(false);
            toStringStyle.setContentStart("{");
            toStringStyle.setContentEnd("}");
            toStringStyle.setFieldNameValueSeparator(": ");
            toStringStyle.setArrayStart("[");
            toStringStyle.setArrayEnd("]");
        }

        return toStringStyle;
    }

    public static String joinLongs(long values[]) {
        return org.apache.commons.lang3.StringUtils.join(values, ',');
    }

    public static long[] splitLongs(String str) {
        String parts[] = org.apache.commons.lang3.StringUtils.split(str, ',');

        long numbers[] = new long[parts.length];
        for (int i = 0; i < parts.length; i++) numbers[i] = Long.valueOf(parts[i]);
        return numbers;
    }

    private final static HashMap<Character, Integer> ChnMap = getChnMap();

    public static int stringToInt(String str) {
        if (str != null) {
            String num = fullToHalf(str).replaceAll("\\s", "");
            try {
                return Integer.parseInt(num);
            } catch (Exception e) {
                return chineseNumToInt(num);
            }
        }
        return -1;
    }


    //功能：字符串全角转换为半角
    public static String fullToHalf(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) //全角空格
            {
                c[i] = (char) 32;
                continue;
            }

            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    private static HashMap<Character, Integer> getChnMap() {
        String cnStr = "零一二三四五六七八九十";
        HashMap<Character, Integer> map = new HashMap<>();
        char[] c = cnStr.toCharArray();
        for (int i = 0; i <= 10; i++) {
            map.put(c[i], i);
        }
        map.put('〇', 0);
        map.put('两', 2);
        map.put('百', 100);
        map.put('千', 1000);
        map.put('万', 10000);
        map.put('亿', 100000000);
        return map;
    }

    // 修改自 https://binux.blog/2011/03/python-tools-chinese-digit/
    public static int chineseNumToInt(String chNum) {
        int result = 0;
        int tmp = 0;
        int billion = 0;
        char[] cn = chNum.toCharArray();

        // "一零二五" 形式
        if (cn.length > 1 && chNum.matches("^[〇零一二三四五六七八九]$")) {
            for (int i = 0; i < cn.length; i++) {
                cn[i] = (char) (48 + ChnMap.get(cn[i]));
            }
            return Integer.parseInt(new String(cn));
        }

        // "一千零二十五", "一千二" 形式
        try {
            for (int i = 0; i < cn.length; i++) {
                int tmpNum = ChnMap.get(cn[i]);
                if (tmpNum == 100000000) {
                    result += tmp;
                    result *= tmpNum;
                    billion = billion * 100000000 + result;
                    result = 0;
                    tmp = 0;
                } else if (tmpNum == 10000) {
                    result += tmp;
                    result *= tmpNum;
                    tmp = 0;
                } else if (tmpNum >= 10) {
                    if (tmp == 0) {
                        tmp = 1;
                    }
                    result += tmpNum * tmp;
                    tmp = 0;
                } else {
                    if (i >= 2 && i == cn.length - 1 && ChnMap.get(cn[i - 1]) > 10) {
                        tmp = tmpNum * ChnMap.get(cn[i - 1]) / 10;
                    } else {
                        tmp = tmp * 10 + tmpNum;
                    }
                }
            }
            result += tmp + billion;
            return result;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 从 index==start 开始，返回长度不超过 len 的子串
     *
     * @param start 起点
     * @param len   最大长度
     * @return 子串
     */
    public static String subString(String s, int start, int len) {
        if (s == null) {
            return "";
        }
        String title = s;
        title = title.replace("\n", "\\n");
        title = title.substring(start, Math.max(start, Math.min(start + len, title.length() - 1)));
        return title;
    }

    public static String toString(Iterable iterable, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object item : iterable) {
            stringBuilder.append(String.valueOf(item));
            stringBuilder.append(separator);
        }

        if (stringBuilder.length() > 2) {
            return stringBuilder.substring(0, stringBuilder.length() - separator.length());
        } else {
            return stringBuilder.toString();
        }
    }

    private static boolean isWhiteSpaces(@Nullable String s) {
        return s != null && s.matches("\\s+");
    }

    /**
     * 判断字符是否为空 trim = true
     *
     * @param text text
     * @return string
     */
    public static boolean isEmpty(@Nullable String text) {
        return text == null || TextUtils.isEmpty(text) || isWhiteSpaces(text) || text
                .equalsIgnoreCase("null");
    }

    public static boolean isEmpty(@Nullable Object text) {
        return text == null || isEmpty(text.toString());
    }

    /**
     * 长度不超过 20
     */
    @NonNull
    public static String getShortText(String title) {
        return getShortText(title, 20);
    }

    /**
     * 长度不超过 maxLength
     */
    @NonNull
    public static String getShortText(String title, int maxLength) {
        if (TextUtils.isEmpty(title)) {
            return "";
        }
        int firstLineEndIdx = title.indexOf("\n");
        return firstLineEndIdx == -1
                ? (title.length() > maxLength ? title.substring(0, maxLength) : title)
                : title.substring(0, firstLineEndIdx > maxLength ? maxLength : firstLineEndIdx);
    }
}
