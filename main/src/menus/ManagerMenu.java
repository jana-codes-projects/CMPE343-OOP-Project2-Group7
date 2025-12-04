package menus;

import auth.AuthService;
import models.User;
import services.StatisticalInfoService;
import services.UndoService;
import services.UserService;
import utils.ConsoleColor;

import java.util.List;
import java.util.Map;

/**
 * Legacy-style manager menu that exposes high-level user and statistics
 * operations without interactive console handling.
 */
public class ManagerMenu
{
    private final User currentUser;
    private final UserService userService = new UserService();
    private final StatisticalInfoService statsService = new StatisticalInfoService();
    private final AuthService authService = new AuthService();
    private final UndoService undoService = new UndoService();

    /**
     * Creates a new menu instance for the specified manager.
     *
     * @param user authenticated manager
     */
    public ManagerMenu(User user)
    {
        this.currentUser = user;
    }

    /**
     * Changes the password of the current manager.
     *
     * @param newPassword new plain‑text password
     * @throws Exception if the update fails
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
                "Manager " + currentUser.getFirstName() + " " + currentUser.getLastName() + " logged out." +
                ConsoleColor.RESET);
    }

    /**
     * Shows basic statistical information about contacts and users.
     */
    public void viewContactStatistics()
    {
        try {
            Map<String, Object> stats = statsService.getStatistics();
            stats.forEach((k, v) -> System.out.printf("%-25s : %s%n", k, v));
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Failed to load statistics: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Lists all users managed by the system.
     */
    public void listAllUsers()
    {
        try {
            List<User> users = userService.listUsers();
            users.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Failed to list users: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Updates a user identified by ID.
     *
     * @param userId identifier of the user to update
     */
    public void updateUser(int userId)
    {
        try {
            List<User> users = userService.listUsers();
            users.stream()
                    .filter(u -> u.getUserId() == userId)
                    .findFirst()
                    .ifPresent(undoService::saveState);
            // A full implementation would modify the properties before updating
            users.stream()
                    .filter(u -> u.getUserId() == userId)
                    .findFirst()
                    .ifPresent(u -> {
                        try {
                            userService.updateUser(u);
                        } catch (Exception e) {
                            System.out.println(ConsoleColor.MAGENTA + "Update failed: " + e.getMessage() + ConsoleColor.RESET);
                        }
                    });
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Update failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Adds/employs a new user.
     *
     * @param user fully-populated user instance to persist
     */
    public void addUser(User user)
    {
        try {
            userService.addUser(user);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Add user failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Deletes/fires an existing user.
     *
     * @param userId ID of the user to delete
     */
    public void deleteUser(int userId)
    {
        try {
            List<User> users = userService.listUsers();
            users.stream()
                    .filter(u -> u.getUserId() == userId)
                    .findFirst()
                    .ifPresent(undoService::saveState);

            userService.deleteUser(userId);
        } catch (Exception e) {
            System.out.println(ConsoleColor.MAGENTA + "Delete user failed: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Requests an undo operation from the {@link UndoService}.
     */
    void undoLast()
    {
        undoService.undoLastOperation();
    }
}
