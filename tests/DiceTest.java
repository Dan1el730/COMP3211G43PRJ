import model.utils.Dice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class DiceTest {

    private Dice dice;

    @BeforeEach
    public void setUp() {
        dice = new Dice(); // Initialize a new Dice instance before each test
    }

    @Test
    public void testInitialRoll() {
        // Test the initial roll value is between 1 and DICE_FACES
        int initialFace = dice.getNewFace();
        Assertions.assertTrue(initialFace >= 1 && initialFace <= Dice.DICE_FACES,
                "Initial roll should be between 1 and " + Dice.DICE_FACES);
    }

    @Test
    public void testGetNewFace() {
        // Test that getting a new face returns a value between 1 and DICE_FACES
        int newFace = dice.getNewFace();
        Assertions.assertTrue(newFace >= 1 && newFace <= Dice.DICE_FACES,
                "New face should be between 1 and " + Dice.DICE_FACES);
    }
}