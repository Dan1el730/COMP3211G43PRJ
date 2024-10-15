package view;

import model.FileHandler;
import model.utils.GAME_CONSTANTS;

import static controller.InputListener.rangedIntegerResponse;
import static controller.InputListener.receivedResponse;
import static model.utils.FILE_PATHS.PROPERTY_PATH;

public class AdminArea extends FileHandler implements GAME_CONSTANTS {
    //allow admin to obtain variable/ file changes
    public AdminArea() {

    }
    public void checkPrompt(String input){
        String name;
        switch(input){
            case "create property":
                System.out.println("What is the name of the property?");
                name = receivedResponse();
                boolean gotPrice = false;
                int price = -1;
                do{
                    //Asking for a valid player count
                    System.out.println("What is the price of the property? ("+ MINIMUM_PRICE + "-" + MAXIMUM_PRICE + ")");
                    price = rangedIntegerResponse(MINIMUM_PRICE,MAXIMUM_PRICE,0);
                    if(price != -1){
                        gotPrice = true;
                    }
                } while(!gotPrice);
                boolean gotRent = false;
                int rent = -1;
                do{
                    //Asking for a valid player count
                    System.out.println("What is the rent of the property? ("+ MINIMUM_RENT + "-" + MAXIMUM_RENT + ")");
                    rent = rangedIntegerResponse(MINIMUM_RENT,MAXIMUM_RENT,0);
                    if(rent != -1){
                        gotRent = true;
                    }
                } while(!gotRent);
                writeNewProperty(name,price,rent,PROPERTY_PATH);
                break;
            case "create gameboard":
                System.out.println("What is the name of this brand new gameboard?");
                name = receivedResponse();

                break;

            default:
                System.out.println("Command not found.");
                break;
        }
    }
}
