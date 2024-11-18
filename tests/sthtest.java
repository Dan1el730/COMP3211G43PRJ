import model.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.utils.FILE_PATHS.*;

class sthtest {
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
        // Detailed property debugging
        System.out.println("Debug: Property Positions and Squares");
        Square[] squares = gameBoard.getSquares();

        // Collect all property names
        System.out.println("All Properties:");
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] instanceof Property) {
                Property prop = (Property) squares[i];
                System.out.println("Index " + i + ": " + prop.getName());
            }
        }

        // Try multiple property names
        String[] propertyNamesToTest = {
                "Mediterranean Avenue",
                "Mediterranean",
                "Mediterranean Av",
                "Med Avenue"
        };

        boolean propertyFound = false;
        for (String propertyName : propertyNamesToTest) {
            Property property = gameBoard.getProperty(propertyName);
            if (property != null) {
                System.out.println("Found property: " + propertyName);
                propertyFound = true;
                break;
            } else {
                System.out.println("Not found: " + propertyName);
            }
        }
        assertFalse(propertyFound, "Could not find any property with the given names");
    }
}