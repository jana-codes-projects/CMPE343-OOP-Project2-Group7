package controllers;

import auth.AuthService;
import exceptions.DatabaseException;
import models.User;
import services.UndoService;
import utils.ConsoleColor;

import java.util.Scanner;

import utils.AsciiAnimations;

/**
 * Base class for all role menu controllers.
 * <p>
 * Holds common state such as the currently authenticated {@link User},
 * a shared {@link Scanner} for console input, an {@link UndoService} for
 * undo operations, and an {@link AuthService} for password updates.
 * Subclasses are responsible for rendering their own role-specific menus.
 */
public abstract class BaseMenuController
{
    protected final User currentUser;
    protected final Scanner scanner;
    protected final UndoService undoService = new UndoService();
    protected final AuthService authService = new AuthService();

    /**
     * Flag indicating whether the current menu loop should exit
     * and control should be returned to the login screen.
     */
    private boolean exitRequested = false;

    /**
     * Creates a new base controller instance.
     *
     * @param user    the authenticated user whose role menu will be shown
     * @param scanner shared scanner for console input
     */
    public BaseMenuController(User user, Scanner scanner)
    {
        this.currentUser = user;
        this.scanner = scanner;
    }

    /**
     * Displays role‑specific menu options to the current user.
     */
    public abstract void displayMenu();

    /**
     * Handles a menu option selected by the user.
     *
     * @param option the numeric option chosen by the user
     * @throws DatabaseException if any underlying database call fails
     */
    public abstract void handleUserSelection(int option) throws DatabaseException;

    /**
     * Runs the interactive menu loop for the current user until logout is requested.
     * <p>
     * This method never terminates the application; it only returns control
     * back to the login screen when the user logs out. It also guards against
     * invalid numeric input to prevent crashes.
     *
     * @throws DatabaseException if any handled menu action triggers a database error
     */
    public void runMenuLoop() throws DatabaseException {
        exitRequested = false;

        while (!exitRequested)
        {
            AsciiAnimations.clearScreen();
            displayMenu();
            System.out.print("Select option: ");

            int opt;
            try
            {
                opt = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println(ConsoleColor.MAGENTA + "Invalid input. Please enter a number." + ConsoleColor.RESET);
                continue;
            }

            handleUserSelection(opt);

            if (opt == 0)
            {
                // Conventional "0 = logout" option
                exitRequested = true;
            }
        }
    }

    /**
     * Allows the current user to change their password.
     * <p>
     * The user is prompted for a new password and confirmation. If the values
     * do not match or are empty, a warning is printed and the password is not changed.
     * The new password is always stored as a hash in the database.
     */
    public void changePassword()
    {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (newPassword == null || newPassword.isBlank())
        {
            System.out.println(ConsoleColor.MAGENTA + "Password cannot be empty." + ConsoleColor.RESET);
            return;
        }

        if (!newPassword.equals(confirmPassword))
        {
            System.out.println(ConsoleColor.MAGENTA + "Passwords do not match. Please try again." + ConsoleColor.RESET);
            return;
        }

        try
        {
            authService.changePassword(currentUser.getUserId(), newPassword);
            System.out.println(ConsoleColor.BRIGHT_GREEN + "Password updated successfully." + ConsoleColor.RESET);
        }
        catch (Exception e)
        {
            System.out.println(ConsoleColor.MAGENTA + "Failed to change password: " + e.getMessage() + ConsoleColor.RESET);
        }
    }

    /**
     * Requests logout from the current role menu.
     * <p>
     * This method does not exit the JVM; it only sets an internal flag
     * so that {@link #runMenuLoop()} returns and application control
     * goes back to the login screen in {@link app.Application}.
     */
    public void logout()
    {
        System.out.println(ConsoleColor.BRIGHT_CYAN + "Logging out " +
                currentUser.getFirstName() + " " + currentUser.getLastName() + "..." +
                ConsoleColor.RESET);
        exitRequested = true;
    }

    /**
     * Invokes the {@link UndoService} to restore the last saved state.
     * <p>
     * Concrete controllers are responsible for pushing meaningful state
     * objects into the undo stack before performing destructive operations
     * (add, update, delete). This method only pops and reports the last state.
     */
    public void undo()
    {
        if (!undoService.canUndo())
        {
            System.out.println(ConsoleColor.MAGENTA + "Nothing to undo." + ConsoleColor.RESET);
            return;
        }

        Object previousState = undoService.undoLastOperation();
        System.out.println(ConsoleColor.BRIGHT_YELLOW +
                "Undo requested. Previous state restored in memory: " + previousState +
                ConsoleColor.RESET);
    }
}
