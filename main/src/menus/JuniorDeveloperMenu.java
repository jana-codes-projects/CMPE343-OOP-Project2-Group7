package menus;

import models.User;

public class JuniorDeveloperMenu {

    private User currentUser;

    public JuniorDeveloperMenu(User user)
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

    /**
     * Update an existing contact
     */
    public void updateContact(int contactId)
    {
        // Call ContactService.updateContact
    }

    void undoLast()
    {
        // TODO call UndoService.undo()
    }
}
