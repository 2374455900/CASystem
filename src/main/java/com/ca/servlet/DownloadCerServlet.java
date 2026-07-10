package com.ca.servlet;

import com.ca.dao.CertificateDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet(name = "DownloadCerServlet", value = "/DownloadCerServlet")
public class DownloadCerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serial_number = request.getParameter("serial_number");
        System.out.println(serial_number);
        CertificateDao certificateDao = new CertificateDao();
        String filePath = certificateDao.getFilePath(serial_number);
        if (filePath == null) {
            request.setAttribute("msg", "此证书不存在！");
            request.getRequestDispatcher("download.jsp").forward(request, response);
            return;
        }
        String filename = serial_number + ".mycer";
        ServletContext servletContext = this.getServletContext();
        //        String mimeType = servletContext.getMimeType(file_path);
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
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
