package controller;
import view.*;
import model.PIMModel;
import model.PersonalInformationRecord;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PIMcontroller {
    private PIMModel model;
    private PIMView view;
    private Scanner scanner;
    public PIMcontroller(PIMModel model, PIMView view) {
        this.model = model;
        this.view = view;
        scanner = new Scanner(System.in);
    }
    public void run() {
        System.out.println("Welcome to the Personal Information Manager (PIM)!");
        System.out.println("Enter 'help' to see the available commands.");

        while (true) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("help")) {
                    view.displayHelp();
                } else if (input.equalsIgnoreCase("create")) {
                    model.createRecord();
                } else if (input.equalsIgnoreCase("display")) {
                    view.displayRecords(getallrecords());
                } else if (input.equalsIgnoreCase("modify")) {
                    model.modifyRecord();
                } else if (input.equalsIgnoreCase("delete")) {
                    model.deleteRecord();
                } else if (input.equalsIgnoreCase("store")) {
                    model.getStoreFileName();
                } else if (input.equalsIgnoreCase("load")) {
                    model.getLoadFileName();
                } else if (input.equalsIgnoreCase("search")) {
                    view.displayRecords(model.searchRecord());
                } else if (input.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    System.out.println("Unknown command. Enter 'help' to see the available commands.");
                }
            } catch (Exception e) {
                System.out.println("Input error: " + e.getMessage());
            }
        }

        System.out.println("Thank you for using the Personal Information Manager (PIM). Goodbye!");
    }
    public List<PersonalInformationRecord> getallrecords(){
        return model.getAllRecords();
    }

}