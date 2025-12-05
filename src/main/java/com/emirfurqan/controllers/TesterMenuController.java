package com.emirfurqan.controllers;

import com.emirfurqan.exceptions.DatabaseException;
import com.emirfurqan.models.User;
import com.emirfurqan.services.ContactService;
import com.emirfurqan.auth.AuthService;
import java.util.Scanner;

public class TesterMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    public TesterMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println("===== TESTER MENU =====");
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Undo Last Operation");
        System.out.println("[0] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option)
        {
            case 1 -> changePassword();
            case 2 -> logout();
            case 3 -> contactService.listAll();
            case 4 -> searchContactsSingle();
            case 5 -> searchContactsMultiple();
            case 6 -> sortContacts();
            case 7 -> undo();
            case 0 -> logout();
        }
    }

    private void searchContactsSingle() {
        // TODO ask for (field, value) → call contactService.searchSingleField(...)
    }

    private void searchContactsMultiple() {
        // TODO ask for map of fields → call contactService.searchMultipleFields(...)
    }

    private void sortContacts() {
        // TODO ask for (field, asc/desc) → call contactService.sortContacts(...)
    }
}
