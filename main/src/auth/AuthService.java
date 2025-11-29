package auth;

import db.DatabaseConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService
{

    private final DatabaseConnection db = new DatabaseConnection();

    // Login method: fetch user and verify password
    public User login(String username, String password) throws Exception
    {
        Connection conn = null;
        User user = null;

        try
        {
            conn = db.getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                user = new User(rs); // User constructor mapping ResultSet
                if (!verifyPassword(password, user.getPasswordHash()))
                {
                    throw new Exception("Invalid password");
                }
            }
            else
            {
                throw new Exception("User not found");
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            throw new Exception("Database error: " + e.getMessage());
        }
        finally
        {
            db.close(conn);
        }

        return user;
    }

    // Compare plain-text password with hashed password
    boolean verifyPassword(String plain, String hashed)
    {
        return PasswordHasher.verify(plain, hashed);
    }

    // Hash a plain password
    String hashPassword(String password)
    {
        return PasswordHasher.hash(password);
    }

    // Change password for a user
    public void changePassword(int userId, String newPassword) throws Exception
    {
        Connection conn = null;
        try
        {
            conn = db.getConnection();
            String hashed = hashPassword(newPassword);
            String query = "UPDATE users SET password_hash=? WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, hashed);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e)
        {
            throw new Exception("Database error: " + e.getMessage());
        }
        finally
        {
            db.close(conn);
        }
    }
}
