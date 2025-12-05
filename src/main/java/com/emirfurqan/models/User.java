package com.emirfurqan.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class User
{
    private int userId;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String userRole;
    private LocalDateTime createdAt;

    // Default constructor
    public User()
    {

    }

    // Constructor from ResultSet
    public User(ResultSet rs) throws SQLException
    {
        this.userId = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.passwordHash = rs.getString("password_hash");
        this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
        this.userRole = rs.getString("user_role");
        this.createdAt = rs.getTimestamp("created_at").toLocalDateTime();
    }

    // Getters
    public int getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getUserRole()
    {
        return userRole;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    // Setters
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setUsername(String username)
    {
        if (username != null && !username.isEmpty())
        {
            this.username = username;
        }
    }

    public void setPasswordHash(String passwordHash)
    {
        // Example: ensure password hash is not empty
        if (passwordHash != null && !passwordHash.isEmpty())
        {
            this.passwordHash = passwordHash;
        }
    }

    public void setFirstName(String firstName)
    {
        if (firstName != null && !firstName.isEmpty())
        {
            this.firstName = firstName;
        }
    }

    public void setLastName(String lastName)
    {
        if (lastName != null && !lastName.isEmpty())
        {
            this.lastName = lastName;
        }
    }

    public void setUserRole(String userRole)
    {
        // Example: allow only "admin" or "user"
        if ("admin".equalsIgnoreCase(userRole) || "user".equalsIgnoreCase(userRole))
        {
            this.userRole = userRole;
        }
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    // Other methods
    public boolean isAdmin()
    {
        return "admin".equalsIgnoreCase(this.userRole);
    }

    public boolean validatePassword(String plainPassword)
    {
        // Placeholder: compare hash with plain password
        return this.passwordHash.equals(plainPassword); // Replace with proper hash check
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userRole='" + userRole + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
