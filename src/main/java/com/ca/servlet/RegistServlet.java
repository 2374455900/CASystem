package com.ca.servlet;

import com.ca.dao.RegistDao;
import com.ca.model.User;
import com.ca.util.DateUtil;
import com.ca.util.DbUtil;
import com.ca.util.Md5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

import static java.lang.System.out;

@WebServlet(name = "RegistServlet", value = "/RegistServlet")
public class RegistServlet extends HttpServlet {

    DbUtil dbUtil = new DbUtil();
    RegistDao registDao = new RegistDao();
    Md5Util md5Util = new Md5Util();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        //设置返回类型为json，字符集为utf-8
        response.addHeader("content-type", "text/json;charset=utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        password = md5Util.getMd5ofStr(password);
        System.out.println(password);
        String email = request.getParameter("email");
        String created_at = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        String update_at = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        String deleted_at = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

        User user = new User(created_at, update_at, deleted_at, username, password, email, "0");

        Connection connection;

        try {
            connection = dbUtil.getConnection();

            if (registDao.regist(connection, user)) {
                out.println("添加成功，三秒后返回登陸頁面");

                response.setHeader("refresh", "3;url= login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
