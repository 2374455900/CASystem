package com.ca.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = request.getParameter("filename");
        System.out.println(filename);
        ServletContext servletContext = this.getServletContext();
        String file_path = servletContext.getRealPath("download/" + filename);
        String mimeType = servletContext.getMimeType(file_path);
        System.out.println(file_path);
        response.setHeader("content-type", mimeType);
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        FileInputStream fileInputStream = new FileInputStream(file_path);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len;
        while ((len = fileInputStream.read(buff)) != -1) {
            servletOutputStream.write(buff, 0, len);
        }
        fileInputStream.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
