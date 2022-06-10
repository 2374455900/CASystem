package com.ca.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {
    public static void write(HttpServletResponse response, Object object)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(object.toString());
        out.flush();
        out.close();
    }
}
