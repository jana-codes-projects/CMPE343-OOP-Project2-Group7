package controllers;

import exceptions.DatabaseException;
import models.User;
import services.UndoService;
import java.util.Scanner;

/**
 * Base class for all role menu controllers
 */
public abstract class BaseMenuController
{
    protected final User currentUser;
    protected final Scanner scanner;
    protected final UndoService undoService = new UndoService();

    /**
     * Creates a new menu controller for the given user.
     *
     * @param user    the currently logged-in user
     * @param scanner the shared scanner instance for console input
     */
    public BaseMenuController(User user, Scanner scanner)
    {
        this.currentUser = user;
        this.scanner = scanner;
    }

    /** Display menu options to user */
    public abstract void displayMenu();

    /**
     * Handles the given non-zero menu option.
     *
     * @param option the numeric option selected by the user (never 0)
     * @throws DatabaseException if a database error occurs
     *                           while processing the option
     */
    public abstract void handleUserSelection(int option) throws DatabaseException;

    /**
     * Runs the main menu loop until the user selects the logout option (0).
     *
     * @throws DatabaseException if a database error occurs while
     *                           handling a menu selection
     */
    public void runMenuLoop() throws DatabaseException {
        boolean running = true;

        while (running)
        {
            displayMenu();
            int opt = readIntOption("Select option: ");

            if (opt == 0) {
                // logout command
                logout();
                running = false;
            } else {
                handleUserSelection(opt);
            }
        }
    }

    /**
     * Reads an integer menu option from the user.
     * <p>
     * If the user enters a non-numeric value, this method prints
     * an error message and keeps asking until a valid integer is entered.
     *
     * @param prompt the message shown before reading input
     * @return the parsed integer option
     */
    protected int readIntOption(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();

            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a NUMBER.");
            }
        }
    }

    /** Change user's password */
    public void changePassword()
    {
        System.out.println("=== Change Password ===");

        System.out.print("Enter CURRENT password: ");
        String currentPassword = scanner.nextLine();

        System.out.print("Enter NEW password: ");
        String newPassword1 = scanner.nextLine();

        System.out.print("Confirm NEW password: ");
        String newPassword2 = scanner.nextLine();

        if (newPassword1 == null || newPassword1.isBlank()) {
            System.out.println("New password cannot be empty.");
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            System.out.println("New passwords DO NOT match.");
            return;
        }

        try {
            // TODO: Call your AuthService to actually change the password.
            System.out.println("Password changed successfully (TODO: implement AuthService call).");
        } catch (Exception e) {
            System.out.println("Could not change password: " + e.getMessage());
        }

        System.out.println("Press ENTER to continue...");
        scanner.nextLine();
    }

    /** Logout from system */
    public void logout()
    {
        System.out.println("Logging out user: " + currentUser.getUsername());
        System.out.println("Returning to login screen...");
        System.out.println("Press ENTER to continue...");
        scanner.nextLine();
    }

    /** Undo last change */
    public void undo()
    {
        System.out.println("=== Undo Last Operation ===");
        try {
            // UndoService returns the previous state as an Object,
            // or null if there is nothing to undo.
            Object previousState = undoService.undoLastOperation();

            if (previousState != null) {
                System.out.println("Last operation has been undone successfully.");
            } else {
                System.out.println("There is no operation to undo.");
            }
        } catch (Exception e) {
            System.out.println("Undo failed: " + e.getMessage());
        }

        System.out.println("Press ENTER to continue...");
        scanner.nextLine();
    }
}
