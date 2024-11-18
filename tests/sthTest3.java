import model.utils.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class sthTest3 {
    // Since Square is abstract, we'll create a concrete implementation for testing
    private static class TestSquare extends Square {
        public TestSquare(int position, String name) {
            super(position, name);
        }

        @Override
        public String effectLine(Player player) {
            return player.getName() + " landed on " + name;
        }

        @Override
        public void affectPlayer(Player player) {
            // No effect for test purposes
        }

        @Override
        public String getStatus() {
            return position + ". " + name;
        }
    }

    @Test
    void testSquareConstructor() {
        TestSquare square = new TestSquare(5, "Test Square");

        assertEquals(5, square.getPosition());
        assertEquals("Test Square", square.getName());
    }

    @Test
    void testAbstractMethods() {
        GameBoard mockBoard = mock(GameBoard.class);
        Player testPlayer = new Player("TestPlayer", 1, mockBoard);
        TestSquare square = new TestSquare(5, "Test Square");

        assertEquals("TestPlayer landed on Test Square", square.effectLine(testPlayer));
        assertEquals("5. Test Square", square.getStatus());
    }
}