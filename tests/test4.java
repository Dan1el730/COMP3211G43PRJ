import model.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;

class test4 {
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
    void testPropertyOwnership() {
        // Find the first property on the board that isn't null
        Property firstProperty = findFirstProperty();

        assertNotNull(firstProperty, "No valid properties found on the board");

        // Verify initial state
        assertFalse(firstProperty.isOwned());
        assertNull(firstProperty.getOwner());

        // Test ownership
        firstProperty.beOwnedBy(owner);
        assertTrue(firstProperty.isOwned());
        assertEquals(owner, firstProperty.getOwner());
    }

    private Property findFirstProperty() {
        Square[] squares = gameBoard.getSquares();

        // Debug print all squares
        System.out.println("Total squares: " + squares.length);
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] != null) {
                System.out.println("Square " + i + ": " + squares[i].getName() +
                        " (Type: " + squares[i].getClass().getSimpleName() + ")");
            }
        }

        // Find first Property square
        for (Square square : squares) {
            if (square instanceof Property) {
                return (Property) square;
            }
        }
        return null;
    }
}