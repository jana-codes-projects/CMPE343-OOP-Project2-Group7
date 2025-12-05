package com.emirfurqan.controllers;

import com.emirfurqan.exceptions.DatabaseException;
import com.emirfurqan.models.User;
import com.emirfurqan.services.UserService;
import com.emirfurqan.services.StatisticalInfoService;
import com.emirfurqan.auth.AuthService;
import java.util.Scanner;

public class ManagerMenuController extends BaseMenuController
{
    private final UserService userService = new UserService();
    private final StatisticalInfoService statsService = new StatisticalInfoService();
    private final AuthService authService = new AuthService();

    public ManagerMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println("===== MANAGER MENU =====");
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
            case 2 -> logout();
            case 3 -> viewStatistics();
            case 4 -> userService.listUsers();
            case 5 -> updateUser();
            case 6 -> addUser();
            case 7 -> deleteUser();
            case 8 -> undo();
            case 0 -> logout();
        }
    }

    private void viewStatistics()
    {
        // TODO call statsService.getStatistics() and print results
    }

    private void updateUser()
    {
        // TODO ask for userId + new values → call userService.updateUser(...)
    }

    private void addUser()
    {
        // TODO prompt new user fields → call userService.addUser(...)
    }

    private void deleteUser()
    {
        // TODO ask userId → saveState before delete → call userService.deleteUser(...)
    }
}
