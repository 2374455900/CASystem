package com.ca.dao;

import com.ca.model.User;
import com.ca.util.DateUtil;
import com.ca.util.DbUtil;

import java.awt.image.DataBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class changePasswordDao {
    public boolean change(Connection connection, User user) throws SQLException {
        String sql = "update users set updated_at=?, password=? where username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUpdated_at());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getUsername());
        return preparedStatement.executeUpdate() > 0;
    }

    public static void main(String[] args) throws Exception {
        changePasswordDao changePasswordDao = new changePasswordDao();
        DbUtil dbUtil = new DbUtil();
        Connection connection = dbUtil.getConnection();
        User user = new User(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),"222111222","22222");
        if (changePasswordDao.change(connection, user)){
            System.out.println("成功");
        }
    }
}
