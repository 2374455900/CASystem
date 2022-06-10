package com.ca.servlet;

import com.ca.dao.CertificateDao;
import com.ca.model.CertItem;
import com.ca.model.Page;
import com.ca.util.PageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "certificateInfoServlet", value = "/certificateInfoServlet")
public class CertificateInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CertificateDao certificateDao = new CertificateDao();
        int everyPage = 1;
        int totalCount = certificateDao.getCount();
        String scurrentPage = request.getParameter("currentPage");
        int currentPage = 1;
        if (scurrentPage == null) {
            currentPage = 1;
        } else {
            currentPage = Integer.parseInt(scurrentPage);
        }
        Page page = PageUtil.createPage(everyPage, totalCount, currentPage);
        List<CertItem> certItems = certificateDao.getCerByPage(page);
        System.out.println(page.toString());
        request.setAttribute("page", page);
        request.setAttribute("certItems", certItems);
        request.getRequestDispatcher("information.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

