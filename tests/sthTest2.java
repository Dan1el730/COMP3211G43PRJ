import model.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;

class sthTest2 {
    private GameBoard gameBoard;
    private Player owner;
    private Player nonOwner;

    @BeforeEach
    void setUp() {
        String boardName = "Classic";
        int boardIndex = 0;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);

        owner = new Player("Owner", 1, gameBoard);
        nonOwner = new Player("Non-Owner", 2, gameBoard);
    }

    @Test
    void testInitialPropertyState() {
        // Find the first property on the board
        Property firstProperty = null;
        for (Square square : gameBoard.getSquares()) {
            if (square instanceof Property) {
                firstProperty = (Property) square;
                break;
            }
        }

        assertNotNull(firstProperty, "No properties found on the board");

        // Print property details for debugging
        System.out.println("Property Name: " + firstProperty.getName());

        // Verify basic property attributes
        assertNotNull(firstProperty.getName());
        assertFalse(firstProperty.isOwned());
        assertNull(firstProperty.getOwner());
    }

    @Test
    void testBeOwnedBy() {
        // Find the first property on the board
        Property firstProperty = null;
        for (Square square : gameBoard.getSquares()) {
            if (square instanceof Property) {
                firstProperty = (Property) square;
                break;
            }
        }

        assertNotNull(firstProperty, "No properties found on the board");

        firstProperty.beOwnedBy(owner);
        assertTrue(firstProperty.isOwned());
        assertEquals(owner, firstProperty.getOwner());
    }

    @Test
    void testEffectLines() {
        // Find the first property on the board
        Property firstProperty = null;
        for (Square square : gameBoard.getSquares()) {
            if (square instanceof Property) {
                firstProperty = (Property) square;
                break;
            }
        }

        assertNotNull(firstProperty, "No properties found on the board");

        // Test unowned property effect line
        String unownedEffectLine = firstProperty.effectLine(nonOwner);
        assertTrue(unownedEffectLine.contains("Do you want to buy"));

        // Own the property and test effect line
        firstProperty.beOwnedBy(owner);
        String ownedEffectLine = firstProperty.effectLine(nonOwner);
        assertTrue(ownedEffectLine.contains("owned by"));
    }
}