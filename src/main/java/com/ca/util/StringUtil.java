package com.ca.util;

public class StringUtil {
    public static boolean isEmpty(String str){
        return "".equals(str) || str == null;
    }

    public static boolean isNotEmpty(String str){
        return !"".equals(str) && str != null;
    }
}

