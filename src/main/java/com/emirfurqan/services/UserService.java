package com.emirfurqan.services;

import com.emirfurqan.dao.UserDao;
import com.emirfurqan.dao.UserDaoImplementation;
import com.emirfurqan.models.User;
import com.emirfurqan.exceptions.DatabaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService
{
    private final UserDao userDao = new UserDaoImplementation();

    // List all users
    public List<User> listUsers() throws DatabaseException
    {
        try
        {
            return userDao.findAllUsers();
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to list users", e);
        }
    }

    // Update a user
    public void updateUser(User user) throws DatabaseException
    {
        try
        {
            userDao.updateUser(user);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to update user", e);
        }
    }

    // Add a new user
    public void addUser(User user) throws DatabaseException
    {
        try
        {
            userDao.addUser(user);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to add user", e);
        }
    }

    // Delete a user by ID
    public void deleteUser(int userId) throws DatabaseException
    {
        try
        {
            userDao.deleteUser(userId);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to delete user", e);
        }
    }

    // Generate statistics about users (example: count per role)
    public Map<String, Object> generateStatistics() throws DatabaseException
    {
        Map<String, Object> stats = new HashMap<>();
        try
        {
            List<User> users = userDao.findAllUsers();
            Map<String, Integer> roleCounts = new HashMap<>();

            for (User user : users)
            {
                roleCounts.put(user.getUserRole(), roleCounts.getOrDefault(user.getUserRole(), 0) + 1);
            }

            stats.put("totalUsers", users.size());
            stats.put("roleCounts", roleCounts);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to generate user statistics", e);
        }
        return stats;
    }
}
