package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Train";
    private static final String USER = "root";
    private static final String PASSWORD = "123Sharvesh$$$";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to establish and return a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to close the connection and statement

    public static void closeResources(Connection connection, Statement statement) {
        try {
            if (statement != null) statement.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception during resource close: " + e.getMessage());
        }
    }

    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception during ResultSet close: " + e.getMessage());
        }

        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception during Statement close: " + e.getMessage());
        }

        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception during Connection close: " + e.getMessage());
        }
    }

    public static void closeResources(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception during Connection close: " + e.getMessage());
        }
    }
}
