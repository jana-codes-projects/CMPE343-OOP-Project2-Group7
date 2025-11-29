package menus;

import models.User;

public class ManagerMenu
{
    private User currentUser;

    public ManagerMenu(User user)
    {
        this.currentUser = user;
    }

    public void changePassword(String newPassword)
    {

    }

    public void logout()
    {

    }

    /**
     * Show statistical info about contacts
     */
    public void viewContactStatistics()
    {
        // Call StatisticalInfoService.getStatistics
    }

    /**
     * List all users
     */
    public void listAllUsers()
    {
        // Call UserService.listUsers
    }

    /**
     * Update an existing user
     */
    public void updateUser(int userId)
    {
        // Call UserService.updateUser
    }

    /**
     * Add/employ new user
     */
    public void addUser()
    {
        // Call UserService.addUser
    }

    /**
     * Delete/fire existing user
     */
    public void deleteUser(int userId)
    {
        // Call UserService.deleteUser
    }

    void undoLast()
    {
        // TODO call UndoService.undo()
    }
}
