package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    final String database_url = "jdbc:mysql://localhost:3306/project2database?useTimezone=true&serverTimezone=UTC";
    final String username = "myuser";
    final String password = "1234";

    // Open a connection
    public Connection getConnection() {
        Connection conn = null;
        try
        {
            // Ensure MySQL JDBC driver is registered (helps avoid "No suitable driver" errors)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(database_url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("MySQL JDBC Driver not found on classpath: " + e.getMessage());
        }
        catch (SQLException e)
        {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    // Close a connection
    public void close(Connection conn) {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
