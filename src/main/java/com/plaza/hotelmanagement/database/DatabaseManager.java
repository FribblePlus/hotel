package com.plaza.hotelmanagement.database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
