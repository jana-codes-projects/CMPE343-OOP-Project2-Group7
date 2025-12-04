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
 * Legacy-style senior developer menu that exposes high-level operations
 * on contacts without interactive menu loops.
 */
public class SeniorDeveloperMenu {

    private final User currentUser;
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();
    private final UndoService undoService = new UndoService();

    /**
     * Creates a new menu instance for the specified senior developer.
     *
     * @param user authenticated senior developer
     */
    public SeniorDeveloperMenu(User user)
    {
        this.currentUser = user;
    }

    /**
     * Changes the password of the current senior developer.
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
     * Prints a logout message.
     */
    public void logout()
    {
        System.out.println(ConsoleColor.BRIGHT_WHITE +
                "Senior Developer " + currentUser.getFirstName() + " " + currentUser.getLastName() + " logged out." +
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
     * Searches contacts by a single field.
     *
     * @param field contact field (e.g. {@code first_name})
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
     * @param fields map of field to search value
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
     * Sorts all contacts and prints them.
     *
     * @param field     the name of the field used for sorting
     * @param ascending {@code true} for ascending, {@code false} for descending
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
     * Updates an existing contact based on the provided ID.
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
     * Adds a new contact to the system.
     *
     * @param contact populated contact instance to persist
     */
    public void addContact(Contact contact)
    {
        try {
            contactService.addContact(contact);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Add failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Deletes an existing contact by ID.
     *
     * @param contactId ID of the contact to delete
     */
    public void deleteContact(int contactId)
    {
        try {
            List<Contact> matches = contactService.listAll();
            matches.stream()
                    .filter(c -> c.getContactId() == contactId)
                    .findFirst()
                    .ifPresent(undoService::saveState);

            contactService.deleteContact(contactId);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Delete failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Requests an undo from the internal {@link UndoService}.
     */
    void undoLast()
    {
        undoService.undoLastOperation();
    }
}
