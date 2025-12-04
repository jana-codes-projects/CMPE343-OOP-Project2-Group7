package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import utils.ConsoleColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu controller for users with the {@code TESTER} role.
 * <p>
 * Testers can change their password, list and search contacts, and sort
 * search results, but cannot modify the underlying contact data.
 */
public class TesterMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();

    /**
     * Creates a new controller for a tester.
     *
     * @param user    the authenticated tester
     * @param scanner shared scanner for console input
     */
    public TesterMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColor.BRIGHT_CYAN + "===== TESTER MENU - " +
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                " =====" + ConsoleColor.RESET);
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Undo Last Operation");
        System.out.println("[0] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option)
        {
            case 1 -> changePassword();
            case 2, 0 -> logout();
            case 3 -> listAllContacts();
            case 4 -> searchContactsSingle();
            case 5 -> searchContactsMultiple();
            case 6 -> sortContacts();
            case 7 -> undo();
            default -> System.out.println(ConsoleColor.MAGENTA + "Unknown option. Please try again." + ConsoleColor.RESET);
        }
    }

    /**
     * Lists all contacts and prints them to the console.
     *
     * @throws DatabaseException if the DAO layer fails
     */
    private void listAllContacts() throws DatabaseException {
        List<Contact> contacts = contactService.listAll();
        if (contacts.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contacts found." + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_BLUE + "All contacts:" + ConsoleColor.RESET);
        for (Contact c : contacts) {
            System.out.println(c);
        }
    }

    /**
     * Performs a single‑field search based on user input.
     * <p>
     * The user is prompted for a field name (e.g. {@code first_name},
     * {@code last_name}, {@code phone_primary}) and a search value. Matching
     * contacts are printed to the console.
     *
     * @throws DatabaseException if the DAO layer fails
     */
    private void searchContactsSingle() throws DatabaseException {
        System.out.print("Enter field to search by (first_name, last_name, phone_primary): ");
        String field = scanner.nextLine();
        System.out.print("Enter value to search for: ");
        String value = scanner.nextLine();

        List<Contact> results = contactService.searchSingleField(field, value);
        if (results.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contacts matched your criteria." + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_BLUE + "Search results:" + ConsoleColor.RESET);
        for (Contact c : results) {
            System.out.println(c);
        }
    }

    /**
     * Performs a multi‑field search based on user input.
     * <p>
     * The user is asked to provide two or more field/value pairs, joined
     * with logical {@code AND}. Flexible, substring-based matching is used.
     *
     * @throws DatabaseException if the DAO layer fails
     */
    private void searchContactsMultiple() throws DatabaseException {
        Map<String, String> fields = new HashMap<>();

        System.out.println("Enter field/value pairs for search (leave field empty to finish).");
        while (true) {
            System.out.print("Field name (e.g. first_name, birth_date, email): ");
            String field = scanner.nextLine();
            if (field == null || field.isBlank()) {
                break;
            }
            System.out.print("Value for '" + field + "': ");
            String value = scanner.nextLine();

            fields.put(field, value);
        }

        if (fields.isEmpty()) {
            System.out.println(ConsoleColor.MAGENTA + "No fields specified. Search cancelled." + ConsoleColor.RESET);
            return;
        }

        List<Contact> results = contactService.searchMultipleFields(fields);
        if (results.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contacts matched your criteria." + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_BLUE + "Search results:" + ConsoleColor.RESET);
        for (Contact c : results) {
            System.out.println(c);
        }
    }

    /**
     * Sorts contacts based on a field and direction chosen by the user.
     * <p>
     * The contacts are first loaded from the database, then sorted
     * in memory by {@link ContactService#sortContacts(List, String, boolean)}.
     */
    private void sortContacts() throws DatabaseException {
        System.out.print("Enter field to sort by (first_name, last_name, nickname, email, birth_date): ");
        String field = scanner.nextLine();
        System.out.print("Sort ascending? (y/n): ");
        String ascInput = scanner.nextLine();
        boolean ascending = !"n".equalsIgnoreCase(ascInput);

        List<Contact> contacts = contactService.listAll();
        try {
            contactService.sortContacts(contacts, field, ascending);
        } catch (IllegalArgumentException ex) {
            System.out.println(ConsoleColor.MAGENTA + ex.getMessage() + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_BLUE + "Sorted contacts:" + ConsoleColor.RESET);
        for (Contact c : contacts) {
            System.out.println(c);
        }
    }
}
