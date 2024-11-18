import model.utils.FreeParking;
import model.utils.GameBoard;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;
import static model.utils.GAME_CONSTANTS.*;
import static org.junit.jupiter.api.Assertions.*;

class FreeParkingTest {
    private FreeParking freeParking;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        String boardName = "Parking";
        int boardIndex = 2;
        // Initialize GameBoard and FreeParking square
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
        freeParking = new FreeParking(0, "Free Parking");
        player = new Player("Alice", 1, gameBoard);
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(0); // Simulate landing on Free Parking
        String expected = "Alice just landed the Free Parking square! Nothing happened.";
        assertEquals(expected, freeParking.effectLine(player));
    }

    @Test
    void testEffectLineWhenPassed() {
        player.setPosition(1); // Simulate passing Free Parking
        String expected = "Alice just passed the Free Parking square! Nothing happened.";
        assertEquals(expected, freeParking.effectLine(player));
    }

    @Test
    void testAffectPlayer() {
        // FreeParking should not affect the player
        freeParking.affectPlayer(player);
        assertEquals(PLAYER_INITIAL_MONEY, player.getMoney()); // Money should remain unchanged
        assertEquals(PLAYER_INITIAL_POSITION, player.getPosition()); // Position should remain unchanged
    }

    @Test
    void testGetStatus() {
        String expectedStatus = "0. Free Parking";
        assertEquals(expectedStatus, freeParking.getStatus());
    }
}