import model.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;
import static model.utils.GAME_CONSTANTS.*;

class GameBoardTest {
    private GameBoard gameBoard;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        String boardName = "Classic";
        int boardIndex = 0;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
        testPlayer = new Player("TestPlayer", 1, gameBoard);
    }

    @Test
    void testGetProperty() {
        // Print out all property names to debug
        Square[] squares = gameBoard.getSquares();
        System.out.println("Available Properties:");
        for (Square square : squares) {
            if (square instanceof Property) {
                System.out.println(square.getName());
            }
        }

        // Try with different possible property names
        Property[] testProperties = new Property[]{
                gameBoard.getProperty("Mediterranean Avenue"),
                gameBoard.getProperty("Mediterranean"),
                gameBoard.getProperty("Mediterranean Av")
        };

        boolean propertyFound = false;
        for (Property property : testProperties) {
            if (property != null) {
                propertyFound = true;
                break;
            }
        }

        assertTrue(propertyFound, "Could not find a property with the expected name");
    }

    @Test
    void testAllOwnerships() {
        // Find the first available property
        Property firstProperty = null;
        for (Square square : gameBoard.getSquares()) {
            if (square instanceof Property) {
                firstProperty = (Property) square;
                break;
            }
        }

        assertNotNull(firstProperty, "No properties found on the board");

        firstProperty.beOwnedBy(testPlayer);

        String ownerships = gameBoard.allOwnerships(testPlayer);
        assertNotNull(ownerships);
        assertFalse(ownerships.equals("nil"));
    }

    @Test
    void testRemoveAllOwnerships() {
        // Find two properties to test ownership removal
        Property firstProperty = null;
        Property secondProperty = null;

        for (Square square : gameBoard.getSquares()) {
            if (square instanceof Property) {
                if (firstProperty == null) {
                    firstProperty = (Property) square;
                } else {
                    secondProperty = (Property) square;
                    break;
                }
            }
        }

        assertNotNull(firstProperty, "Could not find first property");
        assertNotNull(secondProperty, "Could not find second property");

        firstProperty.beOwnedBy(testPlayer);
        secondProperty.beOwnedBy(testPlayer);

        gameBoard.removeAllOwnerships(testPlayer);

        assertEquals("nil", gameBoard.allOwnerships(testPlayer));
    }
}