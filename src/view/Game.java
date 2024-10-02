package view;

import model.FileHandler;
import model.utils.Dice;
import model.utils.GAME_CONSTANTS;
import model.utils.GameBoard;
import model.utils.Player;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static controller.InputListener.rangedIntegerResponse;
import static controller.InputListener.receivedResponse;
import static model.utils.FILE_PATHS.*;

public class Game extends FileHandler implements GAME_CONSTANTS {
    private GameBoard gb;
    private Dice dice;
    private Player[] players;
    private int round;
    private int currentPlayerIndex;
    public Game(int players, String[] names, String boardName, String[] boardDetails){
        this.dice = new Dice();
        this.players = new Player[players];
        for(int i = 0; i < players; i++){
            this.players[i] = new Player(names[i], (i+1));
        }
        this.currentPlayerIndex = 0;
        this.round = 1;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName,GAMEBOARD_PATH,PROPERTY_PATH,SQUARE_PATH);
        this.gb = new GameBoard(propertiesPositions, boardDetails);
    }
    public void executeTurn(){
        Player currentPlayer = players[currentPlayerIndex];
        boolean turnEnd = false;
        while(!turnEnd){
            int originalPosition = currentPlayer.getPosition();
            if(currentPlayer.isJailed()){
                currentPlayer.checkEffects(this.gb,originalPosition,originalPosition);
                currentPlayer.tryJailbreak(this.dice,this.gb);
                turnEnd = true;
            }else{
                System.out.println("Player " + currentPlayer.getNumber() + " (" + currentPlayer.getName() + "), what is your move.");
                System.out.print(currentPlayer.getStatus());
                System.out.print("1. Throw dice\t2. Check status\t3. Query\t4. Save game");
                String r = receivedResponse();
                if(r.equals("1")){
                    currentPlayer.useTurn(this.dice, this.gb);
                    turnEnd = true;
                }else if(r.equals("2")){
                    int foo = -1;
                    while(foo == -1){
                        int i = 0;
                        System.out.println("Whose status you want to check on?");
                        while(i < players.length){
                            System.out.print((i+1) + ". " + players[i++].getName() + "\t");
                        }
                        System.out.print((i+1) + ". All players");
                        foo = rangedIntegerResponse(1,players.length+1,0);
                        if(players.length+1 > foo && foo > -1){
                            System.out.print("\n" + players[foo-1].getName() + " :\n" + players[foo-1].getStatus() + "\n");
                        } else if(foo == players.length+1){
                            for(Player p : players){
                                System.out.println("\n" + "Player " + p.getNumber() + " : " + p.getName() + " :\n" + p.getStatus() + "\n");
                            }
                        }
                    }
                }
            }
        }
        // Round-robin handling
        if(this.currentPlayerIndex == this.players.length - 1){
            this.round++;
        }
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.length;
    }
    public boolean reachEnd(){
        return this.round >= MAXIMUM_ROUNDS;
    }
}
