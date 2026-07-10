package com.ca.dao;

import com.ca.model.User;
import com.ca.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {

    public static void main(String[] args) throws Exception {
        String username = "joker";
        String password = "05cf29cd56c95015e66a0b2f529c2695";
        Connection connection = null;
        LoginDao loginDao = new LoginDao();
        DbUtil dbUtil = new DbUtil();
        User user = new User(username, password);
        connection = dbUtil.getConnection();
        if (loginDao.login(connection, user) != null) {
            User result = loginDao.login(connection,user);
            System.out.println(result.getUsername());
            System.out.println(result.getPassword());
        }
    }

    public User login(Connection connection, User user) throws Exception {
        User resultUser = null;
        String sql = "select * from users where username=? and password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            resultUser = new User();
            resultUser.setUsername(rs.getString("username"));
            resultUser.setPassword(rs.getString("password"));
        }
        return resultUser;
    }
}

