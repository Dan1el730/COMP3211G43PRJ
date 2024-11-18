package view;

import model.PersonalInformationRecord;

import java.util.List;

public class    PIMView {
    public void displayRecords(List<PersonalInformationRecord> records) {
        if (records.isEmpty()) {
            System.out.println("No records to display.");
        } else {
            for (PersonalInformationRecord record : records) {
                System.out.println(record.getInfo());
                System.out.println("--------------------");
            }
        }
    }
    public void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("- create: Create a new personal information record.");
        System.out.println("- search: Display the target personal information records.");
        System.out.println("- display: Display all the existing personal information records.");
        System.out.println("- modify: Modify an existing personal information record.");
        System.out.println("- delete: delete an existing personal information record.");
        System.out.println("- store: store the personal information record into a pim file.");
        System.out.println("- load: load a pim file.");
        System.out.println("- exit: Exit the program.");
    }
}