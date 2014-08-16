package com.library.util;

/**
 * Created by Administrator on 2014/7/16.
 */
public class Format {
    Format() {
    }

    public static String formatString(String str) {
        String res = String.format("%2s", str);
        res = res.replaceAll("\\s", "0");
        return res;
    }

    public static String formatString(String weishu, String str) {
        String res = String.format("%" + weishu + "s", str);
        res = res.replaceAll("\\s", "0");
        return res;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
