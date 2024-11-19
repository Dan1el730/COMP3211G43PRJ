import model.utils.Chance;
import model.utils.GameBoard;
import model.utils.Go;
import model.utils.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FILE_PATHS.*;
import static model.FileHandler.getBoardDetails;
import static model.FileHandler.getPropertiesPosFromFile;
import static model.GAME_CONSTANTS.GO_INCOME;
import static model.GAME_CONSTANTS.PLAYER_INITIAL_MONEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GoTest {
    private Go goSquare;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        String boardName = "GoGoVan";
        int boardIndex = 1;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName, GAMEBOARD_PATH, PROPERTY_PATH, MAPPING_PATH);
        String[] squareNames = getBoardDetails(boardIndex);
        gameBoard = new GameBoard(propertiesPositions, squareNames);
        goSquare = new Go(1, "Go");
        player = new Player("Alice", 1, gameBoard);
    }

    @Test
    void testEffectLineWhenLandedOn() {
        player.setPosition(1); // Simulate landing on Chance
        String expected = "Alice just passed the Go square! $1500 salary received.";
        assertEquals(expected, goSquare.effectLine(player));
    }

    @Test
    void testAffect(){
        goSquare.affectPlayer(player);
        assertEquals(PLAYER_INITIAL_MONEY+GO_INCOME, player.getMoney());
    }


    @Test
    void testGetStatus() {
        String expectedStatus = "1. Go";
        assertEquals(expectedStatus, goSquare.getStatus());
    }
}