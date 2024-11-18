import model.utils.Dice;
import model.utils.GameBoard;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;
import static model.utils.GAME_CONSTANTS.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        // Example values for the game setup
        String boardName = "Classic"; // Substitute with actual board name
        int boardIndex = 0; // Substitute with actual board index

        // Initialize GameBoard with properties positions and details
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);

        // Initialize Player
        player = new Player("Alice", 1, gameBoard);
    }


    @Test
    void testInitialPlayerStatus() {
        assertEquals("Alice", player.getName());
        assertEquals(PLAYER_INITIAL_MONEY, player.getMoney());
        assertEquals(PLAYER_INITIAL_POSITION, player.getPosition());
        assertFalse(player.isJailed());
        assertFalse(player.isRetired());
    }

    @Test
    void testMove() {
        player.move(5);
        assertEquals((PLAYER_INITIAL_POSITION + 5) % GAMEBOARD_SQUARES, player.getPosition());
    }

    @Test
    void testAddAndReduceMoney() {
        player.addMoney(100);
        assertEquals(PLAYER_INITIAL_MONEY + 100, player.getMoney());

        player.reduceMoney(50);
        assertEquals(PLAYER_INITIAL_MONEY + 50, player.getMoney());
    }

    @Test
    void testCheckRetirement() {
        player.reduceMoney(2000); // Simulate debt
        player.checkRetirement();
        assertTrue(player.isRetired());
    }
    @Test
    void testJailFunctionality() {
        player.jail();
        assertTrue(player.isJailed());
        player.passJailTurn();
        assertEquals(JAIL_DAYS - 2, player.getJailCounter());
        player.unjail();
        assertFalse(player.isJailed());
    }

    @Test
    void testUseTurnWithSteps() {
        player.useTurn(3);
        assertEquals((PLAYER_INITIAL_POSITION + 3) % GAMEBOARD_SQUARES, player.getPosition());
    }

    // Ensure that checkEffects is invoked in a scenario
    @Test
    void testCheckEffects() {
        player.move(3); // Moves the player
        // Add assertions to validate the effects
    }
}