package controller;

import java.util.Scanner;

public class InputListener {
    //for initialization uses

    private static final Scanner inputTaker = new Scanner(System.in);
    public InputListener(){
        System.out.println("InputListener created");
    }
    public static String receivedResponse(){
        return inputTaker.nextLine();
    }
    public static boolean yesResponse(){
        String input = inputTaker.nextLine();
        return input.equals("y") || input.equals("Y");
    }
    public static int rangedIntegerResponse(int lowerBound, int upperBound, int offset){
        String input = inputTaker.nextLine();
        if(!(input.matches("[0-9]+"))){
            System.out.println("Input was not an integer. Try again.");
            return -1;
        }
        int foo = Integer.parseInt(input);
        if(upperBound >= foo && foo >= lowerBound){
            //Got valid input
            return foo + offset;
        } else{
            //Ask the user to retype a valid input
            System.out.println("Invalid numbers. (Not in range) Try again.");
            return -1;
        }
    }
    public static String fileNameResponse() {
        System.out.print("Enter your desired file name (without .txt): ");
        String filename = receivedResponse();

        // Validate the filename
        while (!isValidFilename(filename)) {
            System.out.print("Invalid filename. Please enter a valid file name (without .txt): ");
            filename = receivedResponse();
        }
        return filename + ".txt"; // Append .txt and return
    }

    private static boolean isValidFilename(String filename) {
        // Define illegal characters for filenames
        String illegalCharacters = "\\/:*?\"<>|"; // Characters not allowed in Windows file names
        for (char c : illegalCharacters.toCharArray()) {
            if (filename.indexOf(c) >= 0) {
                return false; // Found an illegal character
            }
        }
        return filename.length() > 0; // Ensure filename is not empty
    }
}
