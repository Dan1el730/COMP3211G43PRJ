package view;


import controller.InputListener;
import model.utils.GAME_CONSTANTS;

import java.util.HashMap;
import java.util.Random;

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
    /**
     * @param args we dont take them, dont give any.
     */
    public static void main(String[] args){
        // Initialize and run the system
        boolean adminConfirm = false;
        boolean gotPlayerCount = false;
        boolean gotGameBoard = false;
        boolean confirmed = false;

        int playerCount = -1;
        String[] playerNames = new String[0];
        int selectedBoardIndex = -1;
        String response;
        String selectedBoard = null;
        String[] details = new String[GAMEBOARD_SQUARES];

        final int DEFAULT_NAMES_COUNT = 100;

        while(!confirmed){
            //User decides to build a game or terminate
            System.out.println("Building a game? (Type Y to build, type any key to terminate)");
            System.out.print("You can as well enter the admin code to do changes, fellow coders.");
            response = receivedResponse();
            if(response.equals("11037")){
                adminConfirm = true;
                break;
            }else if(!(response.equals("Y") || response.equals("y"))){
                terminate();
            }

            //User decides to load existing game from file or not
            System.out.print("Load game from save slots? (Type Y to load a file, type any key to start from scratch)");
            response = receivedResponse();
            if(response.equals("Y") || response.equals("y")){
                //Loading logic
                System.out.println("Confirm the settings? (Type Y to begin game, type any key to rebuild)");
                if(yesResponse()){
                    confirmed = true;
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

                //Getting names for players

                //User decides to randomly generate names or not
                System.out.println("Do you want to generate random names for players? (Type Y to generate, type any key to customize)");
                if(yesResponse()){
                    //Initialize an array of String for default names storage if random generation is picked
                    String[] defaultNames = new String[DEFAULT_NAMES_COUNT];
                    //Help user to randomly assign names for players
                    getStringFromFile(defaultNames,NAME_PATH);
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
                HashMap<String, String> squareNames = new HashMap<String,String>();
                getMappingFromFile(squareNames, SQUARE_PATH);
                int boardCount = getLinesCountFromFile(GAMEBOARD_PATH);
                String[][] mapsDetails = new String[boardCount][GAMEBOARD_SQUARES+1];
                getStringsFromFile(mapsDetails,GAMEBOARD_PATH);

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
                System.out.println("There are " + playerCount + " players in the game.");
                for(int i = 0; i < playerCount; i++){
                    System.out.println("Player " + (i + 1) + " : " + playerNames[i]);
                }
                System.out.println("The board you selected : " + selectedBoard);
                System.out.println("And its details : ");
                for(int i = 0; i < GAMEBOARD_SQUARES; i++){
                    details[i] = squareNames.get(mapsDetails[selectedBoardIndex][i+1]);
                    System.out.println("Board " + (i + 1) + ": " + details[i] + " ");
                }
                System.out.println("Confirm the settings? (Type Y to begin game, type any key to rebuild)");
                if(yesResponse()){
                    confirmed = true;
                }
            }
        }


        if(adminConfirm){
            AdminArea aa = new AdminArea();
            do{
                response = receivedResponse();
                aa.checkPrompt(response);
            } while(!response.equals("11037"));
        }else{
            //After instantiating everything, start the Game Object
            Game game = new Game(playerCount,playerNames,selectedBoard,details);
            while(!game.reachEnd()){
                game.executeTurn();
            }
        }

        terminate();
    }
}
