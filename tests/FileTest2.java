import model.FileHandler;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static model.FILE_PATHS.*;
import static model.GAME_CONSTANTS.*;

class FileHandlerTest {
    private static final String TEST_DIR = "test_files";
    private static final String TEST_PROPERTY_FILE = TEST_DIR + "/test_properties.txt";
    private static final String TEST_MAPPING_FILE = TEST_DIR + "/test_mapping.txt";
    private static final String TEST_GAMEBOARD_FILE = TEST_DIR + "/test_gameboard.txt";

    @BeforeAll
    static void setUpTestFiles() throws IOException {
        // Create test directory
        new File(TEST_DIR).mkdirs();

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
        }

        // Create test gameboard file
        try (PrintWriter writer = new PrintWriter(TEST_GAMEBOARD_FILE)) {
            writer.println("Classic,GO,MA,CC,PP,IT,RR1,BW,CH");
            writer.println("Mini,GO,MA,PP,BW");
        }
    }

    @AfterAll
    static void cleanUpTestFiles() {
        new File(TEST_PROPERTY_FILE).delete();
        new File(TEST_MAPPING_FILE).delete();
        new File(TEST_GAMEBOARD_FILE).delete();
        new File(TEST_DIR).delete();
    }

    @Test
    void testGetLinesCountFromFile() {
        int count = FileHandler.getLinesCountFromFile(TEST_PROPERTY_FILE);
        assertEquals(3, count, "Should count exactly 3 lines in test property file");
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
    void testGetLinesOfWordsFromFile() {
        String[][] words = new String[2][8];  // Match the Classic board size
        FileHandler.getLinesOfWordsFromFile(words, TEST_GAMEBOARD_FILE);
        assertEquals("GO", words[0][0], "First word of first line should be GO");
        assertEquals("MA", words[0][1], "Second word of first line should be MA");
        assertEquals("GO", words[1][0], "First word of second line should be GO");
    }

    @Test
    void testExtractMappingFromFile() {
        HashMap<String, String> mapping = new HashMap<>();
        FileHandler.extractMappingFromFile(mapping, TEST_MAPPING_FILE);
        assertEquals("Boardwalk", mapping.get("BW"), "BW should map to Boardwalk");
        assertEquals("Park Place", mapping.get("PP"), "PP should map to Park Place");
        assertEquals(3, mapping.size(), "Should have exactly 3 mappings");
    }

    @Test
    void testGetBoardDetails() {
        String[] details = FileHandler.getBoardDetails(0); // Testing Classic board
        assertNotNull(details, "Board details should not be null");
        assertEquals(GAMEBOARD_SQUARES, details.length, "Should have correct number of squares");
    }

    @Test
    void testGetPropertiesPosFromFile() {
        int[] positions = FileHandler.getPropertiesPosFromFile(
                "Classic",
                TEST_GAMEBOARD_FILE,
                TEST_PROPERTY_FILE,
                TEST_MAPPING_FILE
        );
        assertNotNull(positions, "Positions array should not be null");
        assertTrue(positions.length > 0, "Should find at least one property position");
        assertEquals(1, positions[0], "First property should be at position 1 (MA)");
    }

    @Test
    void testWriteNewProperty() {
        FileHandler.writeNewProperty("Test Property", 200, 20, TEST_PROPERTY_FILE);
        List<String> lines = readAllLines(TEST_PROPERTY_FILE);
        String lastLine = lines.get(lines.size() - 1);
        assertEquals("Test Property,200,20", lastLine, "New property should be written correctly");
    }

    @Test
    void testWriteNewMapping() {
        FileHandler.writeNewMapping("TP", "Test Property", TEST_MAPPING_FILE);
        List<String> lines = readAllLines(TEST_MAPPING_FILE);
        String lastLine = lines.get(lines.size() - 1);
        assertEquals("TP,Test Property", lastLine, "New mapping should be written correctly");
    }

    @Test
    void testGetPropertyAttributeValue() {
        Integer price = FileHandler.getPropertyAttributeValue("Boardwalk", "price", TEST_PROPERTY_FILE);
        Integer rent = FileHandler.getPropertyAttributeValue("Boardwalk", "rent", TEST_PROPERTY_FILE);

        assertEquals(400, price, "Should return correct price for Boardwalk");
        assertEquals(50, rent, "Should return correct rent for Boardwalk");
    }

    @Test
    void testGetGameBoardContents() {
        String[] contents = FileHandler.getGameBoardContents("Classic", TEST_GAMEBOARD_FILE);
        assertNotNull(contents, "Contents should not be null");
        assertEquals("GO", contents[0], "First square should be GO");
        assertEquals("MA", contents[1], "Second square should be MA");
    }

    @Test
    void testModifyPropertyValue() {
        FileHandler fileHandler = new FileHandler();
        fileHandler.modifyPropertyValue("Boardwalk", "price", 450, TEST_PROPERTY_FILE);

        Integer newPrice = FileHandler.getPropertyAttributeValue("Boardwalk", "price", TEST_PROPERTY_FILE);
        assertEquals(450, newPrice, "Price should be updated to 450");
    }

    // Helper method to read all lines from a file
    private List<String> readAllLines(String filepath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}