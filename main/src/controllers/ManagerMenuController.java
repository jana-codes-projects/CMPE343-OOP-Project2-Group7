package controllers;

import exceptions.DatabaseException;
import models.User;
import services.UserService;
import services.StatisticalInfoService;
import auth.AuthService;
import utils.ConsoleColors; // Assuming ConsoleColors is available for styling
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller class for the Manager role.
 * Managers have full administrative access to user accounts (CRUD) and can view application statistics.
 * This class inherits basic functionality from BaseMenuController.
 */
public class ManagerMenuController extends BaseMenuController
{
    // Service layer instances for handling business logic interactions.
    private final UserService userService = new UserService();
    private final StatisticalInfoService statsService = new StatisticalInfoService();
    private final AuthService authService = new AuthService();

    /**
     * Constructor for ManagerMenuController.
     * @param user The authenticated User object (must be a Manager).
     * @param scanner The Scanner object used for console input.
     */
    public ManagerMenuController(User user, Scanner scanner)
    {
        super(user, scanner); // Initialize the base controller.
    }

    /**
     * Displays the menu options specific to the Manager role, including full user management and statistics.
     */
    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColors.PURPLE + "======================================================");
        System.out.println(ConsoleColors.PURPLE + "      MANAGER MENU - WELCOME, " + user.getFirstName() + "!");
        System.out.println(ConsoleColors.PURPLE + "======================================================" + ConsoleColors.RESET);
        
        // Menu options for the Manager role
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] View Contact Statistics");
        System.out.println("[4] List All Users");
        System.out.println("[5] Update User Role/Details");
        System.out.println("[6] Add New User");
        System.out.println("[7] Delete User");
        System.out.println("[8] Undo Last User Operation"); // Assuming only user management operations can be undone here.
        System.out.println("[0] Logout and Exit");
        System.out.print(ConsoleColors.GREEN + "Enter your choice: " + ConsoleColors.RESET);
    }

    /**
     * Handles the user's input selection and delegates the action to the appropriate manager method.
     * @param option The integer representing the user's menu choice.
     * @throws DatabaseException If a database operation fails during execution.
     */
    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();    // Case 1: Password change (Inherited/Implemented in Base)
            case 2 -> logout();            // Case 2: Logout (Inherited/Implemented in Base)
            case 3 -> viewStatistics();    // Case 3: View system-wide statistics
            case 4 -> userService.listUsers(); // Case 4: List all system users
            case 5 -> updateUser();        // Case 5: Modify user details (e.g., role)
            case 6 -> addUser();           // Case 6: Create a new user account
            case 7 -> deleteUser();        // Case 7: Permanently delete a user
            case 8 -> undo();              // Case 8: Undo the last user modification (Add/Update/Delete)
            case 0 -> logout();
            default -> System.out.println(ConsoleColors.RED + "Invalid selection. Please enter a number from the menu." + ConsoleColors.RESET);
        }
    }

    // =========================================================================
    // Methods Inherited from BaseMenuController (Re-implementation/Override)
    // NOTE: These are copied from the JuniorController example for completeness.
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
    // Implementations Specific to Manager Role
    // =========================================================================

    /**
     * Case 3: Calls the StatisticalInfoService to display application-wide contact statistics.
     */
    private void viewStatistics() throws DatabaseException
    {
        System.out.println(ConsoleColors.CYAN + "\n--- CONTACT STATISTICS ---" + ConsoleColors.RESET);
        // Call service method to retrieve and display aggregated data (e.g., total contacts, distribution by field).
        statsService.getStatistics(); 
        System.out.println(ConsoleColors.CYAN + "Statistics displayed successfully." + ConsoleColors.RESET);
    }

    /**
     * Case 5: Allows the manager to update details or role of an existing user.
     */
    private void updateUser() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- UPDATE USER ---" + ConsoleColors.RESET);
        System.out.print("Enter the ID of the user to update: ");
        
        try {
            long userId = Long.parseLong(scanner.nextLine());
            Map<String, String> updatedFields = new HashMap<>();
            
            System.out.println("Enter fields to update (username, role, first_name, etc. - Leave field empty to finish):");

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

            // Call userService to perform the update.
            userService.updateUser(userId, updatedFields);
            System.out.println(ConsoleColors.GREEN + "User (ID: " + userId + ") successfully updated." + ConsoleColors.RESET);

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid User ID format." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 6: Allows the manager to add a new user (with defined credentials and role).
     */
    private void addUser() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- ADD NEW USER ---" + ConsoleColors.RESET);
        
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Enter Role (Manager, SeniorDev, JuniorDev): ");
        String role = scanner.nextLine().trim();
        
        // Note: Password should ideally be set/generated securely by the service layer. 
        // For simplicity, we ask for a temporary password here.
        System.out.print("Enter Temporary Password: ");
        String password = scanner.nextLine(); 

        // Call userService to create the new user record.
        userService.addUser(username, firstName, lastName, role, password);
        System.out.println(ConsoleColors.GREEN + "New User (" + username + ") successfully added with role " + role + "." + ConsoleColors.RESET);
    }

    /**
     * Case 7: Allows the manager to delete a user by ID.
     */
    private void deleteUser() throws DatabaseException
    {
        System.out.println(ConsoleColors.RED + "\n--- DELETE USER ---" + ConsoleColors.RESET);
        System.out.print("Enter the ID of the user to delete: ");
        
        try {
            long userId = Long.parseLong(scanner.nextLine());
            
            // Confirmation step for a destructive operation
            System.out.print(ConsoleColors.RED_BRIGHT + "WARNING: Are you sure you want to delete user ID " + userId + "? (YES/no): " + ConsoleColors.RESET);
            String confirmation = scanner.nextLine().trim();
            
            if (confirmation.equalsIgnoreCase("YES")) {
                // Call userService to delete the user.
                userService.deleteUser(userId);
                System.out.println(ConsoleColors.GREEN + "User (ID: " + userId + ") successfully deleted." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.PURPLE + "Deletion cancelled by user." + ConsoleColors.RESET);
            }

        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid User ID format." + ConsoleColors.RESET);
        }
    }

    /**
     * Case 8: Attempts to undo the last persistent operation performed within the UserService.
     */
    private void undo() throws DatabaseException
    {
        System.out.println(ConsoleColors.YELLOW + "\n--- UNDO LAST USER OPERATION ---" + ConsoleColors.RESET);
        try {
            // Assuming UserService tracks its own state for undoable operations (Add/Update/Delete).
            userService.undoLastOperation();
            System.out.println(ConsoleColors.GREEN + "Last user operation successfully undone." + ConsoleColors.RESET);
        } catch (IllegalStateException e) {
            // Catch if the undo stack is empty.
            System.out.println(ConsoleColors.PURPLE + "No user operation available to undo: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
