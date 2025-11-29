package menus;

import models.User;

public class SeniorDeveloperMenu {

    private User currentUser;

    public SeniorDeveloperMenu(User user)
    {
        this.currentUser = user;
    }

    public void changePassword(String newPassword)
    {

    }

    public void logout()
    {

    }

    public void listAllContacts()
    {

    }

    public void searchContacts(String field, String value)
    {

    }

    public void searchContacts(java.util.Map<String, String> fields)
    {

    }

    public void sortContacts(String field, boolean ascending)
    {

    }

    public void updateContact(int contactId)
    {

    }

    /**
     * Add new contact
     */
    public void addContact()
    {
        // Call ContactService.addContact
    }

    /**
     * Delete existing contact
     */
    public void deleteContact(int contactId)
    {
        // Call ContactService.deleteContact
    }

    void undoLast()
    {
        // TODO call UndoService.undo()
    }
}
