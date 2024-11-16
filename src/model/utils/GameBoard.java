package model.utils;

import model.FileHandler;

import java.util.ArrayList;
import java.util.List;

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
                    this.squares[i] = new Go(i+1, squareNames[i]);
                    break;
                case "Chance":
                    this.squares[i] = new Chance(i+1, squareNames[i]);
                    break;
                case "Income Tax":
                    this.squares[i] = new IncomeTax(i+1, squareNames[i]);
                    break;
                case "Free Parking":
                    this.squares[i] = new FreeParking(i+1, squareNames[i]);
                    break;
                case "Go To Jail":
                    this.squares[i] = new GoToJail(i+1, squareNames[i], ijjvIndex);
                    break;
                case "In Jail/Just Visiting":
                    this.squares[i] = new InJailJustVisiting(i+1, squareNames[i]);
                    break;
                default :
                    break;
            }
        }
        String[][] propInfo = new String[propertiesPositions.length][3];
        getLinesOfWordsFromFile(propInfo,PROPERTY_PATH);

        int i = 0;
        for(int pos : propertiesPositions) {
            this.squares[pos] = this.properties[i] = new Property(pos+1, squareNames[pos], Integer.parseInt(propInfo[i][1]), Integer.parseInt(propInfo[i++][2]));
        }
    }
    public Property getProperty(String name) {
        for(Property p : properties) {
            if(p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    public Square[] getSquares() {
        return this.squares;
    }
    public void executePassEffect(Player player, int position){
        Square currentSquare = this.squares[position-1];
        if(currentSquare.passEffective){
            System.out.println(currentSquare.effectLine(player));
            currentSquare.affectPlayer(player);
        }
    }
    public void executeLandEffect(Player player){
        Square currentSquare = this.squares[player.getPosition()-1];
        System.out.println(currentSquare.effectLine(player));
        currentSquare.affectPlayer(player);
    }
    public String allOwnerships(Player player) {
        List<String> propsList = new ArrayList<>();
        for (Property property : this.properties) {
            if (property.getOwner() != null) {
                if (property.getOwner().equals(player)) {
                    propsList.add(property.getName());
                }
            }
        }
        if (propsList.isEmpty()) {
            return "nil"; // or return "No properties owned" if preferred
        }

        return String.join(",", propsList);
    }
    public void removeAllOwnerships(Player player){
        for(Property property : this.properties){
            if (property.getOwner() != null) {
                if (property.getOwner().equals(player)) {
                    property.disown();
                }
            }
        }
    }
}