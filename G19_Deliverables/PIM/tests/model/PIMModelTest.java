package model;

import model.utils.Matcher;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PIMModelTest {

    private PIMModel model;

    @BeforeEach
    void setUp() {
        model = new PIMModel();
    }
    @Test
    void createAndRemovePlainTextRecordTest() {
        String title = "XYZ";
        String content = "Lorem ipsum";

        // Test creating a plain text record

        model.createPlainTextRecord(title, content);
        List<PersonalInformationRecord> recordList = model.getAllRecords();
        PersonalInformationRecord expectedRecord = new PlainTextRecord(title, content);
        assertTrue(recordList.contains(expectedRecord));

        // Test removing a plain text record
        model.rmPlainTextRecord(title, content);
        recordList = model.getAllRecords();
        Assertions.assertFalse(recordList.contains(expectedRecord));
    }
    @Test
    void createAndRemoveTaskRecordTest() {
        String title = "ABC";
        String description = "DEF";
        LocalDateTime dateTime = LocalDateTime.of(2001, 12, 25, 12, 12);
        // Test creating a task record
        model.createTaskRecord(title, description, dateTime);
        List<PersonalInformationRecord> recordList = model.getAllRecords();
        PersonalInformationRecord expectedRecord = new TaskRecord(title, description, dateTime);
        assertTrue(recordList.contains(expectedRecord));

        // Test removing a task record
        model.rmTaskRecord(title, description, dateTime);
        recordList = model.getAllRecords();
        Assertions.assertFalse(recordList.contains(expectedRecord));
    }

    @Test
    void createAndRemoveEventRecordTest() {
        String title = "Meeting";
        String description = "Team meeting";
        LocalDateTime startTime = LocalDateTime.of(2022, 6, 30, 10, 30);
        boolean hasAlarm = true;
        // Test creating a event record
        model.createEventRecord(title, description, startTime, hasAlarm);
        List<PersonalInformationRecord> recordList = model.getAllRecords();
        PersonalInformationRecord expectedRecord = new EventRecord(title, description, startTime, hasAlarm);
        assertTrue(recordList.contains(expectedRecord));

        // Test removing a event record
        model.rmEventRecord(title, description, startTime, hasAlarm);
        recordList = model.getAllRecords();
        Assertions.assertFalse(recordList.contains(expectedRecord));
    }
    @Test
    void createAndRemoveContactRecordTest() {
        String title = "Work";
        String name = "John Doe";
        String address = "123 Main St";
        String mobileNumber = "9876543210";
        // Test creating a contact record
        model.createContactRecord(title, name, address, mobileNumber);
        List<PersonalInformationRecord> recordList = model.getAllRecords();
        PersonalInformationRecord expectedRecord = new ContactRecord(title, name, address, mobileNumber);
        assertTrue(recordList.contains(expectedRecord));

        // Test removing a contact record
        model.rmContactRecord(title, name, address, mobileNumber);
        recordList = model.getAllRecords();
        Assertions.assertFalse(recordList.contains(expectedRecord));
    }

    @Test
    void modifyContactRecordTest() {
        String title = "Work";
        String name = "John Doe";
        String address = "123 Main St";
        String mobileNumber = "9876543210";
        String newTitle = "Job";
        String newName = "John Chan";
        String newAddress = "345 Main St";
        String newMobileNumber = "0123456789";
        PersonalInformationRecord record = new ContactRecord(title, name, address, mobileNumber);
        PersonalInformationRecord expectedRecord = new ContactRecord(newTitle, newName, newAddress, newMobileNumber);
        model.contactRecordModifier(record,newTitle, newName,newAddress,newMobileNumber);
        assertTrue(record.equals(expectedRecord));
    }

    @Test
    void modifyEventRecordTest() {
        String title = "Meeting";
        String description = "Team meeting";
        LocalDateTime startTime = LocalDateTime.of(2022, 6, 30, 10, 30);
        boolean hasAlarm = true;
        String newTitle = "Gathering";
        String newDescription = "Zoom meeting";
        LocalDateTime newStartTime = LocalDateTime.of(2024, 8, 2, 13, 33);
        boolean newHasAlarm = false;
        PersonalInformationRecord record = new EventRecord(title, description, startTime, hasAlarm);
        PersonalInformationRecord expectedRecord = new EventRecord(newTitle, newDescription, newStartTime, newHasAlarm);
        model.eventRecordModifier(record,newTitle,newDescription,newStartTime,newHasAlarm);
        assertTrue(record.equals(expectedRecord));
    }

    @Test
    void modifyPlainTextRecordTest() {
        String title = "XYZ";
        String content = "Lorem ipsum";
        String newTitle = "ABC";
        String newContent = "John Chan";
        PersonalInformationRecord record = new PlainTextRecord(title, content);
        PersonalInformationRecord expectedRecord = new PlainTextRecord(newTitle, newContent);
        model.plainTextRecordModifier(record, newTitle, newContent);
        assertTrue(record.equals(expectedRecord));
    }

    @Test
    void modifyTaskRecordTest() {
        String title = "ABC";
        String description = "DEF";
        LocalDateTime dateTime = LocalDateTime.of(2001, 12, 25, 12, 12);
        String newTitle = "123";
        String newDescription = "456";
        LocalDateTime newDateTime = LocalDateTime.of(2003, 2, 27, 23, 17);
        // Test creating a task record
        PersonalInformationRecord record = new TaskRecord(title, description, dateTime);
        PersonalInformationRecord expectedRecord = new TaskRecord(newTitle, newDescription, newDateTime);
        model.taskRecordModifier(record,newTitle,newDescription,newDateTime);
        assertTrue(record.equals(expectedRecord));
    }

    @Test
    void storeAndLoadTest(){
        String fileName = "testcase.pim";
        String plaintTestTitle = "XYZ";
        String plaintextContent = "Lorem ipsum";
        model.createPlainTextRecord(plaintTestTitle,plaintextContent);
        PersonalInformationRecord expectedPlainTextRecord = new PlainTextRecord(plaintTestTitle, plaintextContent);
        String TaskTitle = "ABC";
        String TaskDescription = "DEF";
        LocalDateTime TaskDateTime = LocalDateTime.of(2001, 12, 25, 12, 12);
        model.createTaskRecord(TaskTitle, TaskDescription, TaskDateTime);
        PersonalInformationRecord expectedTaskRecord = new TaskRecord(TaskTitle, TaskDescription, TaskDateTime);
        String eventTitle = "Meeting";
        String eventDescription = "Team meeting";
        LocalDateTime eventStartTime = LocalDateTime.of(2022, 6, 30, 10, 30);
        boolean eventHasAlarm = true;
        model.createEventRecord(eventTitle, eventDescription, eventStartTime, eventHasAlarm);
        PersonalInformationRecord expectedEventRecord = new EventRecord(eventTitle, eventDescription, eventStartTime, eventHasAlarm);
        String contactTitle = "Work";
        String contactName = "John Doe";
        String contactAddress = "123 Main St";
        String contactMobileNumber = "9876543210";
        model.createContactRecord(contactTitle, contactName, contactAddress, contactMobileNumber);
        PersonalInformationRecord expectedContactRecord = new ContactRecord(contactTitle, contactName, contactAddress, contactMobileNumber);
        model.storedRecordList(fileName);
        File f = new File(fileName);
        assertTrue(f.exists() && !f.isDirectory());

        model.rmPlainTextRecord(plaintTestTitle,plaintextContent);
        model.rmTaskRecord(TaskTitle, TaskDescription, TaskDateTime);
        model.rmEventRecord(eventTitle, eventDescription, eventStartTime, eventHasAlarm);
        model.rmContactRecord(contactTitle, contactName, contactAddress, contactMobileNumber);
        try {
            model.loadRecord(fileName);
        } catch (FileNotFoundException e) {
            Assertions.fail("File not found");
        }
        List<PersonalInformationRecord> recordList = model.getAllRecords();
        System.out.println(recordList.contains(expectedPlainTextRecord));
        System.out.println(recordList.contains(expectedTaskRecord));
        System.out.println(recordList.contains(expectedEventRecord));
        System.out.println(recordList.contains(expectedContactRecord));
        assertTrue(recordList.contains(expectedPlainTextRecord)&&
                recordList.contains(expectedTaskRecord)&&
                recordList.contains(expectedEventRecord)&&
                recordList.contains(expectedContactRecord));
    }

    @Test
    void searchTest(){
        String plaintTestTitle = "XYZ";
        String plaintextContent = "Lorem ipsum";
        model.createPlainTextRecord(plaintTestTitle,plaintextContent);
        PersonalInformationRecord expectedPlainTextRecord = new PlainTextRecord(plaintTestTitle, plaintextContent);
        String TaskTitle = "ABC";
        String TaskDescription = "DEF";
        LocalDateTime TaskDateTime = LocalDateTime.of(2001, 12, 25, 12, 12);
        model.createTaskRecord(TaskTitle, TaskDescription, TaskDateTime);
        PersonalInformationRecord expectedTaskRecord = new TaskRecord(TaskTitle, TaskDescription, TaskDateTime);
        String eventTitle = "Meeting";
        String eventDescription = "Team meeting";
        LocalDateTime eventStartTime = LocalDateTime.of(2022, 6, 30, 10, 30);
        boolean eventHasAlarm = true;
        model.createEventRecord(eventTitle, eventDescription, eventStartTime, eventHasAlarm);
        PersonalInformationRecord expectedEventRecord = new EventRecord(eventTitle, eventDescription, eventStartTime, eventHasAlarm);
        String contactTitle = "Work";
        String contactName = "John Doe";
        String contactAddress = "123 Main St";
        String contactMobileNumber = "9876543210";
        model.createContactRecord(contactTitle, contactName, contactAddress, contactMobileNumber);
        PersonalInformationRecord expectedContactRecord = new ContactRecord(contactTitle, contactName, contactAddress, contactMobileNumber);
        List<PersonalInformationRecord> searchPlainTextRecord = model.searchPlainTextRecord(plaintTestTitle,plaintextContent);
        List<PersonalInformationRecord> searchTaskRecord = model.searchTaskRecord(TaskTitle, TaskDescription, TaskDateTime, '=');
        List<PersonalInformationRecord> searchEventRecord = model.searchEventRecords(eventTitle, eventDescription, eventStartTime, '=');
        List<PersonalInformationRecord> searchContactRecord = model.searchContactRecords(contactTitle, contactName, contactAddress, contactMobileNumber);
        assertTrue(searchContactRecord.contains(expectedContactRecord)&&
                searchEventRecord.contains(expectedEventRecord)&&
                searchTaskRecord.contains(expectedTaskRecord)&&
                searchPlainTextRecord.contains(expectedPlainTextRecord));
    }

    @Test
    void getRecordByTitleTest(){
        String plaintTestTitle = "XYZ";
        String plaintextContent = "Lorem ipsum";
        model.createPlainTextRecord(plaintTestTitle,plaintextContent);
        PersonalInformationRecord expectedPlainTextRecord = new PlainTextRecord(plaintTestTitle, plaintextContent);
        assertTrue(model.getRecordByTitle("XYZ").equals(expectedPlainTextRecord));
    }

    //Below test if for the matcher of search function
    @Test
    void singlePatternTest(){
        String pattern = "A";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void singleAndTrueTest(){
        String pattern = "A && B";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void singleAndFalseTest(){
        String pattern = "A && Z";
        String str = "ABCDEF";
        Assertions.assertFalse(Matcher.match(str, pattern));
    }

    @Test
    void singleOrTrueTest(){
        String pattern = "A || H";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void singleOrFalseTest(){
        String pattern = "G || H";
        String str = "ABCDEF";
        Assertions.assertFalse(Matcher.match(str, pattern));
    }

    @Test
    void negationTrueTest(){
        String pattern = "!P";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void negationCombinTest(){
        String pattern = "!P || !A &&D ||!F";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void emptyTrueTest(){
        String pattern = "";
        String str = "ABCDEF";
        assertTrue(Matcher.match(str, pattern));
    }

    @Test
    void combinTest(){
        String pattern = "B && A || G && !A";
        String str = "ABCDEF";
        Assertions.assertFalse(Matcher.match(str, pattern));
    }
}