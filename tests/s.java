import model.FileHandler;
import model.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FileHandler.getPropertiesPosFromFile;
import static model.FILE_PATHS.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerConstructorTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        String boardName = "Classic"; // Example board name
        int boardIndex = 0;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = FileHandler.getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
    }

    @Test
    void testPlayerRecordsConstructor() {
        // Prepare a sample record string
        String records = "Georgina,1,700,2,F,0,Central";

        // Create Player using records constructor
        Player player = new Player("PlayerName", gameBoard, records);

        // Assertions
        assertEquals("PlayerName", player.getName());
        assertEquals(1, player.getNumber());
        assertEquals(700, player.getMoney()); // Ensure this matches the record
        assertEquals(2, player.getPosition()); // Ensure this matches the record
        assertFalse(player.isJailed());
        assertEquals(-1, player.getJailCounter());
        assertFalse(player.isRetired());
    }

    @Test
    void testPlayerRecordsConstructorWithJail() {
        // Prepare a record string with player in jail
        String records = "PlayerName,1,1000,15,T,3,Central";

        // Create Player using records constructor
        Player player = new Player("PlayerName", gameBoard, records);

        // Assertions
        assertTrue(player.isJailed());
        assertEquals(2, player.getJailCounter()); // Constructor subtracts 1
    }

    @Test
    void testPlayerRetiredState() {
        // Prepare a record string with negative money
        String records = "PlayerName,1,-100,15,F,0,nil";

        // Create Player using records constructor
        Player player = new Player("PlayerName", gameBoard, records);

        // Assertions
        assertTrue(player.isRetired());
    }



}