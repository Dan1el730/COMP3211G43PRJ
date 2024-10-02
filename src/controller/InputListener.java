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
}
