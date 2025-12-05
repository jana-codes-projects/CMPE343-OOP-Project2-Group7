package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import utils.ConsoleColor;
import utils.InputValidator;

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
        System.out.println(ConsoleColor.BRIGHT_CYAN + "===== SENIOR DEVELOPER MENU - " +
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                " =====" + ConsoleColor.RESET);
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Add Contact");
        System.out.println("[9] Delete Contact");
        System.out.println("[10] Undo Last Operation");
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
            case 8 -> addContact();
            case 9 -> deleteContact();
            case 10 -> undo();
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
        for (Contact c : contacts) {
            System.out.println(c);
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

        System.out.print("First name (" + contact.getFirstName() + ") [letters only, max 50 chars]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            firstName = validator.formatName(firstName);
            String firstNameError = validator.getNameErrorMessage(firstName, "First name");
            if (firstNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + firstNameError + ConsoleColor.RESET);
                return;
            }
            try {
                contact.setFirstName(firstName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        System.out.print("Middle name (" + contact.getMiddleName() + ") [letters only, max 50 chars or empty]: ");
        String middleName = scanner.nextLine();
        if (!middleName.isBlank()) {
            middleName = validator.formatName(middleName);
            String middleNameError = validator.getNameErrorMessage(middleName, "Middle name");
            if (middleNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + middleNameError + ConsoleColor.RESET);
                return;
            }
            try {
                contact.setMiddleName(middleName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        System.out.print("Last name (" + contact.getLastName() + ") [letters only, max 50 chars]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            lastName = validator.formatName(lastName);
            String lastNameError = validator.getNameErrorMessage(lastName, "Last name");
            if (lastNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + lastNameError + ConsoleColor.RESET);
                return;
            }
            try {
                contact.setLastName(lastName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
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
        System.out.print("First name [letters only, max 50 chars]: ");
        String firstName = scanner.nextLine();
        firstName = validator.formatName(firstName);
        String firstNameError = validator.getNameErrorMessage(firstName, "First name");
        if (firstNameError != null) {
            System.out.println(ConsoleColor.MAGENTA + firstNameError + ConsoleColor.RESET);
            return;
        }
        try {
            contact.setFirstName(firstName);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
            return;
        }

        System.out.print("Middle name (optional, max 50 chars): ");
        String middleName = scanner.nextLine();
        if (!middleName.isBlank()) {
            middleName = validator.formatName(middleName);
            String middleNameError = validator.getNameErrorMessage(middleName, "Middle name");
            if (middleNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + middleNameError + ConsoleColor.RESET);
                return;
            }
            try {
                contact.setMiddleName(middleName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        System.out.print("Last name [letters only, max 50 chars]: ");
        String lastName = scanner.nextLine();
        lastName = validator.formatName(lastName);
        String lastNameError = validator.getNameErrorMessage(lastName, "Last name");
        if (lastNameError != null) {
            System.out.println(ConsoleColor.MAGENTA + lastNameError + ConsoleColor.RESET);
            return;
        }
        try {
            contact.setLastName(lastName);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
            return;
        }

        System.out.print("Nickname (optional, max 40 chars): ");
        String nickname = scanner.nextLine();
        if (!nickname.isBlank() && nickname.length() > 40) {
            System.out.println(ConsoleColor.MAGENTA + "Nickname must be at most 40 characters" + ConsoleColor.RESET);
            return;
        }
        try {
            contact.setNickname(nickname);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
            return;
        }

        // Phones
        System.out.print("Primary phone [e.g. +90XXXXXXXXXX, codes: +1,+7,+20,+44,+55,+81,+86,+90,+91,+234]: ");
        String primaryPhone = scanner.nextLine();
        if (!validator.isValidPhoneNumber(primaryPhone)) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid primary phone format." + ConsoleColor.RESET);
            return;
        }
        contact.setPhonePrimary(primaryPhone);

        System.out.print("Secondary phone (optional) [same format as primary]: ");
        String secondaryPhone = scanner.nextLine();
        if (!secondaryPhone.isBlank() && !validator.isValidPhoneNumber(secondaryPhone)) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid secondary phone format." + ConsoleColor.RESET);
            return;
        }
        contact.setPhoneSecondary(secondaryPhone.isBlank() ? null : secondaryPhone);

        // Email
        System.out.print("Email [domains: @gmail.com, @outlook.com, @hotmail.com, @yahoo.com, @protonmail.com]: ");
        String email = scanner.nextLine();
        if (!validator.isValidEmail(email)) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid email format." + ConsoleColor.RESET);
            return;
        }
        contact.setEmail(email);

        System.out.print("LinkedIn URL (optional): ");
        contact.setLinkedinUrl(scanner.nextLine());

        // Birth date
        System.out.print("Birth date (yyyy-MM-dd) (optional): ");
        String birthDateStr = scanner.nextLine();
        if (!birthDateStr.isBlank()) {
            if (!validator.isValidBirthdate(birthDateStr)) {
                String errorMsg = validator.getBirthdateErrorMessage(birthDateStr);
                System.out.println(ConsoleColor.MAGENTA + errorMsg + ConsoleColor.RESET);
                return;
            }
            try {
                contact.setBirthDate(LocalDate.parse(birthDateStr));
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        // Uniqueness checks for phone and email
        List<Contact> existingByPrimary = contactService.searchSingleField("phone_primary", primaryPhone);
        if (!existingByPrimary.isEmpty()) {
            System.out.println(ConsoleColor.MAGENTA + "Primary phone is already used by another contact." + ConsoleColor.RESET);
            return;
        }
        if (contact.getPhoneSecondary() != null && !contact.getPhoneSecondary().isBlank()) {
            List<Contact> existingBySecondary = contactService.searchSingleField("phone_secondary", contact.getPhoneSecondary());
            if (!existingBySecondary.isEmpty()) {
                System.out.println(ConsoleColor.MAGENTA + "Secondary phone is already used by another contact." + ConsoleColor.RESET);
                return;
            }
        }
        List<Contact> existingByEmail = contactService.searchSingleField("email", email);
        if (!existingByEmail.isEmpty()) {
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
}
