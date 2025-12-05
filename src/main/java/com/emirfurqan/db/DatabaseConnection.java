package com.emirfurqan.db;

import com.emirfurqan.auth.LoginDetails;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final String url = "jdbc:mysql://localhost:3306/Project2Database?useTimezone=true&serverTimezone=UTC";
    private final String user = LoginDetails.username;
    private final String password = LoginDetails.password;

    // Open a connection
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    // Close a connection
    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
