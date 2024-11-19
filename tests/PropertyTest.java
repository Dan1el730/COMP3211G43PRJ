

import model.utils.GameBoard;
import model.utils.Player;
import model.utils.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.FILE_PATHS.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class PropertyTest {
    private Property property;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        // Assuming these methods are available and return appropriate values
        String boardName = "Classic"; // Example board name
        int boardIndex = 0; // Example board index
        int playerCount = 2; // Example player count
        String[] playerNames = {"Alice", "Bob"}; // Example player names

        // Initialize GameBoard with properties positions and details
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);

        // Initialize Property and Player
        property = new Property(1, "Park Place", 350, 50);
        player = new Player("Alice", 1, gameBoard);
    }

    private void simulateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @Test
    void testInitialOwnership() {
        assertFalse(property.isOwned());
        assertNull(property.getOwner());
    }

    @Test
    void testBeOwnedBy() {
        property.beOwnedBy(player);
        assertTrue(property.isOwned());
        assertEquals(player, property.getOwner());
    }

    @Test
    void testDisown() {
        property.beOwnedBy(player);
        property.disown();
        assertFalse(property.isOwned());
        assertNull(property.getOwner());
    }

    @Test
    void testEffectLineWhenOwned() {
        property.beOwnedBy(player);
        assertEquals("Alice just landed on the Park Place square, which is the owner of this property!", property.effectLine(player));
    }

    @Test
    void testEffectLineWhenNotOwned() {
        assertEquals("Alice just landed on the Park Place square! Do you want to buy Park Place for $350?\n(Y to buy /any key to decline)", property.effectLine(player));
    }

    @Test
    void testAffectPlayerToBuyProperty() {
        simulateUserInput("y");
        property.affectPlayer(player);
        assertTrue(property.isOwned());
        assertEquals(player, property.getOwner());
        assertEquals(1150, player.getMoney()); // 1500 - 350
    }

    @Test
    void testAffectPlayerToPayRent() {
        property.beOwnedBy(new Player("Bob", 2, gameBoard)); // Simulate ownership
        property.affectPlayer(player);
        assertEquals(1450, player.getMoney()); // Player should have paid rent
    }
}