package controllers;

import exceptions.DatabaseException;
import models.Contact;
import models.User;
import services.ContactService;
import auth.AuthService;
import utils.ConsoleColors; // Assuming ConsoleColors is available for styling
import utils.InputValidator; // Assumed to be available for validation
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller class for the Senior Developer role.
 * This role typically has full CRUD access (Create, Read, Update, Delete) on contacts, 
 * plus search, sort, and undo capabilities.
 * It combines the read/write features of the Junior Dev with the addition of Add and Delete operations.
 */
public class SeniorDeveloperMenuController extends BaseMenuController
{
    // Service layer instances for handling business logic interactions.
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    /**
     * Constructor for SeniorDeveloperMenuController.
     * @param user The authenticated User object.
     * @param scanner The Scanner object used for console input.
     */
    public SeniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner); // Initialize the base controller.
    }

    /**
     * Displays the menu options specific to the Senior Developer role.
     * This menu includes full Contact CRUD capabilities.
     */
    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColors.YELLOW + "======================================================");
        System.out.println(ConsoleColors.YELLOW + "  SENIOR DEVELOPER MENU - WELCOME, " + user.getFirstName() + "!");
        System.out.println(ConsoleColors.YELLOW + "======================================================" + ConsoleColors.RESET);
        
        // Menu options
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Add Contact (Create)"); // Full CRUD permission granted
        System.out.println("[9] Delete Contact (Delete)"); // Full CRUD permission granted
        System.out.println("[10] Undo Last Operation");
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
        switch (option) {
            case 1 -> changePassword();            // Case 1: Password change
            case 2 -> logout();                    // Case 2: Logout
            case 3 -> contactService.listAll(true);// Case 3: List all contacts
            case 4 -> searchContactsSingle();      // Case 4: Search by one field
            case 5 -> searchContactsMultiple();    // Case 5: Search by multiple criteria
            case 6 -> sortContacts();              // Case 6: Sort contacts list
            case 7 -> updateContact();             // Case 7: Update an existing contact
            case 8 -> addContact();                // Case 8: Create a new contact
            case 9 -> deleteContact();             // Case 9: Delete a contact
            case 10 -> undo();                     // Case 10: Revert the last operation
            case 0 -> logout();
            default -> System.out.println(ConsoleColors.RED + "Invalid selection. Please enter a number from the menu." + ConsoleColors.RESET);
        }
    }

    // =========================================================================
    // Methods Inherited from BaseMenuController (Re-implementation/Override)
    // =========================================================================

    /**
     * Case 1: Allows the authenticated user to change their password. (Detailed implementation assumed in Base/AuthService)
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
    // Implementations Shared with Junior Developer Role (Search, Sort, Update)
    // =========================================================================

    /**
     * Case 4: Searches for contacts based on a single field (First Name or Last Name).
     */
    private void searchContactsSingle() throws DatabaseException
    {
        System.out.println(ConsoleColors.BLUE + "\n--- SINGLE FIELD SEARCH ---" + ConsoleColors.RESET);
        System.out.println("[1] Search by First Name | [2] Search by Last Name");
        System.out.print("Select search type: ");
        
        try {
            int searchType = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter value to search: ");
            String searchValue = scanner.nextLine().trim();
            String fieldName = switch (searchType) {
                case 1 -> "first_name";
                case 2 -> "last_name";
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
    private void searchContactsMultiple() throws DatabaseException
    {
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
    private void sortContacts() throws DatabaseException
    {
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
     * Case 7: Updates one or more fields of an existing contact based on its ID.
     */
    private void updateContact() throws DatabaseException
    {
        System.out.println(ConsoleColors.BLUE + "\n--- UPDATE CONTACT ---" + ConsoleColors.RESET);
        System.out.print("Enter the ID of the contact to update: ");
        
        try {
            long contactId = Long.parseLong(scanner.nextLine());
            
            Map<String, String> updatedFields = new HashMap<>();
            System.out.println("Enter the fields to update (Leave field name empty to finish).");

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

            contactService.updateContact(contactId, updatedFields);
            System.out.println(ConsoleColors.GREEN + "Contact (ID: " + contactId + ") successfully updated." + ConsoleColors.RESET);

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid ID format." + ConsoleColors.RESET);
        }
    }

    // =========================================================================
    // Implementations Specific to Senior Developer Role (Create, Delete, Undo)
    // =========================================================================

    /**
     * Case 8: Allows the creation of a new contact record. (CREATE operation)
     */
    private void addContact() throws DatabaseException
    {
        System.out.println(ConsoleColors.GREEN + "\n--- ADD NEW CONTACT ---" + ConsoleColors.RESET);
        
        // Collect minimum required contact information
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        // Call the service method to persist the new contact.
        contactService.addContact(firstName, lastName, email, phone); 
        System.out.println(ConsoleColors.GREEN + "New contact successfully added: " + firstName + " " + lastName + ConsoleColors.RESET);
    }

    /**
     * Case 9: Allows the deletion of an existing contact record by ID. (DELETE operation)
     */
    private void deleteContact() throws DatabaseException
    {
        System.out.println(ConsoleColors.RED + "\n--- DELETE CONTACT ---" + ConsoleColors.RESET);
        System.out.print("Enter the ID of the contact to delete: ");
        
        try {
            long contactId = Long.parseLong(scanner.nextLine());
            
            // Confirmation for destructive action
            System.out.print(ConsoleColors.RED_BRIGHT + "WARNING: Are you sure you want to delete contact ID " + contactId + "? (YES/no): " + ConsoleColors.RESET);
            String confirmation = scanner.nextLine().trim();
            
            if (confirmation.equalsIgnoreCase("YES")) {
                // Call the service method to delete the contact.
                contactService.deleteContact(contactId);
                System.out.println(ConsoleColors.GREEN + "Contact (ID: " + contactId + ") successfully deleted." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.PURPLE + "Deletion cancelled by user." + ConsoleColors.RESET);
            }

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid ID format." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 10: Reverts the last persistent operation performed (Add, Update, or Delete).
     */
    private void undo() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- UNDO LAST OPERATION ---" + ConsoleColors.RESET);
        try {
            // Attempt to undo the last operation recorded in the service history.
            contactService.undoLastOperation();
            System.out.println(ConsoleColors.GREEN + "Last operation successfully undone." + ConsoleColors.RESET);
        } catch (IllegalStateException e) {
            // Handle case where there is no operation to undo.
            System.out.println(ConsoleColors.PURPLE + "No operation available to undo: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
