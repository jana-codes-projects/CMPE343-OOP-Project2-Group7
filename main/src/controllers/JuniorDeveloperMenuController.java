package controllers;

import exceptions.DatabaseException;
import models.User;
import services.ContactService;
import auth.AuthService;
import java.util.Scanner;

public class JuniorDeveloperMenuController extends BaseMenuController
{
    private final ContactService contactService = new ContactService();
    private final AuthService authService = new AuthService();

    public JuniorDeveloperMenuController(User user, Scanner scanner)
    {
        super(user, scanner);
    }

    @Override
    public void displayMenu()
    {
        System.out.println("===== JUNIOR DEVELOPER MENU =====");
        System.out.println("[1] Change Password");
        System.out.println("[2] Logout");
        System.out.println("[3] List All Contacts");
        System.out.println("[4] Search Contacts (Single Field)");
        System.out.println("[5] Search Contacts (Multiple Fields)");
        System.out.println("[6] Sort Contacts");
        System.out.println("[7] Update Contact");
        System.out.println("[8] Undo Last Operation");
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
            case 8 -> undo();
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
        // TODO ask for contactId + fields → call contactService.updateContact(...)
    }
}
