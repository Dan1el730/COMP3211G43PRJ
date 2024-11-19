import model.FILE_PATHS;
import model.FileHandler;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static model.FILE_PATHS.*;

public class FileTest4 {
    private static final String TEST_DIR = "savefiles/test_files";
    private static final String TEST_PROPERTY_FILE = TEST_DIR + "/test_properties.txt";
    private static final String TEST_MAPPING_FILE = TEST_DIR + "/test_mapping.txt";
    private static final String TEST_GAMEBOARD_FILE = TEST_DIR + "/test_gameboard.txt";
    private static final String TEST_SAVE_FILE = TEST_DIR + "/test_save.txt";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    static void setUpTestFiles() throws IOException {
        // Create test directory with 'savefiles' prefix
        new File(TEST_DIR).mkdirs();
        // If 'saves' directory is needed, ensure it's created under 'savefiles/test_files'
        new File(TEST_DIR + "/saves").mkdirs();

        // Create test property file
        try (PrintWriter writer = new PrintWriter(TEST_PROPERTY_FILE)) {
            writer.println("Boardwalk,400,50");
            writer.println("Park Place,350,35");
            writer.println("Mediterranean Avenue,60,2");
        }

        // Create test mapping file
        try (PrintWriter writer = new PrintWriter(TEST_MAPPING_FILE)) {
            writer.println("BW,Boardwalk");
            writer.println("PP,Park Place");
            writer.println("MA,Mediterranean Avenue");
            writer.println("GO,Go");
            writer.println("CC,Community Chest");
        }

        // Create test gameboard file
        try (PrintWriter writer = new PrintWriter(TEST_GAMEBOARD_FILE)) {
            writer.println("Classic,GO,MA,CC,PP,BW");
            writer.println("Mini,GO,MA,PP");
        }

        // Create test save file
        try (PrintWriter writer = new PrintWriter(TEST_SAVE_FILE)) {
            writer.println("Game Board: Classic");
            writer.println("Current Player: Player1");
            writer.println("Position: 3");
        }

        // Verify file creation
        assertTrue(new File(TEST_GAMEBOARD_FILE).exists(), "Gameboard file should exist");
        assertTrue(new File(TEST_SAVE_FILE).exists(), "Save file should exist");
        // Add similar assertions for other files if necessary
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @AfterAll
    static void cleanUpTestFiles() {
        new File(TEST_PROPERTY_FILE).delete();
        new File(TEST_MAPPING_FILE).delete();
        new File(TEST_GAMEBOARD_FILE).delete();
        new File(TEST_SAVE_FILE).delete();
        new File(TEST_DIR + "/saves").delete();
        new File(TEST_DIR).delete();
    }

    @Test
    void testGetLinesCountFromFile() {
        int count = FileHandler.getLinesCountFromFile(TEST_PROPERTY_FILE);
        assertEquals(3, count, "Should count 3 lines in property file");

        // Test with non-existent file
        count = FileHandler.getLinesCountFromFile("nonexistent.txt");
        assertEquals(0, count, "Should return 0 for non-existent file");
    }

    @Test
    void testGetLinesFromFile() {
        String[] lines = new String[3];
        FileHandler.getLinesFromFile(lines, TEST_PROPERTY_FILE);

        assertEquals("Boardwalk,400,50", lines[0], "First line should match");
        assertEquals("Park Place,350,35", lines[1], "Second line should match");
        assertEquals("Mediterranean Avenue,60,2", lines[2], "Third line should match");
    }

    @Test
    // HV PROBLEM
    void testGetLinesOfWordsFromFile() {
        String[][] words = new String[2][6]; // 2 rows, 5 columns for gameboard file
        FileHandler.getLinesOfWordsFromFile(words, TEST_GAMEBOARD_FILE);

        assertEquals("Classic", words[0][0], "First word of first line should be Classic");
        assertEquals("Mini", words[1][0], "First word of second line should be Mini");
    }

    @Test
    void testModifyPropertyValue() {
        // Create a temporary property file for modification
        File tempFile = new File(TEST_DIR + "/temp_properties.txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("TestProp,100,10");
        } catch (IOException e) {
            fail("Failed to create temporary file");
        }

        FileHandler handler = new FileHandler();
        handler.modifyPropertyValue("TestProp", "price", 200, tempFile.getPath());

        Integer newPrice = FileHandler.getPropertyAttributeValue("TestProp", "price", tempFile.getPath());
        assertEquals(200, newPrice, "Price should be updated to 200");

        tempFile.delete();
    }

    @Test
    void testGetPropertyAttributeValue() {
        Integer price = FileHandler.getPropertyAttributeValue("Boardwalk", "price", TEST_PROPERTY_FILE);
        assertEquals(400, price, "Should return correct price for Boardwalk");

        Integer rent = FileHandler.getPropertyAttributeValue("Park Place", "rent", TEST_PROPERTY_FILE);
        assertEquals(35, rent, "Should return correct rent for Park Place");

        // Test non-existent property
        assertNull(FileHandler.getPropertyAttributeValue("NonExistent", "price", TEST_PROPERTY_FILE));
    }

    @Test
    void testGetGameBoardContents() {
        String[] contents = FileHandler.getGameBoardContents("Classic", TEST_GAMEBOARD_FILE);
        assertNotNull(contents, "Contents should not be null");
        assertEquals(5, contents.length, "Classic board should have 5 squares");
        assertEquals("GO", contents[0], "First square should be GO");

        // Test non-existent board
        assertNull(FileHandler.getGameBoardContents("NonExistent", TEST_GAMEBOARD_FILE));
    }

    @Test
    void testGetInfoFromSaveFile() {
        File saveFile = new File(TEST_SAVE_FILE);
        assertTrue(saveFile.exists(), "Save file should exist");

        String[] info = FileHandler.getInfoFromSaveFile("test_files/test_save.txt");

        // Debugging: Print the contents of the info array
        if (info != null) {
            System.out.println("Info array length: " + info.length);
            for (int i = 0; i < info.length; i++) {
                System.out.println("info[" + i + "]: " + info[i]);
            }
        } else {
            System.out.println("Info array is null");
        }

        assertNotNull(info, "Save info should not be null");
        assertEquals("Classic", info[0], "Should get correct game board name");
        assertEquals("Player1", info[1], "Should get correct player name");
        assertEquals("3", info[2], "Should get correct position");
    }

    @Test
    void testGetGameboardIndex() {
        int index = FileHandler.getGameboardIndex("Classic");
        assertEquals(0, index, "Classic board should be at index 0");

        index = FileHandler.getGameboardIndex("NonExistent");
        assertEquals(-1, index, "Should return -1 for non-existent board");
    }

    @Test
    void testWriteNewProperty() {
        File tempPropFile = new File(TEST_DIR + "/temp_properties.txt");
        FileHandler.writeNewProperty("NewProperty", 150, 15, tempPropFile.getPath());

        Integer price = FileHandler.getPropertyAttributeValue("NewProperty", "price", tempPropFile.getPath());
        assertEquals(150, price, "New property should be written with correct price");

        tempPropFile.delete();
    }

    @Test
    void testWriteNewMapping() {
        File tempMapFile = new File(TEST_DIR + "/temp_mapping.txt");
        FileHandler.writeNewMapping("NP", "NewProperty", tempMapFile.getPath());

        Map<String, String> mappings = FileHandler.loadSquareMappings(tempMapFile.getPath());
        assertEquals("NewProperty", mappings.get("NP"), "New mapping should be written correctly");

        tempMapFile.delete();
    }
}