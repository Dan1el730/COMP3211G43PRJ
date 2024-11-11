package model.utils;

import java.util.Random;

public class Dice implements GAME_CONSTANTS {
    private int face;
    private final Random roller = new Random();
    public Dice() {
        this.roll();
    }
    private void roll(){
        this.face = roller.nextInt(DICE_FACES) + 1;
    }
    public int getNewFace(){
        this.roll();
        return this.face;
    }
}
