package menus;

import models.User;

import java.util.Map;

public class TesterMenu
{
    private User user;

    public TesterMenu(User user)
    {
        this.user = user;
    }

    /**
     * Change password
     */
    void changePassword()
    {
        // TODO prompt new password → call AuthService
    }

    /**
     * Logout user
     */
    void logout()
    {
        // TODO exit menu to Login
    }

    /**
     * List all contacts
     */
    void listAllContacts()
    {
        // TODO call ContactService.listAll()
    }

    /**
     * Search contacts by single or multiple fields
     */
    void searchByField(String field, String value)
    {
        // TODO call ContactService.searchSingleField()
    }

    void searchByFields(Map<String, String> fields)
    {
        // TODO call ContactService.searchMultipleFields()
    }

    /**
     * Sort results ascending/descending by field
     */
    void sortResults(String field, boolean ascending)
    {
        // TODO call ContactService.sortContacts()
    }

    void undoLast()
    {
        // TODO call UndoService.undo()
    }

}
