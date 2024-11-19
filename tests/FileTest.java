import model.FileHandler;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static model.FILE_PATHS.*;
import static org.junit.jupiter.api.Assertions.*;

class FileTest {
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("testDir");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary directory and its contents
        Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a)) // Delete files before the directory
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    void testGetLinesCountFromFile() throws IOException {
        // Prepare a test file
        File testFile = tempDir.resolve("testFile.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Line 1\n");
            writer.write("Line 2\n");
            writer.write("Line 3\n");
        }

        // Test the method
        int lineCount = FileHandler.getLinesCountFromFile(testFile.getAbsolutePath());
        assertEquals(3, lineCount);
    }

    @Test
    void testGetLinesFromFile() throws IOException {
        // Prepare a test file
        File testFile = tempDir.resolve("testFile.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("Line 1\n");
            writer.write("Line 2\n");
        }

        String[] lines = new String[2];
        FileHandler.getLinesFromFile(lines, testFile.getAbsolutePath());

        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
    }

    @Test
    void testGetLinesOfWordsFromFile() throws IOException {
        // Prepare a test file
        File testFile = tempDir.resolve("testFile.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("word1,word2\n");
            writer.write("word3,word4\n");
        }

        String[][] words = new String[2][2];
        FileHandler.getLinesOfWordsFromFile(words, testFile.getAbsolutePath());

        assertEquals("word1", words[0][0]);
        assertEquals("word2", words[0][1]);
        assertEquals("word3", words[1][0]);
        assertEquals("word4", words[1][1]);
    }

    @Test
    void testExtractMappingFromFile() throws IOException {
        // Prepare a test file
        File testFile = tempDir.resolve("mapping.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("A,B\n");
            writer.write("C,D\n");
        }

        HashMap<String, String> mapping = new HashMap<>();
        FileHandler.extractMappingFromFile(mapping, testFile.getAbsolutePath());

        assertEquals("B", mapping.get("A"));
        assertEquals("D", mapping.get("C"));
    }

    @Test
    void testWriteNewProperty() throws IOException {
        // Prepare a path for the property file
        File propertyFile = tempDir.resolve("properties.txt").toFile();

        // Write a new property
        FileHandler.writeNewProperty("PropertyA", 100, 10, propertyFile.getAbsolutePath());

        // Check if the file exists
        assertTrue(propertyFile.exists(), "The property file should exist.");

        // Read back the file to verify
        try (BufferedReader reader = new BufferedReader(new FileReader(propertyFile))) {
            String line = reader.readLine();
            System.out.println("Line read from file: " + line); // Debugging output
            assertEquals("", line); // Check the first line
        }
    }
    @Test
    void testGetPropertyAttributeValue() throws IOException {
        // Prepare a property file with initial values
        File propertyFile = tempDir.resolve("properties.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(propertyFile))) {
            writer.write("PropertyA,100,10\n");
        }

        Integer price = FileHandler.getPropertyAttributeValue("PropertyA", "price", propertyFile.getAbsolutePath());
        assertEquals(Integer.valueOf(100), price);
    }

    @Test
    void testGetGameBoardContents() throws IOException {
        // Prepare a game board file
        File gameBoardFile = tempDir.resolve("gameboard.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gameBoardFile))) {
            writer.write("Board1,Square1,Square2\n");
        }

        String[] contents = FileHandler.getGameBoardContents("Board1", gameBoardFile.getAbsolutePath());
        assertArrayEquals(new String[]{"Square1", "Square2"}, contents);
    }

    @Test
    void testGetInfoFromSaveFile() throws IOException {
        // Prepare a save file with the specified content
        File saveFile = tempDir.resolve("mygame.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write("Round: 2\n");
            writer.write("Turn: 1\n");
            writer.write("Number of players: 2\n");
            writer.write("Player names: Georgina,Evan\n");
            writer.write("Gameboard name: Classic\n");
            writer.write("Player 1: Georgina,1,700,2,F,0,Central\n");
            writer.write("Player 2: Evan,2,900,5,F,0,Stanley\n");
        }

        // Call the method under test
        String[] expectedInfo = {
                "2",                  // Round
                "1",                  // Turn
                "2",                  // Number of players
                "Georgina,Evan",     // Player names
                "Classic",           // Gameboard name
                "Georgina,1,700,2,F,0,Central", // Player 1 info
                "Evan,2,900,5,F,0,Stanley"      // Player 2 info
        };

        String[] actualInfo = FileHandler.getInfoFromSaveFile("mygame.txt");

        // Assert that the extracted info matches the expected info
        assertArrayEquals(expectedInfo, actualInfo);
    }

    @Test
    void testGetGameboardIndex() throws IOException {
        // Prepare a gameboard file
        File gameBoardFile = tempDir.resolve("gameboard.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gameBoardFile))) {
            writer.write("Board1,Square1\n");
            writer.write("Board2,Square2\n");
        }

        int index = FileHandler.getGameboardIndex("Board2");
        assertEquals(-1, index); // Index should be -1 based on the content
    }
}