package view;

import model.FileHandler;
import model.utils.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static controller.InputListener.*;
import static model.utils.FILE_PATHS.*;

public class Game extends FileHandler implements GAME_CONSTANTS {
    private int currentPlayerIndex;
    private int round;
    private GameBoard gb;
    private String gameBoardName;
    private Dice dice;
    private Player[] players;
    private String[] playerNames;

    public Game(int players, String[] names, String boardName, int boardIndex){
        this.currentPlayerIndex = 0;
        this.round = 1;
        int[] propertiesPositions = getPropertiesPosFromFile(boardName,GAMEBOARD_PATH,PROPERTY_PATH,SQUARE_PATH);
        this.gb = new GameBoard(propertiesPositions, getBoardDetails(boardIndex));
        this.gameBoardName = boardName;
        this.dice = new Dice();
        this.players = new Player[players];
        this.playerNames = new String[players];
        for(int i = 0; i < players; i++){
            this.players[i] = new Player(names[i], (i+1), this.gb);
            this.playerNames[i] = names[i];
        }
    }
    public void executeTurn(){
        Player currentPlayer = players[currentPlayerIndex];
        boolean turnEnd = false;
        boolean quitting = false;
        while(!turnEnd){
            int originalPosition = currentPlayer.getPosition();
            if(currentPlayer.isRetired()){
                System.out.println("Rest in peace Player " + currentPlayer.getNumber() + " (" + currentPlayer.getName() + ")");
                turnEnd = true;
            }else if(currentPlayer.isJailed()){
                currentPlayer.checkEffects(originalPosition,originalPosition);
                turnEnd = true;
            }else{
                showPlayerChoices(currentPlayer);
                String r = receivedResponse();
                if(r.equals("1")){
                    currentPlayer.useTurn(this.dice);
                    turnEnd = true;
                }else if(r.equals("2")){
                    handleStatusQueries();
                }else if(r.equals("3")){
                    System.out.println("I'm querying!");
                }else if(r.equals("4")){
                    System.out.println("Attempt to save the game?");
                    int foo = -1;
                    while(foo == -1){
                        System.out.print("1. Save the game and exit immediately\n" +
                                         "2. Save the game and proceed current game afterwards\n" +
                                         "3. Cancel the save action and proceed the game");
                        foo = rangedIntegerResponse(1,3,0);
                        if(foo == 1){
                            saveGameStateToFile();
                            quitting = true;
                            turnEnd = true;
                        } else if(foo == 2){
                            saveGameStateToFile();
                        } else if(foo == 3){
                            System.out.println("Game proceeded without saving.");
                        }
                    }
                }
            }
        }
        if(quitting){
            System.exit(0);
        }
        if(this.currentPlayerIndex == this.players.length - 1){
            this.round++;
        }
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.length;
    }
    private int remainingPlayer(){
        int numberOfPlayers = 0;
        for(Player p : players){
            if(!p.isRetired()){
                numberOfPlayers++;
            }
        }
        return numberOfPlayers;
    }
    public boolean reachEnd(){
        return this.round >= MAXIMUM_ROUNDS || remainingPlayer() <= 1;
    }

    //methods that are used even more internally

    private void showPlayerChoices(Player currentPlayer){
        System.out.println("Player " + currentPlayer.getNumber() + " (" + currentPlayer.getName() + "), what is your move.");
        System.out.print(currentPlayer.getStatus());
        System.out.print("1. Throw dice\t2. Check status\t3. Query\t4. Save game");
    }
    private void handleStatusQueries(){
        int foo = -1;
        while(foo == -1){
            int i = 0;
            System.out.println("Whose status you want to check on?");
            while(i < players.length){
                System.out.print((i+1) + ". " + players[i++].getName() + "\t");
            }
            System.out.print((i+1) + ". All players" + "\t");
            System.out.print((i+2) + ". Game status");
            foo = rangedIntegerResponse(1,players.length+2,0);
            if(players.length+1 > foo && foo > -1){
                System.out.print("\n" + players[foo-1].getName() + " :\n" + players[foo-1].getStatus() + "\n");
            } else if(foo == players.length+1){
                for(Player p : players){
                    System.out.println("\n" + "Player " + p.getNumber() + " : " + p.getName() + " :\n" + p.getStatus() + "\n");
                }
            } else if(foo == players.length+2){
                System.out.println("\n\nCurrent gameboard status: ");
                for(Square square : this.gb.getSquares()){
                    boolean hasPlayerThere = false;
                    System.out.println(square.getStatus());
                    System.out.print("Players there :\t");
                    for(Player p : players){
                        if(p.getPosition() == square.getPosition()){
                            hasPlayerThere = true;
                            System.out.print(p.getName() + "\t");
                        }
                    }
                    if(!hasPlayerThere){
                        System.out.print("nil");
                    }
                    System.out.println();
                }
            }
        }
    }
    private void saveGameStateToFile(){
        File saveDirectory = new File(SAVE_PATH);
        if(!saveDirectory.exists()){
            saveDirectory.mkdirs(); // Create the directory if it does not exist
        }
        String filename;
        System.out.println("Customize the file name? (Type Y to customize, type any key for a default name)");
        if(yesResponse()){
            filename = fileNameResponse();
        }else{
            filename = generateDefaultFilename(saveDirectory);
        }
        File saveFile = new File(saveDirectory, filename);
        try(PrintWriter writer = new PrintWriter(new FileWriter(saveFile))){
            writer.println("Round: " + this.round);
            writer.println("Turn: " + (this.currentPlayerIndex+1));
            writer.println("Number of players: " + this.players.length);
            writer.println("Player names: " + Arrays.toString(this.playerNames).replaceAll("[\\[\\] ]", ""));
            writer.println("Gameboard name: " + this.gameBoardName);
            for(Player player : this.players){
                writer.println("Player " + player.getNumber() + ": " + player.getInformation());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }
    private String generateDefaultFilename(File directory) {
        int fileCount = directory.listFiles((dir, name) -> name.startsWith("save") && name.endsWith(".txt")).length;
        return "save" + (fileCount + 1) + ".txt"; // Create filename like save1.txt, save2.txt, etc.
    }
}
