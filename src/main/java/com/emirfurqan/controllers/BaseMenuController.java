package com.emirfurqan.controllers;

import com.emirfurqan.exceptions.DatabaseException;
import com.emirfurqan.models.User;
import com.emirfurqan.services.UndoService;
import java.util.Scanner;

/**
 * Base class for all role menu controllers
 */
public abstract class BaseMenuController
{
    protected final User currentUser;
    protected final Scanner scanner;
    protected final UndoService undoService = new UndoService();

    public BaseMenuController(User user, Scanner scanner)
    {
        this.currentUser = user;
        this.scanner = scanner;
    }

    /** Display menu options to user */
    public abstract void displayMenu();

    /** Handle menu selection input */
    public abstract void handleUserSelection(int option) throws DatabaseException;

    /** Loop for menu until logout */
    public void runMenuLoop() throws DatabaseException {
        while (true)
        {
            displayMenu();
            System.out.print("Select option: ");
            int opt = Integer.parseInt(scanner.nextLine());
            handleUserSelection(opt);

            if (opt == 0)
                break; // logout command
        }
    }

    /** Change user's password */
    public void changePassword()
    {
        // TODO prompt for password + confirm, verify old hash, call AuthService
    }

    /** Logout from system */
    public void logout()
    {
        // TODO break loop and return to login menu
    }

    /** Undo last change */
    public void undo()
    {
        // TODO call undoService.undoLastOperation() and restore state
    }
}
