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
 * Legacy-style tester menu exposing the same operations as the controller,
 * but without handling user interaction loops.
 * <p>
 * Each method assumes it is called with already collected parameters.
 */
public class TesterMenu
{
    private final User user;
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();
    private final UndoService undoService = new UndoService();

    /**
     * Creates a new tester menu for the given user.
     *
     * @param user current tester
     */
    public TesterMenu(User user)
    {
        this.user = user;
    }

    /**
     * Changes the password of the current tester.
     *
     * @param newPassword plain‑text new password
     * @throws Exception if updating the password in the database fails
     */
    public void changePassword(String newPassword) throws Exception
    {
        authService.changePassword(user.getUserId(), newPassword);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "Password updated successfully." + ConsoleColor.RESET);
    }

    /**
     * Simulates logout for this menu abstraction.
     * <p>
     * The actual application handles loop control; this method only prints a message.
     */
    public void logout()
    {
        System.out.println(ConsoleColor.BRIGHT_WHITE +
                "Tester " + user.getFirstName() + " " + user.getLastName() + " logged out." +
                ConsoleColor.RESET);
    }

    /**
     * Lists all contacts to the console.
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
     * Searches contacts by a single field and prints results.
     *
     * @param field field name in the contacts table
     * @param value value or substring to search for
     */
    public void searchByField(String field, String value)
    {
        try {
            List<Contact> results = contactService.searchSingleField(field, value);
            results.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Search failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Searches contacts by multiple fields joined with logical AND.
     *
     * @param fields map of field name to value/substring
     */
    public void searchByFields(Map<String, String> fields)
    {
        try {
            List<Contact> results = contactService.searchMultipleFields(fields);
            results.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Search failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Sorts and prints all contacts using a given field and direction.
     *
     * @param field     database field to sort by
     * @param ascending {@code true} for ascending, {@code false} for descending
     */
    public void sortResults(String field, boolean ascending)
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
     * Performs an undo request using the internal {@link UndoService}.
     */
    public void undoLast()
    {
        undoService.undoLastOperation();
    }
}