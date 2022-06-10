package com.ca.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatDate(Date date, String format){
        String result = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if(date!=null){
            result=simpleDateFormat.format(date);
        }
        return result;
    }

    public static Date formatString(String str, String format) throws Exception{
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

}
