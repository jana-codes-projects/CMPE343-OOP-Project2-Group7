package menus;

import auth.AuthService;
import models.Contact;
import models.User;
import services.ContactService;
import services.UndoService;
import utils.ConsoleColor;

import java.util.List;
import java.util.Map;

/**
 * Legacy-style junior developer menu which provides simple, parameter-driven
 * access to contact operations without handling console input loops.
 */
public class JuniorDeveloperMenu {

    private final User currentUser;
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();
    private final UndoService undoService = new UndoService();

    /**
     * Creates a new menu wrapper for the given junior developer.
     *
     * @param user authenticated junior developer
     */
    public JuniorDeveloperMenu(User user)
    {
        this.currentUser = user;
    }

    /**
     * Changes the password of the current junior developer.
     *
     * @param newPassword new plain‑text password
     * @throws Exception if the database update fails
     */
    public void changePassword(String newPassword) throws Exception
    {
        authService.changePassword(currentUser.getUserId(), newPassword);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "Password updated successfully." + ConsoleColor.RESET);
    }

    /**
     * Prints a simple logout message.
     */
    public void logout()
    {
        System.out.println(ConsoleColor.BRIGHT_WHITE +
                "Junior Developer " + currentUser.getFirstName() + " " + currentUser.getLastName() + " logged out." +
                ConsoleColor.RESET);
    }

    /**
     * Lists all contacts available in the system.
     */
    public void listAllContacts()
    {
        try {
            List<Contact> contacts = contactService.listAll();
            contacts.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Failed to list contacts: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Searches contacts by a single field.
     *
     * @param field field name such as {@code first_name} or {@code email}
     * @param value substring to match
     */
    public void searchContacts(String field, String value)
    {
        try {
            List<Contact> results = contactService.searchSingleField(field, value);
            results.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Search failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Searches contacts by multiple fields joined with AND.
     *
     * @param fields field/value pairs
     */
    public void searchContacts(Map<String, String> fields)
    {
        try {
            List<Contact> results = contactService.searchMultipleFields(fields);
            results.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Search failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Sorts and prints all contacts using the provided field and direction.
     *
     * @param field     field to sort by
     * @param ascending sort direction flag
     */
    public void sortContacts(String field, boolean ascending)
    {
        try {
            List<Contact> contacts = contactService.listAll();
            contactService.sortContacts(contacts, field, ascending);
            contacts.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Sorting failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Updates an existing contact identified by ID.
     *
     * @param contactId identifier of the contact to update
     */
    public void updateContact(int contactId)
    {
        try {
            List<Contact> matches = contactService.searchSingleField("contact_id", String.valueOf(contactId));
            if (matches.isEmpty()) {
                System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contact found with ID " + contactId + "." + ConsoleColor.RESET);
                return;
            }

            Contact existing = matches.get(0);
            undoService.saveState(existing);
            contactService.updateContact(existing);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Update failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Requests an undo of the last saved state.
     */
    void undoLast()
    {
        undoService.undoLastOperation();
    }
}
