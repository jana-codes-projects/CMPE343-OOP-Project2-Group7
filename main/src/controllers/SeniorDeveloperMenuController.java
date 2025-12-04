package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import utils.AsciiAnimations;
import utils.ConsoleColor;
import utils.InputValidator;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Menu controller for users with the {@code SENIOR_DEVELOPER} role.
 * <p>
 * Seniors can perform all junior operations and are additionally allowed
 * to add new contacts and delete existing contacts.
 */
public class SeniorDeveloperMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();
    private final InputValidator validator = new InputValidator();

    /**
     * Creates a new controller for a senior developer.
     *
     * @param user    the authenticated senior developer
     * @param scanner shared scanner for console input
     */
    public SeniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        AsciiAnimations.clearScreen();
        System.out.println(ConsoleColor.BRIGHT_CYAN + "===== SENIOR DEVELOPER MENU - " +
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                " =====" + ConsoleColor.RESET);
        System.out.println("[1] Change Password");
        System.out.println("[2] List All Contacts");
        System.out.println("[3] Search Contacts (Single Field)");
        System.out.println("[4] Search Contacts (Multiple Fields)");
        System.out.println("[5] Sort Contacts");
        System.out.println("[6] Update Contact");
        System.out.println("[7] Add Contact");
        System.out.println("[8] Delete Contact");
        System.out.println("[9] Undo Last Operation");
        System.out.println("[10] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();
            case 2 -> listAllContacts();
            case 3 -> searchContactsSingle();
            case 4 -> searchContactsMultiple();
            case 5 -> sortContacts();
            case 6 -> updateContact();
            case 7 -> addContact();
            case 8 -> deleteContact();
            case 9 -> undo();
            case 10 -> logout();
            default -> System.out.println(ConsoleColor.MAGENTA + "Unknown option. Please try again." + ConsoleColor.RESET);
        }
    }

    /**
     * Lists all contacts.
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
        // Header
        System.out.printf("%-4s %-12s %-10s %-12s %-10s %-20s %-20s %-30s %-15s %-11s %-11s %-11s%n",
                "ID", "First Name", "Middle", "Last Name", "Nickname", "Phone 1", "Phone 2", "Email", "LinkedIn",
                "Birth", "Created", "Updated");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Contact c : contacts) {
            PrintStream printf = System.out.printf("%-4d %-12s %-10s %-12s %-10s %-20s %-20s %-30s %-15s %-11s %-11s %-11s%n",
                    c.getContactId(),
                    truncate(c.getFirstName(), 12),
                    truncate(c.getMiddleName(), 10),
                    truncate(c.getLastName(), 12),
                    truncate(c.getNickname(), 10),
                    truncate(c.getPhonePrimary(), 20),
                    truncate(c.getPhoneSecondary(), 20),
                    truncate(c.getEmail(), 30),
                    truncate(c.getLinkedinUrl(), 15),
                    c.getBirthDate() != null ? c.getBirthDate().toString() : "",
                    c.getCreatedAt() != null ? c.getCreatedAt().toLocalDate().toString() : "",
                    c.getUpdatedAt() != null ? c.getUpdatedAt().toLocalDate().toString() : "");
        }
    }

    /**
     * Single‑field contact search.
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
     * Multi‑field contact search using field/value pairs joined with AND.
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
     * Sorts contacts in memory.
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
     * Updates an existing contact; same behaviour as in the junior controller
     * but available to senior developers as well.
     *
     * @throws DatabaseException if updating fails
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

        List<Contact> matches = contactService.searchSingleField("contact_id", String.valueOf(contactId));
        if (matches.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contact found with ID " + contactId + "." + ConsoleColor.RESET);
            return;
        }

        Contact contact = matches.get(0);
        undoService.saveState(contact);

        System.out.println("Leave a field empty to keep the current value.");

        System.out.print("First name (" + contact.getFirstName() + ") [letters only]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            if (firstName == null || firstName.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "First name cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(firstName))
            {
                System.out.println(ConsoleColor.MAGENTA + "First name must be at most 50 characters (current: " + firstName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(firstName))
            {
                System.out.println(ConsoleColor.MAGENTA + "First name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(firstName);
            contact.setFirstName(firstName);
        }

        System.out.print("Middle name (" + contact.getMiddleName() + ") [letters only or empty]: ");
        String middleName = scanner.nextLine();
        if (!middleName.isBlank()) {
            if (!validator.isValidNameLength(middleName))
            {
                System.out.println(ConsoleColor.MAGENTA + "Middle name must be at most 50 characters (current: " + middleName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(middleName)) {
                System.out.println(ConsoleColor.MAGENTA + "Middle name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(middleName);
            contact.setMiddleName(middleName);
        }

        System.out.print("Last name (" + contact.getLastName() + ") [letters only]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            if (lastName == null || lastName.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "Last name cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(lastName))
            {
                System.out.println(ConsoleColor.MAGENTA + "Last name must be at most 50 characters (current: " + lastName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(lastName)) {
                System.out.println(ConsoleColor.MAGENTA + "Last name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(lastName);
            contact.setLastName(lastName);
        }

        System.out.print("Nickname (" + contact.getLastName() + ") [letters only]: ");
        String nickname = scanner.nextLine();
        if (!nickname.isBlank()) {
            if (nickname == null || nickname.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "Nickname cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(nickname))
            {
                System.out.println(ConsoleColor.MAGENTA + "Nickname must be at most 50 characters (current: " + nickname.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(nickname)) {
                System.out.println(ConsoleColor.MAGENTA + "Nickname must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(nickname);
            contact.setLastName(nickname);
        }

        System.out.print("Primary phone (" + contact.getPhonePrimary() + ") [e.g. +90XXXXXXXXXX, codes: +1,+7,+20,+44,+55,+81,+86,+90,+91,+234]: ");
        String phonePrimary = scanner.nextLine();
        if (!phonePrimary.isBlank()) {
            if (!validator.isValidPhoneNumber(phonePrimary)) {
                System.out.println(ConsoleColor.MAGENTA + "Invalid phone number format." + ConsoleColor.RESET);
                return;
            }
            // Uniqueness: primary phone must not be used by another contact
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
            // Uniqueness: email must not be used by another contact
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

    /**
     * Adds a brand new contact to the database.
     *
     * @throws DatabaseException if persisting the contact fails
     */
    private void addContact() throws DatabaseException
    {
        Contact contact = new Contact();

        // Names
        System.out.print("First name [letters only]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            if (firstName == null || firstName.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "First name cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(firstName))
            {
                System.out.println(ConsoleColor.MAGENTA + "First name must be at most 50 characters (current: " + firstName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(firstName))
            {
                System.out.println(ConsoleColor.MAGENTA + "First name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(firstName);
            contact.setFirstName(firstName);
        }

        System.out.print("Middle name (optional): ");
        String middleName = scanner.nextLine();
        if (!middleName.isBlank()) {
            if (!validator.isValidNameLength(middleName))
            {
                System.out.println(ConsoleColor.MAGENTA + "Middle name must be at most 50 characters (current: " + middleName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(middleName)) {
                System.out.println(ConsoleColor.MAGENTA + "Middle name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(middleName);
            contact.setMiddleName(middleName);
        }

        System.out.print("Last name [letters only]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            if (lastName == null || lastName.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "Last name cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(lastName))
            {
                System.out.println(ConsoleColor.MAGENTA + "Last name must be at most 50 characters (current: " + lastName.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(lastName)) {
                System.out.println(ConsoleColor.MAGENTA + "Last name must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(lastName);
            contact.setLastName(lastName);
        }

        System.out.print("Nickname: ");
        String nickname = scanner.nextLine();
        if (!nickname.isBlank()) {
            if (nickname == null || nickname.trim().isEmpty())
            {
                System.out.println(ConsoleColor.MAGENTA + "Nickname cannot be empty." + ConsoleColor.RESET);
                return;
            }
            if (!validator.isValidNameLength(nickname))
            {
                System.out.println(ConsoleColor.MAGENTA + "Nickname must be at most 50 characters (current: " + nickname.length() + ")" + ConsoleColor.RESET);
                return;
            }
            if (!validator.containsOnlyLetters(nickname)) {
                System.out.println(ConsoleColor.MAGENTA + "Nickname must contain letters only." + ConsoleColor.RESET);
                return;
            }
            validator.formatName(nickname);
            contact.setLastName(nickname);
        }

        // Phones
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

        System.out.print("Secondary phone (optional) [same format as primary]: ");
        String phoneSecondary = scanner.nextLine();
        if (!phoneSecondary.isBlank()) {
            if (!validator.isValidPhoneNumber(phoneSecondary)) {
                System.out.println(ConsoleColor.MAGENTA + "Invalid phone number format." + ConsoleColor.RESET);
                return;
            }
            // Uniqueness: primary phone must not belong to another contact
            List<Contact> existingBySecondary = contactService.searchSingleField("phone_secondary", phoneSecondary);
            boolean usedByOther = existingBySecondary.stream()
                    .anyMatch(c -> c.getContactId() != contact.getContactId());
            if (usedByOther) {
                System.out.println(ConsoleColor.MAGENTA + "Primary phone is already used by another contact." + ConsoleColor.RESET);
                return;
            }
            contact.setPhoneSecondary(phoneSecondary);
        }

        // Email
        System.out.print("Email [domains: @gmail.com, @outlook.com, @hotmail.com, @yahoo.com, @protonmail.com]: ");
        String email = scanner.nextLine();
        if (!validator.isValidEmail(email))
        {
            System.out.println(ConsoleColor.MAGENTA + "Invalid email format." + ConsoleColor.RESET);
            return;
        }
        contact.setEmail(email);

        System.out.print("LinkedIn URL (optional): ");
        contact.setLinkedinUrl(scanner.nextLine());

        // Birth date
        System.out.print("Birth date (yyyy-MM-dd) (optional): ");
        String birthDateStr = scanner.nextLine();
        if (!birthDateStr.isBlank())
        {
            if (!validator.isValidDate(birthDateStr))
            {
                System.out.println(ConsoleColor.MAGENTA + "Invalid date. Please use yyyy-MM-dd (e.g. 2005-09-12)." + ConsoleColor.RESET);
                return;
            }
            contact.setBirthDate(LocalDate.parse(birthDateStr));
        }

        // Uniqueness checks for email
        List<Contact> existingByEmail = contactService.searchSingleField("email", email);
        if (!existingByEmail.isEmpty())
        {
            System.out.println(ConsoleColor.MAGENTA + "Email address is already used by another contact." + ConsoleColor.RESET);
            return;
        }

        contact.setCreatedAt(java.time.LocalDateTime.now());
        contact.updateTimestamp();

        contactService.addContact(contact);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "Contact added successfully." + ConsoleColor.RESET);
    }

    /**
     * Deletes a contact by ID after saving its previous state.
     *
     * @throws DatabaseException if deletion fails
     */
    private void deleteContact() throws DatabaseException
    {
        System.out.print("Enter contact ID to delete: ");
        String idInput = scanner.nextLine();
        int contactId;
        try {
            contactId = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid ID format." + ConsoleColor.RESET);
            return;
        }

        List<Contact> matches = contactService.searchSingleField("contact_id", String.valueOf(contactId));
        if (matches.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No contact found with ID " + contactId + "." + ConsoleColor.RESET);
            return;
        }

        Contact toDelete = matches.get(0);
        undoService.saveState(toDelete);

        contactService.deleteContact(contactId);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "Contact deleted successfully." + ConsoleColor.RESET);
    }

    /**
     * Helper to truncate strings that exceed a certain length.
     */
    private String truncate(String input, int maxLength) {
        if (input == null)
            return "";
        if (input.length() <= maxLength)
            return input;
        return input.substring(0, maxLength - 3) + "...";
    }
}
