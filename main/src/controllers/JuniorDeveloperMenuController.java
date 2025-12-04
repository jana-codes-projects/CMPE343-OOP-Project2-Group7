package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import utils.ConsoleColor;
import utils.InputValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu controller for users with the {@code JUNIOR_DEVELOPER} role.
 * <p>
 * Juniors inherit all tester operations and additionally gain permission
 * to update existing contacts.
 */
public class JuniorDeveloperMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();
    private final InputValidator validator = new InputValidator();

    /**
     * Creates a new controller for a junior developer.
     *
     * @param user    the authenticated junior developer
     * @param scanner shared scanner for console input
     */
    public JuniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColor.BRIGHT_CYAN + "===== JUNIOR DEVELOPER MENU - " +
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                " =====" + ConsoleColor.RESET);
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Undo Last Operation");
        System.out.println("[0] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();
            case 2, 0 -> logout();
            case 3 -> listAllContacts();
            case 4 -> searchContactsSingle();
            case 5 -> searchContactsMultiple();
            case 6 -> sortContacts();
            case 7 -> updateContact();
            case 8 -> undo();
            default -> System.out.println(ConsoleColor.MAGENTA + "Unknown option. Please try again." + ConsoleColor.RESET);
        }
    }

    /**
     * Lists all contacts for the junior developer to view.
     *
     * @throws DatabaseException if loading contacts fails
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
     * Allows the user to perform a single‑field search.
     *
     * @throws DatabaseException if search fails
     */
    private void searchContactsSingle() throws DatabaseException
    {
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
     * Allows the user to perform a multi‑field search by specifying
     * several field/value pairs joined with logical AND.
     *
     * @throws DatabaseException if search fails
     */
    private void searchContactsMultiple() throws DatabaseException
    {
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
     * Sorts contacts in memory using the {@link ContactService}.
     *
     * @throws DatabaseException if loading contacts fails
     */
    private void sortContacts() throws DatabaseException
    {
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

    /**
     * Updates an existing contact in the database.
     * <p>
     * The junior developer is asked for a contact ID, then for new values for
     * several fields. Leaving a field blank keeps the existing value.
     *
     * @throws DatabaseException if updating the contact fails
     */
    private void updateContact() throws DatabaseException
    {
        System.out.print("Enter contact ID to update: ");
        String idInput = scanner.nextLine();
        int contactId;
        try {
            contactId = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid ID format." + ConsoleColor.RESET);
            return;
        }

        // Simple retrieval by ID using single‑field search (assuming unique IDs)
        List<Contact> matches = contactService.searchSingleField("contact_id", String.valueOf(contactId));
        if (matches.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contact found with ID " + contactId + "." + ConsoleColor.RESET);
            return;
        }

        Contact contact = matches.get(0);
        undoService.saveState(contact); // save previous state for potential undo

        System.out.println("Leave a field empty to keep the current value.");

        System.out.print("First name (" + contact.getFirstName() + ") [letters only]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            if (!validator.containsOnlyLetters(firstName)) {
                System.out.println(ConsoleColor.MAGENTA + "First name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            contact.setFirstName(firstName);
        }

        System.out.print("Middle name (" + contact.getMiddleName() + ") [letters only or empty]: ");
        String middleName = scanner.nextLine();
        if (!middleName.isBlank()) {
            if (!validator.containsOnlyLetters(middleName)) {
                System.out.println(ConsoleColor.MAGENTA + "Middle name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            contact.setMiddleName(middleName);
        }

        System.out.print("Last name (" + contact.getLastName() + ") [letters only]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            if (!validator.containsOnlyLetters(lastName)) {
                System.out.println(ConsoleColor.MAGENTA + "Last name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            contact.setLastName(lastName);
        }

        System.out.print("Primary phone (" + contact.getPhonePrimary() + ") [e.g. +90XXXXXXXXXX, codes: +1,+7,+20,+44,+55,+81,+86,+90,+91,+234]: ");
        String phonePrimary = scanner.nextLine();
        if (!phonePrimary.isBlank()) {
            if (!validator.isValidPhoneNumber(phonePrimary)) {
                System.out.println(ConsoleColor.MAGENTA + "Invalid phone number format." + ConsoleColor.RESET);
                return;
            }
            // Uniqueness: primary phone must not belong to another contact
            List<Contact> existingByPrimary = contactService.searchSingleField("phone_primary", phonePrimary);
            boolean usedByOther = existingByPrimary.stream()
                    .anyMatch(c -> c.getContactId() != contact.getContactId());
            if (usedByOther) {
                System.out.println(ConsoleColor.MAGENTA + "Primary phone is already used by another contact." + ConsoleColor.RESET);
                return;
            }
            contact.setPhonePrimary(phonePrimary);
        }

        System.out.print("Email (" + contact.getEmail() + ") [domains: @gmail.com, @outlook.com, @hotmail.com, @yahoo.com, @protonmail.com]: ");
        String email = scanner.nextLine();
        if (!email.isBlank()) {
            if (!validator.isValidEmail(email)) {
                System.out.println(ConsoleColor.MAGENTA + "Invalid email format." + ConsoleColor.RESET);
                return;
            }
            // Uniqueness: email must not belong to another contact
            List<Contact> existingByEmail = contactService.searchSingleField("email", email);
            boolean usedByOtherEmail = existingByEmail.stream()
                    .anyMatch(c -> c.getContactId() != contact.getContactId());
            if (usedByOtherEmail) {
                System.out.println(ConsoleColor.MAGENTA + "Email address is already used by another contact." + ConsoleColor.RESET);
                return;
            }
            contact.setEmail(email);
        }

        contact.updateTimestamp();
        contactService.updateContact(contact);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "Contact updated successfully." + ConsoleColor.RESET);
    }
}
