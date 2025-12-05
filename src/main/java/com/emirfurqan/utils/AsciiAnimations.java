package com.emirfurqan.utils;


public class AsciiAnimations {

    public static void showStartup() {
        System.out.println(ConsoleColor.CYAN);
        System.out.println("  _____            _             _     ____            _                ");
        System.out.println(" / ____|          | |           | |   |  _ \\          | |               ");
        System.out.println("| |     ___  _ __ | |_ _ __ ___ | |_  | |_) | __ _ ___| |__   ___ _ __  ");
        System.out.println("| |    / _ \\| '_ \\| __| '__/ _ \\| __| |  _  / _` / __| '_ \\ / _ \\ '__| ");
        System.out.println("| |___| (_) | | | | |_| | | (_) | |_  | | \\ \\ (_| \\__ \\ | | |  __/ |   ");
        System.out.println(" \\_____\\___/|_| |_|\\__|_|  \\___/ \\__| |_|  \\_\\__,_|___/_| |_|\\___|_|   ");
        System.out.println("\n         Contact Management System Launching...\n");
        System.out.println(ConsoleColor.RESET);
    }

    public static void showShutdown() {
        System.out.println(ConsoleColor.MAGENTA);
        System.out.println("\n  _____ _                 _       _                _ ");
        System.out.println(" / ____| |               | |     | |              | |");
        System.out.println("| (___ | |__  _   _  __ _| |_ ___| |__   ___   ___| |");
        System.out.println(" \\___ \\| '_ \\| | | |/ _` | __/ __| '_ \\ / _ \\ / __| |");
        System.out.println(" ____) | | | | |_| | (_| | || (__| | | | (_) | (__|_|");
        System.out.println("|_____/|_| |_|\\__,_|\\__,_|\\__\\___|_| |_|\\___/ \\___(_)");
        System.out.println("\n        Shutting Down System Safely...");
        System.out.println(ConsoleColor.RESET);
    }
}
