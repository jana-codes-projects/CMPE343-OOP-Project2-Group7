package controllers;

import exceptions.DatabaseException;
import models.User;
import services.ContactService;
import auth.AuthService;
import java.util.Scanner;

public class SeniorDeveloperMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    public SeniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println("===== SENIOR DEVELOPER MENU =====");
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Add Contact");
        System.out.println("[9] Delete Contact");
        System.out.println("[10] Undo Last Operation");
        System.out.println("[0] Logout");
    }

    @Override
    public void handleUserSelection(int option) throws DatabaseException {
        switch (option) {
            case 1 -> changePassword();
            case 2 -> logout();
            case 3 -> contactService.listAll();
            case 4 -> searchContactsSingle();
            case 5 -> searchContactsMultiple();
            case 6 -> sortContacts();
            case 7 -> updateContact();
            case 8 -> addContact();
            case 9 -> deleteContact();
            case 10 -> undo();
            case 0 -> logout();
        }
    }

    private void searchContactsSingle()
    {

    }

    private void searchContactsMultiple()
    {

    }

    private void sortContacts()
    {

    }

    private void updateContact()
    {

    }

    private void addContact()
    {
        // TODO prompt contact info → call contactService.addContact(...)
    }

    private void deleteContact()
    {
        // TODO ask for contactId → saveState first → call contactService.deleteContact(...)
    }
}
