package model.utils;

public interface GAME_CONSTANTS {
    public static final int MINIMUM_PLAYERS = 2;
    public static final int MAXIMUM_PLAYERS = 6;
    public static final int PLAYER_INITIAL_MONEY = 1500;
    public static final int GO_INCOME = 1500;
    public static final int[] CHANCE_MONEY_CHANGES = {
            200, 190, 180, 170, 160, 150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10,
            -300, -290, -280, -270, -260, -250, -240, -230, -220, -210, -200, -190, -180, -170, -160,
            -150, -140, -130, -120, -110, -100, -90, -80, -70, -60, -50, -40, -30, -20, -10
    };
    public static final double INCOME_TAX_RATE = 0.1;
    public static final int JAIL_PENALTY = 150;
    public static final int PLAYER_INITIAL_POSITION = 1;
    public static final int DICE_FACES = 4;
    public static final int MAXIMUM_ROUNDS = 20;
    public static final int GAMEBOARD_SQUARES = 20;
    public static final int MINIMUM_PRICE = 1;
    public static final int MAXIMUM_PRICE = 2000;
    public static final int MINIMUM_RENT = 1;
    public static final int MAXIMUM_RENT = 200;
}
