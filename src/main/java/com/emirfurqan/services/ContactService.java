package com.emirfurqan.services;

import com.emirfurqan.dao.ContactDao;
import com.emirfurqan.dao.ContactDaoImplementation;
import com.emirfurqan.models.Contact;
import com.emirfurqan.exceptions.DatabaseException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ContactService
{
    private final ContactDao contactDao = new ContactDaoImplementation();

    // List all contacts
    public List<Contact> listAll() throws DatabaseException
    {
        try
        {
            return contactDao.getAllContacts();
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to list contacts", e);
        }
    }

    // Search by a single field
    public List<Contact> searchSingleField(String field, String value) throws DatabaseException
    {
        try
        {
            return contactDao.searchBySingleField(field, value);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to search contacts by field", e);
        }
    }

    // Search by multiple fields
    public List<Contact> searchMultipleFields(Map<String, String> fields) throws DatabaseException
    {
        try
        {
            return contactDao.searchByMultipleFields(fields);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to search contacts by multiple fields", e);
        }
    }

    // Add a new contact
    public void addContact(Contact contact) throws DatabaseException
    {
        try
        {
            contactDao.addContact(contact);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to add contact", e);
        }
    }

    // Update a contact
    public void updateContact(Contact contact) throws DatabaseException
    {
        try
        {
            contactDao.updateContact(contact);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to update contact", e);
        }
    }

    // Delete a contact
    public void deleteContact(int id) throws DatabaseException
    {
        try
        {
            contactDao.deleteContact(id);
        }
        catch (Exception e)
        {
            throw new DatabaseException("Failed to delete contact", e);
        }
    }

    // Sort a list of contacts by a field
    public List<Contact> sortContacts(List<Contact> contacts, String field, boolean ascending)
    {
        if (contacts == null || contacts.isEmpty())
            return contacts;

        Comparator<Contact> comparator;

        switch (field.toLowerCase())
        {
            case "firstname", "first_name" -> comparator = Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER);
            case "lastname", "last_name" -> comparator = Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER);
            case "nickname" -> comparator = Comparator.comparing(Contact::getNickname, String.CASE_INSENSITIVE_ORDER);
            case "email" -> comparator = Comparator.comparing(Contact::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "birthdate", "birth_date" -> comparator = Comparator.comparing(Contact::getBirthDate);
            default -> throw new IllegalArgumentException("Cannot sort by unknown field: " + field);
        }

        if (!ascending)
        {
            comparator = comparator.reversed();
        }

        contacts.sort(comparator);
        return contacts;
    }
}
