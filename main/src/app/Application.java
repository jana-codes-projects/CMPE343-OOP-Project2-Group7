package app;

import exceptions.DatabaseException;
import menus.LoginMenu;
import models.User;
import models.Role;
import auth.AuthService;
import controllers.TesterMenuController;
import controllers.JuniorDeveloperMenuController;
import controllers.SeniorDeveloperMenuController;
import controllers.ManagerMenuController;
import utils.ConsoleColor;
import utils.AsciiAnimations;

import java.util.Scanner;

public class Application
{
    // Shared objects
    private final AuthService authService = new AuthService();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public static void main(String[] args) throws DatabaseException
    {
        new Application().startApp();
    }

    // Controls the full application lifecycle
    public void startApp() throws DatabaseException
    {
        AsciiAnimations.showStartup();
        System.out.println(ConsoleColor.CYAN + "Welcome to the Contact Management System!" + ConsoleColor.RESET);

        while (running)
        {
            User user = LoginMenu.showLoginScreen();
            if (user != null)
            {
                routeToRoleMenu(user);
            }
            running = false;
        }

        shutdown();
    }

    // Decides which role-menu controller should run
    private void routeToRoleMenu(User user) throws DatabaseException {
        Role role;
        try
        {
            // Convert the user's role string to enum (spaces replaced with underscores)
            role = Role.valueOf(user.getUserRole().toUpperCase().replace(" ", "_"));
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(ConsoleColor.MAGENTA + "Unknown role. Returning to login..." + ConsoleColor.RESET);
            return;
        }

        switch (role)
        {
            case TESTER -> new TesterMenuController(user, scanner).runMenuLoop();
            case JUNIOR_DEVELOPER -> new JuniorDeveloperMenuController(user, scanner).runMenuLoop();
            case SENIOR_DEVELOPER -> new SeniorDeveloperMenuController(user, scanner).runMenuLoop();
            case MANAGER -> new ManagerMenuController(user, scanner).runMenuLoop();
        }
    }

    // Exits the system cleanly with animation
    private void shutdown() {
        AsciiAnimations.showShutdown();
        System.out.println(ConsoleColor.CYAN + "Thank you for using the system. Goodbye! ☕✨" + ConsoleColor.RESET);
        scanner.close(); // Close scanner
        System.exit(0);
    }
}
