package model;

import java.io.*;
import java.util.*;

import static model.FILE_PATHS.*;

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
    public static void getLinesFromFile(String[] S, String filepath){
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
    public static void getLinesOfWordsFromFile(String[][] S, String filepath){
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
    public static void extractMappingFromFile(HashMap<String, String> mapping, String filepath){
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

    public static String[] getBoardDetails(int selectedBoardIndex){
        String[] details = new String[GAMEBOARD_SQUARES];
        int boardCount = getLinesCountFromFile(GAMEBOARD_PATH);
        String[][] mapsDetails = new String[boardCount][GAMEBOARD_SQUARES+1];
        getLinesOfWordsFromFile(mapsDetails,GAMEBOARD_PATH);
        HashMap<String, String> squareNames = new HashMap<String,String>();
        extractMappingFromFile(squareNames, MAPPING_PATH);
        for(int i = 0; i < GAMEBOARD_SQUARES; i++){
            details[i] = squareNames.get(mapsDetails[selectedBoardIndex][i+1]);
        }
        return details;
    }

    //a very specific method to extract the positions (starting from 0) of property squares for a specific board
    public static int[] getPropertiesPosFromFile(String boardName, String boardpath, String proppath, String mappath){
        String[] boardData;
        String[] boardInformation = new String[GAMEBOARD_SQUARES];
        String[] properties = new String[GAMEBOARD_SQUARES];
        HashMap<String, String> squareNames = new HashMap<String,String>();
        extractMappingFromFile(squareNames, mappath);
        int i = 0;
        int j = 0;
        int count = 0;
        int[] positions;


        try{
            File boardFile = new File(boardpath);
            Scanner r1 = new Scanner(boardFile);
            while (r1.hasNextLine()) {
                boardData = r1.nextLine().split(",");
                if (boardData[0].equals(boardName)) {
                    System.arraycopy(boardData, 1, boardInformation, 0, boardData.length - 1);
                    break; // Exit the loop if the match is found
                }
            }
            File propFile = new File(proppath);
            Scanner r2 = new Scanner(propFile);
            while(r2.hasNextLine()){
                String property = r2.nextLine().split(",")[0];

                properties[i++] = property;
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
        }
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(proppath, true))) {
            writer.newLine();
            writer.write(propertyName + "," + propertyPrice + "," + propertyRent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNewMapping(String propertyAbbreviation, String propertyName, String mappath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mappath, true))) {
            writer.newLine();
            writer.write(propertyAbbreviation + "," + propertyName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayProperties(String propertypath) {
        try {
            File file = new File(propertypath);
            Scanner scanner = new Scanner(file);

            // Print Header
            System.out.printf("%-15s | %-15s | %-15s%n", "Name", "Price", "Rent");
            System.out.println("-----------------------------------------------");

            // Read and print each line in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Ensure the line has the expected number of values
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    String price = parts[1].trim();
                    String rent = parts[2].trim();

                    // Print the property information in formatted way
                    System.out.printf("%-15s | %-15s | %-15s%n", name, price, rent);
                }
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    public static void displayGameboards() {
        // Step 1: Load square mappings into a Map
        Map<String, String> squareMapping = loadSquareMappings(MAPPING_PATH);
        if (squareMapping.isEmpty()) {
            System.out.println("No square mappings found.");
            return;
        }

        // Step 2: Read gameboards and display them
        try {
            File file = new File(GAMEBOARD_PATH);
            Scanner scanner = new Scanner(file);

            // Print Header
            System.out.printf("%-15s | ", "Gameboard Name");
            for (int i = 1; i <= 20; i++) {
                System.out.printf("%-15s | ", i);
            }
            System.out.println();
            System.out.println(new String(new char[15 + 15 * 20]).replace('\0', '-'));

            // Read and print each game board
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Ensure the line has at least one value
                if (parts.length > 0) {
                    String gameboardName = parts[0].trim();
                    System.out.printf("%-15s | ", gameboardName);

                    // Print the mapped squares
                    for (int i = 1; i < parts.length; i++) {
                        String token = parts[i].trim();
                        String mappedName = squareMapping.getOrDefault(token, token); // Default to token if not found
                        System.out.printf("%-15s | ", mappedName);
                    }
                    System.out.println();
                }
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    public static Map<String, String> loadSquareMappings(String path) {
        Map<String, String> mapping = new HashMap<>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String token = parts[0].trim();
                    String description = parts[1].trim();
                    mapping.put(token, description);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Square mapping file not found.");
        }
        return mapping;
    }

    public static String[] getClassNames(String filepath) {
        List<String> propertyNames = new ArrayList<>();

        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);

            // Read each line in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Ensure the line has at least one value
                if (parts.length > 0) {
                    String name = parts[0].trim();
                    propertyNames.add(name); // Add the property name to the list
                }
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }

        // Convert the list to an array and return it
        return propertyNames.toArray(new String[0]);
    }

    public void modifyPropertyValue(String propertyName, String attribute, int newValue, String proppath) {
        List<String> lines = new ArrayList<>();
        boolean propertyFound = false;

        // Read the existing properties from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(proppath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(propertyName)) {
                    propertyFound = true;
                    // Modify the appropriate attribute
                    if (attribute.equals("price")) {
                        parts[1] = String.valueOf(newValue); // Update price
                    } else if (attribute.equals("rent")) {
                        parts[2] = String.valueOf(newValue); // Update rent
                    }
                    // Join the parts back into a single line
                    line = String.join(",", parts);
                }
                lines.add(line); // Add the line (modified or not) to the list
            }
        } catch (IOException e) {
            System.out.println("Error reading the property file: " + e.getMessage());
            return;
        }

        // If the property was found, write the updated lines back to the file
        if (propertyFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(proppath))) {
                for (String modifiedLine : lines) {
                    writer.write(modifiedLine);
                    writer.newLine(); // Add a newline after each line
                }
                System.out.println("Property " + propertyName + " updated successfully.");
            } catch (IOException e) {
                System.out.println("Error writing to the property file: " + e.getMessage());
            }
        } else {
            System.out.println("Property " + propertyName + " not found.");
        }
    }
    public static Integer getPropertyAttributeValue(String propertyName, String attribute, String proppath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(proppath))) {
            String line;

            // Read each line to find the property
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the property name matches
                if (parts[0].equalsIgnoreCase(propertyName)) {
                    // Return the requested attribute value
                    if (attribute.equalsIgnoreCase("price")) {
                        return Integer.parseInt(parts[1].trim()); // Return price
                    } else if (attribute.equalsIgnoreCase("rent")) {
                        return Integer.parseInt(parts[2].trim()); // Return rent
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the property file: " + e.getMessage());
        }

        // Return null if the property or attribute was not found
        System.out.println("Property " + propertyName + " with attribute " + attribute + " not found.");
        return null;
    }


    public static String[] getGameBoardContents(String gameBoardName, String boardpath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(boardpath))) {
            String line;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the first part matches the game board name
                if (parts[0].equalsIgnoreCase(gameBoardName)) {
                    // Return the contents as a String array (excluding the game board name)
                    String[] contents = new String[parts.length - 1];
                    System.arraycopy(parts, 1, contents, 0, parts.length - 1);
                    return contents; // Return contents
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the game board file: " + e.getMessage());
        }

        // Return null if the game board name was not found
        System.out.println("Game board " + gameBoardName + " not found.");
        return null;
    }


    public static String[] getInfoFromSaveFile(String selectedFile) {
        File file = new File(SAVE_PATH, selectedFile);
        String[] extractedInfo = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read lines from the file and store them in a temporary list
            StringBuilder contentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

            // Split the content into lines
            String[] rows = contentBuilder.toString().split("\n");
            extractedInfo = new String[rows.length];

            for (int i = 0; i < rows.length; i++) {
                // Split the string at the first occurrence of ":"
                String[] parts = rows[i].split(": ", 2);
                if (parts.length > 1) {
                    // Trim whitespace from the part after the colon
                    extractedInfo[i] = parts[1].trim();
                } else {
                    // If there's no colon, keep the original line
                    extractedInfo[i] = rows[i].trim();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return extractedInfo;
    }
    public static int getGameboardIndex(String gameboardName) {
        File file = new File(GAMEBOARD_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                // Split the line by commas to get individual gameboards
                String[] gameboards = line.split(",");

                // Search for the gameboard name in the current line
                for (int i = 0; i < gameboards.length; i++) {
                    if (gameboards[i].trim().equalsIgnoreCase(gameboardName)) {
                        return index + i; // Return the overall index
                    }
                }
                index += gameboards.length; // Update the index for the next line
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return -1; // Return -1 if the gameboard is not found
    }

}
