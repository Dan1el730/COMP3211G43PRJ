package model.utils;

public class Player implements GAME_CONSTANTS {
    private String name;
    private int money;
    private int position;
    private int number;
    private boolean jailed;
    private int jailCounter;
    private GameBoard playingBoard;
    private boolean retired;
    public Player(String name, int number, GameBoard gameBoard) {
        this.name = name;
        this.money = PLAYER_INITIAL_MONEY;
        this.position = PLAYER_INITIAL_POSITION;
        this.number = number;
        this.jailed = false;
        this.jailCounter = -1;
        this.playingBoard = gameBoard;
        this.retired = false;
    }
    public Player(String[] fileRecord){

    }
    public int throwDice(Dice dice){
        return dice.getNewFace();
    }
    public String getName(){
        return this.name;
    }
    public int getMoney(){
        return this.money;
    }
    public int getPosition(){
        return this.position;
    }
    public int getNumber(){
        return this.number;
    }
    public boolean isJailed(){
        return this.jailed;
    }
    public boolean isRetired(){ return this.retired; }
    public String getStatus(){
        return "Current status :\tMoney : " + this.money +
                               "\tPosition : " + this.position +
                               "\tJailed : " + this.jailed +
                               "\tProperties owned : " + this.playingBoard.allOwnerships(this) +
                               "\n";
    }
    public String getInformation(){
        return  this.name + "," +
                this.number + "," +
                this.money + "," +
                this.position + "," +
                (this.jailed ? "T" : "F") + "," +
                (this.jailCounter+1) + "," +
                this.playingBoard.allOwnerships(this);
    }
    public void move(Dice dice){
        System.out.print(this.name + " just moved from " + this.position);
        this.position = this.position + throwDice(dice);
        if(this.position > GAMEBOARD_SQUARES){
            this.position-= GAMEBOARD_SQUARES;
        }
        System.out.println(" to " + this.position);
    }
    public void move(int steps){
        System.out.print(this.name + " just moved from " + this.position);
        this.position = (this.position + steps) % GAMEBOARD_SQUARES;
        System.out.println(" to " + this.position);
    }
    public void jail(){
        this.jailed = true;
        this.jailCounter = JAIL_DAYS - 1;
    }
    public void unjail(){
        this.jailed = false;
        this.jailCounter = -1;
    }
    public int getJailCounter(){
        return this.jailCounter;
    }
    public void passJailTurn(){
        this.jailCounter--;
    }
    public void setPosition(int newPosition){
        this.position = newPosition;
    }
    public void addMoney(int change){
        this.money += change;
    }
    public void reduceMoney(int change){
        this.money -= change;
    }
    public void useTurn(Dice dice){
        int originalPosition = this.position;
        this.move(dice);
        int newPosition = this.getPosition();
        this.checkEffects(originalPosition,newPosition);
    }
    public void useTurn(int steps){
        int originalPosition = this.position;
        this.move(steps);
        int newPosition = this.getPosition();
        this.checkEffects(originalPosition,newPosition);
    }
    public void checkEffects(int startPosition, int endPosition){
        if(this.jailed){
            //jailed player does not move, simply check current jail effect
            checkLandEffect();
        }else{
            int opi = startPosition - 1;
            int epi = endPosition - 1;
            // in general cases : backThreshold = 20 - 4 = 16, hence, when opi = {16,17,18,19} i.e. pos {17,18,19,20}
            int backThreshold = GAMEBOARD_SQUARES - DICE_FACES;
            // in general cases : frontThreshold = 4 - 1 = 3, hence, when epi = {0,1,2,3} i.e. pos {1,2,3,4}
            int frontThreshold = DICE_FACES - 1;
            //If player starts at 17/18/19/20 and lands on 1/2/3/4 , the epi should be +20 to handle the logic
            if(opi >= backThreshold && epi <= frontThreshold){
                epi += GAMEBOARD_SQUARES;
            }
            for(int i = opi + 1; i < epi; i++){
                //used modular, for all passing positions check
                int position = (i % GAMEBOARD_SQUARES) + 1;
                checkPassEffect(position);
            }
            checkLandEffect();
        }
    }
    private void checkPassEffect(int position){
        this.playingBoard.executePassEffect(this, position);
    }
    private void checkLandEffect(){
        this.playingBoard.executeLandEffect(this);
    }
    public void checkRetirement(){
        if(this.money < 0){
            System.out.println(this.name + " just retired, the player is out!");
            this.retired = true;
            playingBoard.removeAllOwnerships(this);
        }
    }
}