package com.emirfurqan.dao;

import com.emirfurqan.db.DatabaseConnection;
import com.emirfurqan.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplementation implements UserDao {

    private final DatabaseConnection db = new DatabaseConnection();

    @Override
    public User findByUsername(String username) {
        User userObj = null;
        String query = "SELECT * FROM users WHERE username = ?";

        Connection conn = null;
        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userObj = new User(rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }

        return userObj;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        Connection conn = null;
        try {
            conn = db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                users.add(new User(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }

        return users;
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE users SET username=?, password_hash=?, first_name=?, last_name=?, user_role=? WHERE user_id=?";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getUserRole());
            ps.setInt(6, user.getUserId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }

    @Override
    public void addUser(User user) {
        String query = "INSERT INTO users (username, password_hash, first_name, last_name, user_role, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getUserRole());
            ps.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }

    @Override
    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id=?";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }
}
