package com.emirfurqan.services;

import com.emirfurqan.models.User;
import com.emirfurqan.models.Contact;
import com.emirfurqan.exceptions.DatabaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticalInfoService
{

    private final UserService userService = new UserService();
    private final ContactService contactService = new ContactService();

    /**
     * Returns statistics about users and contacts
     * @return Map with statistical info
     * @throws DatabaseException if fetching data fails
     */
    public Map<String, Object> getStatistics() throws DatabaseException
    {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        List<User> users = userService.listUsers();
        stats.put("totalUsers", users.size());

        Map<String, Integer> roleCounts = new HashMap<>();
        for (User u : users)
        {
            roleCounts.put(u.getUserRole(),
                    roleCounts.getOrDefault(u.getUserRole(), 0) + 1);
        }
        stats.put("userRoleCounts", roleCounts);

        // Contact statistics
        List<Contact> contacts = contactService.listAll();
        stats.put("totalContacts", contacts.size());

        // Example: birthdays this month
        long birthdaysThisMonth = contacts.stream()
                .filter(c -> c.getBirthDate() != null && c.getBirthDate().getMonthValue() == java.time.LocalDate.now().getMonthValue())
                .count();
        stats.put("birthdaysThisMonth", birthdaysThisMonth);

        return stats;
    }
}
