package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class InputValidator
{
    // Regex for phone numbers (E.164-like, must start with '+' and digits)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{8,15}$");

    // Regex for emails
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");

    // Regex for letters only (names), supports Unicode (e.g. Turkish characters)
    private static final Pattern LETTERS_PATTERN = Pattern.compile("^[\\p{L}]+$");

    /**
     * Validate phone number. Expected format:
     * optional '+' followed by 7-15 digits (e.g. +905551112233 or 05551112233).
     *
     * @param phone phone number string
     * @return true if valid
     */
    public boolean isValidPhoneNumber(String phone)
    {
        if (phone == null)
            return false;

        if (!PHONE_PATTERN.matcher(phone).matches())
            return false;

        // Enforce allowed country codes and typical national number lengths
        // Codes: +1, +7, +20, +44, +55, +81, +86, +90, +91, +234
        String[] codes = {"+234", "+90", "+91", "+86", "+81", "+55", "+44", "+20", "+7", "+1"};
        int[] lengths = {10, 10, 10, 11, 10, 11, 10, 10, 10, 10}; // digits after country code

        for (int i = 0; i < codes.length; i++)
        {
            String code = codes[i];
            if (phone.startsWith(code))
            {
                String rest = phone.substring(code.length());
                // Rest must be all digits and have exact expected length
                return rest.matches("\\d{" + lengths[i] + "}");
            }
        }

        // Country code not in allowed list
        return false;
    }

    /**
     * Validate email address in a simple user@domain.tld form.
     *
     * @param email email string
     * @return true if valid
     */
    public boolean isValidEmail(String email)
    {
        if (email == null) return false;
        if (!EMAIL_PATTERN.matcher(email).matches()) return false;

        // Restrict to allowed domains only
        String[] allowedDomains = {
                "@gmail.com",
                "@outlook.com",
                "@hotmail.com",
                "@yahoo.com",
                "@protonmail.com"
        };
        for (String domain : allowedDomains)
        {
            if (email.toLowerCase().endsWith(domain))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate date in yyyy-MM-dd format (e.g. 2005-09-12).
     *
     * @param date string
     * @return true if valid and represents a real calendar date
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
     * Check if string contains only letters (supports Unicode letters, e.g. Turkish).
     *
     * @param name string to check
     * @return true if only letters
     */
    public boolean containsOnlyLetters(String name)
    {
        if (name == null)
            return false;
        return LETTERS_PATTERN.matcher(name).matches();
    }

    /**
     * Validate name field length (max 50 characters).
     *
     * @param name the name to validate
     * @return true if valid (not null, not empty, <= 50 chars)
     */
    public boolean isValidNameLength(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() <= 50;
    }

    /**
     * Get detailed error message for invalid name.
     *
     * @param name the name to validate
     * @param fieldName the field name (e.g., "First name", "Last name")
     * @return error message, or null if valid
     */
    public String getNameErrorMessage(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return fieldName + " cannot be empty";
        }
        if (name.length() > 50) {
            return fieldName + " must be at most 50 characters (current: " + name.length() + ")";
        }
        if (!containsOnlyLetters(name)) {
            return fieldName + " must contain only letters";
        }
        return null; // Valid
    }

    /**
     * Capitalize first letter and lowercase the rest.
     * Handles multi-word names separated by spaces or hyphens.
     *
     * @param name the name to format
     * @return formatted name, or null if input is null
     */
    public String formatName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return name;
        }

        name = name.trim();
        StringBuilder formatted = new StringBuilder();
        boolean capitalizeNext = true;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (Character.isWhitespace(c) || c == '-') {
                formatted.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                formatted.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                formatted.append(Character.toLowerCase(c));
            }
        }

        return formatted.toString();
    }

    /**
     * Validate birthdate - must be in the past and within reasonable range.
     * - Cannot be in the future or today
     * - Must be within 150 years from today
     *
     * @param date string in yyyy-MM-dd format
     * @return true if valid birthdate
     */
    public boolean isValidBirthdate(String date) {
        if (date == null) return false;
        try {
            LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();

            // Cannot be in the future
            if (birthDate.isAfter(today)) {
                return false;
            }

            // Cannot be today
            if (birthDate.isEqual(today)) {
                return false;
            }

            // Must be within reasonable range (max 150 years old)
            LocalDate minDate = today.minusYears(150);
            if (birthDate.isBefore(minDate)) {
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Get detailed error message for invalid birthdate.
     *
     * @param date string in yyyy-MM-dd format
     * @return error message, or null if valid
     */
    public String getBirthdateErrorMessage(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "Birthdate cannot be empty";
        }

        try {
            LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();

            if (birthDate.isAfter(today)) {
                return "Birthdate cannot be in the future";
            }

            if (birthDate.isEqual(today)) {
                return "Birthdate cannot be today";
            }

            LocalDate minDate = today.minusYears(150);
            if (birthDate.isBefore(minDate)) {
                return "Birthdate is too far in the past (max 150 years)";
            }

            return null; // Valid
        } catch (DateTimeParseException e) {
            return "Invalid date format. Please use yyyy-MM-dd format (e.g., 1995-03-15). Note: Invalid dates like February 30 are not allowed.";
        }
    }
}
