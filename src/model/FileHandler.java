package model;

import model.utils.GAME_CONSTANTS;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class FileHandler implements GAME_CONSTANTS {

    //get number of lines of a file
    public static int getLinesCountFromFile(String filepath){
        int i = 0;
        try{
            File nameFile = new File(filepath);
            Scanner r = new Scanner(nameFile);
            while(r.hasNextLine()){
                i++;
                r.nextLine();
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return i;
    }

    //get every line of content store in String-array S
    public static void getStringFromFile(String[] S, String filepath){
        try{
            int i = 0;
            File nameFile = new File(filepath);
            Scanner r = new Scanner(nameFile);
            while(r.hasNextLine()){
                String s = r.nextLine();
                S[i++] = s;
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    //get every word of every line of content, separated by comma, stored in String 2d array S
    public static void getStringsFromFile(String[][] S, String filepath){
        if(S.length == 0){
            return;
        }
        try{
            int i = 0;
            File nameFile = new File(filepath);
            Scanner r = new Scanner(nameFile);
            while(r.hasNextLine()){
                String[] s = r.nextLine().split(",");
                System.arraycopy(s, 0, S[i], 0, S[0].length);
                i++;
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    //extract comma-separated two Strings mappings from file
    public static void getMappingFromFile(HashMap<String, String> mapping, String filepath){
        try{
            File mappingFile = new File(filepath);
            Scanner r = new Scanner(mappingFile);
            while(r.hasNextLine()){
                String[] m = r.nextLine().split(",");
                mapping.put(m[0], m[1]);
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    //a very specific method to extract the positions (starting from 0) of property squares for a specific board
    public static int[] getPropertiesPosFromFile(String boardName, String boardpath, String proppath, String mappath){
        String[] boardData;
        String[] boardInformation = new String[GAMEBOARD_SQUARES];
        String[] properties = new String[GAMEBOARD_SQUARES];
        HashMap<String, String> squareNames = new HashMap<String,String>();
        getMappingFromFile(squareNames, mappath);
        int i = 0;
        int j = 0;
        int count = 0;
        int[] positions;


        try{
            //First find the desired board and its data

            File boardFile = new File(boardpath);
            Scanner r1 = new Scanner(boardFile);
            while (r1.hasNextLine()) {
                boardData = r1.nextLine().split(",");
                if (boardData[0].equals(boardName)) {
                    System.arraycopy(boardData, 1, boardInformation, 0, boardData.length - 1);
                    break; // Exit the loop if the match is found
                }
            }

            //Second import all existing property names

            File propFile = new File(proppath);
            Scanner r2 = new Scanner(propFile);
            while(r2.hasNextLine()){
                String property = r2.nextLine().split(",")[0];

                properties[i++] = property;
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }

        //count how many properties are in the board
        for(String square : boardInformation){
            for(String prop : properties){
                if(squareNames.get(square).equals(prop)){
                    count++;
                    break;
                }
            }
        }
        positions = new int[count];
        i = 0;

        //mark all positions of those properties
        for(String square : boardInformation){
            for(String prop : properties){
                if(squareNames.get(square).equals(prop)){
                    positions[j++] = i;
                    break;
                }
            }
            i++;
        }
        return positions;
    }

    public static void writeNewProperty(String propertyName, int propertyPrice, int propertyRent, String proppath) {
        // Open the BufferedWriter in append mode
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(proppath, true))) { // true for append mode
            writer.newLine();
            writer.write(propertyName + "," + propertyPrice + "," + propertyRent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
