package view;

import model.FileHandler;
import model.utils.GAME_CONSTANTS;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static controller.InputListener.rangedIntegerResponse;
import static controller.InputListener.receivedResponse;
import static model.utils.FILE_PATHS.*;

public class AdminArea extends FileHandler implements GAME_CONSTANTS {
    //allow admin to obtain variable/ file changes
    private String[][] tempGameboards;
    private int pointer;
    private String[] validAbbrs;
    public AdminArea() {
        this.tempGameboards = new String[5][21];
        this.pointer = 0;
        loadValidAbbreviations();
    }
    public void checkPrompt(String input){
        String name;
        boolean doneCommand = false;
        String[] parts = input.split(" ");
        String[] currentPropertiesNames = getClassNames(PROPERTY_PATH);
        String[] currentGameboardNames = getClassNames(GAMEBOARD_PATH);
        if (parts[0].equals("save") && parts.length > 1) {
            String tempBoardName = parts[1];

            // Check if the tempBoardName exists in the current gameboards
            if (!isTempBoardNameValid(tempBoardName)) {
                System.out.println("Error: Tempboard name '" + tempBoardName + "' does not exist.");
                return;
            }

            // Get the index of the gameboard to save
            int index = getTempboardIndex(tempBoardName);
            if (index == -1) {
                System.out.println("Error: Gameboard '" + tempBoardName + "' not found.");
                return;
            }

            // Create the comma-separated string of the gameboard
            StringBuilder gameboardData = new StringBuilder();
            for (int i = 0; i < tempGameboards[index].length; i++) {
                gameboardData.append(tempGameboards[index][i]);
                if (i < tempGameboards[index].length - 1) {
                    gameboardData.append(",");
                }
            }

            // Save the gameboard to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAMEBOARD_PATH, true))) {
                writer.write(gameboardData.toString());
                writer.newLine(); // Add a newline after writing the gameboard
                System.out.println("Tempboard '" + tempBoardName + "' saved successfully.");
            } catch (IOException e) {
                System.err.println("Error saving the gameboard: " + e.getMessage());
            }
            doneCommand = true;
        }
        if(parts.length == 4){
            if(isModifyingProperty(parts,currentPropertiesNames)){
                String propertyName = parts[0];
                String attribute = parts[1]; // "rent" or "price"
                int newValue = Integer.parseInt(parts[3]); // Assumes the value is in the correct format

                // Validate and modify the property
                if (isValidModification(attribute, newValue)) {
                    Integer oldValue = getPropertyAttributeValue(propertyName, attribute, PROPERTY_PATH);
                    modifyPropertyValue(propertyName, attribute, newValue,PROPERTY_PATH);
                    System.out.println(propertyName + " " + attribute + " set from " + oldValue + " to " + newValue);
                } else {
                    System.out.println("Invalid value for " + attribute + ". Must be within valid range.");
                }
            }else if(isCreatingGameboard(parts,currentGameboardNames)){
                if(repeatedGameboardName(parts,currentGameboardNames)){
                    System.out.println("The new gameboard name already exists.");
                }else{
                    String[] existingContents = getGameBoardContents(parts[2],GAMEBOARD_PATH);
                    if (existingContents != null) {
                        String[] combinedGameBoard = new String[existingContents.length + 1];
                        combinedGameBoard[0] = parts[3];
                        System.arraycopy(existingContents, 0, combinedGameBoard, 1, existingContents.length);
                        this.tempGameboards[pointer] = combinedGameBoard;
                        System.out.println("Created new temporary gameboard '" + parts[3] + "'.");
                        this.pointer++;
                    } else {
                        System.out.println("Could not retrieve contents for game board: " + parts[2]);
                    }
                }
            }else if(isModifyingTemp(parts)){
                // Get the corresponding gameboard index
                int correspondingBoardIndex = getTempboardIndex(parts[0]);
                if (correspondingBoardIndex == -1) {
                    System.out.println("Gameboard not found: " + parts[0]);
                    return;
                }

                // Parse the square index
                int squareIndex;
                try {
                    squareIndex = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid square index: " + parts[1]);
                    return;
                }

                // Check if the square index is valid
                if (squareIndex < 1 || squareIndex > 20) {
                    System.out.println("Square index must be between 1 and 20.");
                    return;
                }

                // Check if parts[3] is a valid abbreviation
                if (isValidAbbreviation(parts[3])) {
                    // Check if the new abbreviation is the same as the current one
                    if (tempGameboards[correspondingBoardIndex][squareIndex].equals(parts[3])) {
                        System.out.println("Modification denied: The new abbreviation is the same as the current one.");
                    } else {
                        // Perform the modification
                        tempGameboards[correspondingBoardIndex][squareIndex] = parts[3];
                        System.out.println("Modification to square " + squareIndex + " of gameboard " + parts[0] + " is done.");
                    }
                } else {
                    System.out.println("Invalid abbreviation: " + parts[3]);
                }
            }else{
                System.out.println("Command not found.");
            }
            doneCommand = true;
        }
        if(!doneCommand){
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
                    String abbrev;
                    do{
                        System.out.println("What is the abbreviation of the property? (two capital English letters)");
                        abbrev = receivedResponse();
                    } while(!(abbrev.length() == 2 && abbrev.chars().allMatch(Character::isUpperCase)));
                    writeNewProperty(name,price,rent,PROPERTY_PATH);
                    System.out.println("Wrote new property " + name + "!");
                    writeNewMapping(abbrev,name,MAPPING_PATH);
                    System.out.println("Wrote its mapping!");
                    System.out.println("Property created!");
                    break;

                case "view property":
                    System.out.println("List of existing properties in file: ");
                    displayProperties(PROPERTY_PATH);
                    break;

                case "view gameboard":
                    System.out.println("List of existing gameboards in file: ");
                    displayGameboards();
                    break;
                case "view temp":
                    displayTempGameboards();
                    break;
                case "view mapping":
                    displayMapping();
                    break;
                case "help", "/help" :
                    System.out.println("All possible commands:\n\n" +
                            "create property : To design a new property by inputting values manually, then add it into file.\n\n" +
                            "view property : A overview of all existing properties, and their values of price and rent.\n\n" +

                            "<property_name> <property_attribute> > <numeric_value> : modifying an attribute's attribute value,\n" +
                            "the new value must lie within the range of valid values (can be accessed through commands).\n" +
                            "e.g.  MongKok rent > 400\n\n" +


                            "create gameboard <existing_gameboard_name> <temporary_gameboard_name> : To create a new gameboard instance\n" +
                            "based on an existing gameboard\n\n" +
                            "view gameboard : A overview of all existing gameboards, and their squares\n\n" +
                            "view temp : See how many gameboard templates/ temporary designs are in dynamic\n\n" +
                            "view mapping : A overview of abbreviations of squares\n\n" +
                            "<temporary gameboard> <square_number> > <square_abbreviation> : Modifications of squares in a\n" +
                            "temporary gameboard, not finalized until saving it*.\n\n" +
                            "save <temporary gameboard> : Saving a temp gameboard into actual file, requires confirmation\n\n" +
                            ":q : quits developer side");

                    break;

                default:
                    System.out.println("Command not found.");
                    break;
            }
        }

    }

    private boolean isModifyingProperty(String[] parts, String[] propertyNames){
        return  Arrays.asList(propertyNames).contains(parts[0]) &&
                (parts[1].equals("rent") || parts[1].equals("price")) && parts[2].equals(">");
    }
    private boolean isCreatingGameboard(String[] parts, String[] gameboardNames){
        return Arrays.asList(gameboardNames).contains(parts[2]);
    }
    private boolean isModifyingTemp(String[] parts){
        boolean tempExists = false;
        for (int i = 0; i < tempGameboards.length; i++) {
            if (tempGameboards[i][0] != null && tempGameboards[i][0].equalsIgnoreCase(parts[0])) {
                tempExists = true;
                break;
            }
        }
        return tempExists && Integer.parseInt(parts[1]) > 0 && Integer.parseInt(parts[1]) <= GAMEBOARD_SQUARES  && parts[2].equals(">");
    }
    private boolean repeatedGameboardName(String[] parts, String[] gameboardNames){
        return parts[0].equals("create") && parts[1].equals("gameboard") && Arrays.asList(gameboardNames).contains(parts[3]);
    }
    private boolean isValidModification(String attribute, int value) {
        if (attribute.equals("price")) {
            return value >= MINIMUM_PRICE && value <= MAXIMUM_PRICE;
        } else if (attribute.equals("rent")) {
            return value >= MINIMUM_RENT && value <= MAXIMUM_RENT;
        }
        return false;
    }
    private void displayTempGameboards() {
        boolean hasContent = false;
        System.out.printf("%-4s | ", "Name");
        for (int i = 1; i <= 20; i++) {
            System.out.printf("%-4s | ", i);
        }
        System.out.println();
        System.out.println(new String(new char[15 + 15 * 20]).replace('\0', '-'));
        for (int i = 0; i < tempGameboards.length; i++) {
            if (tempGameboards[i][0] != null) {
                hasContent = true;
                System.out.printf("%-4s | ", tempGameboards[i][0]);

                // Print the rest of the entries in the row
                for (int j = 1; j < tempGameboards[i].length; j++) {
                    // Print the contents, or a placeholder if null
                    System.out.printf("%-4s | ", tempGameboards[i][j] != null ? tempGameboards[i][j] : "");
                }
                System.out.println();
            }
        }
        if (!hasContent) {
            System.out.println("No game boards available.");
        }
    }
    private static void displayMapping() {
        try {
            File file = new File(MAPPING_PATH);
            Scanner scanner = new Scanner(file);

            // Print each mapping in the specified format
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Ensure the line has exactly two parts
                if (parts.length == 2) {
                    String token = parts[0].trim();
                    String description = parts[1].trim();
                    System.out.printf("%s --> %s%n", token, description);
                }
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Mapping file not found.");
        }
    }

    private boolean isTempBoardNameValid(String tempBoardName) {
        for (int i = 0; i < tempGameboards.length; i++) {
            if (tempGameboards[i][0] != null && tempGameboards[i][0].equalsIgnoreCase(tempBoardName)) {
                return true; // Found a matching temp board name
            }
        }
        return false; // No matching temp board name found
    }

    private void loadValidAbbreviations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MAPPING_PATH))) {
            validAbbrs = reader.lines()
                    .map(line -> line.split(",")[0].trim())
                    .toArray(String[]::new);
        } catch (IOException e) {
            System.err.println("Error reading the mapping file: " + e.getMessage());
        }
    }

    private boolean isValidAbbreviation(String abbreviation) {
        for (String validAbbr : validAbbrs) {
            if (validAbbr.equalsIgnoreCase(abbreviation)) {
                return true;
            }
        }
        return false;
    }

    private int getTempboardIndex(String gameboardName) {
        for (int i = 0; i < tempGameboards.length; i++) {
            if (tempGameboards[i][0] != null && tempGameboards[i][0].equalsIgnoreCase(gameboardName)) {
                return i; // Return the index of the matching gameboard
            }
        }
        return -1; // Not found
    }

}
