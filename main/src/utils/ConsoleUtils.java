package utils;

import models.User;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Utility methods for consistent, colorful console output and basic input prompts.
 * <p>
 * This class centralises formatting so that menus look similar for all roles and
 * messages guide the user to provide valid input.
 */
public class ConsoleUtils
{
    private static final DateTimeFormatter TS_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints a colored header surrounded by a simple box.
     *
     * @param title header text to display
     */
    public void printHeader(String title)
    {
        String line = "=".repeat(Math.max(10, title.length() + 8));
        System.out.println(ConsoleColor.BRIGHT_BLUE + line + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BRIGHT_CYAN + "   " + title + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BRIGHT_BLUE + line + ConsoleColor.RESET);
    }

    /**
     * Prints a success message in green.
     *
     * @param message message to display
     */
    public void printSuccess(String message)
    {
        System.out.println(ConsoleColor.BRIGHT_GREEN + message + ConsoleColor.RESET);
    }

    /**
     * Prints an error message in magenta/purple.
     *
     * @param message message to display
     */
    public void printError(String message)
    {
        System.out.println(ConsoleColor.MAGENTA + message + ConsoleColor.RESET);
    }

    /**
     * Prompts the user for input and returns the typed line.
     * <p>
     * This helper prevents repeated {@link Scanner} creation throughout the codebase.
     *
     * @param message description of the expected input and format
     * @return user input line (never {@code null}, may be empty)
     */
    public String prompt(String message)
    {
        System.out.print(ConsoleColor.CYAN + message + ConsoleColor.RESET + " ");
        return scanner.nextLine();
    }

    /**
     * Prints a numbered list of menu options.
     *
     * @param options options to display in order
     */
    public void printMenuOptions(List<String> options)
    {
        for (int i = 0; i < options.size(); i++)
        {
            System.out.println(ConsoleColor.BRIGHT_BLUE + "[" + (i + 1) + "] " +
                    options.get(i) + ConsoleColor.RESET);
        }
    }

    /**
     * Prints a simple key/value table for statistics or debug output.
     *
     * @param stats map of statistic name to value
     */
    public void printStatistics(Map<String, Object> stats)
    {
        System.out.println(ConsoleColor.BRIGHT_CYAN + "=== STATISTICS ===" + ConsoleColor.RESET);
        for (Map.Entry<String, Object> entry : stats.entrySet())
        {
            System.out.printf("%-25s : %s%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Formats a SQL {@link Timestamp} into a readable string.
     *
     * @param ts timestamp to format, may be {@code null}
     * @return formatted string or {@code "-"} for {@code null}
     */
    public String formatTimestamp(Timestamp ts)
    {
        if (ts == null)
        {
            return "-";
        }
        return ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(TS_FORMATTER);
    }

    /**
     * Prints the {@link #toString()} result of each row in a simple table-like layout.
     *
     * @param rows list of objects to print
     * @param <T>  type of row
     */
    public <T> void printTable(List<T> rows)
    {
        if (rows == null || rows.isEmpty())
        {
            System.out.println(ConsoleColor.BRIGHT_YELLOW + "No data to display." + ConsoleColor.RESET);
            return;
        }

        System.out.println(ConsoleColor.BRIGHT_CYAN + "=== RESULTS ===" + ConsoleColor.RESET);
        for (T row : rows)
        {
            System.out.println(row);
        }
    }

    /**
     * Prints a logout farewell message for a user.
     *
     * @param user the user that is logging out
     */
    public void printLogout(User user)
    {
        System.out.println(ConsoleColor.BRIGHT_WHITE +
                "Goodbye, " + user.getFirstName() + " " + user.getLastName() + "!" +
                ConsoleColor.RESET);
    }

    /**
     * Prints an additional shutdown message after the ASCII animation.
     */
    public void printShutdown() {
        System.out.println(ConsoleColor.CYAN +
                "System has been shut down safely. Press Enter to close the console if needed." +
                ConsoleColor.RESET);
    }

    /**
     * Prints a confirmation that an undo operation was performed.
     */
    public void printUndoConfirmation() {
        System.out.println(ConsoleColor.BRIGHT_GREEN +
                "Last operation has been undone (where supported)." +
                ConsoleColor.RESET);
    }
}