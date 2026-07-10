package com.ca.dao;

import com.ca.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistDao {

    public boolean regist(Connection connection, User user) throws SQLException {
        String sql = "insert into users values(null,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getCreated_at());
        preparedStatement.setString(2, user.getUpdated_at());
        preparedStatement.setString(3, user.getDeleted_at());
        preparedStatement.setString(4, user.getUsername());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getEmail());
        preparedStatement.setString(7, user.getAuthority());
        int rs = preparedStatement.executeUpdate();
        return rs > 0;
    }


}
