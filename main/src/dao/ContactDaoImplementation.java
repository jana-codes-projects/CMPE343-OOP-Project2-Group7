package dao;

import db.DatabaseConnection;
import models.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactDaoImplementation implements ContactDao {

    private final DatabaseConnection db = new DatabaseConnection();

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contacts";
        Connection conn = null;

        try {
            conn = db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                contacts.add(new Contact(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }

        return contacts;
    }

    @Override
    public List<Contact> searchBySingleField(String field, String value) {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contacts WHERE " + field + " LIKE ?";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + value + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contacts.add(new Contact(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }

        return contacts;
    }

    @Override
    public List<Contact> searchByMultipleFields(Map<String, String> fields) {
        List<Contact> contacts = new ArrayList<>();
        if (fields.isEmpty()) return contacts;

        StringBuilder query = new StringBuilder("SELECT * FROM contacts WHERE ");
        List<String> values = new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            query.append(entry.getKey()).append(" LIKE ?");
            if (i < fields.size() - 1) query.append(" AND ");
            values.add("%" + entry.getValue() + "%");
            i++;
        }

        Connection conn = null;
        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query.toString());
            for (int j = 0; j < values.size(); j++) {
                ps.setString(j + 1, values.get(j));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }

        return contacts;
    }

    @Override
    public void addContact(Contact contact) {
        String query = "INSERT INTO contacts (first_name, middle_name, last_name, nickname, phone_primary, phone_secondary, email, linkedin_url, birth_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getMiddleName());
            ps.setString(3, contact.getLastName());
            ps.setString(4, contact.getNickname());
            ps.setString(5, contact.getPhonePrimary());
            ps.setString(6, contact.getPhoneSecondary());
            ps.setString(7, contact.getEmail());
            ps.setString(8, contact.getLinkedinUrl());
            ps.setDate(9, Date.valueOf(contact.getBirthDate()));
            ps.setTimestamp(10, Timestamp.valueOf(contact.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(contact.getUpdatedAt()));

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }

    @Override
    public void updateContact(Contact contact) {
        String query = "UPDATE contacts SET first_name=?, middle_name=?, last_name=?, nickname=?, phone_primary=?, phone_secondary=?, email=?, linkedin_url=?, birth_date=?, updated_at=? WHERE contact_id=?";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getMiddleName());
            ps.setString(3, contact.getLastName());
            ps.setString(4, contact.getNickname());
            ps.setString(5, contact.getPhonePrimary());
            ps.setString(6, contact.getPhoneSecondary());
            ps.setString(7, contact.getEmail());
            ps.setString(8, contact.getLinkedinUrl());
            ps.setDate(9, Date.valueOf(contact.getBirthDate()));
            ps.setTimestamp(10, Timestamp.valueOf(contact.getUpdatedAt()));
            ps.setInt(11, contact.getContactId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }

    @Override
    public void deleteContact(int id) {
        String query = "DELETE FROM contacts WHERE contact_id=?";
        Connection conn = null;

        try {
            conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close(conn);
        }
    }
}