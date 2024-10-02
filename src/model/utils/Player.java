package model.utils;

import view.Game;

import static controller.InputListener.receivedResponse;
import static controller.InputListener.yesResponse;

public class Player implements GAME_CONSTANTS {
    private String name;
    private int money;
    private Property[] ownedProperties;
    private int position;
    private int number;
    private boolean jailed;
    private int jailCounter;
    public Player(String name, int number) {
        this.name = name;
        this.money = PLAYER_INITIAL_MONEY;
        this.position = PLAYER_INITIAL_POSITION;
        this.number = number;
        this.jailed = false;
        this.jailCounter = -1;
        //logic to init properties array
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
    public String getStatus(){
        return "Current status :\tMoney : " + this.money + "\tPosition : " + this.position + "\tJailed : " + this.jailed + "\n";
    }
    public String getDetails(){
        return "Player " + this.number + " (" + this.name + ") , Money : " + this.money;
    }
    public void move(Dice dice){
        System.out.print(this.name + " just moved from " + this.position);
        this.position = this.position + throwDice(dice);
        if(this.position > 20){
            this.position-= 20;
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
        this.jailCounter = 2;
    }
    public int getJailCounter(){
        return this.jailCounter;
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
    public void useTurn(Dice dice, GameBoard gb){
        int originalPosition = this.position;
        this.move(dice);
        int newPosition = this.getPosition();
        this.checkEffects(gb,originalPosition,newPosition);
    }
    public void useTurn(int steps, GameBoard gb){
        int originalPosition = this.position;
        this.move(steps);
        int newPosition = this.getPosition();
        this.checkEffects(gb,originalPosition,newPosition);
    }
    public void checkEffects(GameBoard gameBoard, int startPosition, int endPosition){
        int opi = startPosition - 1;
        int epi = endPosition - 1;
        if(opi >= 16 && epi <= 3){
            epi += 20;
        }
        for(int i = opi + 1; i < epi; i++){
            int position = (i % 20) + 1;
            checkPassEffect(gameBoard,position);
        }
        checkLandEffect(gameBoard);
    }
    private void checkPassEffect(GameBoard gameBoard, int position){
        gameBoard.executePassEffect(this, position);
    }
    private void checkLandEffect(GameBoard gameBoard){
        gameBoard.executeLandEffect(this);
    }
    public void tryJailbreak(Dice dice, GameBoard gb){
        if(this.jailCounter < 2){
            System.out.println("An offer to pay $" + JAIL_PENALTY + " fine instead of betting two same faces, yield? (Y/ any key)");
            if(yesResponse()){
                this.reduceMoney(JAIL_PENALTY);
                this.jailed = false;
                this.jailCounter = -1;
                this.useTurn(dice, gb);
            }
        }
        int originalPosition = this.getPosition();
        System.out.println("Attempt to throw first dice. (Press any key)");
        receivedResponse();
        int firstFace = dice.getNewFace();
        System.out.println("The face was " + firstFace + "\n");
        System.out.println("Attempt to throw second dice. (Press any key)");
        receivedResponse();
        int secondFace = dice.getNewFace();
        System.out.println("The face was " + secondFace + "\n");
        if(firstFace == secondFace){
            System.out.println("The faces are same, you succeeded in getting out of jail.");
            this.jailed = false;
            this.jailCounter = -1;
            this.useTurn(firstFace+secondFace, gb);
        }else{
            System.out.println("The faces are not same, you are still jailed for " + this.jailCounter + " turns.");
            this.jailCounter--;
        }
        if(this.jailCounter == -1){
            System.out.println("You are forced to pay a fine of $" + JAIL_PENALTY + " to get out of jail and move this turn according to two dices you threw.");
            this.reduceMoney(JAIL_PENALTY);
            this.jailed = false;
            this.jailCounter = -1;
            this.useTurn(firstFace+secondFace, gb);
        }
    }
}