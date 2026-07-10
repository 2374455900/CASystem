package com.ca.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private final String dbUrl = "jdbc:mysql://192.168.3.234:3306/ca";
    private final String dbUserName = "root";
    private final String dbPassword = "";
    private final String jdbcName = "com.mysql.jdbc.Driver";

    /**
     * 获取数据库连接
     */
    public Connection getConnection() throws Exception {
        Class.forName(jdbcName);
        return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
    }

    /**
     * 关闭数据库连接
     */

    public void closeCon(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    // TODO 测试
    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();
        try {
            dbUtil.getConnection();
            System.out.println("数据库连接成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
