package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class InputValidator
{
    // Regex for phone numbers (simple version, supports +country code)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{7,15}$");

    // Regex for emails
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");

    // Regex for letters only (names)
    private static final Pattern LETTERS_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    /**
     * Validate phone number
     * @param phone phone number string
     * @return true if valid
     */
    public boolean isValidPhoneNumber(String phone)
    {
        if (phone == null)
            return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate email
     * @param email email string
     * @return true if valid
     */
    public boolean isValidEmail(String email)
    {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate date in yyyy-MM-dd format
     * @param date string
     * @return true if valid
     */
    public boolean isValidDate(String date) {
        if (date == null) return false;
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Check if string contains only letters (A-Z, a-z)
     * @param name string to check
     * @return true if only letters
     */
    public boolean containsOnlyLetters(String name)
    {
        if (name == null)
            return false;
        return LETTERS_PATTERN.matcher(name).matches();
    }
}
