package com.emirfurqan.app;

import com.emirfurqan.exceptions.DatabaseException;
import com.emirfurqan.models.User;
import com.emirfurqan.models.Role;
import com.emirfurqan.auth.AuthService;
import com.emirfurqan.controllers.TesterMenuController;
import com.emirfurqan.controllers.JuniorDeveloperMenuController;
import com.emirfurqan.controllers.SeniorDeveloperMenuController;
import com.emirfurqan.controllers.ManagerMenuController;
import com.emirfurqan.utils.ConsoleColor;
import com.emirfurqan.utils.AsciiAnimations;

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
            User user = showLoginScreen();
            if (user != null) {
                routeToRoleMenu(user);
            }
        }

        shutdown();
    }

    // Handles login + retry if credentials are wrong
    private User showLoginScreen()
    {
        while (true)
        {
            System.out.println("\n" + ConsoleColor.BRIGHT_YELLOW + "===== LOGIN =====" + ConsoleColor.RESET);
            System.out.print("Username (or type exit): ");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("exit"))
            {
                running = false;
                return null;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            try
            {
                User user = authService.login(username, password);
                System.out.println(ConsoleColor.BRIGHT_GREEN + "Login successful! Hello " + user.getFirstName() + " 👋" + "\n" + ConsoleColor.RESET);
                return user;
            }
            catch (Exception e)
            {
                System.out.println(ConsoleColor.MAGENTA + "Login failed: " + e.getMessage() + ConsoleColor.RESET);
                System.out.println(ConsoleColor.BLUE + "Try again or type 'exit' to quit." + ConsoleColor.RESET);
            }
        }
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
