import model.utils.InJailJustVisiting;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static model.utils.GAME_CONSTANTS.JAIL_PENALTY;
import static model.utils.GAME_CONSTANTS.PLAYER_INITIAL_MONEY;
import static org.junit.jupiter.api.Assertions.*;

class InJailJustVisitingTest {
    private InJailJustVisiting inJailSquare;
    private Player player;

    private void simulateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @BeforeEach
    void setUp() {
        inJailSquare = new InJailJustVisiting(3, "In Jail - Just Visiting");
        player = new Player("Bob", 1, null); // Initialize player with no game board for simplicity
    }

    @Test
    void testEffectLineWhenJailed() {
        player.jail();
        String expected = "Bob has been jailed in the In Jail - Just Visiting square! Make your choice.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(3); // Simulate landing on the InJail square
        String expected = "Bob just landed the In Jail - Just Visiting square! It was a visit.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }

    @Test
    void testEffectLineWhenPassed() {
        player.setPosition(4); // Simulate passing the InJail square
        String expected = "Bob just passed the In Jail - Just Visiting square! It was a visit.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }



    @Test
    void testAffectPlayerWhenNotJailed() {
        player.unjail(); // Ensure player is not jailed
        inJailSquare.affectPlayer(player);

        assertFalse(player.isJailed()); // Player should still be not jailed
        assertEquals(PLAYER_INITIAL_MONEY, player.getMoney()); // Money should remain unchanged
    }

    @Test
    void testGetStatus() {
        String expectedStatus = "3. In Jail - Just Visiting";
        assertEquals(expectedStatus, inJailSquare.getStatus());
    }
}