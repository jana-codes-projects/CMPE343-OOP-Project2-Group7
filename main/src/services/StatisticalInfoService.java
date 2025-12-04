package services;

import models.Contact;
import exceptions.DatabaseException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticalInfoService
{
    private final ContactService contactService = new ContactService();

    /**
     * Returns statistics about users and contacts
     * @return Map with statistical info
     * @throws DatabaseException if fetching data fails
     */
    public Map<String, Object> getStatistics() throws DatabaseException
    {
        Map<String, Object> stats = new HashMap<>();

        /* ============================================================
           CONTACT STATISTICS
        ============================================================ */
        List<Contact> contacts = contactService.listAll();
        stats.put("totalContacts", contacts.size());


        /* -----------------------------
           1. Sharing same FIRST NAME
        ----------------------------- */
        Map<String, Long> firstNameCounts = contacts.stream()
                .collect(Collectors.groupingBy(Contact::getFirstName, Collectors.counting()));
        stats.put("firstNameCounts", firstNameCounts);


        /* -----------------------------
           2. Sharing same LAST NAME
        ----------------------------- */
        Map<String, Long> lastNameCounts = contacts.stream()
                .collect(Collectors.groupingBy(Contact::getLastName, Collectors.counting()));
        stats.put("lastNameCounts", lastNameCounts);


        /* -----------------------------
           3. With / Without LinkedIn URL
        ----------------------------- */
        long withLinkedIn = contacts.stream()
                .filter(c -> c.getLinkedinUrl() != null && !c.getLinkedinUrl().isEmpty())
                .count();

        long withoutLinkedIn = contacts.size() - withLinkedIn;

        stats.put("withLinkedIn", withLinkedIn);
        stats.put("withoutLinkedIn", withoutLinkedIn);


        /* -----------------------------
           4. Youngest Contact
        ----------------------------- */
        Contact youngest = contacts.stream()
                .filter(c -> c.getBirthDate() != null)
                .max(Comparator.comparing(Contact::getBirthDate))
                .orElse(null);
        stats.put("youngestContact", youngest);


        /* -----------------------------
           5. Oldest Contact
        ----------------------------- */
        Contact oldest = contacts.stream()
                .filter(c -> c.getBirthDate() != null)
                .min(Comparator.comparing(Contact::getBirthDate))
                .orElse(null);
        stats.put("oldestContact", oldest);


        /* -----------------------------
           6. Average Age of Contacts
        ----------------------------- */
        List<Integer> ages = contacts.stream()
                .filter(c -> c.getBirthDate() != null)
                .map(c -> Period.between(c.getBirthDate(), LocalDate.now()).getYears())
                .toList();

        double averageAge = ages.isEmpty()
                ? 0
                : ages.stream().mapToInt(Integer::intValue).average().orElse(0);

        stats.put("averageAge", averageAge);


        /* -----------------------------
           7. Birthdays this Month
        ----------------------------- */
        long birthdaysThisMonth = contacts.stream()
                .filter(c -> c.getBirthDate() != null &&
                        c.getBirthDate().getMonthValue() == LocalDate.now().getMonthValue())
                .count();
        stats.put("birthdaysThisMonth", birthdaysThisMonth);


        /* -----------------------------
           8. Email domain distribution (gmail, outlook, yahoo etc.)
        ----------------------------- */
        Map<String, Long> emailDomains = contacts.stream()
                .filter(c -> c.getEmail() != null && c.getEmail().contains("@"))
                .map(c -> c.getEmail().substring(c.getEmail().indexOf("@") + 1))
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
        stats.put("emailDomainCounts", emailDomains);


        return stats;
    }
}
