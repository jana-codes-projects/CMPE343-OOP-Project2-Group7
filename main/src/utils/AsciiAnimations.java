package utils;

/**
 * Utility class responsible for colorful ASCII-art animations shown at
 * application startup and shutdown.
 */
public class AsciiAnimations {

    /**
     * Clears the console screen in most ANSI-capable terminals.
     */
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 50; i++)
            System.out.println();
    }

    /**
     * Displays a multi-frame startup animation with a short loading effect.
     */
    public static void showStartup() {
        clearScreen();
        System.out.println(ConsoleColor.CYAN);
        System.out.println("  _____            _             _   ____              _             ");
        System.out.println(" / ____|          | |           | | |  _ \\            | |            ");
        System.out.println("| |     ___  _ __ | |_ _ __ ___ | | | |_) |  ___  ____| |_  ___ _ __ ");
        System.out.println("| |    / _ \\| '_ \\| __| '__/ _ \\| | |  _  / / _ \\(   _| __|/ _ \\ '__|");
        System.out.println("| |___| (_) | | | | |_| | | (_) | | | | \\ \\| (_) |_\\ \\| |_|  __/ |   ");
        System.out.println(" \\_____\\___/|_| |_|\\__|_|  \\___/|_| |_|  \\__\\___/|____)\\__|\\___|_|   ");
        System.out.println("\n         Contact Management System Launching...\n");

        // Simple loading bar animation
        String bar = "[==========]";
        for (int i = 1; i <= bar.length() - 2; i++) {
            String current = "[" + "=".repeat(i) + " ".repeat((bar.length() - 2) - i) + "]";
            System.out.print(ConsoleColor.BRIGHT_CYAN + "\rLoading " + current + ConsoleColor.RESET);
            try {
                Thread.sleep(120);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
        System.out.println(ConsoleColor.RESET);
    }

    /**
     * Displays a shutdown banner with a small countdown effect.
     */
    public static void showShutdown() {
        clearScreen();
        System.out.println(ConsoleColor.MAGENTA);
        System.out.println("\n  _____ _           _        _                             _ ");
        System.out.println(" / ____| |         | |      | |                           | |");
        System.out.println("| (___ | |__  _   _| |_  ___| | ___  _    ___    __ _ __  | |");
        System.out.println(" \\___ \\| '_ \\| | | | __|/ __  |/ _ \\\\ \\  / _ \\  / /| '_ \\ | |");
        System.out.println(" ____) | | | | |_| | |_| (__| | (_) |\\ \\/ / \\ \\/ / | | | ||_|");
        System.out.println("|_____/|_| |_|\\__,_|\\__/\\_____|\\___/  \\__/   \\__/  |_| |_|(_)");
        System.out.println("\n        Shutting Down System Safely...\n");

        for (int i = 3; i >= 1; i--) {
            System.out.print(ConsoleColor.BRIGHT_PURPLE + "\rExiting in " + i + "..." + ConsoleColor.RESET);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
        System.out.println(ConsoleColor.RESET);
    }
}
