package view;


import controller.InputListener;
import model.utils.GAME_CONSTANTS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static model.FileHandler.getBoardDetails;
import static model.utils.FILE_PATHS.*;
import static model.FileHandler.*;

/**
 * Starting point of the code
 */
public class Application extends InputListener implements GAME_CONSTANTS {

    private static void terminate(){
        System.out.println("Exiting the game...");
        System.exit(0);
    }
    public static void main(String[] args){
        // Initialize and run the system
        boolean usesFile = false;
        boolean adminConfirm = false;

        boolean gotPlayerCount = false;
        boolean gotGameBoard = false;
        boolean confirmed = false;

        String selectedFile = "";

        int playerCount = -1;
        String[] playerNames = new String[0];
        int selectedBoardIndex = -1;
        String response;
        String selectedBoard = null;
        final int DEFAULT_NAMES_COUNT = 100;

        while(!confirmed){
            //User decides to build a game or terminate
            System.out.println("Building a game? (Type Y to build, type any key to terminate)");
            response = receivedResponse();
            if(response.equals("COMP3211")){
                adminConfirm = true;
                break;
            }else if(!(response.equals("Y") || response.equals("y"))){
                terminate();
            }

            //User decides to load existing game from file or not
            System.out.print("Load game from save slots? (Type Y to load a file, type any key to start from scratch)");
            if(yesResponse()){
                System.out.println("Please select a file to load:");
                List<String> files = listFiles(SAVE_PATH);
                if (files.isEmpty()) {
                    System.out.println("No save files found.");
                    return;
                }
                for (int i = 0; i < files.size(); i++) {
                    System.out.println((i + 1) + ". " + files.get(i)); // Displaying file names with numbers
                }
                int selectedFileIndex = -1;
                while(selectedFileIndex == -1) {
                    selectedFileIndex = InputListener.rangedIntegerResponse(1, files.size(), 0);
                }
                selectedFile = files.get(selectedFileIndex - 1); // Adjust index for 0-based list
                System.out.println("You selected: " + selectedFile);
                System.out.println("Confirm the settings? (Type Y to begin game, type any key to rebuild)");
                if (InputListener.yesResponse()) {
                    confirmed = true;
                    usesFile = true;
                }
            }else{
                //Creating new game, getting details
                System.out.println("Building a new game...");

                //Getting player count
                do{
                    //Asking for a valid player count
                    System.out.println("No. of players? ("+ MINIMUM_PLAYERS + "-" + MAXIMUM_PLAYERS + ")");
                    playerCount = rangedIntegerResponse(MINIMUM_PLAYERS,MAXIMUM_PLAYERS,0);
                    if(playerCount != -1){
                        gotPlayerCount = true;
                    }
                } while(!gotPlayerCount);

                System.out.println(playerCount + " players have been added to the game!");

                //Now that we know amount of players, initialize an array of String for player names storage
                playerNames = new String[playerCount];

                //User decides to randomly generate names or not
                System.out.println("Do you want to generate random names for players? (Type Y to generate, type any key to customize)");
                if(yesResponse()){
                    //Initialize an array of String for default names storage if random generation is picked
                    String[] defaultNames = new String[DEFAULT_NAMES_COUNT];
                    //Help user to randomly assign names for players
                    getLinesFromFile(defaultNames,NAME_PATH);
                    Random rand = new Random();
                    for(int i = 0; i < playerCount; i++){
                        int r = rand.nextInt(DEFAULT_NAMES_COUNT);
                        playerNames[i] = defaultNames[r];
                    }
                } else{
                    //Manual name inputs
                    for(int i = 0; i < playerCount; i++){
                        System.out.println("Input the name of Player " + (i + 1) + ":");
                        playerNames[i] = receivedResponse();
                    }
                }
                System.out.println("Names assigned!");


                //Loading gameboard map information

                int boardCount = getLinesCountFromFile(GAMEBOARD_PATH);
                String[][] mapsDetails = new String[boardCount][GAMEBOARD_SQUARES+1];
                getLinesOfWordsFromFile(mapsDetails,GAMEBOARD_PATH);

                //Gameboard selection
                do{
                    System.out.println("Select a game board type :");
                    for(int i = 0; i < boardCount; i++){
                        System.out.println("Board " + (i + 1) + ": " + mapsDetails[i][0]);
                    }
                    selectedBoardIndex = rangedIntegerResponse(1,boardCount,-1);
                    if(selectedBoardIndex != -1){
                        selectedBoard = mapsDetails[selectedBoardIndex][0];
                        gotGameBoard = true;
                    } else{
                        System.out.println("Invalid boards number. Try again.");
                    }
                } while(!gotGameBoard);

                //Confirmation
                displayPreviewDetails(playerCount, selectedBoardIndex,selectedBoard,playerNames);
                if(yesResponse()){
                    confirmed = true;
                }
            }
        }

        // goes to developer side
        if(adminConfirm){
            System.out.println("Welcome to development mode. You can now modify the game. (/help for Manual!)");
            AdminArea aa = new AdminArea();
            do{
                response = receivedResponse();
                aa.checkPrompt(response);
            } while(!response.equals(":q"));
        }else if (usesFile) {
            String[] fileData = getInfoFromSaveFile(selectedFile);

            // Assigning meaningful variable names
            String round = fileData[0];
            String turn = fileData[1];
            String numberOfPlayers = fileData[2];
            String plrNames = fileData[3];
            String gameboardName = fileData[4];

            // Extract player data based on the number of players
            int currentPlayer = Integer.parseInt(turn);
            int currentRound = Integer.parseInt(round);
            int gameBoardIndex = getGameboardIndex(gameboardName);
            int numPlayers = Integer.parseInt(numberOfPlayers);
            String[] playerDataArray = new String[numPlayers];

            // Collect player data
            for (int i = 0; i < numPlayers; i++) {
                playerDataArray[i] = fileData[5 + i]; // Assuming player data starts from index 5
            }
            Game game = new Game((currentPlayer-1),currentRound,gameboardName,gameBoardIndex,numPlayers,plrNames.split(","),playerDataArray);
            while(!game.reachEnd()){
                game.executeTurn();
            }
            System.out.println("The winner(s) of game is : " + game.winner() + ".");

        }else{
            //After instantiating everything, start the Game Object
            Game game = new Game(playerCount,playerNames,selectedBoard,selectedBoardIndex);
            while(!game.reachEnd()){
                game.executeTurn();
            }
            System.out.println("The winner(s) of game is : " + game.winner() + ".");
        }



        terminate();
    }

    private static void displayPreviewDetails(int numberOfPlayers, int selected, String selectedBoard, String[] names){
        System.out.println("There are " + numberOfPlayers + " players in the game.");

        String[] boardDetails = getBoardDetails(selected);
        for(int i = 0; i < numberOfPlayers; i++){
            System.out.println("Player " + (i + 1) + " : " + names[i]);
        }
        System.out.println("The board you selected : " + selectedBoard);
        System.out.println("And its details : ");
        for(int i = 0; i < GAMEBOARD_SQUARES; i++){
            System.out.println("Square " + (i + 1) + ": " + boardDetails[i]);
        }
        System.out.println("Confirm the settings? (Type Y to begin game, type any key to rebuild)");
    }
    private static List<String> listFiles(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        } else {
            System.out.println("The specified directory does not exist.");
        }
        return fileList;
    }
}
