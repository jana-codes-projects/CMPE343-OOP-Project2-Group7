package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import auth.AuthService;
import utils.ConsoleColors; // Assuming ConsoleColors is available for styling
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller class for the Tester role.
 * This role typically has read-only access to contact data (List, Search, Sort) 
 * but does not have CUD (Create, Update, Delete) permissions on the main entities.
 * The 'Undo Last Operation' feature here might refer to undoing the last read-only state manipulation
 * like a filter or sort, or it might be disabled/abstracted if no state-changing actions are allowed.
 */
public class TesterMenuController extends BaseMenuController
{
    // Service layer instances for handling business logic interactions.
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    /**
     * Constructor for TesterMenuController.
     * @param user The authenticated User object.
     * @param scanner The Scanner object used for console input.
     */
    public TesterMenuController(User user, Scanner scanner)
    {
        super(user, scanner); // Initialize the base controller.
    }

    /**
     * Displays the menu options specific to the Tester role, which emphasizes data viewing and querying.
     */
    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColors.BLUE_BRIGHT + "======================================================");
        System.out.println(ConsoleColors.BLUE_BRIGHT + "       TESTER MENU - WELCOME, " + user.getFirstName() + "!");
        System.out.println(ConsoleColors.BLUE_BRIGHT + "======================================================" + ConsoleColors.RESET);
        
        // Menu options (Read-only access focus)
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Undo Last Operation (If applicable)"); // Read-only role often means this is less functional
        System.out.println("[0] Logout and Exit");
        System.out.print(ConsoleColors.GREEN + "Enter your choice: " + ConsoleColors.RESET);
    }

    /**
     * Handles the user's input selection and delegates the action to the appropriate method.
     * @param option The integer representing the user's menu choice.
     * @throws DatabaseException If a database operation fails during execution.
     */
    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option)
        {
            case 1 -> changePassword();             // Case 1: Password change
            case 2 -> logout();                     // Case 2: Logout
            case 3 -> contactService.listAll(false);// Case 3: List all contacts (Read-only view)
            case 4 -> searchContactsSingle();       // Case 4: Search by one field
            case 5 -> searchContactsMultiple();     // Case 5: Search by multiple criteria
            case 6 -> sortContacts();               // Case 6: Sort contacts list
            case 7 -> undo();                       // Case 7: Revert the last operation (If applicable)
            case 0 -> logout();
            default -> System.out.println(ConsoleColors.RED + "Invalid selection. Please enter a number from the menu." + ConsoleColors.RESET);
        }
    }

    // =========================================================================
    // Methods Inherited from BaseMenuController (Re-implementation/Override)
    // =========================================================================

    /**
     * Case 1: Allows the authenticated user to change their password.
     */
    @Override
    protected void changePassword() throws DatabaseException {
        System.out.println(ConsoleColors.YELLOW + "\n--- CHANGE PASSWORD ---" + ConsoleColors.RESET);
        System.out.print("Enter Current Password: ");
        String currentPassword = scanner.nextLine();
        
        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        if (authService.changePassword(user, currentPassword, newPassword)) {
            System.out.println(ConsoleColors.GREEN + "Password successfully changed." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Error: Current password is incorrect or new password requirements are not met." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 2 and Case 0: Logs the user out and terminates the application.
     */
    @Override
    protected void logout() {
        System.out.println(ConsoleColors.CYAN + "Logging out... Goodbye!" + ConsoleColors.RESET);
        System.exit(0);
    }

    // =========================================================================
    // Implementations Specific to Tester Role (Querying)
    // =========================================================================

    /**
     * Case 4: Searches for contacts based on a single field (First Name or Last Name).
     */
    private void searchContactsSingle() throws DatabaseException {
        System.out.println(ConsoleColors.BLUE + "\n--- SINGLE FIELD SEARCH ---" + ConsoleColors.RESET);
        System.out.println("[1] Search by First Name | [2] Search by Last Name | [3] Search by Email");
        System.out.print("Select search type: ");
        
        try {
            int searchType = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter value to search: ");
            String searchValue = scanner.nextLine().trim();
            
            String fieldName = switch (searchType) {
                case 1 -> "first_name";
                case 2 -> "last_name";
                case 3 -> "email";
                default -> {
                    System.out.println(ConsoleColors.RED + "Invalid search type." + ConsoleColors.RESET);
                    yield null;
                }
            };
            
            if (fieldName != null) {
                // 'true' indicates case-insensitive search
                List<Contact> results = contactService.searchByField(fieldName, searchValue, true); 
                if (results.isEmpty()) {
                    System.out.println(ConsoleColors.PURPLE + "No contacts found matching the criteria." + ConsoleColors.RESET);
                } else {
                    contactService.displayContacts(results);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid input. Please enter a number." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 5: Searches for contacts based on multiple, user-defined fields (flexible criteria).
     */
    private void searchContactsMultiple() throws DatabaseException {
        System.out.println(ConsoleColors.BLUE + "\n--- MULTI-FIELD SEARCH ---" + ConsoleColors.RESET);
        Map<String, String> criteria = new HashMap<>();
        
        System.out.println("Flexible Search: Enter fields and values (Leave field name empty to finish):");
        while (true) {
            System.out.print("Field Name (first_name, last_name, email, etc.): ");
            String field = scanner.nextLine().trim().toLowerCase();
            if (field.isEmpty()) break;
            System.out.print("Value: ");
            String value = scanner.nextLine().trim();
            criteria.put(field, value);
        }

        if (!criteria.isEmpty()) {
            List<Contact> results = contactService.searchByMultipleFields(criteria);
            if (results.isEmpty()) {
                System.out.println(ConsoleColors.PURPLE + "No contacts found matching the criteria." + ConsoleColors.RESET);
            } else {
                contactService.displayContacts(results);
            }
        }
    }

    /**
     * Case 6: Retrieves and displays all contacts sorted by a user-specified field and order.
     */
    private void sortContacts() throws DatabaseException {
        System.out.println(ConsoleColors.BLUE + "\n--- SORT CONTACTS ---" + ConsoleColors.RESET);
        System.out.println("Sortable Fields: first_name, last_name, birth_date, created_at, ...");
        System.out.print("Enter the Field to Sort By: ");
        String field = scanner.nextLine().trim();

        System.out.print("Sort Order (ASC/DESC): ");
        String order = scanner.nextLine().trim().toUpperCase();

        if (!order.equals("ASC") && !order.equals("DESC")) {
            System.out.println(ConsoleColors.RED + "Error: Sort order must be 'ASC' or 'DESC'." + ConsoleColors.RESET);
            return;
        }

        List<Contact> sortedContacts = contactService.sortContacts(field, order);
        contactService.displayContacts(sortedContacts);
    }
    
    /**
     * Case 7: Attempts to undo the last operation. 
     * Since Testers are usually read-only, this might only apply to internal session state 
     * or could be a feature reserved for higher roles.
     */
    private void undo() throws DatabaseException {
        System.out.println(ConsoleColors.YELLOW + "\n--- UNDO LAST OPERATION ---" + ConsoleColors.RESET);
        try {
            // Since this is a Tester role (read-only), the undo functionality is likely limited
            // to undoing filters, sorts, or other session-level actions, or might be disabled.
            // Assuming for completeness that contactService supports this abstraction.
            contactService.undoLastOperation();
            System.out.println(ConsoleColors.GREEN + "Last operation successfully undone." + ConsoleColors.RESET);
        } catch (IllegalStateException e) {
            // Handle case where there is no operation to undo.
            System.out.println(ConsoleColors.PURPLE + "No operation available to undo: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
