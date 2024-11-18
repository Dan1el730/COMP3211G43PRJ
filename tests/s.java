import model.utils.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerConstructorTest {
    @Test
    void testPlayerRecordsConstructor() {
        // Mock GameBoard
        GameBoard mockGameBoard = mock(GameBoard.class);

        // Prepare a sample record string
        String records = "Georgina,1,700,2,F,0,Central";

        // Create Player using records constructor
        Player player = new Player("PlayerName", mockGameBoard, records);

        // Assertions
        assertEquals("PlayerName", player.getName());
        assertEquals(1, player.getNumber());
        assertEquals(1500, player.getMoney());
        assertEquals(10, player.getPosition());
        assertFalse(player.isJailed());
        assertEquals(-1, player.getJailCounter());
        assertFalse(player.isRetired());
    }

    @Test
    void testPlayerRecordsConstructorWithJail() {
        // Mock GameBoard
        GameBoard mockGameBoard = mock(GameBoard.class);

        // Prepare a record string with player in jail
        String records = "PlayerName,1,1000,15,T,3,Mediterranean Avenue";

        // Create Player using records constructor
        Player player = new Player("PlayerName", mockGameBoard, records);

        // Assertions
        assertTrue(player.isJailed());
        assertEquals(2, player.getJailCounter()); // Remember, constructor subtracts 1
    }

    @Test
    void testPlayerRetiredState() {
        // Mock GameBoard
        GameBoard mockGameBoard = mock(GameBoard.class);

        // Prepare a record string with negative money
        String records = "PlayerName,1,-100,15,F,0,";

        // Create Player using records constructor
        Player player = new Player("PlayerName", mockGameBoard, records);

        // Assertions
        assertTrue(player.isRetired());
    }

    @Test
    void testThrowDice() {
        // Mock GameBoard and Dice
        GameBoard mockGameBoard = mock(GameBoard.class);
        Dice mockDice = mock(Dice.class);

        // Prepare mock behavior
        when(mockDice.getNewFace()).thenReturn(4);

        // Create Player
        Player player = new Player("PlayerName", 1, mockGameBoard);

        // Test throwDice method
        int diceResult = player.throwDice(mockDice);

        // Assertion
        assertEquals(4, diceResult);

        // Verify dice method was called
        verify(mockDice).getNewFace();
    }
}