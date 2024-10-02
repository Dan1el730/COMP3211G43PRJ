package model.utils;

import model.FileHandler;

import java.util.Arrays;

import static model.utils.FILE_PATHS.PROPERTY_PATH;

public class GameBoard extends FileHandler implements GAME_CONSTANTS{
    private Property[] properties;
    private Square[] squares;
    public GameBoard(int[] propertiesPositions, String[] squareNames) {
        this.properties = new Property[propertiesPositions.length];
        this.squares = new Square[GAMEBOARD_SQUARES];
        int ijjvIndex = -1;
        //First identify where InJail / JustVisiting board is.
        for(int i = 0; i < GAMEBOARD_SQUARES; i++) {
            if(squareNames[i].equals("In Jail/Just Visiting")){
                ijjvIndex = i;
                break;
            }
        }
        //Assign all non-property squares
        for(int i = 0; i < GAMEBOARD_SQUARES; i++) {
            switch(squareNames[i]){
                case "Go":
                    squares[i] = new Go(i+1, squareNames[i]);
                    break;
                case "Chance":
                    squares[i] = new Chance(i+1, squareNames[i]);
                    break;
                case "Income Tax":
                    squares[i] = new IncomeTax(i+1, squareNames[i]);
                    break;
                case "Free Parking":
                    squares[i] = new FreeParking(i+1, squareNames[i]);
                    break;
                case "Go To Jail":
                    squares[i] = new GoToJail(i+1, squareNames[i], ijjvIndex);
                    break;
                case "In Jail/Just Visiting":
                    squares[i] = new InJailJustVisiting(i+1, squareNames[i]);
                    break;
                default :
                    break;
            }
        }
        String[][] propInfo = new String[propertiesPositions.length][3];
        getStringsFromFile(propInfo,PROPERTY_PATH);

        int i = 0;
        for(int pos : propertiesPositions) {
            squares[pos] = properties[i] = new Property(pos+1, squareNames[pos], Integer.parseInt(propInfo[i][1]), Integer.parseInt(propInfo[i++][2]));
        }
    }
    public void executePassEffect(Player player, int position){
        Square currentSquare = squares[position-1];
        if(currentSquare.passEffective){
            System.out.println(currentSquare.effectLine(player));
            currentSquare.affectPlayer(player);
        }
    }
    public void executeLandEffect(Player player){
        Square currentSquare = squares[player.getPosition()-1];
        System.out.println(currentSquare.effectLine(player));
        currentSquare.affectPlayer(player);
    }
}