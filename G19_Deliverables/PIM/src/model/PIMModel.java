package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;
import model.utils.Matcher;
public class PIMModel {
    private List<PersonalInformationRecord> records;
    private Scanner scanner;
    public PIMModel() {
        records = new ArrayList<>();
        scanner = new Scanner(System.in);
    }


    private String getUserInput(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
    public void deleteRecord(){
        System.out.println("For the deletion, you need to enter the exact data");
        String type = getUserInput("Enter the record type (contact/event/plaintext/task):");
        String title;
        boolean result;
        switch (type.toLowerCase()) {
            case "contact":
                title = getUserInput("Enter title:");
                String name = getUserInput("Enter name:");
                String address = getUserInput("Enter address:");
                String mobileNumber = getUserInput("Enter mobile number:");
                result = rmContactRecord(title, name, address, mobileNumber);
                if (result){
                    System.out.println("ContactRecord delete successfully.");
                }else{
                    System.out.println("ContactRecord delete fail.");
                }
                break;
            case "event":
                title = getUserInput("Enter title:");
                String description = getUserInput("Enter description:");
                String startTimeStr = getUserInput("Enter start time (yyyy-MM-dd HH:mm):");
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String hasAlarmStr = getUserInput("Does it have an alarm? (yes/no):");
                boolean hasAlarm = hasAlarmStr.equalsIgnoreCase("yes");
                result = rmEventRecord(title, description, startTime, hasAlarm);
                if (result){
                    System.out.println("EventRecord delete successfully.");
                }else{
                    System.out.println("EventRecord delete fail.");
                }
                break;
            case "plaintext":
                title = getUserInput("Enter title:");
                String content = getUserInput("Enter content:");
                result = rmPlainTextRecord(title, content);
                if (result){
                    System.out.println("PlainTextRecord delete successfully.");
                }else{
                    System.out.println("PlainTextRecord delete fail.");
                }
                break;
            case "task":
                title = getUserInput("Enter title:");
                String taskDescription = getUserInput("Enter task description:");
                String deadlineStr = getUserInput("Enter deadline (yyyy-MM-dd HH:mm):");
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                result = rmTaskRecord(title, taskDescription, deadline);
                if (result){
                    System.out.println("TaskRecord delete successfully.");
                }else{
                    System.out.println("TaskRecord delete fail.");
                }
                break;
            default:
                System.out.println("Invalid record type.");
                break;
        }
    }

    public List<PersonalInformationRecord> searchRecord() {
        String type = getUserInput("Enter the record type (contact/event/plaintext/task):");
        String title;
        char symbol;
        List<PersonalInformationRecord> target=null;
        System.out.println("Enter the intformation you search, if not suitable, press enter");
        System.out.println("For multiple information, use &&(and), ||(or) and !(negation)");
        switch (type.toLowerCase()) {
            case "contact":
                title = getUserInput("Enter title:");
                String name = getUserInput("Enter name:");
                String address = getUserInput("Enter address:");
                String mobileNumber = getUserInput("Enter mobile number:");
                System.out.println("\nContactRecord:");
                target = searchContactRecords(title, name, address, mobileNumber);
                break;
            case "event":
                title = getUserInput("Enter title:");
                String description = getUserInput("Enter description:");
                String targetTimeStr = getUserInput("Enter target start time ((<,>,=)yyyy-MM-dd HH:mm):");
                symbol = targetTimeStr.charAt(0);
                targetTimeStr = targetTimeStr.substring(1);
                LocalDateTime targetTime = LocalDateTime.parse(targetTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                System.out.println("\nEventRecord:");
                target = searchEventRecords(title, description, targetTime, symbol);
                break;
            case "plaintext":
                title = getUserInput("Enter title:");
                String content = getUserInput("Enter content:");
                System.out.println("PlainTextRecord:");
                target = searchPlainTextRecord(title, content);
                break;
            case "task":
                title = getUserInput("Enter title:");
                String taskDescription = getUserInput("Enter task description:");
                String deadlineStr = getUserInput("Enter target deadline ((<,>,=)yyyy-MM-dd HH:mm):");
                symbol = deadlineStr.charAt(0);
                deadlineStr = deadlineStr.substring(1);
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                System.out.println("TaskRecord:");
                target = searchTaskRecord(title, taskDescription, deadline, symbol);
                break;
            default:
                System.out.println("Invalid record type.");
                break;
        }
        return target;
    }
    public void modifyRecord() {
        String title = getUserInput("Enter the title of the record you want to modify:");

        // Check if the record exists
        PersonalInformationRecord record = getRecordByTitle(title);
        if (record == null) {
            System.out.println("Record not found.");
            return;
        }

        // Prompt the user for the updated data
        //String type = getUserInput("Enter the updated record type (contact/event/plaintext/task):");

        String type = displayRecordTypeMenu();

        switch (type.toLowerCase()) {
            case "contact":
                modifyContactRecord(record);
                break;
            case "event":
                modifyEventRecord(record);
                break;
            case "plaintext":
                modifyPlainTextRecord(record);
                break;
            case "task":
                modifyTaskRecord(record);
                break;
            default:
                System.out.println("Invalid record type.");
                break;
        }
    }

    private String displayRecordTypeMenu() {
        boolean flag = true;
        String type = null;
        while (flag) {
            System.out.println("Please choose the record type to modify:");
            System.out.println("1. Contact Record");
            System.out.println("2. Event Record");
            System.out.println("3. Plain Text Record");
            System.out.println("4. Task Record");
            type = getUserInput("Your choice: ");
            switch (type) {
                case "1":
                    flag = false;
                    type = "contact";
                    break;
                case "2":
                    flag = false;
                    type = "event";
                    break;
                case "3":
                    flag = false;
                    type = "plaintext";
                    break;
                case "4":
                    flag = false;
                    type = "task";
                    break;
                default:
                    System.out.println("Invalid record type, please enter again");
                    break;
            }
        }
        return type;
    }
    public void createRecord() {
        // Prompt the user for input
        String type = getUserInput("Enter the record type (contact/event/plaintext/task):");
        String title = getUserInput("Enter title:");

        switch (type.toLowerCase()) {
            case "contact":
                String name = getUserInput("Enter name:");
                String address = getUserInput("Enter address:");
                String mobileNumber = getUserInput("Enter mobile number:");
                createContactRecord(title, name, address, mobileNumber);
                System.out.println("ContactRecord created successfully.");
                break;
            case "event":
                String description = getUserInput("Enter description:");
                String startTimeStr = getUserInput("Enter start time (yyyy-MM-dd HH:mm):");
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String hasAlarmStr = getUserInput("Does it have an alarm? (yes/no):");
                boolean hasAlarm = hasAlarmStr.equalsIgnoreCase("yes");
                createEventRecord(title, description, startTime, hasAlarm);
                System.out.println("EventRecord created successfully.");
                break;
            case "plaintext":
                String content = getUserInput("Enter content:");
                createPlainTextRecord(title, content);
                System.out.println("PlainTextRecord created successfully.");
                break;
            case "task":
                String taskDescription = getUserInput("Enter task description:");
                String deadlineStr = getUserInput("Enter deadline (yyyy-MM-dd HH:mm):");
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                createTaskRecord(title, taskDescription, deadline);
                System.out.println("TaskRecord created successfully.");
                break;
            default:
                System.out.println("Invalid record type.");
                break;
        }
    }
    public void createPlainTextRecord(String title, String content) {
        PersonalInformationRecord record = new PlainTextRecord(title, content);
        addRecord(record);
    }


    public void createTaskRecord(String title, String description, LocalDateTime deadline) {
        PersonalInformationRecord record = new TaskRecord(title, description, deadline);
        addRecord(record);
    }

    public void createEventRecord(String title, String description, LocalDateTime startTime, boolean hasAlarm) {
        PersonalInformationRecord record = new EventRecord(title, description, startTime, hasAlarm);
        addRecord(record);
    }

    public void createContactRecord(String title, String name, String address, String mobileNumber) {
        PersonalInformationRecord record = new ContactRecord(title, name, address, mobileNumber);
        addRecord(record);
    }

    public void addRecord(PersonalInformationRecord record) {
        records.add(record);
    }

    public List<PersonalInformationRecord> getAllRecords() {
        return records;
    }


    public PersonalInformationRecord getRecordByTitle(String title) {
        // Iterate through your collection of records or query your data source
        // Check if the title matches any of the records
        // If a match is found, return the record
        // If no match is found, return null

        // Example implementation with a list of records
        List<PersonalInformationRecord> records = getAllRecords();
        for (PersonalInformationRecord record : records) {
            if (record.getTitle().equals(title)) {
                return record;
            }
        }
        return null;
    }
    private void modifyContactRecord(PersonalInformationRecord record) {
        String title = getUserInput("Enter the updated title:");
        String name = getUserInput("Enter the updated name:");
        String address = getUserInput("Enter the updated address:");
        String mobileNumber = getUserInput("Enter the updated mobile number:");
        contactRecordModifier(record, title, name, address, mobileNumber);
    }

    public void contactRecordModifier(PersonalInformationRecord record, String title, String name, String address, String mobileNumber) {
        if (record instanceof ContactRecord) {
            ContactRecord contactRecord = (ContactRecord) record;
            contactRecord.setTitle(title);
            contactRecord.setName(name);
            contactRecord.setAddress(address);
            contactRecord.setMobileNumber(mobileNumber);
            System.out.println("ContactRecord modified successfully.");
        } else {
            System.out.println("Invalid record type.");
        }
    }
    private void modifyEventRecord(PersonalInformationRecord record) {
        String title = getUserInput("Enter the updated title:");
        String description = getUserInput("Enter the updated description:");
        String startTimeStr = getUserInput("Enter the updated start time (yyyy-MM-dd HH:mm):");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String hasAlarmStr = getUserInput("Does it have an alarm? (yes/no):");
        boolean hasAlarm = hasAlarmStr.equalsIgnoreCase("yes");
        // Get the updated start time and other event-related details
        // Modify the event record accordingly
        eventRecordModifier(record, title, description, startTime, hasAlarm);
    }

    public void eventRecordModifier(PersonalInformationRecord record, String title, String description, LocalDateTime startTime, boolean hasAlarm) {
        if (record instanceof EventRecord) {
            EventRecord eventRecord = (EventRecord) record;
            eventRecord.setTitle(title);
            eventRecord.setDescription(description);
            eventRecord.setStartTime(startTime);
            eventRecord.setHasAlarm(hasAlarm);
            System.out.println("EventRecord modified successfully.");
        } else {
            System.out.println("Invalid record type.");
        }
    }
    private void modifyPlainTextRecord(PersonalInformationRecord record) {
        String title = getUserInput("Enter the updated title:");
        String content = getUserInput("Enter the updated content:");
        plainTextRecordModifier(record, title, content);
    }
    public void plainTextRecordModifier(PersonalInformationRecord record, String title, String content) {
        if (record instanceof PlainTextRecord) {
            PlainTextRecord plainTextRecord = (PlainTextRecord) record;
            plainTextRecord.setTitle(title);
            plainTextRecord.setContent(content);
            System.out.println("PlainTextRecord modified successfully.");
        } else {
            System.out.println("Invalid record type.");
        }
    }
    private void modifyTaskRecord(PersonalInformationRecord record) {
        String title = getUserInput("Enter the updated title:");
        String taskDescription = getUserInput("Enter the updated task description:");
        String deadlineStr = getUserInput("Enter the updated deadline (yyyy-MM-dd HH:mm):");
        LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // Get the updated deadline and other task-related details
        // Modify the task record accordingly
        taskRecordModifier(record, title, taskDescription, deadline);

    }

    public void taskRecordModifier(PersonalInformationRecord record, String title, String taskDescription, LocalDateTime deadline) {
        if (record instanceof TaskRecord) {
            TaskRecord taskRecord = (TaskRecord) record;
            taskRecord.setTitle(title);
            taskRecord.setDescription(taskDescription);
            taskRecord.setDeadline(deadline);
            System.out.println("TaskRecord modified successfully.");
        } else {
            System.out.println("Invalid record type.");
        }
    }

    //search: loop the record and compare it with user input
    public List<PersonalInformationRecord> searchContactRecords(String title, String name, String address, String mobileNumber) {
        List<PersonalInformationRecord> target = new ArrayList<>();
        List<PersonalInformationRecord> records = getAllRecords();
        for (PersonalInformationRecord record : records) {
            if (!(record instanceof ContactRecord)) {
                continue;
            } else {
                ContactRecord contactRecord = (ContactRecord) record;
                if (Matcher.match(contactRecord.getTitle(), title)
                        && Matcher.match(contactRecord.getName(), name)
                        && Matcher.match(contactRecord.getAddress(),address)
                        && Matcher.match(contactRecord.getMobileNumber(), mobileNumber)) {
                    target.add(contactRecord);
                }
            }
        }
        return target;
    }

    public List<PersonalInformationRecord> searchEventRecords(String title, String description, LocalDateTime targetTime, char symbol) {
        List<PersonalInformationRecord> target = new ArrayList<>();
        List<PersonalInformationRecord> records = getAllRecords();
        EventRecord temp;
        for (PersonalInformationRecord record : records) {
            boolean flag = true;
            if (!(record instanceof EventRecord)) {
                continue;
            } else {
                temp = (EventRecord) record;
            }
            if (!Matcher.match(temp.getTitle(), title) && !Matcher.match(temp.getDescription(), description)) {
                flag = false;
            }
            switch (symbol) {
                case '<':
                    if (!temp.getStartTime().isBefore(targetTime)) {
                        flag = false;
                    }
                    break;
                case '>':
                    if (!temp.getStartTime().isAfter(targetTime)) {
                        flag = false;
                    }
                    break;
                case '=':
                    if (!temp.getStartTime().isEqual(targetTime)) {
                        flag = false;
                    }
                    break;
            }
            if (flag) {
                target.add(temp);
            }
        }
        return target;
    }

    public List<PersonalInformationRecord> searchPlainTextRecord(String title, String content) {
        List<PersonalInformationRecord> target = new ArrayList<>();
        List<PersonalInformationRecord> records = getAllRecords();
        for (PersonalInformationRecord record : records) {
            if (!(record instanceof PlainTextRecord)) {
                continue;
            } else {
                PlainTextRecord plainTextRecord = (PlainTextRecord) record;
                if (Matcher.match(plainTextRecord.getTitle(), title) && Matcher.match(plainTextRecord.getContent(), content)) {
                    target.add(plainTextRecord);
                }
            }
        }
        return target;
    }

    public List<PersonalInformationRecord> searchTaskRecord(String title, String description, LocalDateTime deadline, char symbol) {
        List<PersonalInformationRecord> target = new ArrayList<>();
        List<PersonalInformationRecord> records = getAllRecords();
        TaskRecord temp;
        for (PersonalInformationRecord record : records) {
            boolean flag = true;
            if (!(record instanceof TaskRecord)) {
                continue;
            } else {
                temp = (TaskRecord) record;
            }
            if (!Matcher.match(temp.getTitle(), title) && !Matcher.match(temp.getDescription(), description)) {
                flag = false;
            }
            switch (symbol) {
                case '<':
                    if (!temp.getDeadline().isBefore(deadline)) {
                        flag = false;
                    }
                    break;
                case '>':
                    if (!temp.getDeadline().isAfter(deadline)) {
                        flag = false;
                    }
                    break;
                case '=':
                    if (!temp.getDeadline().isEqual(deadline)) {
                        flag = false;
                    }
                    break;
            }
            if (flag) {
                target.add(temp);
            }
        }
        return target;
    }
    public boolean rmPlainTextRecord(String title, String content) {
        Iterator<PersonalInformationRecord> iterator = records.iterator();
        while (iterator.hasNext()) {
            PersonalInformationRecord item = iterator.next();
            if (item instanceof PlainTextRecord) {
                PlainTextRecord temp = (PlainTextRecord) item;
                String t = temp.getTitle();
                String c = temp.getContent();
                if (t.equals(title) && c.equals(content)) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean rmTaskRecord(String title, String description, LocalDateTime deadline) {
        Iterator<PersonalInformationRecord> iterator = records.iterator();
        while (iterator.hasNext()) {
            PersonalInformationRecord item = iterator.next();
            if (item instanceof TaskRecord) {
                TaskRecord temp = (TaskRecord) item;
                String t = temp.getTitle();
                String d = temp.getDescription();
                LocalDateTime dl = temp.getDeadline();
                if (t.equals(title) && d.equals(description) && dl.equals(deadline)) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean rmEventRecord(String title, String description, LocalDateTime startTime, boolean hasAlarm) {
        Iterator<PersonalInformationRecord> iterator = records.iterator();
        while (iterator.hasNext()) {
            PersonalInformationRecord item = iterator.next();
            if (item instanceof EventRecord) {
                EventRecord temp = (EventRecord) item;
                String t = temp.getTitle();
                String d = temp.getDescription();
                LocalDateTime st = temp.getStartTime();
                boolean alarm = temp.hasAlarm();
                if (t.equals(title) && d.equals(description) && st.equals(startTime) && alarm == hasAlarm) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean rmContactRecord(String title, String name, String address, String mobileNumber) {
        Iterator<PersonalInformationRecord> iterator = records.iterator();
        while (iterator.hasNext()) {
            PersonalInformationRecord item = iterator.next();
            if (item instanceof ContactRecord) {
                ContactRecord temp = (ContactRecord) item;
                String t = temp.getTitle();
                String n = temp.getName();
                String a = temp.getAddress();
                String m = temp.getMobileNumber();
                if (t.equals(title) && n.equals(name) && a.equals(address) && m.equals(mobileNumber)) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }
    private void storeRecord(String record, FileWriter writer) throws IOException {
        writer.write(record);
        writer.write("\n--------------------\n");
    }

    public void storedRecordList(String name) {
        try (FileWriter writer = new FileWriter(name, false)) {
            for (PersonalInformationRecord record : records) {
                String text = record.getInfo();
                storeRecord(text, writer);
            }
            System.out.println("Success");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }
    public void getStoreFileName(){
        List<PersonalInformationRecord> records = getAllRecords();
        if (records.isEmpty()) {
            System.out.println("No records to store.");
        } else {
            String name = getUserInput("Enter the file name:");
            if (!name.contains(".pim")) {
                name = name + ".pim";
            }
            storedRecordList(name);
        }
    }
    public void getLoadFileName(){
        String name = getUserInput("Enter the file name:");
        if (!name.contains(".pim")) {
            name = name + ".pim";
        }
        try{
            loadRecord(name);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
    public void loadRecord(String name) throws FileNotFoundException {
        File f = new File(name);
        Scanner r = new Scanner(f);
        while (r.hasNextLine()) {
            String data = r.nextLine();
            if (data.equals("--------------------")) {
                continue;
            }
            String n = data;
            ArrayList<String> temp = new ArrayList<>();
            int first = 0;
            int last = 0;
                while (last != n.length() && n.charAt(last) != ' ') {
                    last++;
                }
                String part = n.substring(first, last);
                temp.add(part);
                data = data.substring(last+1);

            switch (temp.get(0)) {
                case "Contact:":
                    readContactRecord(data, r.nextLine(), r.nextLine(), r.nextLine());
                    break;
                case "Event:":
                    readEventRecord(data, r.nextLine(), r.nextLine(), r.nextLine());
                    break;
                case "Task:":
                    readTaskRecord(data, r.nextLine(), r.nextLine());
                    break;
                case "PlainText:":
                    readPlainTextRecord(data, r.nextLine());
                    break;
                default:
                    break;
            }
        }
        r.close();
        System.out.println("Record Load successfully");
    }
    private void readContactRecord(String Stitle, String Sname, String Saddress, String Sno) {

        String name = Sname.split(" ", 2)[1];
        String address = Saddress.split(" ", 2)[1];
        String no = Sno.split(" ", 3)[2];
        createContactRecord(Stitle, name, address, no);
    }

    private void readEventRecord(String Stitle, String Sdescription, String SstartTime, String ShasAlarm) {

        String description = Sdescription.split(" ", 2)[1];
        String startTimeStr = SstartTime.split(" ", 3)[2];
        ShasAlarm = ShasAlarm.split(" ", 2)[1];
        startTimeStr = startTimeStr.replace("T"," ");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        boolean hasAlarm = ShasAlarm.equalsIgnoreCase("true");
        createEventRecord(Stitle, description, startTime, hasAlarm);
    }

    private void readTaskRecord(String Stitle, String Sdescription, String Sdeadline) {

        String taskDescription = Sdescription.split(" ", 2)[1];
        String deadlineStr = Sdeadline.split(" ", 2)[1];
        deadlineStr = deadlineStr.replace("T"," ");
        LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        createTaskRecord(Stitle, taskDescription, deadline);
    }

    private void readPlainTextRecord(String Stitle, String Scontent) {

        String content = Scontent.split(" ", 2)[1];
        createPlainTextRecord(Stitle, content);
    }
}