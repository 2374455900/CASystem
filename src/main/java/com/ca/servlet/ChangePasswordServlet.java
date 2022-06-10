package com.ca.servlet;

import com.ca.dao.changePasswordDao;
import com.ca.model.User;
import com.ca.util.DateUtil;
import com.ca.util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

import static java.lang.System.out;

@WebServlet(name = "ChangePasswordServlet", value = "/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    DbUtil dbUtil = new DbUtil();
    changePasswordDao chang = new changePasswordDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //设置返回类型为json，字符集为utf-8
        response.addHeader("content-type", "text/json;charset=utf-8");

        String username = request.getParameter("username");
        String old_password = request.getParameter("old_password");
        String new_password = request.getParameter("new_password");

        String updated_at = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

        User result = new User(updated_at, new_password, username);
        Connection connection = null;

        try {
            connection = dbUtil.getConnection();

            if (chang.change(connection, result)) {
                request.setAttribute("msg","修改成功，三秒后返回登录界面");
                out.println("修改成功，三秒后返回登陸頁面");

                response.setHeader("refresh", "3;url= login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
