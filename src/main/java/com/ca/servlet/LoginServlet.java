package com.ca.servlet;

import com.ca.dao.LoginDao;
import com.ca.model.User;
import com.ca.util.DbUtil;
import com.ca.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    DbUtil dbUtil = new DbUtil();
    LoginDao loginDao = new LoginDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        //设置返回类型为json，字符集为utf-8
        response.addHeader("content-type", "text/json;charset=utf-8");

        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println("joker468=" + password);


        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            request.setAttribute("error", "用户名或密码为空！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = new User(username, password);

        Connection connection = null;

        try {
            connection = dbUtil.getConnection();
            User queryUser = loginDao.login(connection, user);
            if (queryUser == null) {
                request.setAttribute("LoginError", "用户名或密码错误！");
                // 服务器跳转
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                session.setAttribute("currentUser", queryUser);
                session.setAttribute("username", username);
                request.getRequestDispatcher("information.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
