package utils;

import java.awt.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInput;



import blackjackExceptions.QuitException;
import imageSimilarity.websites.WebsiteObserver;

/**
 * Created by user on 19/05/17.
 */
public class Utilities {
    public static Scanner inputScanner = new Scanner(System.in);
    public static boolean fileExists(String filename){
        String currentDir = System.getProperty("user.dir");
        currentDir+=filename;
        File file = new File(currentDir);
        return file.exists();
    }
    public static String getUserInput(String prompt){
        System.out.println(prompt);
        return inputScanner.nextLine();
    }
    public static boolean getYesNoQuitFromUser(String prompt) throws QuitException{
        boolean validInputFlag = false;
        String userInput;
        while(!validInputFlag){
            userInput = getUserInput(prompt);
            if(userInput.equalsIgnoreCase("y")){
                return true;
            }
            if(userInput.equalsIgnoreCase("n")){
                return false;
            }
            if(userInput.equalsIgnoreCase("q")){
                if (getYesNoFromUser("End process?")){
                    throw new QuitException();
                }
            }
        }
        throw new QuitException();
    }
    public static WebsiteObserver loadSerialisedWebsiteObserver(String filepath)
                                        throws IOException, ClassNotFoundException{
        InputStream file = new FileInputStream(filepath);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream (buffer);
        WebsiteObserver wo = (WebsiteObserver) input.readObject();
        return wo;
    }

    public static boolean getYesNoFromUser(String prompt){
        boolean validInputFlag = false;
        String userInput;
        while(!validInputFlag){
            userInput = getUserInput(prompt);
            if(userInput.equalsIgnoreCase("y")){
                return true;
            }
            if(userInput.equalsIgnoreCase("n")){
                return false;
            }
        }
        return false;
    }
    public static int[] getCursorLocationAsArray(){
        Point point = MouseInfo.getPointerInfo().getLocation();
        int[] returnedArray = new int[2];
        returnedArray[0] = point.x;
        returnedArray[1] = point.y;
        return returnedArray;
    }
    public static String getUserInput(){
        return inputScanner.nextLine();
    }
    public static int[] getArrayFromString(String string, int length) throws NumberFormatException,
                                                ArrayIndexOutOfBoundsException {
        String[] tokens = string.split("\\s");
        int[] returnedArray = new int[length];
        for (int i = 0; i < length; i++) {
            returnedArray[i] = Integer.parseInt(tokens[i]);
        }
        return returnedArray;
    }

}
