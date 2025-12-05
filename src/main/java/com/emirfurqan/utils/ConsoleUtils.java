package com.emirfurqan.utils;

import com.emirfurqan.models.User;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class ConsoleUtils
{
    void printHeader(String title)
    {
        // TODO print boxed colorful header using ConsoleColor
    }

    void printSuccess(String message)
    {
        // TODO print message using ConsoleColor.BRIGHT_GREEN
    }

    void printError(String message)
    {
        // TODO print message using ConsoleColor.MAGENTA / ConsoleColor.PURPLE
    }

    String prompt(String message)
    {
        // TODO print prompt using CYAN/YELLOW, return scanner input
        return "";
    }

    void printMenuOptions(List<String> options)
    {
        // TODO auto number options with BLUE or BRIGHT_BLUE
    }

    void printStatistics(Map<String, Object> stats)
    {
        // TODO print map entries line-by-line in table-style
    }

    String formatTimestamp(Timestamp ts)
    {
        // TODO convert MySQL Timestamp to readable string
        return "";
    }

    <T> void printTable(List<T> rows)
    {
        // TODO display list rows as aligned console table
    }

    void printLogout(User user)
    {
        // TODO Print logout goodbye message using BRIGHT_WHITE or CYAN
    }

    void printShutdown() {
        // TODO print shutdown message after AsciiAnimations.showShutdown()
    }

    void printUndoConfirmation() {
        // TODO print confirmation that undo succeeded
    }
}
