import model.utils.Chance;
import model.utils.GameBoard;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FILE_PATHS.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.GAME_CONSTANTS.CHANCE_MONEY_CHANGES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.Random;

public class ChanceTest2 {
    private Chance chanceSquare;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        String boardName = "ChanceTaker";
        int boardIndex = 1;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
        chanceSquare = new Chance(1, "Chance");
        player = new Player("Alice", 1, gameBoard);
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(1);
        String expected = "Alice just landed on the Chance square!";
        assertEquals(expected, chanceSquare.effectLine(player));
    }

    @Test
    void testGetStatus() {
        String expectedStatus = "1. Chance";
        assertEquals(expectedStatus, chanceSquare.getStatus());
    }

    @Test
    void testAffectPlayerWithPositiveChange() throws Exception {
        // Set up a mock Random to return a specific index for a positive amount
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0); // Assume first element is positive

        // Inject mock Random into chance square
        Field rngField = Chance.class.getDeclaredField("rng");
        rngField.setAccessible(true);
        rngField.set(chanceSquare, mockRandom);

        int initialMoney = player.getMoney();
        chanceSquare.affectPlayer(player);

        assertTrue(player.getMoney() > initialMoney);
    }

    @Test
    void testAffectPlayerWithNegativeChange() throws Exception {
        // Set up mock Random to return an index for a negative amount
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(CHANCE_MONEY_CHANGES.length - 1); // Assume last element is negative

        // Inject mock Random
        Field rngField = Chance.class.getDeclaredField("rng");
        rngField.setAccessible(true);
        rngField.set(chanceSquare, mockRandom);

        int initialMoney = player.getMoney();
        chanceSquare.affectPlayer(player);

        assertTrue(player.getMoney() < initialMoney);
    }

    @Test
    void testPlayerRetirementCheck() throws Exception {
        // Set up mock Random to return a large negative amount
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(CHANCE_MONEY_CHANGES.length - 1);

        // Inject mock Random
        Field rngField = Chance.class.getDeclaredField("rng");
        rngField.setAccessible(true);
        rngField.set(chanceSquare, mockRandom);

        // Set player's money to minimum amount
        player = spy(player);
        player.reduceMoney(player.getMoney() - 1); // Leave player with 1 dollar

        chanceSquare.affectPlayer(player);

        // Verify that checkRetirement was called
        verify(player).checkRetirement();
    }
}