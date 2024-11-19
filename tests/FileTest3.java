import model.FILE_PATHS;
import model.FileHandler;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static model.FILE_PATHS.*;

class FileTest3 {
    private static final String TEST_DIR = "test_files";
    private static final String TEST_PROPERTY_FILE = TEST_DIR + "/test_properties.txt";
    private static final String TEST_MAPPING_FILE = TEST_DIR + "/test_mapping.txt";
    private static final String TEST_GAMEBOARD_FILE = TEST_DIR + "/test_gameboard.txt";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

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
            writer.println("GO,Go");
            writer.println("CC,Community Chest");
        }

        // Create test gameboard file
        try (PrintWriter writer = new PrintWriter(TEST_GAMEBOARD_FILE)) {
            writer.println("Classic,GO,MA,CC,PP,BW");
            writer.println("Mini,GO,MA,PP");
        }
    }

    @BeforeEach
    void setUp() {
        // Set up output capture before each test
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out after each test
        System.setOut(standardOut);
    }

    @AfterAll
    static void cleanUpTestFiles() {
        new File(TEST_PROPERTY_FILE).delete();
        new File(TEST_MAPPING_FILE).delete();
        new File(TEST_GAMEBOARD_FILE).delete();
        new File(TEST_DIR).delete();
    }

    @Test
    void testGetClassNames() {
        String[] names = FileHandler.getClassNames(TEST_PROPERTY_FILE);

        // Verify the array is not null and has the correct size
        assertNotNull(names, "Names array should not be null");
        assertEquals(3, names.length, "Should have exactly 3 property names");

        // Verify the content of the array
        assertEquals("Boardwalk", names[0], "First property should be Boardwalk");
        assertEquals("Park Place", names[1], "Second property should be Park Place");
        assertEquals("Mediterranean Avenue", names[2], "Third property should be Mediterranean Avenue");
    }

    @Test
    void testGetClassNamesWithEmptyFile() throws IOException {
        // Create temporary empty file
        File emptyFile = new File(TEST_DIR + "/empty.txt");
        emptyFile.createNewFile();

        String[] names = FileHandler.getClassNames(TEST_DIR + "/empty.txt");

        // Verify empty array is returned
        assertNotNull(names, "Names array should not be null even for empty file");
        assertEquals(0, names.length, "Should have no names for empty file");

        // Clean up
        emptyFile.delete();
    }

    @Test
    void testLoadSquareMappings() {
        Map<String, String> mappings = FileHandler.loadSquareMappings(TEST_MAPPING_FILE);

        // Verify the map is not null and has the correct size
        assertNotNull(mappings, "Mappings should not be null");
        assertEquals(5, mappings.size(), "Should have exactly 5 mappings");

        // Verify specific mappings
        assertEquals("Boardwalk", mappings.get("BW"), "BW should map to Boardwalk");
        assertEquals("Park Place", mappings.get("PP"), "PP should map to Park Place");
        assertEquals("Mediterranean Avenue", mappings.get("MA"), "MA should map to Mediterranean Avenue");
        assertEquals("Go", mappings.get("GO"), "GO should map to Go");
        assertEquals("Community Chest", mappings.get("CC"), "CC should map to Community Chest");
    }

    @Test
    void testDisplayProperties() {
        // Call the method
        FileHandler.displayProperties(TEST_PROPERTY_FILE);

        // Get the output
        String output = outputStreamCaptor.toString().trim();

        // Verify header is present
        assertTrue(output.contains("Name"), "Output should contain 'Name' header");
        assertTrue(output.contains("Price"), "Output should contain 'Price' header");
        assertTrue(output.contains("Rent"), "Output should contain 'Rent' header");

        // Verify property information is present
        assertTrue(output.contains("Boardwalk"), "Output should contain 'Boardwalk'");
        assertTrue(output.contains("400"), "Output should contain Boardwalk's price");
        assertTrue(output.contains("50"), "Output should contain Boardwalk's rent");

        assertTrue(output.contains("Park Place"), "Output should contain 'Park Place'");
        assertTrue(output.contains("350"), "Output should contain Park Place's price");
        assertTrue(output.contains("35"), "Output should contain Park Place's rent");
    }

    @Test
    void testDisplayGameboards() {
        // Call the method
        FileHandler.displayGameboards();

        // Get the output
        String output = outputStreamCaptor.toString().trim();

        // Verify header is present
        assertTrue(output.contains("Gameboard Name"), "Output should contain 'Gameboard Name' header");

        // Verify gameboard information is present
        assertTrue(output.contains("Classic"), "Output should contain 'Classic' board");
        assertTrue(output.contains("Mini"), "Output should contain 'Mini' board");

        // Verify mapped square names are present
        assertTrue(output.contains("Go"), "Output should contain 'Go' square");
        assertTrue(output.contains("Boardwalk"), "Output should contain 'Boardwalk' square");
        assertTrue(output.contains("Park Place"), "Output should contain 'Park Place' square");
        assertTrue(output.contains("Mediterranean Avenue"), "Output should contain 'Mediterranean Avenue' square");
    }

    @Test
    void testDisplayGameboardsWithEmptyMapping() throws IOException {
        // Create temporary empty mapping file
        File emptyMappingFile = new File(TEST_DIR + "/empty_mapping.txt");
        emptyMappingFile.createNewFile();

        // Temporarily replace the mapping file path
        String originalPath = MAPPING_PATH;
        try {
            // Use reflection to modify the MAPPING_PATH constant
            java.lang.reflect.Field field = FILE_PATHS.class.getDeclaredField("MAPPING_PATH");
            field.setAccessible(true);
            field.set(null, TEST_DIR + "/empty_mapping.txt");

            // Call the method
            FileHandler.displayGameboards();

            // Get the output
            String output = outputStreamCaptor.toString().trim();

            // Verify appropriate message is displayed
            assertTrue(output.contains("No square mappings found"),
                    "Should indicate no mappings were found");

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to modify MAPPING_PATH for test");
        } finally {
            // Clean up
            emptyMappingFile.delete();
        }
    }
}