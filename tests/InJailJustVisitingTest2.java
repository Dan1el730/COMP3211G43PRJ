import model.utils.InJailJustVisiting;
import model.utils.Player;
import model.utils.Dice;
import model.utils.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import static model.GAME_CONSTANTS.JAIL_PENALTY;
import static model.GAME_CONSTANTS.PLAYER_INITIAL_MONEY;
import static controller.InputListener.receivedResponse;
import static controller.InputListener.yesResponse;
import static org.junit.jupiter.api.Assertions.*;

class InJailJustVisitingTest2 {
    private InJailJustVisiting inJailSquare;
    private Player player;
    private Dice mockDice;
    private GameBoard mockGameBoard;

    private void simulateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @BeforeEach
    void setUp() throws Exception {
        inJailSquare = new InJailJustVisiting(3, "In Jail - Just Visiting");

        // Create mock GameBoard
        mockGameBoard = mock(GameBoard.class);
        // Don't do anything in executePassEffect to avoid null pointer issues
        doNothing().when(mockGameBoard).executePassEffect(any(Player.class), anyInt());

        player = new Player("Bob", 1, mockGameBoard);

        // Create and inject mock dice
        mockDice = mock(Dice.class);
        Field diceField = InJailJustVisiting.class.getDeclaredField("jailDice");
        diceField.setAccessible(true);
        diceField.set(inJailSquare, mockDice);
    }

    @Test
    void testEffectLineWhenJailed() {
        player.jail();
        String expected = "Bob has been jailed in the In Jail - Just Visiting square! Make your choice.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(3);
        String expected = "Bob just landed the In Jail - Just Visiting square! It was a visit.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }

    @Test
    void testEffectLineWhenPassed() {
        player.setPosition(4);
        String expected = "Bob just passed the In Jail - Just Visiting square! It was a visit.";
        assertEquals(expected, inJailSquare.effectLine(player));
    }

    @Test
    void testJailedPlayerPaysFine() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            mockedStatic.when(controller.InputListener::yesResponse).thenReturn(true);
            when(mockDice.getNewFace()).thenReturn(3); // Will move 3 spaces

            int initialMoney = player.getMoney();

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertFalse(player.isJailed());
            assertEquals(initialMoney - JAIL_PENALTY, player.getMoney());
            verify(mockDice).getNewFace();
            verify(mockGameBoard).executePassEffect(eq(player), anyInt());
        }
    }

    @Test
    void testJailedPlayerRollsSameFaces() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            mockedStatic.when(controller.InputListener::yesResponse).thenReturn(false);
            mockedStatic.when(controller.InputListener::receivedResponse).thenReturn("");
            when(mockDice.getNewFace()).thenReturn(6, 6); // Both dice roll 6

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertFalse(player.isJailed());
            assertEquals(PLAYER_INITIAL_MONEY, player.getMoney());
            verify(mockDice, times(2)).getNewFace();
            verify(mockGameBoard).executePassEffect(eq(player), eq(12)); // 6 + 6 = 12
        }
    }

    @Test
    void testJailedPlayerRollsDifferentFaces() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            mockedStatic.when(controller.InputListener::yesResponse).thenReturn(false);
            mockedStatic.when(controller.InputListener::receivedResponse).thenReturn("");
            when(mockDice.getNewFace()).thenReturn(6, 5);

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertTrue(player.isJailed());
            assertEquals(1, player.getJailCounter());
            assertEquals(PLAYER_INITIAL_MONEY, player.getMoney());
            verify(mockDice, times(2)).getNewFace();
            verify(mockGameBoard, never()).executePassEffect(any(Player.class), anyInt());
        }
    }

    @Test
    void testForcedPaymentAfterThreeFailedAttempts() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            player.passJailTurn(); // First failed attempt
            player.passJailTurn(); // Second failed attempt

            mockedStatic.when(controller.InputListener::yesResponse).thenReturn(false);
            mockedStatic.when(controller.InputListener::receivedResponse).thenReturn("");
            when(mockDice.getNewFace()).thenReturn(6, 5);

            int initialMoney = player.getMoney();

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertFalse(player.isJailed());
            assertEquals(initialMoney - JAIL_PENALTY, player.getMoney());
            verify(mockDice, times(2)).getNewFace();
            verify(mockGameBoard).executePassEffect(eq(player), eq(11)); // 6 + 5 = 11
        }
    }

    @Test
    void testInsufficientMoneyForFine() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            player.reduceMoney(player.getMoney() - (JAIL_PENALTY - 1));

            mockedStatic.when(controller.InputListener::receivedResponse).thenReturn("");
            when(mockDice.getNewFace()).thenReturn(6, 5);

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertTrue(player.isJailed());
            verify(mockDice, times(2)).getNewFace();
            verify(mockGameBoard, never()).executePassEffect(any(Player.class), anyInt());
        }
    }

    @Test
    void testRetirementAfterForcedPayment() {
        try (MockedStatic<controller.InputListener> mockedStatic = mockStatic(controller.InputListener.class)) {
            // Setup
            player.jail();
            player.reduceMoney(player.getMoney() - JAIL_PENALTY);
            player.passJailTurn();
            player.passJailTurn();

            mockedStatic.when(controller.InputListener::yesResponse).thenReturn(false);
            mockedStatic.when(controller.InputListener::receivedResponse).thenReturn("");
            when(mockDice.getNewFace()).thenReturn(6, 5);

            // Execute
            inJailSquare.affectPlayer(player);

            // Verify
            assertTrue(player.isRetired());
            assertEquals(0, player.getMoney());
            verify(mockDice, times(2)).getNewFace();
            // Should not execute pass effect if retired
            verify(mockGameBoard, never()).executePassEffect(any(Player.class), anyInt());
        }
    }

    @Test
    void testGetStatus() {
        String expectedStatus = "3. In Jail - Just Visiting";
        assertEquals(expectedStatus, inJailSquare.getStatus());
    }
}