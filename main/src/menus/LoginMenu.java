package menus;

import app.Application;
import auth.AuthService;
import models.User;
import utils.ConsoleColor;

import java.util.Scanner;

/**
 * Simple login menu abstraction.
 * <p>
 * The main application currently performs login itself, but this class
 * documents and encapsulates the same behavior for clarity and potential reuse.
 */
public class LoginMenu
{
    private static final AuthService authService = new AuthService();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the login screen and attempts to authenticate the user.
     * The method keeps prompting until a successful login or an explicit exit.
     *
     * @return the authenticated {@link User}, or {@code null} if the user chose to exit
     */
    public static User showLoginScreen()
    {
        while (true)
        {
            System.out.println("\n" + ConsoleColor.BRIGHT_YELLOW + "===== LOGIN =====" + ConsoleColor.RESET);
            System.out.print("Username (or type exit): ");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("exit"))
            {
                return null;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            try
            {
                User user = authService.login(username, password);
                System.out.println(ConsoleColor.BRIGHT_GREEN + "Login successful! Hello " +
                        user.getFirstName() + ConsoleColor.RESET);
                return user;
            }
            catch (Exception e)
            {
                System.out.println(ConsoleColor.MAGENTA + "Login failed: " + e.getMessage() + ConsoleColor.RESET);
                System.out.println(ConsoleColor.BLUE + "Try again or type 'exit' to quit." + ConsoleColor.RESET);
            }
        }
    }
}
