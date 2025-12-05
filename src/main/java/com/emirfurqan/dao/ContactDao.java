package com.emirfurqan.dao;

import com.emirfurqan.models.Contact;

import java.util.List;
import java.util.Map;

public interface ContactDao {
    List<Contact> getAllContacts();
    List<Contact> searchBySingleField(String field, String value);
    List<Contact> searchByMultipleFields(Map<String, String> fields);
    void addContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(int id);
}
