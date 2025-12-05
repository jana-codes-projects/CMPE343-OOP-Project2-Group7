package controllers;

import exceptions.DatabaseException;
import models.User;
import services.StatisticalInfoService;
import services.UserService;
import utils.ConsoleColor;
import utils.InputValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu controller for users with the {@code MANAGER} role.
 * <p>
 * Managers can change their password, inspect contact statistics, and
 * perform full CRUD operations on users (list, update, add, delete).
 */
public class ManagerMenuController extends BaseMenuController
{
    private final UserService userService = new UserService();
    private final StatisticalInfoService statsService = new StatisticalInfoService();
    private final InputValidator validator = new InputValidator();

    /**
     * Creates a new controller for a manager.
     *
     * @param user    the authenticated manager
     * @param scanner shared scanner for console input
     */
    public ManagerMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println(ConsoleColor.BRIGHT_CYAN + "===== MANAGER MENU - " +
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                " =====" + ConsoleColor.RESET);
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] View Contact Statistics");
        System.out.println("[4] List All Users");
        System.out.println("[5] Update User");
        System.out.println("[6] Add New User");
        System.out.println("[7] Delete User");
        System.out.println("[8] Undo Last Operation");
        System.out.println("[0] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();
            case 2, 0 -> logout();
            case 3 -> viewStatistics();
            case 4 -> listAllUsers();
            case 5 -> updateUser();
            case 6 -> addUser();
            case 7 -> deleteUser();
            case 8 -> undo();
            default -> System.out.println(ConsoleColor.MAGENTA + "Unknown option. Please try again." + ConsoleColor.RESET);
        }
    }

    /**
     * Displays aggregated statistics from both user and contact data.
     *
     * @throws DatabaseException if the statistics cannot be calculated
     */
    private void viewStatistics() throws DatabaseException
    {
        Map<String, Object> stats = statsService.getStatistics();
        System.out.println(ConsoleColor.BRIGHT_CYAN + "=== CONTACT & USER STATISTICS ===" + ConsoleColor.RESET);
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            System.out.printf("%-25s : %s%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Lists all users in the system.
     *
     * @throws DatabaseException if users cannot be loaded
     */
    private void listAllUsers() throws DatabaseException
    {
        List<User> users = userService.listUsers();
        if (users.isEmpty()) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No users found." + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_BLUE + "All users:" + ConsoleColor.RESET);
        for (User u : users) {
            System.out.println(u);
        }
    }

    /**
     * Updates properties of an existing user.
     *
     * @throws DatabaseException if the update fails
     */
    private void updateUser() throws DatabaseException
    {
        System.out.print("Enter user ID to update: ");
        String idInput = scanner.nextLine();
        int userId;
        try {
            userId = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid ID format." + ConsoleColor.RESET);
            return;
        }

        List<User> users = userService.listUsers();
        User target = users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (target == null) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No user found with ID " + userId + "." + ConsoleColor.RESET);
            return;
        }

        undoService.saveState(target);

        System.out.println("Leave a field empty to keep the current value.");
        System.out.print("Username (" + target.getUsername() + "): ");
        String username = scanner.nextLine();
        if (!username.isBlank()) {
            target.setUsername(username);
        }

        System.out.print("First name (" + target.getFirstName() + ") [max 50 chars]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isBlank()) {
            firstName = validator.formatName(firstName);
            String firstNameError = validator.getNameErrorMessage(firstName, "First name");
            if (firstNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + firstNameError + ConsoleColor.RESET);
                return;
            }
            try {
                target.setFirstName(firstName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        System.out.print("Last name (" + target.getLastName() + ") [max 50 chars]: ");
        String lastName = scanner.nextLine();
        if (!lastName.isBlank()) {
            lastName = validator.formatName(lastName);
            String lastNameError = validator.getNameErrorMessage(lastName, "Last name");
            if (lastNameError != null) {
                System.out.println(ConsoleColor.MAGENTA + lastNameError + ConsoleColor.RESET);
                return;
            }
            try {
                target.setLastName(lastName);
            } catch (IllegalArgumentException e) {
                System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
                return;
            }
        }

        System.out.print("Role (" + target.getUserRole() + "): ");
        String role = scanner.nextLine();
        if (!role.isBlank()) {
            target.setUserRole(role);
        }

        userService.updateUser(target);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "User updated successfully." + ConsoleColor.RESET);
    }

    /**
     * Adds/employs a new user with a chosen role.
     *
     * @throws DatabaseException if the user cannot be persisted
     */
    private void addUser() throws DatabaseException
    {
        User user = new User();

        System.out.print("Username (no spaces): ");
        String username = scanner.nextLine();
        if (username.isBlank()) {
            System.out.println(ConsoleColor.MAGENTA + "Username cannot be empty." + ConsoleColor.RESET);
            return;
        }
        user.setUsername(username);

        System.out.print("First name [letters only, max 50 chars]: ");
        String firstName = scanner.nextLine();
        firstName = validator.formatName(firstName);
        String firstNameError = validator.getNameErrorMessage(firstName, "First name");
        if (firstNameError != null) {
            System.out.println(ConsoleColor.MAGENTA + firstNameError + ConsoleColor.RESET);
            return;
        }
        try {
            user.setFirstName(firstName);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
            return;
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
            user.setLastName(lastName);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColor.MAGENTA + e.getMessage() + ConsoleColor.RESET);
            return;
        }

        System.out.print("Role (Tester, Junior Developer, Senior Developer, Manager): ");
        String role = scanner.nextLine();
        if (role.isBlank()) {
            System.out.println(ConsoleColor.MAGENTA + "Role cannot be empty." + ConsoleColor.RESET);
            return;
        }
        user.setUserRole(role);

        user.setCreatedAt(LocalDateTime.now());

        userService.addUser(user);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "User added successfully." + ConsoleColor.RESET);
    }

    /**
     * Deletes/fires an existing user by ID.
     *
     * @throws DatabaseException if the delete fails
     */
    private void deleteUser() throws DatabaseException
    {
        System.out.print("Enter user ID to delete: ");
        String idInput = scanner.nextLine();
        int userId;
        try {
            userId = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColor.MAGENTA + "Invalid ID format." + ConsoleColor.RESET);
            return;
        }

        List<User> users = userService.listUsers();
        User target = users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);

        if (target == null) {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No user found with ID " + userId + "." + ConsoleColor.RESET);
            return;
        }

        undoService.saveState(target);
        userService.deleteUser(userId);
        System.out.println(ConsoleColor.BRIGHT_GREEN + "User deleted successfully." + ConsoleColor.RESET);
    }
}
