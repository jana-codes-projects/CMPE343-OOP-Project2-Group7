package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import auth.AuthService;
import utils.ConsoleColors;
import utils.InputValidator; // Assumed to be available

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller class for the Junior Developer role.
 * This role typically has read/write access (list, search, sort, update) and an undo capability.
 * This class extends BaseMenuController to inherit common functionality (like constructor and basic user methods).
 */
public class JuniorDeveloperMenuController extends BaseMenuController
{
    // Service layer instances for handling business logic interactions.
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    /**
     * Constructor for JuniorDeveloperMenuController.
     * @param user The authenticated User object.
     * @param scanner The Scanner object used for console input.
     */
    public JuniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner); // Initialize the base controller with User and Scanner.
    }

    /**
     * Displays the menu options specific to the Junior Developer role.
     * This role includes basic CRUD operations (excluding Create/Delete) and Undo.
     */
    @Override
    public void displayMenu()
    {
        // Display a stylized header for the menu.
        System.out.println(ConsoleColors.CYAN + "======================================================");
        System.out.println(ConsoleColors.CYAN + " JUNIOR DEVELOPER MENU - WELCOME, " + user.getFirstName() + "!");
        System.out.println(ConsoleColors.CYAN + "======================================================" + ConsoleColors.RESET);
        
        // Menu options tailored for the Junior Developer role permissions.
        System.out.println("[1] Change Password");
        System.out.println("[2] Log Out"); 
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contact (Single Field)");
        System.out.println("[5] Search Contact (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Undo Last Operation (Reverts last update)");
        System.out.println("[0] Log Out and Exit");
        System.out.print(ConsoleColors.GREEN + "Enter your choice: " + ConsoleColors.RESET);
    }

    /**
     * Handles the user's input selection and delegates the action to the appropriate method.
     * @param option The integer representing the user's menu choice.
     * @throws DatabaseException If a database operation fails during execution.
     */
    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();             // Case 1: Password change
            case 2 -> logout();                     // Case 2: Logout
            case 3 -> contactService.listAll(true); // Case 3: List all contacts (with detailed output = true)
            case 4 -> searchContactsSingle();       // Case 4: Search by one field
            case 5 -> searchContactsMultiple();     // Case 5: Search by multiple criteria
            case 6 -> sortContacts();               // Case 6: Sort contacts list
            case 7 -> updateContact();              // Case 7: Modify an existing contact
            case 8 -> undo();                       // Case 8: Revert the last operation (e.g., last update)
            case 0 -> logout();                     // Case 0: Logout and exit application
            default -> System.out.println(ConsoleColors.RED + "Invalid selection. Please enter a number from the menu." + ConsoleColors.RESET);
        }
    }

    // =========================================================================
    // Methods Inherited from BaseMenuController (Implemented Here)
    // =========================================================================

    /**
     * Case 1: Allows the authenticated user to change their password.
     * Requires the current password for verification via AuthService.
     */
    @Override
    protected void changePassword() throws DatabaseException {
        System.out.println(ConsoleColors.YELLOW + "\n--- CHANGE PASSWORD ---" + ConsoleColors.RESET);
        
        // 1. Get current password for authentication.
        System.out.print("Enter Current Password: ");
        String currentPassword = scanner.nextLine();
        
        // 2. Get the desired new password.
        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        // 3. Call AuthService to perform verification, hashing, and database update.
        if (authService.changePassword(user, currentPassword, newPassword)) {
            System.out.println(ConsoleColors.GREEN + "Password successfully changed." + ConsoleColors.RESET);
        } else {
            // Failure usually means current password mismatch or new password fails validation rules.
            System.out.println(ConsoleColors.RED + "Error: Current password is incorrect or new password requirements are not met." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 2 and Case 0: Logs the user out and terminates the application.
     */
    @Override
    protected void logout() {
        System.out.println(ConsoleColors.CYAN + "Logging out... Goodbye!" + ConsoleColors.RESET);
        System.exit(0); // Exit the Java Virtual Machine.
    }

    // =========================================================================
    // Implementations Specific to Junior Developer Role
    // =========================================================================

    /**
     * Case 4: Searches for contacts based on a single field (First Name or Last Name).
     */
    private void searchContactsSingle() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- SINGLE FIELD SEARCH ---" + ConsoleColors.RESET);
        System.out.println("[1] Search by First Name | [2] Search by Last Name");
        System.out.print("Select search type: ");
        
        try {
            int searchType = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter value to search: ");
            String searchValue = scanner.nextLine().trim();
            
            // Determine the database column name based on user choice.
            String fieldName = switch (searchType) {
                case 1 -> "first_name";
                case 2 -> "last_name";
                default -> {
                    System.out.println(ConsoleColors.RED + "Invalid search type." + ConsoleColors.RESET);
                    yield null;
                }
            };
            
            if (fieldName != null) {
                // Call the service layer; 'true' enables case-insensitive matching.
                List<Contact> results = contactService.searchByField(fieldName, searchValue, true); 
                
                if (results.isEmpty()) {
                    System.out.println(ConsoleColors.PURPLE + "No contacts found matching the criteria." + ConsoleColors.RESET);
                } else {
                    contactService.displayContacts(results);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid input. Please enter a number for the search type." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 5: Searches for contacts based on multiple, user-defined fields (flexible criteria).
     */
    private void searchContactsMultiple() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- MULTI-FIELD SEARCH ---" + ConsoleColors.RESET);
        // Map stores the search criteria: Key=Field Name (DB column), Value=Search Value.
        Map<String, String> criteria = new HashMap<>();
        
        System.out.println("Flexible Search: Enter fields and values (Leave field name empty to finish):");
        
        // Loop to collect multiple criteria from the user.
        while (true) {
            System.out.print("Field Name (first_name, last_name, email, etc.): ");
            String field = scanner.nextLine().trim().toLowerCase();
            if (field.isEmpty()) break; // Exit loop if field name is left blank.
            
            System.out.print("Value: ");
            String value = scanner.nextLine().trim();
            criteria.put(field, value);
        }

        if (!criteria.isEmpty()) {
            // Pass the map of criteria to the service layer for complex querying.
            List<Contact> results = contactService.searchByMultipleFields(criteria);
            if (results.isEmpty()) {
                System.out.println(ConsoleColors.PURPLE + "No contacts found matching the criteria." + ConsoleColors.RESET);
            } else {
                contactService.displayContacts(results);
            }
        } else {
             System.out.println(ConsoleColors.RED + "Search cancelled. No criteria entered." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 6: Retrieves and displays all contacts sorted by a user-specified field and order.
     */
    private void sortContacts() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- SORT CONTACTS ---" + ConsoleColors.RESET);
        System.out.println("Sortable Fields: first_name, last_name, birth_date, created_at, ...");
        
        System.out.print("Enter the Field to Sort By: ");
        String field = scanner.nextLine().trim();

        System.out.print("Sort Order (ASC/DESC): ");
        String order = scanner.nextLine().trim().toUpperCase();

        // Validate sort order input.
        if (!order.equals("ASC") && !order.equals("DESC")) {
            System.out.println(ConsoleColors.RED + "Error: Sort order must be 'ASC' or 'DESC'." + ConsoleColors.RESET);
            return;
        }

        // Call service to fetch and sort contacts.
        List<Contact> sortedContacts = contactService.sortContacts(field, order);
        contactService.displayContacts(sortedContacts);
    }

    /**
     * Case 7: Updates one or more fields of an existing contact based on its ID.
     */
    private void updateContact() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- UPDATE CONTACT ---" + ConsoleColors.RESET);
        System.out.print("Enter the ID of the contact to update: ");
        
        try {
            // Get the ID of the contact to be modified.
            long contactId = Long.parseLong(scanner.nextLine());
            
            // Map to store fields and their new values.
            Map<String, String> updatedFields = new HashMap<>();
            System.out.println("Enter the fields to update (Leave field name empty to finish).");

            // Loop to collect fields to update.
            while (true) {
                System.out.print("Field Name: ");
                String field = scanner.nextLine().trim();
                if (field.isEmpty()) break;
                
                System.out.print("New Value for " + field + ": ");
                String value = scanner.nextLine().trim();
                updatedFields.put(field, value);
            }

            if (updatedFields.isEmpty()) {
                System.out.println(ConsoleColors.PURPLE + "No fields entered for update. Operation cancelled." + ConsoleColors.RESET);
                return;
            }

            // Execute the update operation via the service.
            contactService.updateContact(contactId, updatedFields);
            System.out.println(ConsoleColors.GREEN + "Contact (ID: " + contactId + ") successfully updated." + ConsoleColors.RESET);

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid ID format. Please enter a valid number." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 8: Reverts the last persistent operation performed on contacts (e.g., the last update).
     * This relies on an internal command/history mechanism within ContactService.
     */
    private void undo() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- UNDO LAST OPERATION ---" + ConsoleColors.RESET);
        try {
            // Attempt to undo the last operation.
            contactService.undoLastOperation();
            System.out.println(ConsoleColors.GREEN + "Last operation successfully undone." + ConsoleColors.RESET);
        } catch (IllegalStateException e) {
            // Catch if the undo stack is empty.
            System.out.println(ConsoleColors.PURPLE + "No operation available to undo: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
