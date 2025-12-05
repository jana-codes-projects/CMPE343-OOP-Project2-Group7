package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Domain model representing a user record from the {@code users} table.
 */
public class User
{
    private int userId;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String userRole;
    private LocalDateTime createdAt;

    /**
     * Default no-arg constructor.
     */
    public User()
    {

    }

    /**
     * Constructs a {@link User} from a JDBC {@link ResultSet}.
     *
     * @param rs result set positioned at a user row
     * @throws SQLException if a column cannot be read
     */
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
        if (firstName == null || firstName.isEmpty())
        {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (firstName.length() > 50)
        {
            throw new IllegalArgumentException("First name must be at most 50 characters");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        if (lastName == null || lastName.isEmpty())
        {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (lastName.length() > 50)
        {
            throw new IllegalArgumentException("Last name must be at most 50 characters");
        }
        this.lastName = lastName;
    }

    public void setUserRole(String userRole)
    {
        // Accept any non-empty role string; higher layers map it to an enum if needed
        if (userRole != null && !userRole.isEmpty())
        {
            this.userRole = userRole;
        }
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    // Other methods
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