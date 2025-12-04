package services;

import dao.UserDao;
import dao.UserDaoImplementation;
import models.User;
import exceptions.DatabaseException;

import java.util.List;

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
}
