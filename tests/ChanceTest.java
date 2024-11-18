import model.utils.Chance;
import model.utils.GameBoard;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;
import static model.utils.GAME_CONSTANTS.*;
import static org.junit.jupiter.api.Assertions.*;

class ChanceTest {
    private Chance chanceSquare;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        String boardName = "ChanceTaker";
        int boardIndex = 1; // Example index for the chance board
        // Initialize GameBoard and Chance square
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
        chanceSquare = new Chance(1, "Chance");
        player = new Player("Alice", 1, gameBoard);
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(1); // Simulate landing on Chance
        String expected = "Alice just landed on the Chance square!";
        assertEquals(expected, chanceSquare.effectLine(player));
    }



    @Test
    void testGetStatus() {
        String expectedStatus = "1. Chance";
        assertEquals(expectedStatus, chanceSquare.getStatus());
    }
}